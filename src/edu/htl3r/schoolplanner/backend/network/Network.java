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
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import android.util.Log;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.backend.network.exceptions.SSLForcedButUnavailableException;
import edu.htl3r.schoolplanner.backend.network.exceptions.WrongPortNumberException;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;

/**
 * 
 * Direkter Netzwerkzugriff ueber URL.
 * 
 */
public class Network {
	
	private String oldServerUrl = "";
	private String oldSchool = "";
	
	private HttpClient client;
	
	private LoginSet loginCredentials;
	
	private URI url;
	
	private String jsessionid;
	
	// Prioritized
	private SSLSocketFactory[] sslSocketFactories = new SSLSocketFactory[2];
	private Scheme[] sslSchemes = new Scheme[2];
		
	public Network() {
		initSSLSocketFactories();
		
		HttpParams params = new BasicHttpParams();
		
		// TODO: Timeouts sind statisch
		HttpConnectionParams.setConnectionTimeout(params, 20000);
		HttpConnectionParams.setSoTimeout(params, 10000);
		
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		HttpProtocolParams.setUseExpectContinue(params, false);
		
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
	private void initSSLSchemes(int port) {
		sslSchemes[0] = new Scheme("https", sslSocketFactories[0], port != -1 ? port : 443);
		sslSchemes[1] = new Scheme("https", sslSocketFactories[1], port != -1 ? port : 443);
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
	 * @return 
	 * @throws IOException Wenn die URL nicht zur Uebertragung verwendet werden kann
	 */
	private boolean checkServerCapability(URI url) throws IllegalArgumentException {
		boolean sslAvailable = true;
		
		final SocketAddress sa = new InetSocketAddress(url.getHost(), url.getPort() != -1 ? url.getPort() : 443);
		
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
		
		return sslAvailable;
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
		client.getConnectionManager().getSchemeRegistry().register(new Scheme("http", PlainSocketFactory.getSocketFactory(), url != null && url.getPort() != -1 ? url.getPort() : 80));
		
		if(scheme != null) {
			client.getConnectionManager().getSchemeRegistry().register(scheme);
		}
	}

	public String getResponse(String request) throws Exception {		
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
	private String executeRequest(String request) throws Exception {
		checkPreferenceChange();
		
		HttpPost httpRequest = new HttpPost(url);

		if (jsessionid != null) {
			httpRequest.addHeader("Cookie", "JSESSIONID=" + jsessionid);
			Log.d("Network", "Added header: " + httpRequest.getHeaders("Cookie")[0].toString());
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
		return !(loginCredentials.getServerUrl().equals(oldServerUrl) && loginCredentials.getSchool().equals(oldSchool));
	}
	
	private void checkPreferenceChange() throws Exception {
		if(preferencesChanged()) {
			String serverUrl = loginCredentials.getServerUrl();
			String school = loginCredentials.getSchool();
			
			// JSession-ID zuruecksetzen
			jsessionid = null;
			
			// Port ueberpruefen
			int port = -1;
			
			Pattern p = Pattern.compile("^(.*?):(\\d+)$");
			Matcher m = p.matcher(serverUrl);
			if (m.matches()) {
//			  String host = m.group(1);
			  port = Integer.parseInt(m.group(2));
			}
			
			if(!properPort(port)) {
				throw new WrongPortNumberException("Wrong port: " + port);
			}
			
			// Ueberpruefe, ob SSL verfuegbar ist
			initSSLSchemes(port);
			boolean sslAvailable = checkServerCapability(new URI("https://"+serverUrl.toString()));
			Log.i("Network", "SSL available: "+sslAvailable);
			
			// Ueberpruefe ob SSL erzwungen und verfuegbar ist
			if(loginCredentials.isSslOnly() && !sslAvailable) {
				throw new SSLForcedButUnavailableException(serverUrl+" does not have SSL enabled");
			}
			
			// Vervollstaendige Server-URL
			URI url = getServerURLasURI( sslAvailable, serverUrl, school);
//			url = addProtocol(url, sslAvailable);
//			url = addTrail(url);
//			url = addSchool(url, school);
			
			oldServerUrl = new String(loginCredentials.getServerUrl());
			oldSchool = new String(loginCredentials.getSchool());
			
			this.url = url;
			
			Log.d("Network", "Setting url: "+url.toString());
		}
	}

	private URI getServerURLasURI(boolean sslAvailable, String serverUrl,
			String school) throws UnsupportedEncodingException, URISyntaxException {
		final String encodedSchool = URLEncoder.encode(school, "UTF-8");
		return new URI((sslAvailable ? "https" : "http") + "://" + serverUrl + "/WebUntis/jsonrpc.do" + "?school=" + encodedSchool);
	}

	/**
	 * Ueberprueft, ob ein passender oder gar kein Port angegeben wurde.
	 * @param port Port, der ueberprueft werden soll
	 * @return 'true', wenn der Port zwischen -1 (inkl.) und 65535 (inkl.) liegt
	 */
	private boolean properPort(int port) {
		return port >= -1 && port <= 65535;
	}

	public void setJsessionid(String jsessionid) {
		this.jsessionid = jsessionid;
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
	
	public void setLoginCredentials(LoginSet loginSet) {
		this.loginCredentials = loginSet;
	}
	
}
