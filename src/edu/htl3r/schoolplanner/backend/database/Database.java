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
package edu.htl3r.schoolplanner.backend.database;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.backend.MasterdataProvider;
import edu.htl3r.schoolplanner.backend.MasterdataStore;
import edu.htl3r.schoolplanner.backend.StatusData;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class Database implements MasterdataStore, MasterdataProvider {
	
	private DatabaseHelper databaseHelper = new DatabaseHelper(SchoolplannerContext.context);

	private MasterDataDatabase masterDataDatabase = new MasterDataDatabase(this);
	
	public SQLiteDatabase openDatabase(boolean writeable) {
		return writeable ? databaseHelper.getWritableDatabase() : databaseHelper.getReadableDatabase();
	}
	
	public void closeDatabase(SQLiteDatabase database) {
		database.close();
	}
	
	/**
	 * Startet eine Transaction, fuegt Werte ein und endet Transaction wieder.
	 * @param table
	 * @param values
	 */
	public void insert(SQLiteDatabase database, String table, ContentValues values) {
		database.insert(table, null, values);
	}
	
	/**
	 * Liefert den {@link Cursor} zum Query zum uebergebenen Table-Namen.
	 * @param table
	 * @return
	 */
	public Cursor query(SQLiteDatabase database, String table) {
		return database.query(table, null, null, null, null, null, null);
	}

	@Override
	public List<SchoolClass> getSchoolClassList() {
		return masterDataDatabase.getSchoolClassList();
	}

	@Override
	public List<SchoolTeacher> getSchoolTeacherList() {
		return masterDataDatabase.getSchoolTeacherList();
	}

	@Override
	public List<SchoolRoom> getSchoolRoomList() {
		return masterDataDatabase.getSchoolRoomList();
	}

	@Override
	public List<SchoolSubject> getSchoolSubjectList() {
		return masterDataDatabase.getSchoolSubjectList();
	}

	@Override
	public List<SchoolHoliday> getSchoolHolidayList() {
		return masterDataDatabase.getSchoolHolidayList();
	}

	@Override
	public Timegrid getTimegrid() {
		return masterDataDatabase.getTimegrid();
	}

	@Override
	public List<StatusData> getStatusData() {
		return masterDataDatabase.getStatusData();
	}

	@Override
	public void setSchoolClassList(List<SchoolClass> schoolClasses) {
		masterDataDatabase.setSchoolClassList(schoolClasses);
	}

	@Override
	public void setSchoolTeacherList(List<SchoolTeacher> schoolTeachers) {
		masterDataDatabase.setSchoolTeacherList(schoolTeachers);
	}

	@Override
	public void setSchoolSubjectList(List<SchoolSubject> schoolSubjects) {
		masterDataDatabase.setSchoolSubjectList(schoolSubjects);
	}

	@Override
	public void setSchoolRoomList(List<SchoolRoom> schoolRooms) {
		masterDataDatabase.setSchoolRoomList(schoolRooms);
	}

	@Override
	public void setSchoolHolidayList(List<SchoolHoliday> holidayList) {
		masterDataDatabase.setSchoolHolidayList(holidayList);
	}

	@Override
	public void setTimegrid(Timegrid timegrid) {
		masterDataDatabase.setTimegrid(timegrid);
	}

	@Override
	public void setStatusData(List<StatusData> statusData) {
		masterDataDatabase.setStatusData(statusData);
	}
	
}
