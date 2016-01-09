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

import java.io.IOException;

import edu.htl3r.schoolplanner.backend.cache.Cache;
import edu.htl3r.schoolplanner.backend.network.json.JSONNetwork;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;

/**
 * Stellt das Interface zum Backend und die benoetigten Methoden zur Verfuegung.
 * Bekannte Implementierungen: {@link Cache}
 */
public interface DataConnection {

	/**
	 * Setzt den Status des Netzwerkes, wenn sich dieser aendert.
	 * 
	 * @param networkAvailable 'true', wenn das Netzwerk verfuegbar ist, sonst 'false'
	 */
	public void networkAvailabilityChanged(boolean networkAvailable);

	/**
	 * Gibt true zurueck, wenn die Authentifizierung erfolgreich war.
	 * Die Ueberpruefung auf eine Netzwerkverbindung muss vor dem Methodenaufruf stattfinden.
	 * @return true, wenn die Authentifizierung erfolgreich war
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public DataFacade<Boolean> authenticate();
	
	/**
	 * Aktualisiert die Stammdaten im Backend.
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public DataFacade<Boolean> resyncMasterData();

	/**
	 * Setzt das LoginSet mit den Login-Daten, die im Backend verwendet werden sollen.
	 * @param loginSet
	 * @see JSONNetwork#authenticate()
	 */
	public void setLoginCredentials(LoginSet loginSet);
	
	
	/**
	 * Setzt den internen Cache (Memory) zurueck und loescht alle Daten daraus.
	 */
	public void clearInternalCache();
}
