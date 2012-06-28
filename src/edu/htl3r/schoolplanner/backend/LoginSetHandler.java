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
package edu.htl3r.schoolplanner.backend;

import java.util.List;

import edu.htl3r.schoolplanner.backend.database.Database;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;

/**
 * Stellt Methoden zum Managen von Login-Sets zur Verfuegung.
 */
public interface LoginSetHandler {

	/**
	 * Speichere ein LoginSet.
	 * @param loginSet LoginSet, das gespeichert werden soll
	 */
	public void saveLoginSet(LoginSet loginSet);
	
	/**
	 * Loesche eine LoginSet.
	 * @param loginSet LoginSet, das geloescht werden soll
	 */
	public void removeLoginSet(LoginSet loginSet);

	/**
	 * Editiere ein LoginSet.
	 * Benoetige alte Server-URL + alte Schule, um (Stamm-)Daten zum dazugehoerigen LoginSet-Key zu loeschen.
	 * @param name Name des zu speichernden LoginSets
	 * @param serverUrl Server-URL nach dem Editieren des LoginSets
	 * @param school Schulname nach dem Editieren des LoginSets
	 * @param username Benutzername des zu speichernden LoginSets
	 * @param password Passwort des zu speichernden LoginSets
	 * @param checked Status des SSL-Only-Flags des zu speichernden LoginSets
	 * @param oldServerUrl Server-URL vor dem Editieren des LoginSets
	 * @param oldSchool Schulname vor dem Editieren des LoginSets
	 * @see Database#getLoginSetKeyForTable()
	 */
	public void editLoginSet(String name, String serverUrl, String school, String username, String password, boolean checked, String oldName, String oldServerUrl, String oldSchool);

	/**
	 * Liefert eine Liste mit allen LoginSets.
	 * @return
	 */
	public List<LoginSet> getAllLoginSets();
}
