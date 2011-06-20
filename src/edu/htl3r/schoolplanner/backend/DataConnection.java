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
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import edu.htl3r.schoolplanner.backend.preferences.Authentication;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;

/**
 * Stellt das Interface zum Backend und die benoetigten Methoden zur Verfuegung.
 * Bekannte Implementierungen: {@link Cache}
 */
public interface DataConnection extends DataProvider {
	
	/**
	 * Setzt die Preferences. Diese muessen neu gesetzt werden, wenn sie upgedatet wurden.
	 * @param prefs Einstellungen, die gesetzt werden sollen
	 */
	public void setLoginCredentials(Authentication prefs);

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
	public boolean authenticate() throws IOException;
	
	/**
	 * Aktualisiert die Stammdaten im Backend.
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public void resyncMasterData() throws IOException;
	
	/**
	 * Lieferte eine Map mit den jeweiligen Listen mit Lessons zu mehreren Daten. Pro Tag wird eine Liste mit Lessons in der zurueckgegebenen Map gespeichert.<br>
	 * Map["20110203"] liefert z.B. die Liste fuer den ersten abgefragten Tag, Map["20110203"].[0] liefert die erste Lesson fuer den ersten Tag, usw.<br>
	 * <b>Die Listen mit Stunden koennen mit dem Datum als String in Form von YYYYMMDD abgerufen werden.</b>
	 * Diese Methode fuegt die Unterrichtseinheiten so zusammen, dass die GUI sie einfach anzeigen kann.<br>
	 * Gibt es an einem Tag keinen Unterricht, ist dieser Tag 'null'. 
	 * @param view Initialisierter View mit Typ und Wert
	 * @param startDate Anfangsdatum (von), ab dem die Stundenliste abgefragt werden soll (inklusive)
	 * @param endDate Enddatum (bis), bis zu dem die Stundenliste abgefragt werden soll (inklusive)
	 * @param forceNetwork Wenn true, werden die Stunden direkt aus dem Netzwerk heruntergeladen
	 * @return Liste mit Stunden oder 'null', wenn diese nicht gefunden werden konnten
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public Map<String, List<Lesson>> getMergedLessons(ViewType view, Calendar startDate, Calendar endDate, boolean forceNetwork) throws IOException;
	
	/**
	 * Liefert eine Liste mit Stunden anhand der uebergebenen Params. Liefert Stunden zu genau einem Tag.
	 * Diese Methode fuegt die Unterrichtseinheiten so zusammen, dass die GUI sie einfach anzeigen kann.
	 * @param view Initialisierter View mit Typ und Wert
	 * @param date Datum, zu dem die Stundenliste abgefragt werden soll
	 * @param forceNetwork Wenn true, werden die Stunden direkt aus dem Netzwerk heruntergeladen
	 * @return Liste mit Stunden oder 'null', wenn diese nicht gefunden werden konnten
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public List<Lesson> getMergedLessons(ViewType view, Calendar date, boolean forceNetwork) throws IOException;


}
