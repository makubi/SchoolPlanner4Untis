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

import edu.htl3r.schoolplanner.backend.network.json.JSONNetwork;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

/**
 * Stellt Methoden zum Abruf der Stammdaten aus unsicheren Quellen ({@link JSONNetwork}) zur Verfuegung.
 */
public interface UnsaveDataSourceMasterdataProvider {
	
	/**
	 * Ermoeglicht den Zugriff auf die Liste der Schulklassen.<br>
	 * @return Ein {@link DataFacade}-Objekt mit den passenden Daten.
	 */
	public DataFacade<List<SchoolClass>> getSchoolClassList();

	/**
	 * Ermoeglicht den Zugriff auf die Liste der Lehrer.<br>
	 * @return Ein {@link DataFacade}-Objekt mit den passenden Daten.
	 */
	public DataFacade<List<SchoolTeacher>> getSchoolTeacherList();

	/**
	 * Ermoeglicht den Zugriff auf die Liste der Raeume.<br>
	 * @return Ein {@link DataFacade}-Objekt mit den passenden Daten.
	 */
	public DataFacade<List<SchoolRoom>> getSchoolRoomList();

	/**
	 * Ermoeglicht den Zugriff auf die Liste der Faecher.<br>
	 * @return Ein {@link DataFacade}-Objekt mit den passenden Daten.
	 */
	public DataFacade<List<SchoolSubject>> getSchoolSubjectList();

	/**
	 * Ermoeglicht den Zugriff auf die Liste der freien Tage.<br>
	 * @return Ein {@link DataFacade}-Objekt mit den passenden Daten.
	 */
	public DataFacade<List<SchoolHoliday>> getSchoolHolidayList();

	/**
	 * Ermoeglicht den Zugriff auf den Zeitraster.<br>
	 * @return Ein {@link DataFacade}-Objekt mit den passenden Daten.
	 */
	public DataFacade<Timegrid> getTimegrid();
	
}
