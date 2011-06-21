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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import edu.htl3r.schoolplanner.backend.localdata.LocalData;
import edu.htl3r.schoolplanner.backend.network.JSONNetwork;
import edu.htl3r.schoolplanner.backend.preferences.Authentication;
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
 * Laedt Daten aus externen Datenquellen, wie der lokalen Datenbank oder dem Netzwerk.
 * @see LocalData
 * @see JSONNetwork
 */
public class ExternalDataLoader implements DataProvider, InternalData {

	private LocalData database = new LocalData();
	private JSONNetwork network = new JSONNetwork();

	private boolean networkAvailable = true;

	@Override
	public List<SchoolClass> getSchoolClassList() throws IOException {
		List<SchoolClass> schoolClassList;

		// Check database
		if ((schoolClassList = database.getSchoolClassList()) != null) {
			Log.v("DataSource", "schoolClassList: Database");
			return schoolClassList;
		}
		// Check network
		if (networkAvailable) {
			if ((schoolClassList = network.getSchoolClassList()) != null) {
				database.setSchoolClassList(schoolClassList);
				Log.v("DataSource", "schoolClassList: Network");
				return schoolClassList;
			}
		}

		return null;
	}

	@Override
	public List<SchoolTeacher> getSchoolTeacherList() throws IOException {
		List<SchoolTeacher> schoolTeacherList;

		// Check database
		if ((schoolTeacherList = database.getSchoolTeacherList()) != null) {
			Log.v("DataSource", "schoolTeacherList: Database");
			return schoolTeacherList;
		}
		// Check network
		if (networkAvailable) {
			if ((schoolTeacherList = network.getSchoolTeacherList()) != null) {
				database.setSchoolTeacherList(schoolTeacherList);
				Log.v("DataSource", "schoolTeacherList: Network");
				return schoolTeacherList;
			}
		}

		return null;
	}

	@Override
	public List<SchoolRoom> getSchoolRoomList() throws IOException {
		List<SchoolRoom> schoolRoomList;

		// Check database
		if ((schoolRoomList = database.getSchoolRoomList()) != null) {
			Log.v("DataSource", "schoolRoomList: Database");
			return schoolRoomList;
		}
		// Check network
		if (networkAvailable) {
			if ((schoolRoomList = network.getSchoolRoomList()) != null) {
				database.setSchoolRoomList(schoolRoomList);
				Log.v("DataSource", "schoolRoomList: Network");
				return schoolRoomList;
			}
		}

		return null;
	}

	@Override
	public List<SchoolSubject> getSchoolSubjectList() throws IOException {
		List<SchoolSubject> schoolSubjectList;

		// Check database
		if ((schoolSubjectList = database.getSchoolSubjectList()) != null) {
			Log.v("DataSource", "schoolSubjectList: Database");
			return schoolSubjectList;
		}
		// Check network
		if (networkAvailable) {
			if ((schoolSubjectList = network.getSchoolSubjectList()) != null) {
				database.setSchoolSubjectList(schoolSubjectList);
				Log.v("DataSource", "schoolSubjectList: Network");
				return schoolSubjectList;
			}
		}

		return null;
	}

	@Override
	public List<SchoolHoliday> getSchoolHolidayList() throws IOException {
		List<SchoolHoliday> schoolHolidayList;

		// Check database
		if ((schoolHolidayList = database.getSchoolHolidayList()) != null) {
			Log.v("DataSource", "schoolHolidayList: Database");
			return schoolHolidayList;
		}
		// Check network
		if (networkAvailable) {
			if ((schoolHolidayList = network.getSchoolHolidayList()) != null) {
				database.setSchoolHolidayList(schoolHolidayList);
				Log.v("DataSource", "schoolHolidayList: Network");
				return schoolHolidayList;
			}
		}

		return null;
	}

	@Override
	public List<SchoolTestType> getSchoolTestTypeList() {
		List<SchoolTestType> schoolTestTypeList;

		// Check database
		if ((schoolTestTypeList = database.getSchoolTestTypeList()) != null) {
			Log.v("DataSource", "schoolTestTypeList: Database");
			return schoolTestTypeList;
		}
		// Check network
		if (networkAvailable) {
			if ((schoolTestTypeList = network.getSchoolTestTypeList()) != null) {
				database.setSchoolTestTypeList(schoolTestTypeList);
				Log.v("DataSource", "schoolTestTypeList: Network");
				return schoolTestTypeList;
			}
		}

		return null;
	}

