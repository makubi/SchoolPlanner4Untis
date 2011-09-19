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
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeCancelled;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeIrregular;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeBreakSupervision;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeExamination;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeOfficeHour;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeStandby;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class Lesson implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 6168669480973047866L;

	private int id;

	private DateTime date = new DateTime();
	private DateTime startTime = new DateTime();
	private DateTime endTime = new DateTime();
	
	private List<SchoolClass> schoolClasses = new ArrayList<SchoolClass>();
	private List<SchoolTeacher> schoolTeachers = new ArrayList<SchoolTeacher>();
	private List<SchoolSubject> schoolSubjects = new ArrayList<SchoolSubject>();
	private List<SchoolRoom> schoolRooms = new ArrayList<SchoolRoom>();

	private LessonType lessonType;
	private LessonCode lessonCode;

	@Deprecated private long last_update;

	/**
	 * Liefert das Datum der Stunde.<br>
	 * <b>ACHTUNG: Die Monatsnummer laeuft von 1 - 12!</b>
	 * @return Datum der Stunde
	 */
	public DateTime getDate() {
		return date;
	}

	/**
	 * Setzt das Datum der Stunde und der {@link Time}-Objekte, die fuer Start- und Endzeit verwendet werden.
	 * @param year Jahr, das gesetzt werden soll
	 * @param month Monat, der gesetzt werden soll (1-12)
	 * @param day Tag, der gesetzt werden soll
	 */
	public void setDate(int year, int month, int day) {
		date.set(day, month, year);
		startTime.set(day, month, year);
		endTime.set(day, month, year);
	}
	
	/**
	 * Liefert den Anfangszeitpunkt der Stunde (+ richtig gesetztem Datum).<br>
	 * <b>ACHTUNG: Die Monatsnummer laeuft von 1 - 12!</b>
	 * @return Datum der Stunde
	 */
	public DateTime getStartTime() {
		return startTime;
	}

	/**
	 * Setzt den Anfangszeitpunkt der Stunde.
	 * @param hour Stunde, die gesetzt werden soll
	 * @param minute Minute, die gesetzt werden soll
	 */
	public void setStartTime(int hour, int minute) {
		startTime.set(0, minute, hour, startTime.getDay(), startTime.getMonth(), startTime.getYear());
	}
	
	/**
	 * Liefert den Anfangszeitpunkt der Stunde (+ richtig gesetztem Datum).<br>
	 * <b>ACHTUNG: Die Monatsnummer laeuft von 1 - 12!</b>
	 * @return Datum der Stunde
	 */
	public DateTime getEndTime() {
		return endTime;
	}

	/**
	 * Setzt den Endzeitpunkt der Stunde.
	 * @param hour Stunde, die gesetzt werden soll
	 * @param minute Minute, die gesetzt werden soll
	 */
	public void setEndTime(int hour, int minute) {
		endTime.set(0, minute, hour, endTime.getDay(), endTime.getMonth(), endTime.getYear());
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
		this.schoolClasses = schoolClasses;
		sortList(this.schoolClasses);
	}

	public List<SchoolTeacher> getSchoolTeachers() {
		return schoolTeachers;
	}

	/**
	 * Sortiert die uebergebene Liste und setzt sie dann als Liste der Lehrer fuer diese Stunde.
	 * @param schoolTeachers Liste der Lehrer
	 */
	public void setSchoolTeachers(List<SchoolTeacher> schoolTeachers) {
		this.schoolTeachers = schoolTeachers;
		sortList(this.schoolTeachers);
	}

	public List<SchoolSubject> getSchoolSubjects() {
		return schoolSubjects;
	}

	/**
	 * Sortiert die uebergebene Liste und setzt sie dann als Liste der Faecher fuer diese Stunde.
	 * @param schoolSubjects Liste der Faecher
	 */
	public void setSchoolSubjects(List<SchoolSubject> schoolSubjects) {
		this.schoolSubjects = schoolSubjects;
		sortList(this.schoolSubjects);
	}

	public List<SchoolRoom> getSchoolRooms() {
		return schoolRooms;
	}

	/**
	 * Sortiert die uebergebene Liste und setzt sie dann als Liste der Raeume fuer diese Stunde.
	 * @param schoolRooms Liste der Raeume
	 */
	public void setSchoolRooms(List<SchoolRoom> schoolRooms) {
		this.schoolRooms = schoolRooms;
		sortList(this.schoolRooms);
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
		return dateEquals(another) && getStartTime().getHour() == another.getStartTime().getHour() && getStartTime().getMinute() == another.getStartTime().getMinute();
	}
	
	/**
	 * Ueberprueft, ob Datum, Anfangs- und Endzeit, Klassen-, Lehrer-, Raum- und Fachliste uebereinstimmen.
	 * @param another Stunde, mit der diese vergleichen werden soll
	 * @return 'true', wenn Stunde uebereinstimmt (siehe Methodenbeschreibung)
	 */
	public boolean matches(Lesson another) {
		if(equals(another)) {
			List<SchoolClass> anotherSchoolClasses = another.getSchoolClasses();
			List<SchoolTeacher> anotherSchoolTeachers = another.getSchoolTeachers();
			List<SchoolRoom> anotherSchoolRooms = another.getSchoolRooms();
			List<SchoolSubject> anotherSchoolSubjects = another.getSchoolSubjects();
			
			return schoolClasses.size() == anotherSchoolClasses.size() && schoolClasses.containsAll(anotherSchoolClasses) && schoolTeachers.size() == anotherSchoolTeachers.size() && schoolTeachers.containsAll(anotherSchoolTeachers) && schoolRooms.size() == anotherSchoolRooms.size() && schoolRooms.containsAll(anotherSchoolRooms) && schoolSubjects.size() == anotherSchoolSubjects.size() && schoolSubjects.containsAll(anotherSchoolSubjects);									
		}
		return false;
	}
	
	/**
	 * Ueberprueft, ob die uebergebene Stunde am gleichen Datum und zwischen der aktuellen stattfindet.
	 * @param another Zu ueberpruefende Stunde
	 * @return 'true', wenn die uebergebene Stunde am gleichen Datum und zwischen der aktuellen stattfindet
	 */
	
	public boolean inLesson(Lesson another) {
		return dateEquals(another) && 
		(getStartTime().getAndroidTime().before(another.getStartTime().getAndroidTime()) || getStartTime().getAndroidTime().equals(another.getStartTime().getAndroidTime())) && 
		(getEndTime().getAndroidTime().after(another.getEndTime().getAndroidTime()) || getEndTime().getAndroidTime().equals(another.getEndTime().getAndroidTime()));
	}
	
	private boolean dateEquals(Lesson another) {
		return getDate().getYear() == another.getDate().getYear() && getDate().getMonth() == another.getDate().getMonth() && getDate().getDay() == another.getDate().getDay();
	}
	
	private void sortList(List<? extends ViewType> viewTypeList) {
		Collections.sort(viewTypeList);
	}
	
	@Override
	public Lesson clone() {
		Lesson lessonClone =  new Lesson();
		
		lessonClone.id = id;
		
		lessonClone.date = date.clone();
		lessonClone.startTime = startTime.clone();
		lessonClone.endTime = endTime.clone();
		
		lessonClone.schoolClasses = new ArrayList<SchoolClass>(schoolClasses);
		lessonClone.schoolTeachers = new ArrayList<SchoolTeacher>(schoolTeachers);
		lessonClone.schoolRooms = new ArrayList<SchoolRoom>(schoolRooms);
		lessonClone.schoolSubjects = new ArrayList<SchoolSubject>(schoolSubjects);
		
		try {
			lessonClone.lessonType = (LessonType) lessonType.clone();
		} catch (CloneNotSupportedException e) {
			if(lessonType instanceof LessonTypeExamination) {
				lessonClone.lessonType = new LessonTypeExamination();
			}
			else if(lessonType instanceof LessonTypeBreakSupervision) {
				lessonClone.lessonType = new LessonTypeBreakSupervision();
			}
			else if(lessonType instanceof LessonTypeOfficeHour) {
				lessonClone.lessonType = new LessonTypeOfficeHour();
			}
			else if(lessonType instanceof LessonTypeStandby) {
				lessonClone.lessonType = new LessonTypeStandby();
			}
		}
		
		try {
			lessonClone.lessonCode = (LessonCode) lessonCode.clone();
		} catch (CloneNotSupportedException e) {
			if(lessonCode instanceof LessonCodeSubstitute) {
				lessonClone.lessonCode = new LessonCodeSubstitute();
				((LessonCodeSubstitute) lessonClone.lessonCode).setOriginSchoolRoom(((LessonCodeSubstitute) lessonCode).getOriginSchoolRoom());
				((LessonCodeSubstitute) lessonClone.lessonCode).setOriginSchoolTeacher(((LessonCodeSubstitute) lessonCode).getOriginSchoolTeacher());
			}
			else if(lessonCode instanceof LessonCodeIrregular) {
				lessonClone.lessonCode = new LessonCodeIrregular();
			}
			else if(lessonCode instanceof LessonCodeCancelled) {
				lessonClone.lessonCode = new LessonCodeCancelled();
			}
		}
		
		return lessonClone;
	}
	
}
