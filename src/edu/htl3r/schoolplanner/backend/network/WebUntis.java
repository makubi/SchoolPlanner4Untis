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

package edu.htl3r.schoolplanner.backend.network;

/**
 * Statische Werte der WebUntis-Spezifikation.
 */
public interface WebUntis {
	
	// Timegrid days
	/**	Enthaelt die Nummer des Tages fuer Sonntag im Timegrid. */
	public static final int SUNDAY = 1;
	
	/**	Enthaelt die Nummer des Tages fuer Montag im Timegrid. */
	public static final int MONDAY = 2;
	
	/**	Enthaelt die Nummer des Tages fuer Dienstag im Timegrid. */
	public static final int TUESDAY = 3;
	
	/**	Enthaelt die Nummer des Tages fuer Mittwoch im Timegrid. */
	public static final int WEDNESDAY = 4;
	
	/**	Enthaelt die Nummer des Tages fuer Donnerstag im Timegrid. */
	public static final int THURSDAY = 5;
	
	/**	Enthaelt die Nummer des Tages fuer Freitag im Timegrid. */
	public static final int FRIDAY = 6;
	
	/**	Enthaelt die Nummer des Tages fuer Samstag im Timegrid. */
	public static final int SATURDAY = 7;

	
	// Viewtype IDs
	/**	Enthaelt die Nummer, die bei der Anfrage des Stundenplans fuer eine Klasse benoetigt wird. */
	public static final int SCHOOLCLASS = 1;
	
	/**	Enthaelt die Nummer, die bei der Anfrage des Stundenplans fuer einen Lehrer benoetigt wird. */
	public static final int SCHOOLTEACHER = 2;
	
	/**	Enthaelt die Nummer, die bei der Anfrage des Stundenplans fuer ein Fach benoetigt wird. */
	public static final int SCHOOLSUBJECT = 3;
	
	/**	Enthaelt die Nummer, die bei der Anfrage des Stundenplans fuer einen Raum benoetigt wird. */
	public static final int SCHOOLROOM = 4;

	
	// LessonTypeAbbreviation
	/** Enthaelt das Kuerzel des LessonTypes Lesson */
	public static final String LESSON = "ls";
	
	/**	Enthaelt das Kuerzel des LessonTypes OfficeHour. */
	public static final String OFFICEHOUR = "oh";
	
	/**	Enthaelt das Kuerzel des LessonTypes Standby. */
	public static final String STANDBY = "sb";
	
	/**	Enthaelt das Kuerzel des LessonTypes BreakSupervision. */
	public static final String BREAKSUPERVISION = "bs";
	
	/**	Enthaelt das Kuerzel des LessonTypes Examination. */
	public static final String EXAMINATION = "ex";

	
	// LessonCodes
	/**	Enthaelt den String des LessonCodes Cancelled. */
	public static final String CANCELLED = "cancelled";

	/** Enthaelt den String des LessonCodes Irregular */
	public static final String IRREGULAR = "irregular";
}