	@Override
	public Timegrid getTimegrid() throws IOException {
		Timegrid timegrid;

		// Check database
		if ((timegrid = database.getTimegrid()) != null) {
			Log.v("DataSource", "timegrid: Database");
			return timegrid;
		}
		// Check network
		if (networkAvailable) {
			if ((timegrid = network.getTimegrid()) != null) {
				database.setTimegrid(timegrid);
				Log.v("DataSource", "timegrid: Network");
				return timegrid;
			}
		}

		return null;
	}

	@Override
	public List<SchoolTest> getSchoolTestList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Lesson> getLessons(ViewType view, Calendar date) throws IOException {
		List<Lesson> lessons;

		// Check network
		if (networkAvailable) {
			if ((lessons = network.getLessons(view, date)) != null) {
				database.setLessons(view, lessons);
				Log.v("DataSource", "lessons: Network");
				return lessons;
			}
		}

		// Check database
		if ((lessons = database.getLessons(view, date)) != null) {
			Log.v("DataSource", "lessons: Database");
			return lessons;
		}

		return null;
	}

	@Override
	public Map<String, List<Lesson>> getLessons(ViewType view, Calendar startDate, Calendar endDate) throws IOException {
		Map<String, List<Lesson>> lessonMap;

		// Check network
		if (networkAvailable) {
			if ((lessonMap = network.getLessons(view, startDate, endDate)) != null) {
				database.setLessons(view, startDate, endDate, lessonMap);
				Log.v("DataSource", "mergedLessons: Network");
				return lessonMap;
			}
		}

		// Check database
		if ((lessonMap = database.getLessons(view, startDate, endDate)) != null) {
			Log.v("DataSource", "mergedLessons: Database");
			return lessonMap;
		}

		return null;
	}

	@Override
	public Map<String, List<Lesson>> getMergedLessons(ViewType view, Calendar startDate, Calendar endDate) throws IOException {
		Map<String, List<Lesson>> mergedLessonMap;

		// Check network
		if (networkAvailable) {
			if ((mergedLessonMap = network.getMergedLessons(view, startDate, endDate)) != null) {
				database.setMergedLessons(view, startDate, endDate, mergedLessonMap);
				Log.v("DataSource", "mergedLessons: Network");
				return mergedLessonMap;
			}
		}

		// Check database
		if ((mergedLessonMap = database.getMergedLessons(view, startDate, endDate)) != null) {
			Log.v("DataSource", "mergedLessons: Database");
			return mergedLessonMap;
		}

		return null;
	}

	@Override
	public List<Lesson> getMergedLessons(ViewType view, Calendar date) throws IOException {
		List<Lesson> mergedLessons;

		// Check network
		if (networkAvailable) {
			if ((mergedLessons = network.getMergedLessons(view, date)) != null) {
				database.setMergedLessons(view, date, mergedLessons);
				Log.v("DataSource", "mergedLessons: Network");
				return mergedLessons;
			}
		}

		// Check database
		if ((mergedLessons = database.getMergedLessons(view, date)) != null) {
			Log.v("DataSource", "mergedLessons: Database");
			return mergedLessons;
		}

		return null;
	}

	/**
	 * Setzt die Preferences fuer das Netzwerk neu.
	 * @param authentications Preferences, die gesetzt werden sollen
	 */
	public void setLoginCredentials(Authentication authentications) {
		network.setLoginCredentials(authentications);
	}

	/**
	 * Setzt den Status des Netzwerks bei Aenderung dessen.
	 * @param networkAvailable true, wenn das Netzwerk verfuegbar ist, sonst false
	 */
	public void networkAvailabilityChanged(boolean networkAvailable) {
		this.networkAvailable = networkAvailable;
	}

	/**
	 * Authentifiziert sich mit dem Untis-Server ueber das Netzwerk.
	 * @return true, wenn die Authentifizierung erfolgreich war, sonst false
	 * @throws IOException Wenn ein Problem waehrend der Netzwerkanfrage auftritt
	 */
	public boolean authenticate() throws IOException {
		return network.authenticate();
	}

