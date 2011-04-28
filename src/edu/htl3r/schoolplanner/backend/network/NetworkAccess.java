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

import java.io.IOException;

/**
 * 
 * Spezifiziert die noetigen Methoden fuer den Netzwerkzugriff.
 *
 */
public interface NetworkAccess {

	/**
	 * Liefert einen Antwort-String auf die vorher gestellte Anfrage.
	 * Wenn eine IOException waehrend dem Einlesen auftritt, wird null zurueckgegeben.
	 * 
	 * @param request Anfrage, die an das Netzwerk gesendet werden soll
	 * @return Antwort auf die Anfrage
	 * @throws IOException
	 */
	public String getResponse(String request) throws IOException;

	
	/**
	 * Setzt den Namen der Schule, die als GET-Parameter in der Request-URL verwendet werden soll .
	 * @param school Name der zu verwendenden Schule
	 */
	public void setSchool(String school);

	/**
	 * Setzt die SessionID, die nach einem erfolgreichen Login vom Server uebertragen wurde.
	 * @param jsessionid SessionID, die fuer weitere Anfragen verwendet wird
	 */
	public void setJsessionid(String jsessionid);


	/**
	 * Setzt die URL des Servers (inklusive Port).
	 * @param serverUrl URL des Servers inklusive Port
	 */
	public void setServerUrl(String serverUrl);

}
