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

import edu.htl3r.schoolplanner.backend.localdata.LocalData;
import edu.htl3r.schoolplanner.backend.network.JSONNetwork;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTest;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTestType;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

/**
 * Interface zu den Datenabfragen, die vom Backend zur Verfuegung gestellt werden.<br>
 * Bekannte Implementierungen: {@link JSONNetwork}, {@link LocalData}, {@link InternalMemory}, {@link ExternalDataLoader}
 */
public interface DataProvider extends ExternalDataSetter {

	/**
	 * Liefert alle Schulklassen als Liste.
	 * @return Alle Schulklassen als Liste oder 'null', wenn nicht
	 *         gefunden
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public List<SchoolClass> getSchoolClassList() throws IOException;

	/**
	 * Liefert alle Lehrer als Liste. 
	 * @return Alle Lehrer als Liste oder 'null', wenn nicht
	 *         gefunden
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public List<SchoolTeacher> getSchoolTeacherList() throws IOException;

	/**
	 * Liefert alle Raume als Liste. 
	 * @return Alle Raume als Liste oder 'null', wenn nicht
	 *         gefunden
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public List<SchoolRoom> getSchoolRoomList() throws IOException;

	/**
	 * Liefert alle Unterrichtsfaecher als Liste. 
	 * @return Alle Unterrichtsfaecher als Liste oder 'null', wenn nicht
	 *         gefunden
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public List<SchoolSubject> getSchoolSubjectList() throws IOException;

	/**
	 * Liefert alle freien Tage als Liste. 
	 * @return Alle freien Tag als Liste oder 'null', wenn nicht
	 *         gefunden
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public List<SchoolHoliday> getSchoolHolidayList() throws IOException;

	/**
	 * Liefert alle Testarten als Liste. 
	 * @return Alle Testarten als Liste oder 'null', wenn nicht
	 *         gefunden
	 */
	public List<SchoolTestType> getSchoolTestTypeList() throws IOException;

	/**
	 * Liefert den Zeitraster. 
	 * @return Den Zeitraster oder 'null', wenn nicht
	 *         gefunden
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public Timegrid getTimegrid() throws IOException;

	/**
	 * Liefert alle Tests als Liste. 
	 * @return Alle Tests als Liste oder 'null', wenn nicht
	 *         gefunden
	 * @throws IOException 
	 */
	@Deprecated
	public List<SchoolTest> getSchoolTestList() throws IOException;

	/**
	 * Liefert eine Liste mit Stunden anhand der uebergebenen Params. Liefert Stunden zu genau einem Tag.
	 * @param view Initialisierter View mit Typ und Wert
	 * @param date Datum, zu dem die Stundenliste abgefragt werden soll
	 * @return Liste mit Stunden oder 'null', wenn diese nicht gefunden werden konnten
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	@Deprecated
	public List<Lesson> getLessons(ViewType view, Calendar date) throws IOException;
	
	@Deprecated
	Map<String, List<Lesson>> getLessons(ViewType view, Calendar startDate,
	Calendar endDate) throws IOException;

	/**
	 * Lieferte eine Map mit den jeweiligen Listen mit Lessons zu mehreren Daten. Pro Tag wird eine Liste mit Lessons in der zurueckgegebenen Map gespeichert.<br>
	 * Map["20110203"] liefert z.B. die Liste fuer den ersten abgefragten Tag, Map["20110203"].[0] liefert die erste Lesson fuer den ersten Tag, usw.<br>
	 * <b>Die Listen mit Stunden koennen mit dem Datum als String in Form von YYYYMMDD abgerufen werden.</b>
	 * Diese Methode fuegt die Unterrichtseinheiten so zusammen, dass die GUI sie einfach anzeigen kann.<br>
	 * Gibt es an einem Tag keinen Unterricht, ist dieser Tag 'null'. 
	 * @param view Initialisierter View mit Typ und Wert
	 * @param startDate Anfangsdatum (von), ab dem die Stundenliste abgefragt werden soll (inklusive)
	 * @param endDate Enddatum (bis), bis zu dem die Stundenliste abgefragt werden soll (inklusive)
	 * @return Liste mit Stunden oder 'null', wenn diese nicht gefunden werden konnten
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public Map<String, List<Lesson>> getMergedLessons(ViewType view, Calendar startDate, Calendar endDate) throws IOException;
	
	/**
	 * Liefert eine Liste mit Stunden anhand der uebergebenen Params. Liefert Stunden zu genau einem Tag.
	 * Diese Methode fuegt die Unterrichtseinheiten so zusammen, dass die GUI sie einfach anzeigen kann.
	 * @param view Initialisierter View mit Typ und Wert
	 * @param date Datum, zu dem die Stundenliste abgefragt werden soll
	 * @return Liste mit Stunden oder 'null', wenn diese nicht gefunden werden konnten
	 * @throws IOException Wird geworfen, falls beim Datenabruf ein Fehler auftritt
	 */
	public List<Lesson> getMergedLessons(ViewType view, Calendar date) throws IOException;
	
	/**
	 * Liefert eine Liste mit SchoolTests fuer die angefragte View und die angefragten Daten.
	 * @param view Initialisierte View, fuer die SchoolTests abgefragt werden sollen
	 * @param startDate Startdatum des Zeitraums, fuer das die SchoolTests abgefragt werden sollen (inklusive)
	 * @param endDate Enddatum des Zeitraums, fuer das die SchoolTests abgefragt werden sollen (inklusive)
	 * @return Eine Liste mit SchoolTests
	 */
	public List<SchoolTest> getSchoolTestList(ViewType view, Calendar startDate, Calendar endDate) throws IOException;
	
}
