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

import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;

public interface DatabaseCreateConstants {

	/** Verwende diesen Key, um Daten zu mehreren, unterschiedlichen LoginSets gleichzeit zu speichern **/
	public static final String TABLE_LOGINSET_KEY = "loginSetKey";
	
	public static final String TABLE_VIEW_TYPE_COLUMN_DEFINITIONS = DatabaseCreateConstants.TABLE_LOGINSET_KEY + " TEXT, " +
			  DatabaseViewTypeConstants.ID + " INTEGER, "
			+ DatabaseViewTypeConstants.NAME + " TEXT, "
			+ DatabaseViewTypeConstants.LONG_NAME + " TEXT, "
			+ DatabaseViewTypeConstants.FORE_COLOR + " TEXT, "
			+ DatabaseViewTypeConstants.BACK_COLOR + " TEXT";

	public static final String TABLE_SCHOOL_HOLIDAY_COLUMN_DEFINITIONS = DatabaseCreateConstants.TABLE_LOGINSET_KEY + " TEXT, " +
			  DatabaseSchoolHolidayConstants.ID + " INTEGER, "
			+ DatabaseSchoolHolidayConstants.NAME + " TEXT, "
			+ DatabaseSchoolHolidayConstants.LONG_NAME + " TEXT, "
			+ DatabaseSchoolHolidayConstants.START_DATE + " INTEGER, "
			+ DatabaseSchoolHolidayConstants.END_DATE + " INTEGER";

	public static final String TABLE_TIMEGRID_COLUMN_DEFINITIONS = DatabaseCreateConstants.TABLE_LOGINSET_KEY + " TEXT, " + 
			  DatabaseTimegridConstants.DAY + " INTEGER, "
			+ DatabaseTimegridConstants.NAME + " TEXT, "
			+ DatabaseTimegridConstants.START_TIME + " INTEGER, "
			+ DatabaseTimegridConstants.END_TIME + " INTEGER";

	public static final String TABLE_STATUS_DATA_COLUMN_DEFINITIONS = DatabaseCreateConstants.TABLE_LOGINSET_KEY + " TEXT, " + 
			  DatabaseStatusDataConstants.CODE + " TEXT, "
			+ DatabaseStatusDataConstants.FORE_COLOR + " TEXT, "
			+ DatabaseStatusDataConstants.BACK_COLOR + " TEXT";
	
	public static final String TABLE_PERMANENT_LESSONS_DEFINITIONS = DatabaseCreateConstants.TABLE_LOGINSET_KEY + " TEXT, "
			+ DatabasePermanentLessonConstants.ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ DatabasePermanentLessonConstants.WEEK_DAY + " INTEGER, "
			+ DatabasePermanentLessonConstants.START_TIME + " INTEGER, "
			+ DatabasePermanentLessonConstants.END_TIME + " INTEGER";
	
	public static final String TABLE_PERMANENT_LESSONS_VIEW_TYPE_DEFINITIONS = DatabaseCreateConstants.TABLE_LOGINSET_KEY + " TEXT, "
			+ DatabasePermanentLessonViewTypeConstants.LESSON_ID + " INTEGER, "
			+ DatabasePermanentLessonViewTypeConstants.VIEW_TYPE_ID + " INTEGER, "
			+ DatabasePermanentLessonViewTypeConstants.VIEW_TYPE_TYPE + " TEXT";
	
	public static final String TABLE_LOGIN_SETS_DEFINITIONS = LoginSetConstants.nameKey + " TEXT UNIQUE, "
    		+ LoginSetConstants.serverUrlKey + " TEXT, "
    		+ LoginSetConstants.schoolKey + " TEXT, "
    		+ LoginSetConstants.usernameKey + " TEXT, "
    		+ LoginSetConstants.passwordKey + " TEXT, "
    		+ LoginSetConstants.sslOnlyKey + " BOOLEAN";
	
	public static final String TABLE_AUTO_SELECT_DEFINITIONS = DatabaseCreateConstants.TABLE_LOGINSET_KEY + " TEXT, "
			+ DatabaseAutoSelectConstants.ENABLED + " BOOLEAN, "
			+ DatabaseAutoSelectConstants.TYPE + " TEXT, "
			+ DatabaseAutoSelectConstants.VALUE + " INTEGER";
	
	/*public static final String TABLE_LESSONS_COLUMN_DEFINITIONS = LoginSetConstants.nameKey + " TEXT UNIQUE, "
			+ DatabaseLessonConstants.ID+ " INTEGER, "
			+ DatabaseLessonConstants.DATE + " INTEGER, "
			+ DatabaseLessonConstants.START_TIME + " INTEGER, "
			+ DatabaseLessonConstants.END_TIME + " INTEGER, "
			+ DatabaseLessonConstants.LESSON_TYPE + " TEXT, "
			+ DatabaseLessonConstants.LESSON_CODE + " TEXT";*/
}
