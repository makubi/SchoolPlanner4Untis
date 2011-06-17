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

package edu.htl3r.schoolplanner.backend.localdata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.backend.Authentication;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.DataProvider;
import edu.htl3r.schoolplanner.backend.DataStore;
import edu.htl3r.schoolplanner.backend.InternalData;
import edu.htl3r.schoolplanner.backend.network.WebUntis;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTest;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTestType;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCode;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCodeCreator;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonTypeCreator;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeCancelled;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeBreakSupervision;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeExamination;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeOfficeHour;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeStandby;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

/**
 * Implementierung einer Datenbank Zugriff auf eine Datenbank per SQLite
 */
public class LocalData implements DataStore, DataProvider, InternalData {

	private final String LOGTAG = "Database";
	private final Context context = SchoolplannerContext.context;
	private final SQLiteOpenHelper dbHelper = new DatabaseHelper(context);
	private final SQLiteDatabase database = dbHelper.getWritableDatabase();
	private final Cache cache = SchoolplannerContext.cache;

	/**
	 * Liefert Objekte aus der Datenbank zurueck
	 * 
	 * @param table
	 *            Tabelle, in der die Objekte liegen
	 * @param columns
	 *            Spalten, in denen gesucht wird
	 * @param values
	 *            Werte, nach denen gesucht wird
	 * @return Liste mit den gefundenen Objekten
	 */
	private synchronized List<HashMap<String, String>> getObjects(String table, String[] columns, String[] values) {
		String search = "";
		if (columns != null && values != null) {
			for (int i = 0; i < columns.length; i++) {
				search += columns[i] + " = '" + values[i] + "'";
				if (i != columns.length - 1) {
					search += " and ";
				}
			}
		}
		Cursor cursor = database.query(table, null, search, null, null, null, null);
		if (cursor == null) {
			return null;
		}
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}
		List<HashMap<String, String>> objects = new ArrayList<HashMap<String, String>>();
		while (cursor.moveToNext()) {
			HashMap<String, String> temp = new HashMap<String, String>();
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				temp.put(cursor.getColumnName(i), cursor.getString(i));
			}
			objects.add(temp);
		}
		cursor.close();
		return objects;
	}

	/**
	 * Speichert Objekte in der Datenbank
	 * 
	 * @param table
	 *            Tabelle, in der die Objekte gespeichert werden
	 * @param columns
	 *            Spalten, die gespeichert werden
	 * @param values
	 *            Werte, die gespeichert werden
	 */
	private synchronized void storeObject(String table, String[] columns, String[] values) {
		ContentValues content = new ContentValues();
		for (int i = 0; i < columns.length; i++) {
			content.put(columns[i], values[i]);
		}
		database.insert(table, null, content);
	}

	/**
	 * Entfernt Objekte aus der Datenbank
	 * 
	 * @param table
	 *            Tabelle, aus der Objekte entfernt werden
	 * @param columns
	 *            Spalten, nach denen fuer die Loeschung gesucht wird
	 * @param values
	 *            Werte der Spalten
	 */
	private synchronized void removeObjects(String table, String[] columns, String[] values) {
		String where = "";
		if (columns != null && values != null) {
			for (int i = 0; i < columns.length; i++) {
				where += columns[i] + " = '" + values[i] + "'";
				if (i != columns.length - 1) {
					where += " and ";
				}
			}
		}
		database.delete(table, where, null);
	}

	@Override
	public List<SchoolClass> getSchoolClassList() {
		List<HashMap<String, String>> objects = getObjects(SchoolClass.class.getSimpleName(), null, null);
		if (objects == null) {
			return null;
		}
		List<SchoolClass> result = new ArrayList<SchoolClass>();
		for (HashMap<String, String> object : objects) {
			SchoolClass tmp = new SchoolClass();
			tmp.setId(Integer.parseInt(object.get("id")));
			tmp.setName(object.get("name"));
			tmp.setLongName(object.get("longname"));
			tmp.setForeColor(object.get("forecolor"));
			tmp.setBackColor(object.get("backcolor"));
			result.add(tmp);
		}
		return result;
	}

	@Override
	public void setSchoolClassList(final List<SchoolClass> schoolClasses) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				writeSchoolClassList(schoolClasses);
			}
		}).start();
	}

	private void writeSchoolClassList(List<SchoolClass> schoolClasses) {
		removeObjects(SchoolClass.class.getSimpleName(), null, null);
		if (schoolClasses != null) {
			for (SchoolClass schoolClass : schoolClasses) {
				String[] columns = { "id", "name", "longname", "forecolor", "backcolor" };
				String[] values = { schoolClass.getId() + "", schoolClass.getName(), schoolClass.getLongName(), schoolClass.getForeColor() + "", schoolClass.getBackColor() + "" };
				storeObject(SchoolClass.class.getSimpleName(), columns, values);
			}
		}
	}

	@Override
	public List<SchoolTeacher> getSchoolTeacherList() {
		List<HashMap<String, String>> objects = getObjects(SchoolTeacher.class.getSimpleName(), null, null);
		if (objects == null) {
			return null;
		}
		List<SchoolTeacher> result = new ArrayList<SchoolTeacher>();
		for (HashMap<String, String> object : objects) {
			SchoolTeacher tmp = new SchoolTeacher();
			tmp.setId(Integer.parseInt(object.get("id")));
			tmp.setName(object.get("name"));
			tmp.setLongName(object.get("longname"));
			tmp.setForeColor(object.get("forecolor"));
			tmp.setBackColor(object.get("backcolor"));
			result.add(tmp);
		}
		return result;
	}

	@Override
	public void setSchoolTeacherList(final List<SchoolTeacher> schoolTeachers) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				writeSchoolTeacherList(schoolTeachers);
			}
		}).start();
	}

	private void writeSchoolTeacherList(List<SchoolTeacher> schoolTeachers) {
		removeObjects(SchoolTeacher.class.getSimpleName(), null, null);
		if (schoolTeachers != null) {
			for (SchoolTeacher schoolTeacher : schoolTeachers) {
				String[] columns = { "id", "name", "longname", "forecolor", "backcolor" };
				String[] values = { schoolTeacher.getId() + "", schoolTeacher.getName(), schoolTeacher.getLongName(), schoolTeacher.getForeColor() + "", schoolTeacher.getBackColor() + "" };
				storeObject(SchoolTeacher.class.getSimpleName(), columns, values);
			}
		}
	}

	@Override
	public List<SchoolSubject> getSchoolSubjectList() {
		List<HashMap<String, String>> objects = getObjects(SchoolSubject.class.getSimpleName(), null, null);
		if (objects == null) {
			return null;
		}
		List<SchoolSubject> result = new ArrayList<SchoolSubject>();
		for (HashMap<String, String> object : objects) {
			SchoolSubject tmp = new SchoolSubject();
			tmp.setId(Integer.parseInt(object.get("id")));
			tmp.setName(object.get("name"));
			tmp.setLongName(object.get("longname"));
			tmp.setForeColor(object.get("forecolor"));
			tmp.setBackColor(object.get("backcolor"));
			result.add(tmp);
		}
		return result;
	}

	@Override
	public void setSchoolSubjectList(final List<SchoolSubject> schoolSubjects) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				writeSchoolSubjectList(schoolSubjects);
			}
		}).start();
	}

	private void writeSchoolSubjectList(List<SchoolSubject> schoolSubjects) {
		removeObjects(SchoolSubject.class.getSimpleName(), null, null);
		if (schoolSubjects != null) {
			for (SchoolSubject schoolSubject : schoolSubjects) {
				String[] columns = { "id", "name", "longname", "forecolor", "backcolor" };
				String[] values = { schoolSubject.getId() + "", schoolSubject.getName(), schoolSubject.getLongName(), schoolSubject.getForeColor() + "", schoolSubject.getBackColor() + "" };
				storeObject(SchoolSubject.class.getSimpleName(), columns, values);
			}
		}
	}

	@Override
	public List<SchoolRoom> getSchoolRoomList() {
		List<HashMap<String, String>> objects = getObjects(SchoolRoom.class.getSimpleName(), null, null);
		if (objects == null) {
			return null;
		}
		List<SchoolRoom> result = new ArrayList<SchoolRoom>();
		for (HashMap<String, String> object : objects) {
			SchoolRoom tmp = new SchoolRoom();
			tmp.setId(Integer.parseInt(object.get("id")));
			tmp.setName(object.get("name"));
			tmp.setLongName(object.get("longname"));
			tmp.setForeColor(object.get("forecolor"));
			tmp.setBackColor(object.get("backcolor"));
			result.add(tmp);
		}
		return result;
	}

	@Override
	public void setSchoolRoomList(final List<SchoolRoom> schoolRooms) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				writeSchoolRoomList(schoolRooms);
			}
		}).start();
	}

	private void writeSchoolRoomList(List<SchoolRoom> schoolRooms) {
		removeObjects(SchoolRoom.class.getSimpleName(), null, null);
		if (schoolRooms != null) {
			for (SchoolRoom schoolRoom : schoolRooms) {
				String[] columns = { "id", "name", "longname", "forecolor", "backcolor" };
				String[] values = { schoolRoom.getId() + "", schoolRoom.getName(), schoolRoom.getLongName(), schoolRoom.getForeColor() + "", schoolRoom.getBackColor() + "" };
				storeObject(SchoolRoom.class.getSimpleName(), columns, values);
			}
		}
	}

	@Override
	public List<SchoolHoliday> getSchoolHolidayList() {
		List<HashMap<String, String>> objects = getObjects(SchoolHoliday.class.getSimpleName(), null, null);
		if (objects == null) {
			return null;
		}
		List<SchoolHoliday> result = new ArrayList<SchoolHoliday>();
		for (HashMap<String, String> object : objects) {
			SchoolHoliday tmp = new SchoolHoliday();
			tmp.setId(Integer.parseInt(object.get("id")));
			tmp.setName(object.get("name"));
			tmp.setLongName(object.get("longname"));
			Calendar start = Calendar.getInstance();
			start.setTimeInMillis(Long.parseLong(object.get("startdate")));
			tmp.setStartDate(start);
			Calendar end = Calendar.getInstance();
			end.setTimeInMillis(Long.parseLong(object.get("enddate")));
			tmp.setEndDate(end);
			result.add(tmp);
		}
		return result;
	}

	@Override
	public void setSchoolHolidayList(final List<SchoolHoliday> holidays) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				writeSchoolHolidayList(holidays);
			}
		}).start();
	}

	private void writeSchoolHolidayList(List<SchoolHoliday> holidays) {
		removeObjects(SchoolHoliday.class.getSimpleName(), null, null);
		if (holidays != null) {
			for (SchoolHoliday schoolHoliday : holidays) {
				String[] columns = { "id", "name", "longname", "startdate", "enddate" };
				Calendar calendar = schoolHoliday.getStartDate();
				long start = calendar.getTimeInMillis();
				calendar = schoolHoliday.getEndDate();
				long end = calendar.getTimeInMillis();
				String[] values = { schoolHoliday.getId() + "", schoolHoliday.getName(), schoolHoliday.getLongName(), start + "", end + "" };
				storeObject(SchoolHoliday.class.getSimpleName(), columns, values);
			}
		}
	}

	@Override
	public List<SchoolTestType> getSchoolTestTypeList() {
		List<HashMap<String, String>> objects = getObjects(SchoolTestType.class.getSimpleName(), null, null);
		if (objects == null) {
			return null;
		}
		List<SchoolTestType> result = new ArrayList<SchoolTestType>();
		for (HashMap<String, String> object : objects) {
			SchoolTestType tmp = new SchoolTestType();
			tmp.setId(Integer.parseInt(object.get("id")));
			tmp.setTitle(object.get("title"));
			tmp.setDescription(object.get("description"));
			tmp.setColor(Integer.parseInt(object.get("color")));
			result.add(tmp);
		}
		return result;
	}

	@Override
	public void setSchoolTestTypeList(final List<SchoolTestType> testTypes) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				writeSchoolTestTypeList(testTypes);
			}
		}).start();
	}

	private void writeSchoolTestTypeList(List<SchoolTestType> testTypes) {
		removeObjects(SchoolTestType.class.getSimpleName(), null, null);
		if (testTypes != null) {
			for (SchoolTestType schoolTestType : testTypes) {
				String[] columns = { "id", "title", "description", "color" };
				String[] values = { schoolTestType.getId() + "", schoolTestType.getTitle(), schoolTestType.getDescription(), schoolTestType.getColor() + "" };
				storeObject(SchoolTestType.class.getSimpleName(), columns, values);
			}
		}
	}

	@Override
	public Timegrid getTimegrid() {
		long vgetobj = System.currentTimeMillis();
		List<HashMap<String, String>> objects = getObjects(TimegridUnit.class.getSimpleName(), null, null);
		Log.d("SPEEDTESTING", getClass().getSimpleName() + ": getObjects: " + (System.currentTimeMillis() - vgetobj) + " ms");

		if (objects == null) {
			return null;
		}

		HashMap<Integer, List<TimegridUnit>> units = new HashMap<Integer, List<TimegridUnit>>();
		// Verwendung von MONDAY, da SUNDAY normalerweise kein Unterricht
		for (int i = WebUntis.MONDAY; i <= WebUntis.SATURDAY; i++) {
			units.put(i, new ArrayList<TimegridUnit>());
		}

		long start = System.currentTimeMillis();
		// Log.d("Philip", getClass().getSimpleName() + ": startschleife");
		for (HashMap<String, String> object : objects) {
			// Log.d("Philip", getClass().getSimpleName() + ": start inner schleife");
			TimegridUnit tmp = new TimegridUnit();
			Calendar begin = Calendar.getInstance();
			begin.setTimeInMillis(Long.parseLong(object.get("begin")));
			tmp.setBegin(begin);
			Calendar end = Calendar.getInstance();
			end.setTimeInMillis(Long.parseLong(object.get("end")));
			tmp.setEnd(end);
			int day = Integer.parseInt(object.get("day"));
			units.get(day).add(tmp);
			// Log.d("Philip", getClass().getSimpleName() + ": end inner schleife");
		}
		// Log.d("Philip", getClass().getSimpleName() + ": endschleife");

		Timegrid timegrid = new Timegrid();
		// Verwendung von MONDAY, da SUNDAY normalerweise kein Unterricht
		for (int i = WebUntis.MONDAY; i <= WebUntis.SATURDAY; i++) {
			Collections.sort(units.get(i));
			timegrid.setTimegridForDay(i, units.get(i));
		}

		long end = System.currentTimeMillis();
		Log.d("SPEEDTESTING", getClass().getSimpleName() + ": getTimegrid: " + (end - start) + " ms");

		return timegrid;
	}

	@Override
	public void setTimegrid(final Timegrid timegrid) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				writeTimegrid(timegrid);
			}
		}).start();
	}

	private void writeTimegrid(Timegrid timegrid) {
		removeObjects(TimegridUnit.class.getSimpleName(), null, null);
		if (timegrid != null) {
			// Verwendung von MONDAY, da SUNDAY normalerweise kein Unterricht
			for (int i = WebUntis.MONDAY; i <= WebUntis.SATURDAY; i++) {
				if(timegrid.getTimegridForDay(i) != null) {
					for (TimegridUnit timegridUnit : timegrid.getTimegridForDay(i)) {
						String[] columns = { "begin", "end", "day" };
						Calendar calendar = timegridUnit.getBegin();
						long begin = calendar.getTimeInMillis();
						calendar = timegridUnit.getEnd();
						long end = calendar.getTimeInMillis();
						String[] values = { begin + "", end + "", i + "" };
						storeObject(TimegridUnit.class.getSimpleName(), columns, values);
					}
				}
			}
		}
	}

	@Override
	public List<SchoolTest> getSchoolTestList() throws IOException {
		return getSchoolTestList(null, null);
	}

	@Override
	public List<SchoolTest> getSchoolTestList(ViewType view, Calendar startDate, Calendar endDate) throws IOException {
		Calendar date = (Calendar) startDate.clone();
		Calendar endDatePlus1 = (Calendar) endDate.clone();
		endDatePlus1.add(Calendar.DAY_OF_MONTH, 1);
		List<SchoolTest> result = new ArrayList<SchoolTest>();
		while (date.before(endDatePlus1)) {
			List<SchoolTest> tests = getSchoolTestList(view, date);
			if (tests != null) {
				result.addAll(tests);
			}
			date.add(Calendar.DAY_OF_MONTH, 1);
		}
		return result;
	}

	private List<SchoolTest> getSchoolTestList(ViewType view, Calendar date) throws IOException {
		List<SchoolTeacher> teachers = cache.getSchoolTeacherList();
		List<SchoolRoom> rooms = cache.getSchoolRoomList();
		List<SchoolSubject> subjects = cache.getSchoolSubjectList();
		List<SchoolClass> classes = cache.getSchoolClassList();
		List<SchoolTestType> types = cache.getSchoolTestTypeList();

		List<HashMap<String, String>> objects = null;
		if (view != null) {
			// ViewType gesetzt
			String[] columns_tmp = { view.getClass().getSimpleName().replaceFirst("S", "s") + "_id" };
			String[] values_tmp = { view.getId() + "" };
			List<HashMap<String, String>> tmp = getObjects(SchoolTest.class.getSimpleName() + "_" + view.getClass().getSimpleName(), columns_tmp, values_tmp);
//			Log.d("LocalData", "Table: " + SchoolTest.class.getSimpleName() + "_" + view.getClass().getSimpleName());
//			Log.d("LocalData", "Column: " + view.getClass().getSimpleName().replaceFirst("S", "s") + "_id");
//			Log.d("LocalData", "Value: " + view.getId());
			//Log.d("LocalData", "Objects: " + tmp.size()); 
			if(tmp != null) {
				objects = new ArrayList<HashMap<String,String>>();
				for (HashMap<String, String> object : tmp) {
					String[] columns_tests = { "id" };
					String[] values_tests = { object.get("test_id") };
					if(date != null) {
						// Date gesetzt
						columns_tests = new String[]{ "id", "date" };
						Calendar myDate = (Calendar) date.clone();
						myDate.set(Calendar.HOUR_OF_DAY, 0);
						myDate.set(Calendar.MINUTE, 0);
						myDate.set(Calendar.SECOND, 0);
						myDate.set(Calendar.MILLISECOND, 0);
						values_tests = new String[]{ object.get("test_id"), myDate.getTimeInMillis() + "" };
					}
					List<HashMap<String, String>> tests = getObjects(SchoolTest.class.getSimpleName(), columns_tests, values_tests);
					if(tests != null) {
						objects.addAll(tests);
					}
				}
			}
		}
		else {
			// kein ViewType gesetzt
			if(date != null) {
				// Date gesetzt
				String[] columns_tests = { "date" };
				Calendar myDate = (Calendar) date.clone();
				myDate.set(Calendar.HOUR_OF_DAY, 0);
				myDate.set(Calendar.MINUTE, 0);
				myDate.set(Calendar.SECOND, 0);
				myDate.set(Calendar.MILLISECOND, 0);
				String[] values_tests = { myDate.getTimeInMillis() + "" };
				objects = getObjects(SchoolTest.class.getSimpleName(), columns_tests, values_tests);
			}
			else {
				// Date nicht gesetzt
				objects = getObjects(SchoolTest.class.getSimpleName(), null, null);
			}
		}

		if (objects == null) {
			return null;
		}

		List<SchoolTest> result = new ArrayList<SchoolTest>();
		for (HashMap<String, String> object : objects) {
			SchoolTest tmp = new SchoolTest();
			tmp.setId(object.get("id"));
			tmp.setTitle(object.get("title"));
			tmp.setDescription(object.get("description"));
			Calendar testdate = Calendar.getInstance();
			testdate.setTimeInMillis(Long.parseLong(object.get("date")));
			tmp.setDate(testdate);
			Calendar start = Calendar.getInstance();
			start.setTimeInMillis(Long.parseLong(object.get("start")));
			tmp.setStart(start);
			Calendar end = Calendar.getInstance();
			end.setTimeInMillis(Long.parseLong(object.get("end")));
			tmp.setEnd(end);
			int type = Integer.parseInt(object.get("type"));
			for (SchoolTestType testType : types) {
				if (type == testType.getId()) {
					tmp.setType(testType);
					break;
				}
			}
			tmp.setLast_update(Long.parseLong(object.get("last_update")));
			tmp.setLocal(Boolean.parseBoolean(object.get("local")));
			String[] columns = { "test_id" };
			String[] values = { object.get("id") };
			List<HashMap<String, String>> schoolClassObjects = getObjects(SchoolTest.class.getSimpleName() + "_" + SchoolClass.class.getSimpleName(), columns, values);
			List<SchoolClass> schoolClasses = new ArrayList<SchoolClass>();
			if (schoolClassObjects != null) {
				for (HashMap<String, String> schoolClassObject : schoolClassObjects) {
					int current = Integer.parseInt(schoolClassObject.get("schoolClass_id"));
					for (SchoolClass schoolClass : classes) {
						if (current == schoolClass.getId()) {
							schoolClasses.add(schoolClass);
						}
					}
				}
			}
			tmp.setSchoolClasses(schoolClasses);
			List<HashMap<String, String>> schoolTeacherObjects = getObjects(SchoolTest.class.getSimpleName() + "_" + SchoolTeacher.class.getSimpleName(), columns, values);
			List<SchoolTeacher> schoolTeachers = new ArrayList<SchoolTeacher>();
			if (schoolTeacherObjects != null) {
				for (HashMap<String, String> schoolTeacherObject : schoolTeacherObjects) {
					int current = Integer.parseInt(schoolTeacherObject.get("schoolTeacher_id"));
					for (SchoolTeacher schoolTeacher : teachers) {
						if (current == schoolTeacher.getId()) {
							schoolTeachers.add(schoolTeacher);
						}
					}
				}
			}
			tmp.setSchoolTeachers(schoolTeachers);
			List<HashMap<String, String>> schoolRoomObjects = getObjects(SchoolTest.class.getSimpleName() + "_" + SchoolRoom.class.getSimpleName(), columns, values);
			List<SchoolRoom> schoolRooms = new ArrayList<SchoolRoom>();
			if (schoolRoomObjects != null) {
				for (HashMap<String, String> schoolRoomObject : schoolRoomObjects) {
					int current = Integer.parseInt(schoolRoomObject.get("schoolRoom_id"));
					for (SchoolRoom schoolRoom : rooms) {
						if (current == schoolRoom.getId()) {
							schoolRooms.add(schoolRoom);
						}
					}
				}
			}
			tmp.setSchoolRooms(schoolRooms);
			List<HashMap<String, String>> schoolSubjectObjects = getObjects(SchoolTest.class.getSimpleName() + "_" + SchoolSubject.class.getSimpleName(), columns, values);
			List<SchoolSubject> schoolSubjects = new ArrayList<SchoolSubject>();
			if (schoolSubjectObjects != null) {
				for (HashMap<String, String> schoolSubjectObject : schoolSubjectObjects) {
					int current = Integer.parseInt(schoolSubjectObject.get("schoolSubject_id"));
					for (SchoolSubject schoolSubject : subjects) {
						if (current == schoolSubject.getId()) {
							schoolSubjects.add(schoolSubject);
						}
					}
				}
			}
			tmp.setSchoolSubjects(schoolSubjects);
			result.add(tmp);
		}
		return result;
	}

	@Override
	public void saveSchoolTest(SchoolTest schoolTest) {
		List<SchoolTest> tmp = new ArrayList<SchoolTest>();
		tmp.add(schoolTest);
		setSchoolTestList(tmp);
	}

	@Override
	public void setSchoolTestList(final List<SchoolTest> tests) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				writeSchoolTestList(tests);
			}
		}).start();
	}

	private void writeSchoolTestList(List<SchoolTest> tests) {
		if (tests != null) {
			for (SchoolTest schoolTest : tests) {
				String[] removeColumns = { "id" };
				String[] removeValues = { schoolTest.getId() + "" };
				removeObjects(SchoolTest.class.getSimpleName(), removeColumns, removeValues);
				String[] columns = { "id", "title", "description", "date", "start", "end", "type", "last_update", "local" };
				Calendar date = (Calendar) schoolTest.getDate().clone();
				date.set(Calendar.HOUR_OF_DAY, 0);
				date.set(Calendar.MINUTE, 0);
				date.set(Calendar.SECOND, 0);
				date.set(Calendar.MILLISECOND, 0);
				Calendar lastUpdate = Calendar.getInstance();
				long start = 0;
				if (schoolTest.getStart() != null) {
					start = schoolTest.getStart().getTimeInMillis();
				}
				long end = 0;
				if (schoolTest.getEnd() != null) {
					end = schoolTest.getEnd().getTimeInMillis();
				}
				int type = 0;
				if (schoolTest.getType() != null) {
					type = schoolTest.getType().getId();
				}
				String id = schoolTest.getId();
				if(id == null || "".equals(id)) {
					long time = Calendar.getInstance().getTimeInMillis();
					long random = new Random().nextLong();
					id = "Local" + Math.abs(random) + "" + time;
				}
				Log.d("LocalData", "saveTest: ID: " + id);
				String[] values = { id, schoolTest.getTitle(), schoolTest.getDescription(), date.getTimeInMillis() + "", start + "", end + "", type + "", lastUpdate.getTimeInMillis() + "", schoolTest.isLocal() + "" };
				storeObject(SchoolTest.class.getSimpleName(), columns, values);
				for (SchoolClass schoolClass : schoolTest.getSchoolClasses()) {
					String[] tmp_columns = { "schoolClass_id", "test_id" };
					String[] tmp_values = { schoolClass.getId() + "", id };
//					Log.d("LocalData", "schoolClass: " + schoolClass);
					storeObject(SchoolTest.class.getSimpleName() + "_" + SchoolClass.class.getSimpleName(), tmp_columns, tmp_values);
				}
				for (SchoolTeacher schoolTeacher : schoolTest.getSchoolTeachers()) {
					String[] tmp_columns = { "schoolTeacher_id", "test_id" };
					String[] tmp_values = { schoolTeacher.getId() + "", id };
//					Log.d("LocalData", "schoolTeacher: " + schoolTeacher);
					storeObject(SchoolTest.class.getSimpleName() + "_" + SchoolTeacher.class.getSimpleName(), tmp_columns, tmp_values);
				}
				for (SchoolSubject schoolSubject : schoolTest.getSchoolSubjects()) {
					String[] tmp_columns = { "schoolSubject_id", "test_id" };
					String[] tmp_values = { schoolSubject.getId() + "", id };
//					Log.d("LocalData", "schoolSubject: " + schoolSubject);
					storeObject(SchoolTest.class.getSimpleName() + "_" + SchoolSubject.class.getSimpleName(), tmp_columns, tmp_values);
				}
				for (SchoolRoom schoolRoom : schoolTest.getSchoolRooms()) {
					String[] tmp_columns = { "schoolRoom_id", "test_id" };
					String[] tmp_values = { schoolRoom.getId() + "", id };
//					Log.d("LocalData", "schoolRoom: " + schoolRoom);
					storeObject(SchoolTest.class.getSimpleName() + "_" + SchoolRoom.class.getSimpleName(), tmp_columns, tmp_values);
				}
			}
		}
	}

	@Override
	public List<Lesson> getLessons(ViewType view, Calendar date) {
		// throw new NotImplementedException("Please use getMergedLessons instead!");
		Log.e(LOGTAG, getClass().getSimpleName() + ": getLessons not implemented! Use getMergedLessons!!!!!!");
		return null;
	}

	@Override
	public Map<String, List<Lesson>> getLessons(ViewType view, Calendar startDate, Calendar endDate) throws IOException {
		// throw new NotImplementedException("Please use getMergedLessons instead!");
		Log.e(LOGTAG, getClass().getSimpleName() + ": getLessons not implemented! Use getMergedLessons!!!!!!");
		return null;
	}

	@Override
	public void setLessons(ViewType view, List<Lesson> lessons) {
		// throw new NotImplementedException("Please use setMergedLessons instead!");
		Log.e(LOGTAG, getClass().getSimpleName() + ": setLessons not implemented! Use setMergedLessons!!!!!!");
	}

	@Override
	public void setLessons(ViewType view, Calendar startDate, Calendar endDate, Map<String, List<Lesson>> lessonList) {
		// throw new NotImplementedException("Please use setMergedLessons instead!");
		Log.e(LOGTAG, getClass().getSimpleName() + ": setLessons not implemented! Use setMergedLessons!!!!!!");
	}

	public List<Lesson> getMergedLessons(final ViewType view, final Calendar date) throws IOException {
		List<SchoolTeacher> teachers = cache.getSchoolTeacherList();
		List<SchoolRoom> rooms = cache.getSchoolRoomList();
		List<SchoolSubject> subjects = cache.getSchoolSubjectList();
		List<SchoolClass> classes = cache.getSchoolClassList();
		LessonCodeCreator lessonCodeCreator = new LessonCodeCreator();
		LessonTypeCreator lessonTypeCreator = new LessonTypeCreator();

		String[] columns_lessons = { "viewtype", "schoolObject", "date" };
		Calendar myDate = (Calendar) date.clone();
		myDate.set(Calendar.HOUR_OF_DAY, 0);
		myDate.set(Calendar.MINUTE, 0);
		myDate.set(Calendar.SECOND, 0);
		myDate.set(Calendar.MILLISECOND, 0);
		String[] values_lessons = { view.getClass().getSimpleName(), view.getName(), myDate.getTimeInMillis() + "" };

		List<HashMap<String, String>> objects = getObjects(Lesson.class.getSimpleName(), columns_lessons, values_lessons);
		if (objects == null) {
			return null;
		}
		List<Lesson> result = new ArrayList<Lesson>();
//		Log.d("LocalData", "Stundenanzahl: " + objects.size() + ", Tag: " + CalendarUtils.getCalendarAs8601String(myDate));
		if (objects.size() == 1) {
			HashMap<String, String> tmp = objects.get(0);
			if ("0".equals(tmp.get("id"))) {
//				Log.d("LocalData", "Tag ohne Unterricht: " + CalendarUtils.getCalendarAs8601String(myDate));
				return result;
			}
		}
		for (HashMap<String, String> object : objects) {
			Lesson tmp = new Lesson();
			tmp.setId(Integer.parseInt(object.get("id")));
			Calendar datum = Calendar.getInstance();
			datum.setTimeInMillis(Long.parseLong(object.get("date")));
			tmp.setDate(datum);
			Calendar starttime = Calendar.getInstance();
			starttime.setTimeInMillis(Long.parseLong(object.get("starttime")));
			tmp.setStartTime(starttime.get(Calendar.HOUR_OF_DAY), starttime.get(Calendar.MINUTE));
			Calendar endtime = Calendar.getInstance();
			endtime.setTimeInMillis(Long.parseLong(object.get("endtime")));
			tmp.setEndTime(endtime.get(Calendar.HOUR_OF_DAY), endtime.get(Calendar.MINUTE));
			tmp.setLast_update(Long.parseLong(object.get("last_update")));
			LessonType lessonType = lessonTypeCreator.createLessonType(object.get("lessonType"));
			tmp.setLessonType(lessonType);
			LessonCode lessonCode = null;
			if ("substitution".equals(object.get("lessonCode"))) {
				lessonCode = lessonCodeCreator.createLessonCodeSubstitude();
				String[] code_columns = { "lesson_id" };
				String[] code_values = { object.get("id") };
				List<HashMap<String, String>> substituteMap = getObjects("Lesson_Substitution", code_columns, code_values);
				if (substituteMap != null) {
					for (HashMap<String, String> substitute : substituteMap) {
						if (!"".equals(substitute.get("schoolTeacher_id"))) {
							int current = Integer.parseInt(substitute.get("schoolTeacher_id"));
							for (SchoolTeacher schoolTeacher : teachers) {
								if (current == schoolTeacher.getId()) {
									((LessonCodeSubstitute) lessonCode).setOriginSchoolTeacher(schoolTeacher);
								}
							}
						}
						if (!"".equals(substitute.get("schoolRoom_id"))) {
							int current = Integer.parseInt(substitute.get("schoolRoom_id"));
							for (SchoolRoom schoolRoom : rooms) {
								if (current == schoolRoom.getId()) {
									((LessonCodeSubstitute) lessonCode).setOriginSchoolRoom(schoolRoom);
								}
							}
						}
					}
				}
			}
			else {
				lessonCode = lessonCodeCreator.createLessonCode(object.get("lessonCode"));
			}
			tmp.setLessonCode(lessonCode);
			String[] columns = { "lesson_id" };
			String[] values = { object.get("id") };
			List<HashMap<String, String>> schoolClassObjects = getObjects(Lesson.class.getSimpleName() + "_" + SchoolClass.class.getSimpleName(), columns, values);
			List<SchoolClass> schoolClasses = new ArrayList<SchoolClass>();
			if (schoolClassObjects != null) {
				for (HashMap<String, String> schoolClassObject : schoolClassObjects) {
					int current = Integer.parseInt(schoolClassObject.get("schoolClass_id"));
					for (SchoolClass schoolClass : classes) {
						if (current == schoolClass.getId()) {
							schoolClasses.add(schoolClass);
						}
					}
				}
			}
			tmp.setSchoolClasses(schoolClasses);
			List<HashMap<String, String>> schoolTeacherObjects = getObjects(Lesson.class.getSimpleName() + "_" + SchoolTeacher.class.getSimpleName(), columns, values);
			List<SchoolTeacher> schoolTeachers = new ArrayList<SchoolTeacher>();
			if (schoolTeacherObjects != null) {
				for (HashMap<String, String> schoolTeacherObject : schoolTeacherObjects) {
					int current = Integer.parseInt(schoolTeacherObject.get("schoolTeacher_id"));
					for (SchoolTeacher schoolTeacher : teachers) {
						if (current == schoolTeacher.getId()) {
							schoolTeachers.add(schoolTeacher);
						}
					}
				}
			}
			tmp.setSchoolTeachers(schoolTeachers);
			List<HashMap<String, String>> schoolRoomObjects = getObjects(Lesson.class.getSimpleName() + "_" + SchoolRoom.class.getSimpleName(), columns, values);
			List<SchoolRoom> schoolRooms = new ArrayList<SchoolRoom>();
			if (schoolRoomObjects != null) {
				for (HashMap<String, String> schoolRoomObject : schoolRoomObjects) {
					int current = Integer.parseInt(schoolRoomObject.get("schoolRoom_id"));
					for (SchoolRoom schoolRoom : rooms) {
						if (current == schoolRoom.getId()) {
							schoolRooms.add(schoolRoom);
						}
					}
				}
			}
			tmp.setSchoolRooms(schoolRooms);
			List<HashMap<String, String>> schoolSubjectObjects = getObjects(Lesson.class.getSimpleName() + "_" + SchoolSubject.class.getSimpleName(), columns, values);
			List<SchoolSubject> schoolSubjects = new ArrayList<SchoolSubject>();
			if (schoolSubjectObjects != null) {
				for (HashMap<String, String> schoolSubjectObject : schoolSubjectObjects) {
					int current = Integer.parseInt(schoolSubjectObject.get("schoolSubject_id"));
					for (SchoolSubject schoolSubject : subjects) {
						if (current == schoolSubject.getId()) {
							schoolSubjects.add(schoolSubject);
						}
					}
				}
			}
			tmp.setSchoolSubjects(schoolSubjects);
			// Log.v("LocalData Lessons", "getLessons:\n" + tmp.toString());
			result.add(tmp);
		}
		return result;
	}

	@Override
	public Map<String, List<Lesson>> getMergedLessons(ViewType view, Calendar startDate, Calendar endDate) throws IOException {
		Calendar date = (Calendar) startDate.clone();
		Calendar endDatePlus1 = (Calendar) endDate.clone();
		endDatePlus1.add(Calendar.DAY_OF_MONTH, 1);
		HashMap<String, List<Lesson>> result = new HashMap<String, List<Lesson>>();
		while (date.before(endDatePlus1)) {
			result.put(CalendarUtils.getCalendarAs8601String(date), getMergedLessons(view, date));
			date.add(Calendar.DAY_OF_MONTH, 1);
		}
		return result;
	}

	@Override
	public void setMergedLessons(final ViewType view, final Calendar date, final List<Lesson> lessonList) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				writeMergedLessons(view, date, lessonList);
			}
		}).start();
	}

	private void writeMergedLessons(ViewType view, Calendar date, List<Lesson> lessonList) {
		if (lessonList != null) {
			List<Lesson> lessons = new ArrayList<Lesson>(lessonList);
//			Log.d("LocalData", "Stundenanzahl: " + lessons.size() + ", Tag: " + CalendarUtils.getCalendarAs8601String(date));
			if (lessons.size() == 0) {
				// keine Lessons an diesem Tag
				Lesson tmp = new Lesson();
				Calendar myDate = (Calendar) date.clone();
				myDate.set(Calendar.HOUR_OF_DAY, 0);
				myDate.set(Calendar.MINUTE, 0);
				myDate.set(Calendar.SECOND, 0);
				myDate.set(Calendar.MILLISECOND, 0);
				tmp.setDate(myDate);
				tmp.setStartTime(0, 0);
				tmp.setEndTime(0, 0);
				lessons.add(tmp);
			}
			for (Lesson lesson : lessons) {
				String[] search_columns = { "viewType", "schoolObject", "date", "starttime", "endtime" };
				String[] search_values = { view.getClass().getSimpleName(), view.getName(), lesson.getDate().getTimeInMillis() + "", lesson.getStartTime().getTimeInMillis() + "", lesson.getEndTime().getTimeInMillis() + "" };
				if (getObjects(Lesson.class.getSimpleName(), search_columns, search_values) != null) {
					removeObjects(Lesson.class.getSimpleName(), search_columns, search_values);
					String[] lesson_columns = { "lesson_id" };
					String[] lesson_values = { lesson.getId() + "" };
					removeObjects(Lesson.class.getSimpleName() + "_" + SchoolClass.class.getSimpleName(), lesson_columns, lesson_values);
					removeObjects(Lesson.class.getSimpleName() + "_" + SchoolTeacher.class.getSimpleName(), lesson_columns, lesson_values);
					removeObjects(Lesson.class.getSimpleName() + "_" + SchoolSubject.class.getSimpleName(), lesson_columns, lesson_values);
					removeObjects(Lesson.class.getSimpleName() + "_" + SchoolRoom.class.getSimpleName(), lesson_columns, lesson_values);
				}
				String[] columns = { "id", "viewtype", "schoolObject", "date", "starttime", "endtime", "lessonType", "lessonCode", "last_update" };
				String lessonType = "";
				if (lesson.getLessonType() instanceof LessonTypeBreakSupervision) {
					lessonType = WebUntis.BREAKSUPERVISION;
				}
				else if (lesson.getLessonType() instanceof LessonTypeExamination) {
					lessonType = WebUntis.EXAMINATION;
				}
				else if (lesson.getLessonType() instanceof LessonTypeOfficeHour) {
					lessonType = WebUntis.OFFICEHOUR;
				}
				else if (lesson.getLessonType() instanceof LessonTypeStandby) {
					lessonType = WebUntis.STANDBY;
				}
				String lessonCode = "";
				if (lesson.getLessonCode() instanceof LessonCodeSubstitute) {
					lessonCode = "substitution";
					LessonCodeSubstitute lessonCodeObject = (LessonCodeSubstitute) lesson.getLessonCode();
					String[] code_columns = { "lesson_id", "schoolTeacher_id", "schoolRoom_id" };
					String originSchoolTeacher = "";
					if (lessonCodeObject.getOriginSchoolTeacher() != null) {
						originSchoolTeacher = lessonCodeObject.getOriginSchoolTeacher().getId() + "";
					}
					String originSchoolRoom = "";
					if (lessonCodeObject.getOriginSchoolRoom() != null) {
						originSchoolRoom = lessonCodeObject.getOriginSchoolRoom().getId() + "";
					}
					String[] code_values = { lesson.getId() + "", originSchoolTeacher, originSchoolRoom };
					storeObject(Lesson.class.getSimpleName() + "_Substitution", code_columns, code_values);
				}
				else if (lesson.getLessonCode() instanceof LessonCodeCancelled) {
					lessonCode = WebUntis.CANCELLED;
				}
				Calendar lessonDate = (Calendar) lesson.getDate().clone();
				lessonDate.set(Calendar.HOUR_OF_DAY, 0);
				lessonDate.set(Calendar.MINUTE, 0);
				lessonDate.set(Calendar.SECOND, 0);
				lessonDate.set(Calendar.MILLISECOND, 0);
				Calendar lastUpdate = Calendar.getInstance();
				String[] values = { lesson.getId() + "", view.getClass().getSimpleName(), view.getName(), lessonDate.getTimeInMillis() + "", lesson.getStartTime().getTimeInMillis() + "", lesson.getEndTime().getTimeInMillis() + "", lessonType, lessonCode, lastUpdate.getTimeInMillis() + "" };
				storeObject(Lesson.class.getSimpleName(), columns, values);
				for (SchoolClass schoolClass : lesson.getSchoolClasses()) {
					String[] tmp_columns = { "schoolClass_id", "lesson_id" };
					String[] tmp_values = { schoolClass.getId() + "", lesson.getId() + "" };
					storeObject(Lesson.class.getSimpleName() + "_" + SchoolClass.class.getSimpleName(), tmp_columns, tmp_values);
				}
				for (SchoolTeacher schoolTeacher : lesson.getSchoolTeachers()) {
					String[] tmp_columns = { "schoolTeacher_id", "lesson_id" };
					String[] tmp_values = { schoolTeacher.getId() + "", lesson.getId() + "" };
					storeObject(Lesson.class.getSimpleName() + "_" + SchoolTeacher.class.getSimpleName(), tmp_columns, tmp_values);
				}
				for (SchoolSubject schoolSubject : lesson.getSchoolSubjects()) {
					String[] tmp_columns = { "schoolSubject_id", "lesson_id" };
					String[] tmp_values = { schoolSubject.getId() + "", lesson.getId() + "" };
					storeObject(Lesson.class.getSimpleName() + "_" + SchoolSubject.class.getSimpleName(), tmp_columns, tmp_values);
				}
				for (SchoolRoom schoolRoom : lesson.getSchoolRooms()) {
					String[] tmp_columns = { "schoolRoom_id", "lesson_id" };
					String[] tmp_values = { schoolRoom.getId() + "", lesson.getId() + "" };
					storeObject(Lesson.class.getSimpleName() + "_" + SchoolRoom.class.getSimpleName(), tmp_columns, tmp_values);
				}
			}
		}
	}

	@Override
	public void setMergedLessons(final ViewType view, final Calendar startDate, final Calendar endDate, final Map<String, List<Lesson>> lessonList) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				writeMergedLessons(view, startDate, endDate, lessonList);
			}
		}).start();
	}

	private void writeMergedLessons(ViewType view, Calendar startDate, Calendar endDate, Map<String, List<Lesson>> lessonList) {
		Calendar date = (Calendar) startDate.clone();
		Calendar endDatePlus1 = (Calendar) endDate.clone();
		endDatePlus1.add(Calendar.DAY_OF_MONTH, 1);
		while (date.before(endDatePlus1)) {
			writeMergedLessons(view, date, lessonList.get(CalendarUtils.getCalendarAs8601String(date)));
			date.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	@Override
	public HashMap<String, Authentication> getAllPresets() {
		List<HashMap<String, String>> objects = getObjects("Presets", null, null);
		if (objects == null) {
			return null;
		}
		HashMap<String, Authentication> result = new HashMap<String, Authentication>();
		for (HashMap<String, String> object : objects) {
			String title = object.get("title");
			Authentication tmp = new Authentication();
			try {
				tmp.setServerUrl(object.get("serverUrl"));
			} catch (URISyntaxException e) {
				// Wrong URLs are loaded but should be handled later.
			}
			tmp.setSchool(object.get("school"));
			tmp.setUsername(object.get("username"));
			tmp.setPassword(object.get("password"));
			result.put(title, tmp);
		}
		return result;
	}

	@Override
	public void savePreset(final String title, final Authentication auth) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				writePreset(title, auth);
			}
		}).start();
	}

	private void writePreset(String title, Authentication auth) {
		if (auth != null) {
			deletePreset(title);
			String[] columns = { "title", "serverUrl", "school", "username", "password" };
			String[] values = { title, auth.getServerUrl(), auth.getSchool(), auth.getUsername(), auth.getPassword() };
			storeObject("Presets", columns, values);
		}
	}

	@Override
	public void deletePreset(String title) {
		String [] rcolumns = { "title" };
		String [] rvalues = { title };
		removeObjects("Presets", rcolumns, rvalues);
	}
}