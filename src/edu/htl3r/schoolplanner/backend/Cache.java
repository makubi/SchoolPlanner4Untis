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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
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
 * Liefert Daten. Diese werden aus dem internen Speicher und wenn nicht vorhanden, von einer externen Datenquelle geholt.
 * @see InternalMemory
 * @see ExternalDataLoader
 */
public class Cache implements DataConnection, InternalData {
	
	private InternalMemory internalMemory;
	private ExternalDataLoader externalDataLoader;
	
	
	/**
	 * Initialisiert den internen Speicher und die externen Datenquelle.
	 * @see InternalMemory
	 * @see ExternalDataLoader
	 */
	public void init() {
		internalMemory = new InternalMemory();
		externalDataLoader = new ExternalDataLoader();
	}

	@Override
	public List<SchoolClass> getSchoolClassList() throws IOException {
		List<SchoolClass> schoolClassList;
		
		// Check internal memory
		if((schoolClassList = internalMemory.getSchoolClassList()) != null) {
			Log.d("DataSource", "schoolClassList: InternalMemory");
			return schoolClassList;
		}
		
		// Check external data sources
		if((schoolClassList = externalDataLoader.getSchoolClassList()) != null) {
			internalMemory.setSchoolClassList(schoolClassList);
			return schoolClassList;
		}
		
		Log.d("DataSource", "schoolClassList: -");
		return null;
	}

	@Override
	public List<SchoolTeacher> getSchoolTeacherList() throws IOException {
		List<SchoolTeacher> schoolTeacherList;
		
		// Check internal memory
		if((schoolTeacherList = internalMemory.getSchoolTeacherList()) != null) {
			Log.d("DataSource", "schoolTeacherList: InternalMemory");
			return schoolTeacherList;
		}
		
		// Check external data sources
		if((schoolTeacherList = externalDataLoader.getSchoolTeacherList()) != null) {
			internalMemory.setSchoolTeacherList(schoolTeacherList);
			return schoolTeacherList;
		}
		
		Log.d("DataSource", "schoolTeacherList: -");
		return null;
	}

	@Override
	public List<SchoolRoom> getSchoolRoomList() throws IOException {
		List<SchoolRoom> schoolRoomList;
		
		// Check internal memory
		if((schoolRoomList = internalMemory.getSchoolRoomList()) != null) {
			Log.d("DataSource", "schoolRoomList: InternalMemory");
			return schoolRoomList;
		}
		
		// Check external data sources
		if((schoolRoomList = externalDataLoader.getSchoolRoomList()) != null) {
			internalMemory.setSchoolRoomList(schoolRoomList);
			return schoolRoomList;
		}
		
		Log.d("DataSource", "schoolRoomList: -");
		return null;
	}

	@Override
	public List<SchoolSubject> getSchoolSubjectList() throws IOException {
		List<SchoolSubject> schoolSubjectList;
		
		// Check internal memory
		if((schoolSubjectList = internalMemory.getSchoolSubjectList()) != null) {
			Log.d("DataSource", "schoolSubjectList: InternalMemory");
			return schoolSubjectList;
		}
		
		// Check external data sources
		if((schoolSubjectList = externalDataLoader.getSchoolSubjectList()) != null) {
			internalMemory.setSchoolSubjectList(schoolSubjectList);
			return schoolSubjectList;
		}
		
		Log.d("DataSource", "schoolSubjectList: -");
		return null;
	}

	@Override
	public List<SchoolHoliday> getSchoolHolidayList() throws IOException {
		List<SchoolHoliday> schoolHolidayList;
		
		// Check internal memory
		if((schoolHolidayList = internalMemory.getSchoolHolidayList()) != null) {
			Log.d("DataSource", "schoolHolidayList: InternalMemory");
			return schoolHolidayList;
		}
		
		// Check external data sources
		if((schoolHolidayList = externalDataLoader.getSchoolHolidayList()) != null) {
			internalMemory.setSchoolHolidayList(schoolHolidayList);
			return schoolHolidayList;
		}
		
		Log.d("DataSource", "schoolHolidayList: -");
		return null;
	}

