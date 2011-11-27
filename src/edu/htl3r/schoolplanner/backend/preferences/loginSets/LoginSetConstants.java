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


/**
 * Stellt Konstanten fuer die Verwaltung der {@link LoginSet}s in Datenbank / Maps zur Verfuegung.
 */
public interface LoginSetConstants {

	// Konstanten fuer LoginSets (Datenbank: Spaltennamen, Maps: Keys)
	public final String nameKey = "name";
	public final String serverUrlKey = "url";
	public final String schoolKey = "school";
	public final String usernameKey = "user";
	public final String passwordKey = "password";
	public final String sslOnlyKey = "sslOnly";	
}
