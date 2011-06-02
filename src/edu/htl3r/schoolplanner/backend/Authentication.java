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

package edu.htl3r.schoolplanner.backend;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

/**
 * 
 * Diese Klasse repraesentiert die Authentifikationsinformationen, die benoetigt werden.
 * Sie sollte nur ueber {@link Preferences} verwaltet werden.
 *
 */
public class Authentication {

	private String serverUrl;
	private String school;
	private String username;
	private String password;
	
	/**
	 * Liefert die URL des Servers, z.B. urania.webuntis.at
	 * @return
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * Setzt die URL des Servers, sollte z.B. urania.webuntis.at sein.<br>
	 * Protokoll und WebUntis-Verzeichnis + jsonrpc.do koennen weggelassen werden. Diese werden vor dem Setzen der internen Server-URL entfernt.
	 * @param serverUrl Server URL von der GUI, die gesetzt werden soll
	 * @throws URISyntaxException Wird geworfen, falls keine gueltige URL aus dem uebergebenen String ermittelt werden konnte
	 */
	public void setServerUrl(String serverUri) throws URISyntaxException {		
		serverUri = serverUri.replaceAll("/WebUntis(/jsonrpc.do)?$", "");
		Pattern p = Pattern.compile("^([a-zA-Z]+://)?(.*)$");
		Matcher m = p.matcher(serverUri);
		if(m.matches()) {
			String url = m.group(2);
			while(url.endsWith("/")) {
				url = url.substring(0, url.length()-1);
			}
			
			this.serverUrl = url;
		}
		else {
			throw new URISyntaxException(serverUri, "Unable to parse URL");
		}
	}

	/**
	 * Liefert den Schul-Login-Namen.
	 * @return Schul-Login
	 */
	public String getSchool() {
		return school;
	}
	
	/**
	 * Setzt den Schul-Login-Namen.
	 * @param school Schul-Login
	 */
	public void setSchool(String school) {
		this.school = school;
	}
	
	/**
	 * Liefert den Benutzernamen des Logins.
	 * @return Benutzername
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Setzt den Benutzernamen, der zum Login verwendet werden soll.
	 * @param username Benutzername
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Liefert das Passwort des Logins.
	 * @return Passwort
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Setzt das Passwort, das zum Login verwendet werden soll.
	 * @param password Passwort
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public String toString() {
		return "Authentication [" + (password != null ? "password=" + password + ", " : "") + (school != null ? "school=" + school + ", " : "") + (serverUrl != null ? "serverUrl=" + serverUrl + ", " : "") + (username != null ? "username=" + username : "") + "]";
	}
	
	
	
}