	@Override
	public List<SchoolTestType> getSchoolTestTypeList() {
		// TODO: From interal / external data source
		
		Log.d("DataSource", "schoolTestTypeList: Dummy data");
		ArrayList<SchoolTestType> al = new ArrayList<SchoolTestType>();
		
		SchoolTestType t1 = new SchoolTestType();
		t1.setTitle("SA");
		al.add(t1);
		SchoolTestType t2 = new SchoolTestType();
		t2.setTitle("PMÜ");
		al.add(t2);
		SchoolTestType t3 = new SchoolTestType();
		t3.setTitle("SMÜ");
		al.add(t3);
		SchoolTestType t4 = new SchoolTestType();
		t4.setTitle("Test");
		al.add(t4);
		
		return al;
	}

	@Override
	public Timegrid getTimegrid() throws IOException {
		Timegrid timegrid;
		
		// Check internal memory
		if((timegrid = internalMemory.getTimegrid()) != null) {
			Log.d("DataSource", "timegrid: InternalMemory");
			return timegrid;
		}
		
		// Check external data sources
		if((timegrid = externalDataLoader.getTimegrid()) != null) {
			internalMemory.setTimegrid(timegrid);
			return timegrid;
		}
		
		Log.d("DataSource", "timegrid: -");
		return null;
	}

	@Override
	@Deprecated
	public List<SchoolTest> getSchoolTestList() {
		List<SchoolTest> schoolTestList;
		
		// Check internal memory
		if((schoolTestList = internalMemory.getSchoolTestList()) != null) {
			Log.d("DataSource", "schoolTestList: InternalMemory");
			return schoolTestList;
		}
		
		// Check external data sources
		if((schoolTestList = externalDataLoader.getSchoolTestList()) != null) {
			internalMemory.setSchoolTestList(schoolTestList);
			return schoolTestList;
		}
		
		Log.d("DataSource", "schoolTestList: -");
		return null;
	}

	@Override
	public List<Lesson> getLessons(ViewType view, Calendar date) throws IOException {
		List<Lesson> lessonList;
		
		if((lessonList = internalMemory.getLessons(view, date)) != null) {
			Log.d("DataSource", "lessons: InternalMemory");
			return lessonList;
		}
		
		if((lessonList = externalDataLoader.getLessons(view, date)) != null) {
			internalMemory.setLessons(view, lessonList);
			return lessonList;
		}
		
		Log.d("DataSource", "lessons: -");
		return null;
	}
	
	@Override
	public Map<String, List<Lesson>> getLessons(ViewType view,
			Calendar startDate, Calendar endDate) throws IOException {
		Map<String, List<Lesson>> lessonMap;
	
		
		if((lessonMap = internalMemory.getLessons(view, startDate, endDate)) != null) {
			Log.d("DataSource", "lessons: InternalMemory");
			return lessonMap;
		}
		
		if((lessonMap = externalDataLoader.getLessons(view, startDate, endDate)) != null) {
			internalMemory.setLessons(view, startDate, endDate, lessonMap);
			return lessonMap;
		}
		
		Log.d("DataSource", "lessons: -");
		return null;
	}

	@Override
	public List<Lesson> getMergedLessons(ViewType view, Calendar date) throws IOException {
		List<Lesson> mergedLessonList;
		
		if((mergedLessonList = internalMemory.getMergedLessons(view, date)) != null) {
			Log.d("DataSource", "mergedLessons: InternalMemory");
			return mergedLessonList;
		}
		
		if((mergedLessonList = externalDataLoader.getMergedLessons(view, date)) != null) {
			internalMemory.setMergedLessons(view, date, mergedLessonList);
			return mergedLessonList;
		}
		
		Log.d("DataSource", "mergedLessons: -");
		return null;
	}
	
