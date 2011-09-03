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
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import edu.htl3r.schoolplanner.backend.LessonHelper;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;

public class LessonHelperDatabase implements LessonHelper {

	private Database database;
	
	public LessonHelperDatabase(Database database) {
		this.database = database;
	}

	@Override
	public List<Lesson> getPermanentLessons() {
		List<Lesson> permanentLessons = new ArrayList<Lesson>();
		SQLiteDatabase database = this.database.openDatabase(false);
		
//		Cursor query = this.database.queryWithLoginSetKey(database, table);
		
//		int indexID = query.getColumnIndex(DatabaseSchoolHolidayConstants.ID);
//		int indexName = query.getColumnIndex(DatabaseSchoolHolidayConstants.NAME);
//		int indexLongName = query.getColumnIndex(DatabaseSchoolHolidayConstants.LONG_NAME);
//		int indexStartDate = query.getColumnIndex(DatabaseSchoolHolidayConstants.START_DATE);
//		int indexEndDate = query.getColumnIndex(DatabaseSchoolHolidayConstants.END_DATE);
//		
//		while(query.moveToNext()) {
//			int id = query.getInt(indexID);
//			String name = query.getString(indexName);
//			String longName = query.getString(indexLongName);
//			long startDate = query.getLong(indexStartDate);
//			long endDate = query.getLong(indexEndDate);
//			
//			SchoolHoliday schoolHoliday = new SchoolHoliday();
//			schoolHoliday.setId(id);
//			schoolHoliday.setName(name);
//			schoolHoliday.setLongName(longName);
//			schoolHoliday.setStartDate(millisToDateTime(startDate));
//			schoolHoliday.setEndDate(millisToDateTime(endDate));
//			
//			schoolHolidayList.add(schoolHoliday);
//		}
//		query.close();
//		this.database.closeDatabase(database);
		
		return permanentLessons;
	}

	@Override
	public void setPermanentLesson(Lesson lesson) {
//		final String table = DatabasePermanentLessonConstants.TABLE_PERMANENT_LESSONS_NAME;
//		SQLiteDatabase database = this.database.openDatabase(true);
//		
//		this.database.deleteAllRowsWithLoginSetKey(database, table);
//		
//		database.beginTransaction();
//		
//		ContentValues values = new ContentValues();
//		values.put(DatabaseCreateConstants.TABLE_LOGINSET_KEY, this.database.getLoginSetKeyForTable());
//		
//		values.put(DatabasePermanentLessonConstants.DATE, this.database.dateTimeToMillis(lesson.getDate()));
//		values.put(DatabasePermanentLessonConstants.START_TIME, this.database.dateTimeToMillis(lesson.getStartTime()));
//		values.put(DatabasePermanentLessonConstants.END_TIME, this.database.dateTimeToMillis(lesson.getEndTime()));
//		
//		values.put(DatabasePermanentLessonConstants.LIST_CLASSES, value);
//		values.put(DatabasePermanentLessonConstants.LIST_TEACHER, value);
//		values.put(DatabasePermanentLessonConstants.LIST_ROOMS, value);
//		values.put(DatabasePermanentLessonConstants.LIST_SUBJECTS, value);
//		
//		// TODO Listen speichern
//		
//		this.database.insert(database, table, values);
//		
//		database.setTransactionSuccessful();
//		database.endTransaction();
//		this.database.closeDatabase(database);
	}

}
