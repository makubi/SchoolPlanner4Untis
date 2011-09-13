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
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.LessonHelper;
import edu.htl3r.schoolplanner.backend.UnsaveDataSourceMasterdataProvider;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseCreateConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabasePermanentLessonConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabasePermanentLessonViewTypeConstants;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class LessonHelperDatabase implements LessonHelper {

	private Database database;
	private UnsaveDataSourceMasterdataProvider cache;
	
	private Map<Class<? extends ViewType>, String> databasePermanentLessonViewTypeMap = new HashMap<Class<? extends ViewType>, String>();
	
	public LessonHelperDatabase(Database database) {
		this.database = database;
		
		databasePermanentLessonViewTypeMap.put(SchoolClass.class, DatabasePermanentLessonViewTypeConstants.VIEW_TYPE_SCHOOL_CLASS);
		databasePermanentLessonViewTypeMap.put(SchoolTeacher.class, DatabasePermanentLessonViewTypeConstants.VIEW_TYPE_SCHOOL_TEACHER);
		databasePermanentLessonViewTypeMap.put(SchoolRoom.class, DatabasePermanentLessonViewTypeConstants.VIEW_TYPE_SCHOOL_ROOM);
		databasePermanentLessonViewTypeMap.put(SchoolSubject.class, DatabasePermanentLessonViewTypeConstants.VIEW_TYPE_SCHOOL_SUBJECT);
	}

	@Override
	public List<Lesson> getPermanentLessons() {
		List<SchoolClass> schoolClassList = cache.getSchoolClassList().getData();
		List<SchoolTeacher> schoolTeacherList = cache.getSchoolTeacherList().getData();
		List<SchoolRoom> schoolRoomList = cache.getSchoolRoomList().getData();
		List<SchoolSubject> schoolSubjectList = cache.getSchoolSubjectList().getData();
		
		List<Lesson> permanentLessons = new ArrayList<Lesson>();
		final String loginSetKey = this.database.getLoginSetKeyForTable();
		SQLiteDatabase database = this.database.openDatabase(false);
		
		final String table = DatabasePermanentLessonConstants.TABLE_PERMANENT_LESSONS_NAME;
		final String[] columns = new String[] {DatabasePermanentLessonConstants.ROWID,DatabasePermanentLessonConstants.WEEK_DAY,DatabasePermanentLessonConstants.START_TIME,DatabasePermanentLessonConstants.END_TIME};
		
		Cursor query = database.query(table, columns, DatabaseCreateConstants.TABLE_LOGINSET_KEY+"=?", new String[]{loginSetKey}, null, null, null);
		
		int indexRowID = query.getColumnIndex(DatabasePermanentLessonConstants.ROWID);
		int indexWeekDay = query.getColumnIndex(DatabasePermanentLessonConstants.WEEK_DAY);
		int indexStartTime = query.getColumnIndex(DatabasePermanentLessonConstants.START_TIME);
		int indexEndTime = query.getColumnIndex(DatabasePermanentLessonConstants.END_TIME);
		
		while(query.moveToNext()) {
			long rowID = query.getLong(indexRowID);
			int weekDay = query.getInt(indexWeekDay);
			long startTimeValue = query.getLong(indexStartTime);
			long endTimeValue = query.getLong(indexEndTime);
			
			DateTime date = new DateTime();
			date.getAndroidTime().weekDay = weekDay;
			DateTime startTime = this.database.millisToDateTime(startTimeValue);
			DateTime endTime = this.database.millisToDateTime(endTimeValue);
			
			Lesson permanentLesson = new Lesson();
			permanentLesson.setDate(date.getYear(), date.getMonth(), date.getDay());
			permanentLesson.setStartTime(startTime.getHour(), startTime.getMinute());
			permanentLesson.setEndTime(endTime.getHour(), endTime.getMinute());
			
			permanentLesson.setSchoolClasses((List<SchoolClass>) getViewTypeListForPermanentLesson(database, DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_CLASSES_NAME, databasePermanentLessonViewTypeMap.get(SchoolClass.class), loginSetKey, rowID, schoolClassList));
			permanentLesson.setSchoolTeachers((List<SchoolTeacher>) getViewTypeListForPermanentLesson(database, DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_TEACHER_NAME, databasePermanentLessonViewTypeMap.get(SchoolTeacher.class), loginSetKey, rowID, schoolTeacherList));
			permanentLesson.setSchoolRooms((List<SchoolRoom>) getViewTypeListForPermanentLesson(database, DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_ROOMS_NAME, databasePermanentLessonViewTypeMap.get(SchoolRoom.class), loginSetKey, rowID, schoolRoomList));
			permanentLesson.setSchoolSubjects((List<SchoolSubject>) getViewTypeListForPermanentLesson(database, DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_SUBJECTS_NAME, databasePermanentLessonViewTypeMap.get(SchoolSubject.class), loginSetKey, rowID, schoolSubjectList));
			
			permanentLessons.add(permanentLesson);
		}
		query.close();
		this.database.closeDatabase(database);
		
		return permanentLessons;
	}
	
	private List<? extends ViewType> getViewTypeListForPermanentLesson(SQLiteDatabase database, String table, String viewTypeColumn, String loginSetKey, long lessonID, List<? extends ViewType> initializedList) {
		final List<ViewType> list = new ArrayList<ViewType>();
		
		final String selectionString = DatabaseCreateConstants.TABLE_LOGINSET_KEY+"=? AND "+DatabasePermanentLessonViewTypeConstants.LESSON_ID+"=? AND "+DatabasePermanentLessonViewTypeConstants.VIEW_TYPE_TYPE+"=?";
		final String[] selectionArgs = new String[] { loginSetKey,""+lessonID, viewTypeColumn };
		Cursor query = database.query(table, new String[]{DatabasePermanentLessonViewTypeConstants.VIEW_TYPE_ID}, selectionString, selectionArgs, null, null, null);
		
		int indexViewTypeID = query.getColumnIndex(DatabasePermanentLessonViewTypeConstants.VIEW_TYPE_ID);
		while(query.moveToNext()) {
			int viewTypeID = query.getInt(indexViewTypeID);
			
			list.add(getViewType(viewTypeID, initializedList));
		}
		return list;
	}
	
	private ViewType getViewType (int id, List<? extends ViewType> data) {
		for(ViewType viewType : data) {
			if(viewType.getId() == id) {
				return viewType;
			}
		}
		return null;
	}

	@Override
	public void setPermanentLesson(Lesson lesson) {
		final String lessonTable = DatabasePermanentLessonConstants.TABLE_PERMANENT_LESSONS_NAME;
		final String loginSetKey = this.database.getLoginSetKeyForTable();
		SQLiteDatabase database = this.database.openDatabase(true);
		
		int weekDay = lesson.getDate().getAndroidTime().weekDay;
		long startTime = this.database.dateTimeToMillis(lesson.getStartTime());
		long endTime = this.database.dateTimeToMillis(lesson.getEndTime());
		
		database.beginTransaction();
		
		removeLessonIfExists(database, lessonTable, loginSetKey, weekDay, startTime, endTime);
		
		ContentValues values = new ContentValues();		
		values.put(DatabaseCreateConstants.TABLE_LOGINSET_KEY, loginSetKey);
		
		values.put(DatabasePermanentLessonConstants.WEEK_DAY, weekDay);
		values.put(DatabasePermanentLessonConstants.START_TIME, startTime);
		values.put(DatabasePermanentLessonConstants.END_TIME, endTime);
		
		long rowID = this.database.insert(database, lessonTable, values);
		
		ContentValues contentValuesForSchoolClassList = getContentValuesForViewTypeList(lesson.getSchoolClasses(), rowID, loginSetKey);
		this.database.insert(database, DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_CLASSES_NAME, contentValuesForSchoolClassList);
		
		ContentValues contentValuesForSchoolTeacherList = getContentValuesForViewTypeList(lesson.getSchoolTeachers(), rowID, loginSetKey);
		this.database.insert(database, DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_TEACHER_NAME, contentValuesForSchoolTeacherList);
		
		ContentValues contentValuesForSchoolRoomList = getContentValuesForViewTypeList(lesson.getSchoolRooms(), rowID, loginSetKey);
		this.database.insert(database, DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_ROOMS_NAME, contentValuesForSchoolRoomList);
		
		ContentValues contentValuesForSchoolSubjectList = getContentValuesForViewTypeList(lesson.getSchoolSubjects(), rowID, loginSetKey);
		this.database.insert(database, DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_SUBJECTS_NAME, contentValuesForSchoolSubjectList);
		
		database.setTransactionSuccessful();
		database.endTransaction();
		this.database.closeDatabase(database);
	}
	
	private void removeLessonIfExists(SQLiteDatabase database, String table, String loginSetKey, int weekDay, long startTime, long endTime) {
		final String selectionString = DatabaseCreateConstants.TABLE_LOGINSET_KEY+"=? AND "+DatabasePermanentLessonConstants.WEEK_DAY+"=? AND "+DatabasePermanentLessonConstants.START_TIME+"=? AND "+DatabasePermanentLessonConstants.END_TIME+"=?";
		final String[] selectionArgs = new String[]{loginSetKey, ""+weekDay, ""+startTime, ""+endTime};
		
		database.delete(table, selectionString, selectionArgs);
	}

	private ContentValues getContentValuesForViewTypeList(List<? extends ViewType> list, long rowID, String loginSetKey) {
		ContentValues contentValues = new ContentValues();
		
		for(ViewType viewType : list) {
			contentValues.put(DatabaseCreateConstants.TABLE_LOGINSET_KEY, loginSetKey);
			contentValues.put(DatabasePermanentLessonViewTypeConstants.LESSON_ID, rowID);
			contentValues.put(DatabasePermanentLessonViewTypeConstants.VIEW_TYPE_TYPE, databasePermanentLessonViewTypeMap.get(viewType.getClass()));
			contentValues.put(DatabasePermanentLessonViewTypeConstants.VIEW_TYPE_ID, viewType.getId());
		}
		
		return contentValues;
	}
	
	public Cursor queryWithLoginSetKey(SQLiteDatabase database, String table, String selection, String[] selectionArgs) {
		this.database.getLoginSetKeyForTable();
		return database.query(table, null, DatabaseCreateConstants.TABLE_LOGINSET_KEY+"=?", new String[]{}, null, null, null);
	}

	public void setUnsaveDataSourceMasterdataProvider(UnsaveDataSourceMasterdataProvider cache) {
		this.cache = cache;
	}

}