	@Override
	public Map<String, List<Lesson>> getMergedLessons(ViewType view, Calendar startDate,
			Calendar endDate) throws IOException {
		Map<String, List<Lesson>> mergedLessonMap;

		
		if((mergedLessonMap = internalMemory.getMergedLessons(view, startDate, endDate)) != null) {
			Log.d("DataSource", "mergedLessons: InternalMemory");
			return mergedLessonMap;
		}
		
		if((mergedLessonMap = externalDataLoader.getMergedLessons(view, startDate, endDate)) != null) {
			internalMemory.setMergedLessons(view, startDate, endDate, mergedLessonMap);
			return mergedLessonMap;
		}
		
		Log.d("DataSource", "mergedLessons: -");
		return null;
	}

	@Override
	public void setPreferences(Preferences preferences) {
		externalDataLoader.setPreferences(preferences);
	}

	@Override
	public void networkAvailabilityChanged(boolean networkAvailable) {
		externalDataLoader.networkAvailabilityChanged(networkAvailable);
	}

	@Override
	public boolean authenticate() throws IOException {
		return externalDataLoader.authenticate();
	}

	@Override
	public void resyncMasterData() throws IOException {
		Log.d("METHOD_CALL","Cache.resyncMasterData()");
		
		MasterData masterData = externalDataLoader.resyncMasterData();
		
		internalMemory.setSchoolClassList(masterData.getSchoolClassList());
		internalMemory.setSchoolTeacherList(masterData.getSchoolTeacherList());
		internalMemory.setSchoolRoomList(masterData.getSchoolRoomList());
		internalMemory.setSchoolSubjectList(masterData.getSchoolSubjectList());
		internalMemory.setSchoolHolidayList(masterData.getSchoolHolidayList());
		internalMemory.setSchoolTestTypeList(masterData.getSchoolTestTypeList());
		internalMemory.setTimegrid(masterData.getTimegrid());
		
		// TODO: Weitere Stammdaten aktualisieren
	}

	@Override
	public Map<String, List<Lesson>> getMergedLessons(ViewType view,
			Calendar startDate, Calendar endDate, boolean forceNetwork)
			throws IOException {
		return forceNetwork ? externalDataLoader.getMergedLessons(view, startDate, endDate, forceNetwork) : getMergedLessons(view, startDate, endDate);
	}

	@Override
	public List<Lesson> getMergedLessons(ViewType view, Calendar date,
			boolean forceNetwork) throws IOException {
		return forceNetwork ? externalDataLoader.getMergedLessons(view, date, forceNetwork) : getMergedLessons(view, date);
	}

	@Override
	public List<SchoolTest> getSchoolTestList(ViewType view, Calendar startDate,
			Calendar endDate) throws IOException {
		List<SchoolTest> schoolTestList;
		
		if((schoolTestList = internalMemory.getSchoolTestList(view, startDate, endDate)) != null) {
			Log.d("DataSource", "schoolTests: InternalMemory");
			return schoolTestList;
		}
		
		if((schoolTestList = externalDataLoader.getSchoolTestList(view, startDate, endDate)) != null) {
			internalMemory.setSchoolTestList(schoolTestList);
			return schoolTestList;
		}
		
		Log.d("DataSource", "schoolTests: -");
		
		return new ArrayList<SchoolTest>();
	}

	@Override
	public void saveSchoolTest(SchoolTest schoolTest) {
		internalMemory.saveSchoolTest(schoolTest);
		externalDataLoader.saveSchoolTest(schoolTest);
	}

	@Override
	public HashMap<String, Authentication> getAllPresets() {
		return externalDataLoader.getAllPresets();
	}

	@Override
	public void savePreset(String title, Authentication auth) {
		externalDataLoader.savePreset(title, auth);
	}

	@Override
	public void deletePreset(String title) {
		externalDataLoader.deletePreset(title);
		
	}
}
