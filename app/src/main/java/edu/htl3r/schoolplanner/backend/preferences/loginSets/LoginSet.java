/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
			Sebastian Chlan <sebastian@schoolplanner.at>
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
package edu.htl3r.schoolplanner.backend.preferences.loginSets;

import java.io.Serializable;
import java.util.Map;


public class LoginSet implements Serializable {
	
	private static final long serialVersionUID = 6417138756120086971L;

	private WebUntisUrlParser urlParser = new WebUntisUrlParser();
	
	private String name;
	private String serverUrl;
	private String school;
	private String username;
	private String password;
	private boolean sslOnly;
	
	public LoginSet(LoginSet loginSet) {
		name = loginSet.getName();
		serverUrl = urlParser.parseUrl(loginSet.getServerUrl());
		school = loginSet.getSchool();
		username = loginSet.getUsername();
		password = loginSet.getPassword();
		sslOnly = loginSet.isSslOnly();
	}
	
	public LoginSet(Map<String, String> data) {
		name = data.get(LoginSetConstants.nameKey);

		serverUrl = urlParser.parseUrl(data.get(LoginSetConstants.serverUrlKey));
		school = data.get(LoginSetConstants.schoolKey);
		username = data.get(LoginSetConstants.usernameKey);
		
		String password = data.get(LoginSetConstants.passwordKey);
		this.password = password != null ? password : "";
		
		this.sslOnly = Integer.parseInt(data.get(LoginSetConstants.sslOnlyKey))>0;
	}
	
	public LoginSet(String name, String serverUrl, String school, String username, String password, boolean sslOnly) {
		this.name = name;
		this.serverUrl = urlParser.parseUrl(serverUrl);
		this.school = school;
		this.username = username;
		this.password = password != null ? password : "";
		this.sslOnly = sslOnly;
	}

	public String getName() {
		return name;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public String getSchool() {
		return school;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean isSslOnly() {
		return sslOnly;
	}
}
