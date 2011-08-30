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
package edu.htl3r.schoolplanner.backend.database.constants;

public interface DatabaseCreateConstants {

	public static final String TABLE_VIEW_TYPE_COLUMN_DEFINITIONS =
			  DatabaseViewTypeConstants.ID + " INTEGER, "
			+ DatabaseViewTypeConstants.NAME + " TEXT, "
			+ DatabaseViewTypeConstants.LONG_NAME + " TEXT, "
			+ DatabaseViewTypeConstants.FORE_COLOR + " TEXT, "
			+ DatabaseViewTypeConstants.BACK_COLOR + " TEXT";

	public static final String TABLE_SCHOOL_HOLIDAY_COLUMN_DEFINITIONS =
			  DatabaseSchoolHolidayConstants.ID + " INTEGER, "
			+ DatabaseSchoolHolidayConstants.NAME + " TEXT, "
			+ DatabaseSchoolHolidayConstants.LONG_NAME + " TEXT, "
			+ DatabaseSchoolHolidayConstants.START_DATE + " INTEGER, "
			+ DatabaseSchoolHolidayConstants.END_DATE + " INTEGER";

	public static final String TABLE_TIMEGRID_COLUMN_DEFINITIONS =
			  DatabaseTimegridConstants.DAY + " INTEGER, "
			+ DatabaseTimegridConstants.START_TIME + " INTEGER, "
			+ DatabaseTimegridConstants.END_TIME + " INTEGER";

	public static final String TABLE_STATUS_DATA_COLUMN_DEFINITIONS =
			  DatabaseStatusDataConstants.CODE + " TEXT, "
			+ DatabaseStatusDataConstants.FORE_COLOR + " INTEGER, "
			+ DatabaseStatusDataConstants.BACK_COLOR + " INTEGER";
	
	public static final String TABLE_LESSONS_COLUMN_DEFINITIONS =
			  DatabaseLessonConstants.ID+ " INTEGER, "
			+ DatabaseLessonConstants.DATE + " INTEGER, "
			+ DatabaseLessonConstants.START_TIME + " INTEGER, "
			+ DatabaseLessonConstants.END_TIME + " INTEGER, "
			// TODO: Als Liste von IDs speichern?
			+ DatabaseLessonConstants.SCHOOL_CLASS_LIST + " TEXT, "
			// TODO: Als Liste von IDs speichern?
			+ DatabaseLessonConstants.SCHOOL_TEACHER_LIST + " TEXT, "
			// TODO: Als Liste von IDs speichern?
			+ DatabaseLessonConstants.SCHOOL_ROOM_LIST + " TEXT, "
			// TODO: Als Liste von IDs speichern?
			+ DatabaseLessonConstants.SCHOOL_SUBJECT_LIST + " TEXT, "
			+ DatabaseLessonConstants.LESSON_TYPE + " TEXT, "
			+ DatabaseLessonConstants.LESSON_CODE + " TEXT";
}