	/**
	 * Laedt die neusten Stammdaten herunter und aktualisiert danach die Datenbank.<br>
	 * Stammdaten, die zur Zeit aktualisiert werden: Liste der Schuklasse, Liste der Lehrer, Liste der Raeume, Liste der Faecher, Stundenraster. 
	 * @return Ein Objekt, das die Stammdaten, die aktualisiert wurden, enthaelt.
	 * @throws IOException Wenn waehrend dem Abruf der Daten ein Fehler auftritt
	 */
	public MasterData resyncMasterData() throws IOException {
		List<SchoolClass> schoolClassList = network.getSchoolClassList();
		List<SchoolTeacher> schoolTeacherList = network.getSchoolTeacherList();
		List<SchoolRoom> schoolRoomList = network.getSchoolRoomList();
		List<SchoolSubject> schoolSubjectList = network.getSchoolSubjectList();
		
		List<SchoolHoliday> schoolHolidayList = network.getSchoolHolidayList();
		List<SchoolTestType> schoolTestTypeList = network.getSchoolTestTypeList();
		
		Timegrid timegrid = network.getTimegrid();
				
		
		MasterData masterData = new MasterData();
		masterData.setSchoolClassList(schoolClassList);
		masterData.setSchoolRoomList(schoolRoomList);
		masterData.setSchoolSubjectList(schoolSubjectList);
		masterData.setSchoolTeacherList(schoolTeacherList);
		masterData.setSchoolHolidayList(schoolHolidayList);
		masterData.setSchoolTestTypeList(schoolTestTypeList);
		masterData.setTimegrid(timegrid);

		database.setSchoolClassList(schoolClassList);
		database.setSchoolTeacherList(schoolTeacherList);
		database.setSchoolRoomList(schoolRoomList);
		database.setSchoolSubjectList(schoolSubjectList);
		database.setSchoolHolidayList(schoolHolidayList);
		database.setSchoolTestTypeList(schoolTestTypeList);
		database.setTimegrid(timegrid);

		// TODO: Weitere Stammdaten aktualisieren

		return masterData;
	}	
	
	
	/**
	 * Siehe {@link DataConnection#getMergedLessons(ViewType, Calendar, Calendar, boolean)}.
	 * @param view Initialisierter ViewType, fuer den der Stundenplan abgerufen werden soll
	 * @param startDate Das Start-Datum der Abfrage (inklusive)
	 * @param endDate Das End-Datum der Abfrage (inklusive)
	 * @param forceNetwork Wenn true, wird der Stundenplan direkt aus dem Netzwerk heruntergeladen
	 * @return Eine Map mit Datum-Strings und den dazupassenden Stundenlisten
	 * @throws IOException Wenn waehrend der Uebertragung ein Fehler auftritt
	 */
	public Map<String, List<Lesson>> getMergedLessons(ViewType view,
			Calendar startDate, Calendar endDate, boolean forceNetwork)
			throws IOException {
		return forceNetwork ? network.getMergedLessons(view, startDate, endDate) : getMergedLessons(view, startDate, endDate);
	}

	/**
	 * Siehe {@link DataConnection#getMergedLessons(ViewType, Calendar, boolean)}.
	 * @param view Initialisierter ViewType, fuer den der Stundenplan abgerufen werden soll
	 * @param date Das Datum, fuer das der Stundenplan abgefragt werden soll (inklusive)
	 * @param forceNetwork Wenn true, wird der Stundenplan direkt aus dem Netzwerk heruntergeladen
	 * @return Eine Map mit Datum-Strings und den dazupassenden Stundenlisten
	 * @throws IOException Wenn waehrend der Uebertragung ein Fehler auftritt
	 */
	public List<Lesson> getMergedLessons(ViewType view, Calendar date,
			boolean forceNetwork) throws IOException {
		return forceNetwork ? network.getMergedLessons(view, date) : getMergedLessons(view, date);
	}

	@Override
	public List<SchoolTest> getSchoolTestList(ViewType view, Calendar startDate,
			Calendar endDate) throws IOException {
		List<SchoolTest> schoolTestList;
		
		// Check network
		if (networkAvailable) {
			if ((schoolTestList = network.getSchoolTestList(view, startDate, endDate)) != null) {
				database.setSchoolTestList(schoolTestList);
				Log.v("DataSource", "schoolTests: Network");
				return schoolTestList;
			}
		}

		// Check database
		if ((schoolTestList = database.getSchoolTestList(view, startDate, endDate)) != null) {
			Log.v("DataSource", "schoolTest: Database");
			return schoolTestList;
		}

		return null;
	}

	@Override
	public void saveSchoolTest(SchoolTest schoolTest) {
		database.saveSchoolTest(schoolTest);
		network.saveSchoolTest(schoolTest);
	}

	@Override
	public HashMap<String, Authentication> getAllPresets() {
		return database.getAllPresets();
	}

	@Override
	public void savePreset(String title, Authentication auth) {
		database.savePreset(title, auth);
	}

	@Override
	public void deletePreset(String title) {
		database.deletePreset(title);
		
	}

	public void setCache(Cache cache) {
		database.setCache(cache);
		network.setCache(cache);
	}
	
}
