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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.backend.MasterdataProvider;
import edu.htl3r.schoolplanner.backend.MasterdataStore;
import edu.htl3r.schoolplanner.backend.network.WebUntis;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

/**
 * Implementierung einer Datenbank Zugriff auf eine Datenbank per SQLite
 */
public class LocalData implements MasterdataStore, MasterdataProvider {

	private SQLiteDatabase database;
	
	public LocalData() {
		database = new DatabaseHelper(SchoolplannerContext.context).getWritableDatabase();
	}
	

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
	
}