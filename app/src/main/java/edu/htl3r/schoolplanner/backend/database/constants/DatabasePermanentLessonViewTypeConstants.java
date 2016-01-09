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

public interface DatabasePermanentLessonViewTypeConstants {
	public static final String TABLE_PERMANENT_LESSONS_SCHOOL_CLASSES_NAME = "permanentLessons_schoolClasses";
	public static final String TABLE_PERMANENT_LESSONS_SCHOOL_TEACHER_NAME = "permanentLessons_schoolTeacher";
	public static final String TABLE_PERMANENT_LESSONS_SCHOOL_ROOMS_NAME = "permanentLessons_schoolRooms";
	public static final String TABLE_PERMANENT_LESSONS_SCHOOL_SUBJECTS_NAME = "permanentLessons_schoolSubjects";
	
	public static final String LESSON_ID = "lessonID";
	public static final String VIEW_TYPE_ID = "viewTypeID";
	public static final String VIEW_TYPE_TYPE = "viewTypeType";
	
	public static final String VIEW_TYPE_SCHOOL_CLASS = "viewTypeSchoolClass";
	public static final String VIEW_TYPE_SCHOOL_TEACHER = "viewTypeSchoolTeacher";
	public static final String VIEW_TYPE_SCHOOL_ROOM = "viewTypeSchoolRoom";
	public static final String VIEW_TYPE_SCHOOL_SUBJECT = "viewTypeSchoolSubject";
	
}
