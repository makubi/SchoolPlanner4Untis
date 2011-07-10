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

package edu.htl3r.schoolplanner.backend.schoolObjects.lesson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.text.format.Time;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class Lesson implements Serializable {
	
	private static final long serialVersionUID = 6168669480973047866L;

	private int id;

	private Time date = new Time();
	private Time startTime = new Time();
	private Time endTime = new Time();
	
	private List<SchoolClass> schoolClasses = new ArrayList<SchoolClass>();
	private List<SchoolTeacher> schoolTeachers = new ArrayList<SchoolTeacher>();
	private List<SchoolSubject> schoolSubjects = new ArrayList<SchoolSubject>();
	private List<SchoolRoom> schoolRooms = new ArrayList<SchoolRoom>();

	private LessonType lessonType;
	private LessonCode lessonCode;

	@Deprecated private long last_update;

	/**
	 * Liefert das Datum der Stunde.<br>
	 * <b>ACHTUNG: Die Monatsnummer laeuft von 0 - 11!</b>
	 * @return Datum der Stunde
	 */
	public Time getDate() {
		return date;
	}

	/**
	 * Setzt das Datum der Stunde und der {@link Time}-Objekte, die fuer Start- und Endzeit verwendet werden.
	 * @param year Jahr, das gesetzt werden soll
	 * @param month Monat, der gesetzt werden soll (1-12)
	 * @param day Tag, der gesetzt werden soll
	 */
	public void setDate(int year, int month, int day) {
		date.set(day, month-1, year);
		startTime.set(day, month-1, year);
		endTime.set(day, month-1, year);
	}
	
	/**
	 * Liefert den Anfangszeitpunkt der Stunde (+ richtig gesetztem Datum).<br>
	 * <b>ACHTUNG: Die Monatsnummer laeuft von 0 - 11!</b>
	 * @return Datum der Stunde
	 */
	public Time getStartTime() {
		return startTime;
	}

	/**
	 * Setzt den Anfangszeitpunkt der Stunde.
	 * @param hour Stunde, die gesetzt werden soll
	 * @param minute Minute, die gesetzt werden soll
	 */
	public void setStartTime(int hour, int minute) {
		startTime.set(0, minute, hour, startTime.monthDay, startTime.month, startTime.year);
	}
	
	/**
	 * Liefert den Anfangszeitpunkt der Stunde (+ richtig gesetztem Datum).<br>
	 * <b>ACHTUNG: Die Monatsnummer laeuft von 0 - 11!</b>
	 * @return Datum der Stunde
	 */
	public Time getEndTime() {
		return endTime;
	}

	/**
	 * Setzt den Endzeitpunkt der Stunde.
	 * @param hour Stunde, die gesetzt werden soll
	 * @param minute Minute, die gesetzt werden soll
	 */
	public void setEndTime(int hour, int minute) {
		endTime.set(0, minute, hour, endTime.monthDay, endTime.month, endTime.year);
	}	
	
	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
	}

	public LessonCode getLessonCode() {
		return lessonCode;
	}

	public void setLessonCode(LessonCode lessonCode) {
		this.lessonCode = lessonCode;
	}

	public List<SchoolClass> getSchoolClasses() {
		return schoolClasses;
	}

	/**
	 * Sortiert die uebergebene Liste und setzt sie dann als Liste der Klassen fuer diese Stunde.
	 * @param schoolClasses Liste der Klassen
	 */
	public void setSchoolClasses(List<SchoolClass> schoolClasses) {
		sortList(schoolClasses);
		this.schoolClasses = schoolClasses;
	}

	public List<SchoolTeacher> getSchoolTeachers() {
		return schoolTeachers;
	}

	/**
	 * Sortiert die uebergebene Liste und setzt sie dann als Liste der Lehrer fuer diese Stunde.
	 * @param schoolTeachers Liste der Lehrer
	 */
	public void setSchoolTeachers(List<SchoolTeacher> schoolTeachers) {
		sortList(schoolTeachers);
		this.schoolTeachers = schoolTeachers;
	}

	public List<SchoolSubject> getSchoolSubjects() {
		return schoolSubjects;
	}

	/**
	 * Sortiert die uebergebene Liste und setzt sie dann als Liste der Faecher fuer diese Stunde.
	 * @param schoolSubjects Liste der Faecher
	 */
	public void setSchoolSubjects(List<SchoolSubject> schoolSubjects) {
		sortList(schoolSubjects);
		this.schoolSubjects = schoolSubjects;
	}

	public List<SchoolRoom> getSchoolRooms() {
		return schoolRooms;
	}

	/**
	 * Sortiert die uebergebene Liste und setzt sie dann als Liste der Raeume fuer diese Stunde.
	 * @param schoolRooms Liste der Raeume
	 */
	public void setSchoolRooms(List<SchoolRoom> schoolRooms) {
		sortList(schoolRooms);
		this.schoolRooms = schoolRooms;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Deprecated
	public long getLast_update() {
		return last_update;
	}

	@Deprecated
	public void setLast_update(long lastUpdate) {
		last_update = lastUpdate;
	}

	@Override
	public String toString() {
		return "Lesson [id=" + id + ", date=" + date + ", startTime="
				+ startTime + ", endTime=" + endTime + ", schoolClasses="
				+ schoolClasses + ", schoolTeachers=" + schoolTeachers
				+ ", schoolSubjects=" + schoolSubjects + ", schoolRooms="
				+ schoolRooms + ", lessonType=" + lessonType + ", lessonCode="
				+ lessonCode + ", last_update=" + last_update + "]";
	}
	

	/**
	 * Ueberprueft, ob die uebergebene Stunde am selben Datum und zur selben Zeit startet und endet wie diese Stunde.
	 * @param another Stunde, die mit der aktuellen verglichen werden soll
	 * @return 'true', wenn die Stunden am selben Datum und zur selben Zeit stattfinden
	 */
	
	public boolean equals(Lesson another) {
		return dateEquals(another) && getStartTime().hour == another.getStartTime().hour && getStartTime().minute == another.getStartTime().minute;
	}
	
	/**
	 * Ueberprueft, ob die uebergebene Stunde am gleichen Datum und zwischen der aktuellen stattfindet.
	 * @param another Zu ueberpruefende Stunde
	 * @return 'true', wenn die uebergebene Stunde am gleichen Datum und zwischen der aktuellen stattfindet
	 */
	
	public boolean inLesson(Lesson another) {
		return dateEquals(another) && 
		(getStartTime().before(another.getStartTime()) || getStartTime().equals(another.getStartTime())) && 
		(getEndTime().after(another.getEndTime()) || getEndTime().equals(another.getEndTime()));
	}
	
	private boolean dateEquals(Lesson another) {
		return getDate().year == another.getDate().year && getDate().month == another.getDate().month && getDate().monthDay == another.getDate().monthDay;
	}
	
	private void sortList(List<? extends ViewType> viewTypeList) {
		Collections.sort(viewTypeList);
	}
}
