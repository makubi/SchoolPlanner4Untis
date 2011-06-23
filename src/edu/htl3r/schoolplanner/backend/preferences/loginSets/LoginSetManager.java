/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
			Sebastian Chlan <sebastian@schoolplanner.at>
			Christian Pascher <christian@schoolplanner.at>
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

import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.gui.Constants;

public class LoginSetManager {
	
	private List<LoginSet> loginSets;
	private LoginSetDatabase database = new LoginSetDatabase();
	
	private List<LoginSetUpdateObserver> observers = new ArrayList<LoginSetUpdateObserver>();
	
	public LoginSetManager() {
		database.setContext(SchoolplannerContext.context);
		loginSets = database.getAllLoginSets();
	}
	
	public void addLoginSet(LoginSet loginSet) {
		loginSets.add(loginSet);
		database.saveLoginSet(loginSet);
		
		for(LoginSetUpdateObserver observer : observers) {
			observer.loginSetAdded();
		}
	}


	public void addLoginSet(String name, String url, String school, String user, String password, boolean sslOnly) {
		addLoginSet(new LoginSet(name, url, school, user, password, sslOnly));
	}


	public List<Map<String, String>> getAllLoginSetsForListAdapter() {
		List<Map<String, String>> allLoginSets = new ArrayList<Map<String,String>>();
		
		for(LoginSet loginSet : loginSets) {
			Map<String, String> loginSetMap = new HashMap<String, String>();
			
			loginSetMap.put(Constants.nameKey, loginSet.getName());
			loginSetMap.put(Constants.serverUrlKey, loginSet.getServerUrl());
			loginSetMap.put(Constants.schoolKey, loginSet.getSchool());
			loginSetMap.put(Constants.usernameKey, loginSet.getUsername());
			loginSetMap.put(Constants.passwordKey, loginSet.getPassword());
			loginSetMap.put(Constants.sslOnlyKey, loginSet.isSslOnly() ? "1" : "0");
			
			allLoginSets.add(loginSetMap);
		}
		
		return allLoginSets;
	}
	
	
	public List<LoginSet> getAllLoginSets(){
		return loginSets;
	}


	public void removeLoginEntry(LoginSet loginSet) {
		loginSets.remove(loginSet);
		database.removeLoginSet(loginSet);
	}
	
	public void removeLoginEntry(int pos) {
		removeLoginEntry(loginSets.get(pos));
	}
	
	public void addSetUpdateObserver(LoginSetUpdateObserver observer) {
		observers.add(observer);
	}
	
}
