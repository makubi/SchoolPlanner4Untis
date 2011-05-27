/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
						Gerald Schreiber <mail@gerald-schreiber.at>
						Philip Woelfel <philip@woelfel.at>
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package edu.htl3r.schoolplanner.backend.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.springframework.web.util.UriUtils;

import android.util.Log;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolplannerContext;

/**
 * 
 * Direkter Netzwerkzugriff ueber URL.
 * 
 */
public class Network implements NetworkAccess {
	
	private HttpClient client;
	
	private URI serverUrl;
	private URI httpsServerUrl;
	
	private URI usedUrl;
	
	private String jsessionid;
	
	// Prioritized
	private SSLSocketFactory[] sslSocketFactories = new SSLSocketFactory[3];
	private Scheme[] sslSchemes = new Scheme[3];
	
	boolean sslAvailable = false;
	
	public Network() {
		initSSLSocketFactories();
		
		HttpParams params = new BasicHttpParams();
		
		// TODO: Timeouts sind statisch
		HttpConnectionParams.setConnectionTimeout(params, 210000);
		HttpConnectionParams.setSoTimeout(params, 28000);
		
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		HttpProtocolParams.setUseExpectContinue(params, true);
		
		SchemeRegistry registry = new SchemeRegistry();
		
		ClientConnectionManager connman = new ThreadSafeClientConnManager(params, registry);
		
		client = new DefaultHttpClient(connman, params);
	}
	
	private void initSSLSocketFactories() {
		sslSocketFactories[0] = SSLSocketFactory.getSocketFactory();
		sslSocketFactories[1] = caCertSSLSocketFactory();
		sslSocketFactories[2] = startSSLSocketFactory();
	}

	private void initSSLSchemes() {
		sslSchemes[0] = new Scheme("https", sslSocketFactories[0], httpsServerUrl != null && httpsServerUrl.getPort() != -1 ? httpsServerUrl.getPort() : 443);
		sslSchemes[1] = new Scheme("https", sslSocketFactories[1], httpsServerUrl != null && httpsServerUrl.getPort() != -1 ? httpsServerUrl.getPort() : 443);
		sslSchemes[2] = new Scheme("https", sslSocketFactories[2], httpsServerUrl != null && httpsServerUrl.getPort() != -1 ? httpsServerUrl.getPort() : 443);
	}

	private SSLSocket getWorkingSSLSocket(SocketAddress sa, Set<SSLSocket> set) throws SSLException {
		final int sslSocketTimeout = 3000;
		for(SSLSocket sslSocket : set) {
			try {
				sslSocket.connect(sa, sslSocketTimeout);
				sslSocket.setReuseAddress(true);
				sslSocket.startHandshake();
				return sslSocket;
			}
			catch (IOException e) {}
			finally {
				try {
					sslSocket.close();
				} catch (IOException e) {}
			}
		}
		return null;
	}
	
	private void checkServerCapability() {
		final SocketAddress sa = new InetSocketAddress(httpsServerUrl.getHost(), httpsServerUrl != null && httpsServerUrl.getPort() != -1 ? httpsServerUrl.getPort() : 443);
		
		try {
			Map<SSLSocket, Scheme> checkSocketsToSchemeMapping = new HashMap<SSLSocket, Scheme>();			
			checkSocketsToSchemeMapping.put((SSLSocket) sslSocketFactories[0].createSocket(), sslSchemes[0]);
			checkSocketsToSchemeMapping.put((SSLSocket) sslSocketFactories[1].createSocket(), sslSchemes[1]);
			checkSocketsToSchemeMapping.put((SSLSocket) sslSocketFactories[2].createSocket(), sslSchemes[2]);
			
			SSLSocket availableSocket = getWorkingSSLSocket(sa, checkSocketsToSchemeMapping.keySet());
			
			sslAvailable = availableSocket != null;
			
			registerSchemes(checkSocketsToSchemeMapping.get(availableSocket));
			

		}
		catch (IOException e) {
			sslAvailable = false;
			registerSchemes();
		}
		Log.i("Network", "SSL available: "+sslAvailable);
	}
	
	private void registerSchemes() {
		registerSchemes(null);	
	}

	private void registerSchemes(Scheme scheme) {
		client.getConnectionManager().getSchemeRegistry().register(new Scheme("http", PlainSocketFactory.getSocketFactory(), serverUrl != null && serverUrl.getPort() != -1 ? serverUrl.getPort() : 80));
		
		if(scheme != null) {
			client.getConnectionManager().getSchemeRegistry().register(scheme);
		}
	}

	@Override
	public String getResponse(String request) throws IOException {		
				return executeRequest(request);
	}
	
	private String executeRequest(String request) throws IOException {
		HttpPost httpRequest = new HttpPost(usedUrl);

		if (jsessionid != null) {
			httpRequest.addHeader("Cookie", "JSESSIONID=" + jsessionid);
			Log.d("Network",
					"Added header: "
							+ httpRequest.getHeaders("Cookie")[0].toString());
		}

		StringEntity entity = new StringEntity(request, "UTF-8");
		httpRequest.setEntity(entity);

		HttpResponse httpResponse = client.execute(httpRequest);
		Log.d("Network", "Sent: " + request);
		
		ByteArrayOutputStream body = new ByteArrayOutputStream();
		httpResponse.getEntity().writeTo(body);
		String response = body.toString();
		
		Log.d("Network", "Got status: " + httpResponse.getStatusLine());
		Log.d("Network", "Got body: " + response);
		
		return response;
	}
	
	@Override
	public void setSchool(String school) {
		try {
			// Encode school as iso-8859-1 string
			String encodedSchool = UriUtils.encodeQuery(school,"ISO-8859-1");
			
			usedUrl = sslAvailable ? new URI(httpsServerUrl + "?school=" + encodedSchool) : new URI(serverUrl + "?school=" + encodedSchool);
			
			Log.d("Network", "Setting url: "+usedUrl.toString());
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setJsessionid(String jsessionid) {
		this.jsessionid = jsessionid;
	}

	@Override
	public void setServerUrl(String serverUrl) {
		 try {
			this.serverUrl = new URI("http://"+serverUrl+"/WebUntis/jsonrpc.do");
			this.httpsServerUrl = new URI("https://"+serverUrl+"/WebUntis/jsonrpc.do");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initSSLSchemes();
		checkServerCapability();
	}
	
	private SSLSocketFactory caCertSSLSocketFactory() {
	    try {
	        KeyStore trusted = KeyStore.getInstance("BKS");
	        InputStream in = SchoolplannerContext.context.getResources().openRawResource(R.raw.cacert_ks);
	        try {
	            trusted.load(in, "cacert".toCharArray());
	        } finally {
	            in.close();
	        }
	        return new SSLSocketFactory(trusted);
	    } catch (Exception e) {
	        throw new AssertionError(e);
	    }
	}
	
	private SSLSocketFactory startSSLSocketFactory() {
	    try {
	        KeyStore trusted = KeyStore.getInstance("BKS");
	        InputStream in = SchoolplannerContext.context.getResources().openRawResource(R.raw.startssl_ks);
	        try {
	            trusted.load(in, "startssl".toCharArray());
	        } finally {
	            in.close();
	        }
	        return new SSLSocketFactory(trusted);
	    } catch (Exception e) {
	        throw new AssertionError(e);
	    }
	}
	
}
