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

import java.util.List;

import edu.htl3r.schoolplanner.backend.cache.InternalMemory;
import edu.htl3r.schoolplanner.backend.database.Database;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

/**
 * Interface zum Setzen der Daten.<br>
 * Implementierende Klassen muessen bei Erhalt neuer Daten ueber diese benachrichtig werden, um sie passend verwenden zu koennen.<br>
 * Bekannte Implementierungen: {@link Database}, {@link InternalMemory}
 */
public interface MasterdataStore {
	
	/**
	 * Setzt die Schulklassenliste. Wird benoetigt, falls sie z.B. aus dem Netzwerk neu Uebertragen wurden, um sie mit dem lokalen Cache abzugleichen.
	 * @param schoolClasses Neue Liste mit Schulklassen 
	 */
	public void setSchoolClassList(List<SchoolClass> schoolClasses);
	
	
	/**
	 * Setzt die Lehrerliste. Wird benoetigt, falls sie z.B. aus dem Netzwerk neu Uebertragen wurden, um sie mit dem lokalen Cache abzugleichen.
	 * @param schoolTeachers Neue Liste mit Lehrern 
	 */
	public void setSchoolTeacherList(List<SchoolTeacher> schoolTeachers);

	/**
	 * Setzt die Faecherliste. Wird benoetigt, falls sie z.B. aus dem Netzwerk neu Uebertragen wurden, um sie mit dem lokalen Cache abzugleichen.
	 * @param schoolSubjects Neue Liste mit Faechern 
	 */
	public void setSchoolSubjectList(List<SchoolSubject> schoolSubjects);

	/**
	 * Setzt die Raumliste. Wird benoetigt, falls sie z.B. aus dem Netzwerk neu Uebertragen wurden, um sie mit dem lokalen Cache abzugleichen.
	 * @param schoolRooms Neue Liste mit Raeumen 
	 */
	public void setSchoolRoomList(List<SchoolRoom> schoolRooms);

	/**
	 * Setzt die Liste von leeren Tagen.
	 * @param holidayList Liste mit freien Tagen
	 */
	public void setSchoolHolidayList(List<SchoolHoliday> holidayList);

	/**
	 * Setzt den Zeitraster.
	 * @param timegrid Zeitraster, der gesetzt werden soll
	 */
	public void setTimegrid(Timegrid timegrid);
	
}
