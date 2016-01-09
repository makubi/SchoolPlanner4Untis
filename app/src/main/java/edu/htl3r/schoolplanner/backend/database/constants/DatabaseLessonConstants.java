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


public interface DatabaseLessonConstants {
	
	public static final String TABLE_LESSONS_NAME = "lessons";
	
	public static final String ID = "id";
	
	public static final String DATE = "date";
	public static final String START_TIME = "startTime";
	public static final String END_TIME = "endTime";
	
	public static final String LESSON_TYPE = "lessonType";
	public static final String LESSON_CODE = "lessonCode";
	
	public static final String LESSON_TYPE_BREAK_SUPERVISION = "lessonTypeBreakSupervision";
	public static final String LESSON_TYPE_EXAMINATION = "lessonTypeExamination";
	public static final String LESSON_TYPE_OFFICE_HOUR = "lessonTypeOfficeHour";
	public static final String LESSON_TYPE_STANDBY = "lessonTypeStandby";
	
	public static final String LESSON_CODE_CANCELLED = "lessonCodeCancelled";
	public static final String LESSON_CODE_IRREGULAR = "lessonCodeIrregular";
	public static final String LESSON_CODE_SUBSTITUTE = "lessonCodeSubstitute";
}
