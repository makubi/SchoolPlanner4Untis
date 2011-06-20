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
package edu.htl3r.schoolplanner.backend.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginSetManager {

	private Map<String, LoginSet> loginSets = new HashMap<String, LoginSet>();
	
	public void addLoginSet(LoginSet loginSet) {
		loginSets.put(loginSet.getName(), loginSet);
	}
	
	public void removeLoginSet(LoginSet loginSet) {
		loginSets.remove(loginSet);
	}
	
	public LoginSet getLoginSet(String name) {
		return loginSets.get(name);
	}
	
	public List<LoginSet> getAllLoginSets() {
		List<LoginSet> allLoginSets = new ArrayList<LoginSet>();
		
		for(String name: loginSets.keySet()) {
			allLoginSets.add(loginSets.get(name));
		}
		
		return allLoginSets;
	}
}
