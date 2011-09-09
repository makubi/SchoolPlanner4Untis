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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.TimetableDataProvider;
import edu.htl3r.schoolplanner.backend.TimetableDataStore;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseLessonConstants;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCode;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeCancelled;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeIrregular;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeBreakSupervision;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeExamination;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeOfficeHour;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeStandby;

public class TimetableDataDatabase implements TimetableDataStore, TimetableDataProvider{

	private Database database;
	
	private Map<Class<? extends LessonCode>, String> lessonCodeStringMap = new HashMap<Class<? extends LessonCode>, String>();
	private Map<Class<? extends LessonType>, String> lessonTypeStringMap =  new HashMap<Class<? extends LessonType>, String>();
	
	public TimetableDataDatabase(Database database) {
		this.database = database;
		
		lessonCodeStringMap.put(LessonCodeCancelled.class, DatabaseLessonConstants.LESSON_CODE_CANCELLED);
		lessonCodeStringMap.put(LessonCodeIrregular.class, DatabaseLessonConstants.LESSON_CODE_IRREGULAR);
		lessonCodeStringMap.put(LessonCodeSubstitute.class, DatabaseLessonConstants.LESSON_CODE_SUBSTITUTE);
		
		lessonTypeStringMap.put(LessonTypeBreakSupervision.class, DatabaseLessonConstants.LESSON_TYPE_BREAK_SUPERVISION);
		lessonTypeStringMap.put(LessonTypeExamination.class, DatabaseLessonConstants.LESSON_TYPE_EXAMINATION);
		lessonTypeStringMap.put(LessonTypeOfficeHour.class, DatabaseLessonConstants.LESSON_TYPE_OFFICE_HOUR);
		lessonTypeStringMap.put(LessonTypeStandby.class, DatabaseLessonConstants.LESSON_TYPE_STANDBY);
	}
	
	private long dateTimeToMillis(DateTime dateTime) {
		return dateTime.getAndroidTime().toMillis(true);
	}
	
	private DateTime millisToDateTime(long millis) {
		DateTime dateTime = new DateTime();
		dateTime.getAndroidTime().set(millis);
		return dateTime;
	}

	@Override
	public List<Lesson> getLessons(ViewType view, DateTime date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<Lesson>> getLessons(ViewType view,
			DateTime startDate, DateTime endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLessons(ViewType view, DateTime date, List<Lesson> lessons) {
		writeLessons(view, date, lessons, DatabaseLessonConstants.TABLE_LESSONS_NAME);
	}
	
	private void writeLessons(ViewType view, DateTime date, List<Lesson> lessons, String table) {
		// TODO one table per ViewType?
		/*SQLiteDatabase database = this.database.openDatabase(true);
		
		database.beginTransaction();
		for(Lesson lesson : lessons) {
			ContentValues values = new ContentValues();
			values.put(DatabaseLessonConstants.ID, lesson.getId());
			
			values.put(DatabaseLessonConstants.DATE, dateTimeToMillis(lesson.getDate()));
			values.put(DatabaseLessonConstants.START_TIME, dateTimeToMillis(lesson.getStartTime()));
			values.put(DatabaseLessonConstants.END_TIME, dateTimeToMillis(lesson.getEndTime()));
			
			values.put(DatabaseLessonConstants.SCHOOL_CLASS_LIST, getViewTypeListAsString(lesson.getSchoolClasses()));
			values.put(DatabaseLessonConstants.SCHOOL_TEACHER_LIST, getViewTypeListAsString(lesson.getSchoolTeachers()));
			values.put(DatabaseLessonConstants.SCHOOL_ROOM_LIST, getViewTypeListAsString(lesson.getSchoolRooms()));
			values.put(DatabaseLessonConstants.SCHOOL_SUBJECT_LIST, getViewTypeListAsString(lesson.getSchoolSubjects()));
			
			// null if unknown or none, nullColoumnHack?
			values.put(DatabaseLessonConstants.LESSON_CODE, lessonCodeStringMap.get(lesson.getLessonCode().getClass()));
			values.put(DatabaseLessonConstants.LESSON_TYPE, lessonTypeStringMap.get(lesson.getLessonType().getClass()));
			
			this.database.insert(database, table, values);
		}
		database.setTransactionSuccessful();
		database.endTransaction();
		this.database.closeDatabase(database);*/
	}

	private String getViewTypeListAsString(List<? extends ViewType> viewTypeList) {
		StringBuilder sb = new StringBuilder("");
		for(ViewType viewType : viewTypeList) {
			sb.append(viewType.getId()+",");
		}
		
		String viewTypeListString = sb.toString();
		if(viewTypeList.size() > 0) {
			// TODO check substring command
			viewTypeListString = viewTypeListString.substring(0, viewTypeListString.lastIndexOf(","));
		}
		
		return viewTypeListString;
	}

	@Override
	public void setLessons(ViewType view, DateTime startDate, DateTime endDate,
			Map<String, List<Lesson>> lessonList) {
		
	}

}
