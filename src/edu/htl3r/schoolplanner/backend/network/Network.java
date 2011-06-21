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
import edu.htl3r.schoolplanner.backend.preferences.Authentication;

/**
 * 
 * Direkter Netzwerkzugriff ueber URL.
 * 
 */
public class Network implements NetworkAccess {
	
	private String oldServerUrl = "";
	private String oldSchool = "";
	
	private HttpClient client;
	
	private Authentication authentication;
	
	private URI serverUrl;
	private URI httpsServerUrl;
	
	private URI usedUrl;
	
	private String jsessionid;
	
	// Prioritized
	private SSLSocketFactory[] sslSocketFactories = new SSLSocketFactory[2];
	private Scheme[] sslSchemes = new Scheme[2];
	
	boolean sslAvailable = true;
	
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
	
	/**
	 * Initialisiert die {@link SSLSocketFactory}s, die benoetigte CAs enthalten.
	 * Zur Zeit sind das die Standard-CAs, CACert und StartSSL.
	 */
	private void initSSLSocketFactories() {
		sslSocketFactories[0] = SSLSocketFactory.getSocketFactory();
		sslSocketFactories[1] = additionalCASSLSocketFactory();
	}

	/**
	 * Initialisiert alle {@link Scheme}s fuer HTTPS.
	 */
	private void initSSLSchemes() {
		sslSchemes[0] = new Scheme("https", sslSocketFactories[0], httpsServerUrl.getPort() != -1 ? httpsServerUrl.getPort() : 443);
		sslSchemes[1] = new Scheme("https", sslSocketFactories[1], httpsServerUrl.getPort() != -1 ? httpsServerUrl.getPort() : 443);
	}

