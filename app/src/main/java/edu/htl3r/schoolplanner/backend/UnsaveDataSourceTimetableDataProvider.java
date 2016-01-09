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
import java.util.Map;

import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.network.json.JSONNetwork;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;

/**
 * Stellt Methoden zum Abruf des Stundenplans aus unsicheren Quellen ({@link JSONNetwork}) zur Verfuegung.
 */
public interface UnsaveDataSourceTimetableDataProvider {
	
	/**
	 * Ermoeglicht den Zugriff auf den Stundenplan zu einem bestimmten Datum.
	 * @param viewType Initialisierte Klasse, Lehrer, Raum oder Fach (={@link ViewType}), fuer die der Stundenplan abgerufen werden soll
	 * @param date Datum, fuer das der Stundenplan abgerufen werden soll
 	 * @return Ein {@link DataFacade}-Objekt mit den passenden Daten.
	 */
	public DataFacade<List<Lesson>> getLessons(ViewType viewType, DateTime date);
	
	
	/**
	 * Ermoeglicht den Zugriff auf den Stundenplan zu einem bestimmten Datum.
	 * @param viewType Initialisierte Klasse, Lehrer, Raum oder Fach (={@link ViewType}), fuer die der Stundenplan abgerufen werden soll
	 * @param startDate Anfangsdatum, ab dem der Stundenplan abgerufen werden soll
	 * @param endDate Enddatum, bis zu dem der Stundenplan abgerufen werden soll
 	 * @return Ein {@link DataFacade}-Objekt mit den passenden Daten.
	 */
	public DataFacade<Map<String, List<Lesson>>> getLessons(ViewType viewType, DateTime startDate, DateTime endDate);

}
