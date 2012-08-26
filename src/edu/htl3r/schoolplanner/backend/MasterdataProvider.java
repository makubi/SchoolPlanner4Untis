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
import java.util.List;

import edu.htl3r.schoolplanner.backend.cache.InternalMemory;
import edu.htl3r.schoolplanner.backend.database.Database;
import edu.htl3r.schoolplanner.backend.network.json.JSONNetwork;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

/**
 * Interface zu den Datenabfragen, die vom Backend zur Verfuegung gestellt werden.<br>
 * Bekannte Implementierungen: {@link JSONNetwork}, {@link Database}, {@link InternalMemory}, {@link ExternalDataLoader}
 */
public interface MasterdataProvider {

	/**
	 * Liefert alle Schulklassen als Liste.
	 * @return Alle Schulklassen als Liste oder 'null', wenn nicht
	 *         gefunden
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public List<SchoolClass> getSchoolClassList();

	/**
	 * Liefert alle Lehrer als Liste. 
	 * @return Alle Lehrer als Liste oder 'null', wenn nicht
	 *         gefunden
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public List<SchoolTeacher> getSchoolTeacherList();

	/**
	 * Liefert alle Raume als Liste. 
	 * @return Alle Raume als Liste oder 'null', wenn nicht
	 *         gefunden
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public List<SchoolRoom> getSchoolRoomList();

	/**
	 * Liefert alle Unterrichtsfaecher als Liste. 
	 * @return Alle Unterrichtsfaecher als Liste oder 'null', wenn nicht
	 *         gefunden
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public List<SchoolSubject> getSchoolSubjectList();

	/**
	 * Liefert alle freien Tage als Liste. 
	 * @return Alle freien Tag als Liste oder 'null', wenn nicht
	 *         gefunden
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public List<SchoolHoliday> getSchoolHolidayList();

	/**
	 * Liefert den Zeitraster. 
	 * @return Den Zeitraster oder 'null', wenn nicht
	 *         gefunden
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public Timegrid getTimegrid();
	
}