	/**
	 * Liefert ein {@link SSLSocket}, wenn eine Verbindung via SSL zum Server aufgebaut werden konnte oder 'null', wenn SSL nicht verfuegbar ist.
	 * @param sa Die Adresse des Sockets, zum dem die Verbindung aufgebaut werden soll
	 * @param set Ein Set mit {@link SSLSocket}s, mithilfe derer versucht werden soll, eine Verbindung aufzubauen 
	 * @return Das erste SSLSocket aus dem Set, mit dem eine problemlos Verbindung zum Server aufgebaut werden konnte oder 'null', wenn dies mit keinem moeglich war
	 */
	private SSLSocket getWorkingSSLSocket(SocketAddress sa, Set<SSLSocket> set) {
		final int sslSocketTimeout = 2000;
		for(SSLSocket sslSocket : set) {
			try {
				sslSocket.connect(sa, sslSocketTimeout);
				sslSocket.setSoTimeout(sslSocketTimeout);
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
	
	/**
	 * Ueberprueft, ob der Server SSL unterstuetzt oder nicht und registriert die passenden {@link Scheme}s fuer die spaetere Verwendung.
	 * @throws IOException Wenn die URL nicht zur Uebertragung verwendet werden kann
	 */
	private void checkServerCapability() throws IOException {
		final SocketAddress sa;
		try {
			sa = new InetSocketAddress(httpsServerUrl.getHost(), httpsServerUrl.getPort() != -1 ? httpsServerUrl.getPort() : 443);
		}
		catch (IllegalArgumentException e) {
			// Thrown, if server url can not be parsed
			throw new IOException("Unable to parse URL");
		}
		
		try {
			Map<SSLSocket, Scheme> checkSocketsToSchemeMapping = new HashMap<SSLSocket, Scheme>();			
			checkSocketsToSchemeMapping.put((SSLSocket) sslSocketFactories[0].createSocket(), sslSchemes[0]);
			checkSocketsToSchemeMapping.put((SSLSocket) sslSocketFactories[1].createSocket(), sslSchemes[1]);
			
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
	
	/**
	 * Registriert nur eine {@link Scheme} fuer plain HTTP.
	 */
	private void registerSchemes() {
		registerSchemes(null);	
	}

	/**
	 * Registriert eine {@link Scheme} fuer plain HTTP und falls uebergeben, eine fuer HTTPS.
	 * @param scheme Scheme fuer HTTPS, die registriert werden soll
	 */
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
	
	/**
	 * Versucht mittels HTTP(S), eine Verbindung mit dem Server herzustellen, der sich hinter der gesetzten URL befindet, sendet die uebergeben Anfrage an ihn und gibt die Antwort als String zurueck.
	 * Wenn eine JSESSIONID bekannt ist, wird diese an den HTTP-Header angehaengt.
	 * Das verwendete Character-Set ist UTF-8.
	 * @param request Anfrage, die gesendet werden soll
	 * @return Antwort auf die Anfrage
	 * @throws IOException Wenn waehrend der Uebertragung ein Fehler auftritt
	 */
	private String executeRequest(String request) throws IOException {
		checkPreferenceChange();
		
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
	
	private boolean preferencesChanged() {
		return !(authentication.getServerUrl().equals(oldServerUrl) && authentication.getSchool().equals(oldSchool));
	}
	
	private void checkPreferenceChange() throws IOException {
		if(preferencesChanged()) {
			String serverUrl = authentication.getServerUrl();
			String school = authentication.getSchool();
			
			oldServerUrl = new String(serverUrl);
			oldSchool = new String(school);
			
			try {
				setServerUrl(serverUrl);
				setSchool(school);
			} catch (URISyntaxException e) {
				// Thrown, if server url can not be parsed
				throw new IOException("Unable to parse URL");
			}
			
			jsessionid = null;
			
			initSSLSchemes();
			checkServerCapability();
			
			// TODO: Ugly hack, setting URL after server SSL check
			try {
				setServerUrl(serverUrl);
				setSchool(school);
			} catch (URISyntaxException e) {
				// Thrown, if server url can not be parsed
				throw new IOException("Unable to parse URL");
			}
		}
	}

	/**
	 * Setzt den Namen der Schule, die als GET-Parameter in der Request-URL verwendet werden soll .
	 * @param school Name der zu verwendenden Schule
	 * @throws UnsupportedEncodingException Wenn die Kodierung nicht unterstützt wird
	 * @throws URISyntaxException Wenn die URL nicht gesetzt werden konnte
	 */
	private void setSchool(String school) throws UnsupportedEncodingException, URISyntaxException {
			// Encode school as iso-8859-1 string
			String encodedSchool = UriUtils.encodeQuery(school,"ISO-8859-1");
			usedUrl = sslAvailable ? new URI(httpsServerUrl + "?school=" + encodedSchool) : new URI(serverUrl + "?school=" + encodedSchool);
			
			Log.d("Network", "Setting url: "+usedUrl.toString());
	}

	@Override
	public void setJsessionid(String jsessionid) {
		this.jsessionid = jsessionid;
	}

	/**
	 * Setzt die URL des Servers (inklusive Port).
	 * @param serverUrl URL des Servers inklusive Port
	 * @throws URISyntaxException Wenn die URL nicht geparst werden konnte
	 */
	private void setServerUrl(String serverUrl) throws URISyntaxException {
			this.serverUrl = new URI("http://"+serverUrl+"/WebUntis/jsonrpc.do");
			this.httpsServerUrl = new URI("https://"+serverUrl+"/WebUntis/jsonrpc.do");
	}
	
	private SSLSocketFactory additionalCASSLSocketFactory() {
	    try {
	        KeyStore trusted = KeyStore.getInstance("BKS");
	        InputStream in = SchoolplannerContext.context.getResources().openRawResource(R.raw.additional_cas);
	        try {
	        	final String keystorePassword = "additionalCAs";
	            trusted.load(in, keystorePassword.toCharArray());
	        } finally {
	            in.close();
	        }
	        return new SSLSocketFactory(trusted);
	    } catch (Exception e) {
	        throw new AssertionError(e);
	    }
	}

	@Override
	public void setLoginCredentials(Authentication preferences) {
		this.authentication = preferences;
	}
	
}
