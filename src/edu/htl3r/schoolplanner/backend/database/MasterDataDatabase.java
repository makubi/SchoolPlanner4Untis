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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;
import edu.htl3r.schoolplanner.backend.MasterdataProvider;
import edu.htl3r.schoolplanner.backend.MasterdataStore;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseCreateConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseSchoolHolidayConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseStatusDataConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseTimegridConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseViewTypeConstants;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class MasterDataDatabase implements MasterdataStore, MasterdataProvider {

	private Database database;
	
	private Map<Class<? extends ViewType>, String> viewTypeClassTableMapping = new HashMap<Class<? extends ViewType>, String>();
	private ViewTypeFactory viewTypeFactory = new ViewTypeFactory();
	
	public MasterDataDatabase(Database database) {
		this.database = database;
		viewTypeClassTableMapping.put(SchoolClass.class, DatabaseViewTypeConstants.TABLE_SCHOOL_CLASSES_NAME);
		viewTypeClassTableMapping.put(SchoolTeacher.class, DatabaseViewTypeConstants.TABLE_SCHOOL_TEACHER_NAME);
		viewTypeClassTableMapping.put(SchoolSubject.class, DatabaseViewTypeConstants.TABLE_SCHOOL_SUBJECTS_NAME);
		viewTypeClassTableMapping.put(SchoolRoom.class, DatabaseViewTypeConstants.TABLE_SCHOOL_ROOMS_NAME);
	}
	
	@Override
	public List<SchoolClass> getSchoolClassList() {
		return getViewTypeList(DatabaseViewTypeConstants.TABLE_SCHOOL_CLASSES_NAME);
	}

	@Override
	public List<SchoolTeacher> getSchoolTeacherList() {
		// TODO: Setzen des foreName
		return getViewTypeList(DatabaseViewTypeConstants.TABLE_SCHOOL_TEACHER_NAME);
	}

	@Override
	public List<SchoolRoom> getSchoolRoomList() {
		return getViewTypeList(DatabaseViewTypeConstants.TABLE_SCHOOL_ROOMS_NAME);
	}

	@Override
	public List<SchoolSubject> getSchoolSubjectList() {
		return getViewTypeList(DatabaseViewTypeConstants.TABLE_SCHOOL_SUBJECTS_NAME);
	}

	@Override
	public List<SchoolHoliday> getSchoolHolidayList() {
		List<SchoolHoliday> schoolHolidayList = new ArrayList<SchoolHoliday>();
		
		SQLiteDatabase database = this.database.openDatabase(false);
		
		Cursor query = this.database.queryWithLoginSetKey(database, DatabaseSchoolHolidayConstants.TABLE_SCHOOL_HOLIDAYS_NAME);
		
		int indexID = query.getColumnIndex(DatabaseSchoolHolidayConstants.ID);
		int indexName = query.getColumnIndex(DatabaseSchoolHolidayConstants.NAME);
		int indexLongName = query.getColumnIndex(DatabaseSchoolHolidayConstants.LONG_NAME);
		int indexStartDate = query.getColumnIndex(DatabaseSchoolHolidayConstants.START_DATE);
		int indexEndDate = query.getColumnIndex(DatabaseSchoolHolidayConstants.END_DATE);
		
		while(query.moveToNext()) {
			int id = query.getInt(indexID);
			String name = query.getString(indexName);
			String longName = query.getString(indexLongName);
			long startDate = query.getLong(indexStartDate);
			long endDate = query.getLong(indexEndDate);
			
			SchoolHoliday schoolHoliday = new SchoolHoliday();
			schoolHoliday.setId(id);
			schoolHoliday.setName(name);
			schoolHoliday.setLongName(longName);
			schoolHoliday.setStartDate(this.database.millisToDateTime(startDate));
			schoolHoliday.setEndDate(this.database.millisToDateTime(endDate));
			
			schoolHolidayList.add(schoolHoliday);
		}
		query.close();
		this.database.closeDatabase(database);
		
		return schoolHolidayList;
	}

	@Override
	public Timegrid getTimegrid() {
		Timegrid timegrid = new Timegrid();
		
		SQLiteDatabase database = this.database.openDatabase(false);
		
		Cursor query = this.database.queryWithLoginSetKey(database, DatabaseTimegridConstants.TABLE_TIMEGRID_NAME);
		
		int indexDay = query.getColumnIndex(DatabaseTimegridConstants.DAY);
		int indexName = query.getColumnIndex(DatabaseTimegridConstants.NAME);
		int indexStartTime = query.getColumnIndex(DatabaseTimegridConstants.START_TIME);
		int indexEndTime = query.getColumnIndex(DatabaseTimegridConstants.END_TIME);
		
		while(query.moveToNext()) {
			int day = query.getInt(indexDay);
			String name = query.getString(indexName);
			long startTime = query.getLong(indexStartTime);
			long endTime = query.getLong(indexEndTime);
			
			TimegridUnit timegridUnit = new TimegridUnit();
			timegridUnit.setName(name);
			timegridUnit.setStart(this.database.millisToDateTime(startTime));
			timegridUnit.setEnd(this.database.millisToDateTime(endTime));
			
			timegrid.putTimegridUnitForDateTimeDay(day, timegridUnit);
		}
		query.close();
		this.database.closeDatabase(database);
		
		return query.getCount() > 0 ? timegrid : null;
		
	}
	
	private <E> List<E> getViewTypeList(String table) {
		List<E> viewTypeList = new ArrayList<E>();
		
		SQLiteDatabase database = this.database.openDatabase(false);
		
		Cursor query = this.database.queryWithLoginSetKey(database, table);
		
		int indexID = query.getColumnIndex(DatabaseViewTypeConstants.ID);
		int indexName = query.getColumnIndex(DatabaseViewTypeConstants.NAME);
		int indexLongName = query.getColumnIndex(DatabaseViewTypeConstants.LONG_NAME);
		int indexForeColor = query.getColumnIndex(DatabaseViewTypeConstants.FORE_COLOR);
		int indexBackColor = query.getColumnIndex(DatabaseViewTypeConstants.BACK_COLOR);
		
		while(query.moveToNext()) {
			int id = query.getInt(indexID);
			String name = query.getString(indexName);
			String longName = query.getString(indexLongName);
			String foreColor = query.getString(indexForeColor);
			String backColor = query.getString(indexBackColor);
			
			ViewType viewType = getViewType(table);
			viewType.setId(id);
			viewType.setName(name);
			viewType.setLongName(longName);
			viewType.setForeColor(foreColor);
			viewType.setBackColor(backColor);
			
			viewTypeList.add((E) viewType);
		}
		query.close();
		this.database.closeDatabase(database);
		
		return viewTypeList;
	}
	
	private ViewType getViewType(String table) {
		return viewTypeFactory.get(table);
	}
	
	private void setViewTypeList(List<? extends ViewType> viewTypeList, String table) {
		SQLiteDatabase database = this.database.openDatabase(true);
		
		this.database.deleteAllRowsWithLoginSetKey(database, table);
		
		database.beginTransaction();
		for(ViewType viewType: viewTypeList) {
			ContentValues values = new ContentValues();
			values.put(DatabaseCreateConstants.TABLE_LOGINSET_KEY, this.database.getLoginSetKeyForTable());
			values.put(DatabaseViewTypeConstants.ID, viewType.getId());
			values.put(DatabaseViewTypeConstants.NAME, viewType.getName());
			values.put(DatabaseViewTypeConstants.LONG_NAME, viewType.getLongName());
			values.put(DatabaseViewTypeConstants.FORE_COLOR, viewType.getForeColor());
			values.put(DatabaseViewTypeConstants.BACK_COLOR, viewType.getBackColor());
			
			this.database.insert(database,table, values);
		}
		database.setTransactionSuccessful();
		database.endTransaction();
		this.database.closeDatabase(database);
	}

	@Override
	public void setSchoolClassList(List<SchoolClass> schoolClasses) {
		setViewTypeList(schoolClasses, DatabaseViewTypeConstants.TABLE_SCHOOL_CLASSES_NAME);
	}

	@Override
	public void setSchoolTeacherList(List<SchoolTeacher> schoolTeachers) {
		// TODO: Speichern der foreName
		setViewTypeList(schoolTeachers, DatabaseViewTypeConstants.TABLE_SCHOOL_TEACHER_NAME);	
	}

	@Override
	public void setSchoolSubjectList(List<SchoolSubject> schoolSubjects) {
		setViewTypeList(schoolSubjects, DatabaseViewTypeConstants.TABLE_SCHOOL_SUBJECTS_NAME);
	}

	@Override
	public void setSchoolRoomList(List<SchoolRoom> schoolRooms) {
		setViewTypeList(schoolRooms, DatabaseViewTypeConstants.TABLE_SCHOOL_ROOMS_NAME);
	}

	@Override
	public void setSchoolHolidayList(List<SchoolHoliday> holidayList) {
		writeSchoolHolidayList(holidayList, DatabaseSchoolHolidayConstants.TABLE_SCHOOL_HOLIDAYS_NAME);
	}

	private void writeSchoolHolidayList(List<SchoolHoliday> holidayList, String table) {
		SQLiteDatabase database = this.database.openDatabase(true);
		
		this.database.deleteAllRowsWithLoginSetKey(database, table);
		
		database.beginTransaction();
		for(SchoolHoliday schoolHoliday : holidayList) {
			ContentValues values = new ContentValues();
			values.put(DatabaseCreateConstants.TABLE_LOGINSET_KEY, this.database.getLoginSetKeyForTable());
			values.put(DatabaseSchoolHolidayConstants.ID, schoolHoliday.getId());
			values.put(DatabaseSchoolHolidayConstants.NAME, schoolHoliday.getName());
			values.put(DatabaseSchoolHolidayConstants.LONG_NAME, schoolHoliday.getLongName());
			
			values.put(DatabaseSchoolHolidayConstants.START_DATE, this.database.dateTimeToMillis(schoolHoliday.getStartDate()));
			values.put(DatabaseSchoolHolidayConstants.END_DATE, this.database.dateTimeToMillis(schoolHoliday.getEndDate()));
			
			this.database.insert(database, table, values);
		}
		database.setTransactionSuccessful();
		database.endTransaction();
		this.database.closeDatabase(database);
	}
	
	@Override
	public void setTimegrid(Timegrid timegrid) {
		writeTimegrid(timegrid, DatabaseTimegridConstants.TABLE_TIMEGRID_NAME);
	}

	private void writeTimegrid(Timegrid timegrid, String table) {
		SQLiteDatabase database = this.database.openDatabase(true);
		
		this.database.deleteAllRowsWithLoginSetKey(database, table);
		
		final int[] timeWeekDays = new int[] {Time.SUNDAY, Time.MONDAY, Time.TUESDAY, Time.WEDNESDAY, Time.THURSDAY, Time.FRIDAY, Time.SATURDAY};
		
		database.beginTransaction();
		for(int day : timeWeekDays) {
			
			List<TimegridUnit> timegridUnitList = timegrid.getTimegridForDateTimeDay(day);
			
			// This can happen, if there is no timegrid for a day (e.g. sunday).
			if(timegridUnitList != null) {
			
				for(TimegridUnit timegridUnit : timegridUnitList) {
					ContentValues values = new ContentValues();
					values.put(DatabaseCreateConstants.TABLE_LOGINSET_KEY, this.database.getLoginSetKeyForTable());
					values.put(DatabaseTimegridConstants.DAY, day);
					values.put(DatabaseTimegridConstants.NAME, timegridUnit.getName());
					values.put(DatabaseTimegridConstants.START_TIME, this.database.dateTimeToMillis(timegridUnit.getStart()));
					values.put(DatabaseTimegridConstants.END_TIME, this.database.dateTimeToMillis(timegridUnit.getEnd()));
					
					this.database.insert(database, table, values);
				}
			}
		}
		database.setTransactionSuccessful();
		database.endTransaction();
		this.database.closeDatabase(database);
	}

	private void deleteAllRowsFromDatabaseTransaction(String table, String loginSetKey) {
		SQLiteDatabase database = this.database.openDatabase(true);
		database.beginTransaction();
		
		this.database.deleteAllRowsWithLoginSetKey(database, table, loginSetKey);
		
		database.setTransactionSuccessful();
		database.endTransaction();
		this.database.closeDatabase(database);
	}

	public void deleteAllRowsFromSchoolClassListWithLoginSetKey(String loginSetKey) {
		deleteAllRowsFromDatabaseTransaction(DatabaseViewTypeConstants.TABLE_SCHOOL_CLASSES_NAME, loginSetKey);
	}

	public void deleteAllRowsFromSchoolTeacherListWithLoginSetKey(
			String loginSetKey) {
		deleteAllRowsFromDatabaseTransaction(DatabaseViewTypeConstants.TABLE_SCHOOL_TEACHER_NAME, loginSetKey);
	}
	
	public void deleteAllRowsFromSchoolRoomListWithLoginSetKey(
			String loginSetKey) {
		deleteAllRowsFromDatabaseTransaction(DatabaseViewTypeConstants.TABLE_SCHOOL_ROOMS_NAME, loginSetKey);
	}

	public void deleteAllRowsFromSchoolSubjectListWithLoginSetKey(
			String loginSetKey) {
		deleteAllRowsFromDatabaseTransaction(DatabaseViewTypeConstants.TABLE_SCHOOL_SUBJECTS_NAME, loginSetKey);
	}
	
	public void deleteAllRowsFromSchoolHolidayListWithLoginSetKey(String loginSetKey) {
		deleteAllRowsFromDatabaseTransaction(DatabaseSchoolHolidayConstants.TABLE_SCHOOL_HOLIDAYS_NAME, loginSetKey);
	}
	
	public void deleteAllRowsFromTimegridWithLoginSetKey(String loginSetKey) {
		deleteAllRowsFromDatabaseTransaction(DatabaseTimegridConstants.TABLE_TIMEGRID_NAME, loginSetKey);
	}
	
	public void deleteAllRowsFromStatusDataListWithLoginSetKey(String loginSetKey) {
		deleteAllRowsFromDatabaseTransaction(DatabaseStatusDataConstants.TABLE_STATUS_DATA_NAME, loginSetKey);
	}
}
