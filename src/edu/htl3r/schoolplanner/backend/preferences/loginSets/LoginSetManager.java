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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.LoginSetHandler;
import edu.htl3r.schoolplanner.constants.LoginSetConstants;

public class LoginSetManager {
	
	private List<LoginSet> loginSets;
	private LoginSetHandler loginSetHandler;
	
	private WebUntisUrlParser urlParser = new WebUntisUrlParser();
	
	public LoginSetManager(Cache data) {
		this.loginSetHandler = data;
		loginSets = data.getAllLoginSets();
	}
	
	/**
	 * Diese Methode speichert ein neues Login-Set, wenn es noch keines zum Namen gibt, der darin gesetzt ist. 
	 * @param loginSet Login-Set, das gespeichert werden soll
	 * @return 'true', wenn das Speichern erfolgreich war, 'false', wenn es zum Namen des Login-Sets schon ein anderes Login-Set gibt.
	 */
	public boolean addLoginSet(LoginSet loginSet) {
		if(has(loginSet.getName())) {
			return false;
		}
		loginSets.add(loginSet);
		loginSetHandler.saveLoginSet(loginSet);
		
		return true;
	}


	/**
	 * @param name
	 * @param url
	 * @param school
	 * @param user
	 * @param password
	 * @param sslOnly
	 * @return 'true', wenn das Speichern erfolgreich war, 'false', wenn es zum Namen des Login-Sets schon ein anderes Login-Set gibt.
	 */
	public boolean addLoginSet(String name, String url, String school, String user, String password, boolean sslOnly) {
		return addLoginSet(new LoginSet(name, url, school, user, password, sslOnly));
	}


	public List<Map<String, String>> getAllLoginSetsForListAdapter() {
		List<Map<String, String>> allLoginSets = new ArrayList<Map<String,String>>();
		
		for(LoginSet loginSet : loginSets) {
			Map<String, String> loginSetMap = new HashMap<String, String>();
			
			loginSetMap.put(LoginSetConstants.nameKey, loginSet.getName());
			loginSetMap.put(LoginSetConstants.serverUrlKey, loginSet.getServerUrl());
			loginSetMap.put(LoginSetConstants.schoolKey, loginSet.getSchool());
			loginSetMap.put(LoginSetConstants.usernameKey, loginSet.getUsername());
			loginSetMap.put(LoginSetConstants.passwordKey, loginSet.getPassword());
			loginSetMap.put(LoginSetConstants.sslOnlyKey, loginSet.isSslOnly() ? "1" : "0");
			
			allLoginSets.add(loginSetMap);
		}
		
		return allLoginSets;
	}
	
	
	public List<LoginSet> getAllLoginSets(){
		return loginSets;
	}
	
	public LoginSet getLoginSetOnPosition(int pos) {
		return loginSets.get(pos);
	}


	public void removeLoginEntry(LoginSet loginSet) {
		loginSets.remove(loginSet);
		loginSetHandler.removeLoginSet(loginSet);
	}
	
	public void removeLoginEntry(int pos) {
		removeLoginEntry(loginSets.get(pos));
	}
	
	private boolean has(String name) {
		for(LoginSet loginSet : loginSets) {
			if(loginSet.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public LoginSet getLoginSet(String name) {
		for(LoginSet loginSet : loginSets) {
			if(loginSet.getName().equals(name)) {
				return loginSet;
			}
		}
		return null;
	}

	public void editLoginSet(String name, String serverUrl, String school,
			String username, String password, boolean checked) {
		
		if(has(name)) {
			loginSetHandler.editLoginSet(name, urlParser.parseUrl(serverUrl), school,
					username, password, checked);

			for(LoginSet loginSet : loginSets) {
				if(loginSet.getName().equals(name)) {
					loginSets.remove(loginSet);
					loginSets.add(new LoginSet(name, serverUrl, school, username, password, checked));
				}
			}
		}
		else {
			removeLoginEntry(getLoginSet(name));
			addLoginSet(name, serverUrl, school, username, password, checked);
		}
	}
	
}
