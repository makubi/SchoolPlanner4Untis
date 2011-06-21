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
package edu.htl3r.schoolplanner.backend.preferences.loginSets.asyncUpdateTasks;

import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.gui.WelcomeScreen;

/**
 * Diese Klasse updated das UI, nachdem ein {@link LoginSet}-Eintrag hinzugefuegt wurde. 
 */
public class LoginSetAddAsyncTask extends LoginSetUpdateAsyncTask {

	

	public LoginSetAddAsyncTask(WelcomeScreen parent) {
		super(parent);
	}

	@Override
	protected void editList() {
		// TODO Auto-generated method stub

	}

}
