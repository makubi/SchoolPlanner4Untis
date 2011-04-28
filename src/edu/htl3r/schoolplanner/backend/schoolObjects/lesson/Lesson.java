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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class Lesson implements Serializable, Comparable<Lesson> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6168669480973047866L;

	private int id;

	private Calendar date;
	private Calendar startTime;
	private Calendar endTime;

	private List<SchoolClass> schoolClasses = new ArrayList<SchoolClass>();
	private List<SchoolTeacher> schoolTeachers = new ArrayList<SchoolTeacher>();
	private List<SchoolSubject> schoolSubjects = new ArrayList<SchoolSubject>();
	private List<SchoolRoom> schoolRooms = new ArrayList<SchoolRoom>();

	private LessonType lessonType;
	private LessonCode lessonCode;

	private long last_update;

	public Lesson() {
		startTime = Calendar.getInstance();
		endTime = Calendar.getInstance();
	}

	/**
	 * Liefert den aktuellen Kalender (Datum) der Stunde.
	 * 
	 * @return das Datum der Stunde
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * Setzt den Kalender (Datum) der Stunde.
	 * 
	 * @param date
	 *            Kalender, der verwendet werden soll
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(int hour, int minute) {
		// Setze nicht verwendete Werte auf 0
		startTime.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
				date.get(Calendar.DAY_OF_MONTH), hour, minute, 0);
		startTime.set(Calendar.MILLISECOND, 0);
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(int hour, int minute) {
		// Setze nicht verwendete Werte auf 0
		endTime.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
				date.get(Calendar.DAY_OF_MONTH), hour, minute, 0);
		endTime.set(Calendar.MILLISECOND, 0);
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

	public void setSchoolClasses(List<SchoolClass> schoolClasses) {
		this.schoolClasses = schoolClasses;
		sortLists();
	}

	public List<SchoolTeacher> getSchoolTeachers() {
		return schoolTeachers;
	}

	public void setSchoolTeachers(List<SchoolTeacher> schoolTeachers) {
		this.schoolTeachers = schoolTeachers;
		sortLists();
	}

	public List<SchoolSubject> getSchoolSubjects() {
		return schoolSubjects;
	}

	public void setSchoolSubjects(List<SchoolSubject> schoolSubjects) {
		this.schoolSubjects = schoolSubjects;
		sortLists();
	}

	public List<SchoolRoom> getSchoolRooms() {
		return schoolRooms;
	}

	public void setSchoolRooms(List<SchoolRoom> schoolRooms) {
		this.schoolRooms = schoolRooms;
		sortLists();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getLast_update() {
		return last_update;
	}

	public void setLast_update(long lastUpdate) {
		last_update = lastUpdate;
	}

	@Override
	public String toString() {
		String out = "============== Lesson ==============\n";
		for(int i=0;i<schoolClasses.size();i++){
			out += "class[" +i +"]: " + schoolClasses.get(i).getName() + "\n";
		}
		
		for(int i=0;i<schoolRooms.size();i++){
			out += "room[" +i +"]: " + schoolRooms.get(i).getName() + "\n";
		}
		
		for(int i=0;i<schoolSubjects.size();i++){
			out += "subject[" +i +"]: " + schoolSubjects.get(i).getName() + "\n";
		}
		
		for(int i=0;i<schoolTeachers.size();i++){
			out += "teacher[" +i +"]: " + schoolTeachers.get(i).getName() + "\n";
		}
		if(lessonType != null){
			out += "lessontype: " +lessonType.getClass().getSimpleName() +"\n";
		}
		if(lessonCode!= null){
			out += "educationtype: " +lessonCode.getClass().getSimpleName() +"\n";
		}
		if(date!=null){
			out += "date: " + CalendarUtils.getDateString(date, false) + "\n";
		}
		if(startTime!=null){
		out += "starttime: " + CalendarUtils.getTimeStr(startTime, false) + "\n";
		}
		if(endTime!=null){
		out += "endtime: " + CalendarUtils.getTimeStr(endTime, false) + "\n";
		}
		return out;
	}

	@Override
	public int compareTo(Lesson another) {
		int diff = getDate().compareTo(another.getDate());
		return diff == 0 ? getStartTime().compareTo(another.getStartTime()) : diff;
	}

	public boolean equals(Lesson another) {
		return getDate().get(Calendar.YEAR) == another.getDate().get(Calendar.YEAR) && getDate().get(Calendar.MONTH) == another.getDate().get(Calendar.MONTH) && getDate().get(Calendar.DAY_OF_MONTH) == another.getDate().get(Calendar.DAY_OF_MONTH) && getStartTime().get(Calendar.HOUR_OF_DAY) == another.getStartTime().get(Calendar.HOUR_OF_DAY) && getStartTime().get(Calendar.MINUTE) == another.getStartTime().get(Calendar.MINUTE);
	}
	
	public boolean inLesson(Lesson another) {
		return getDate().equals(another.getDate()) && 
		(getStartTime().before(another.getStartTime()) || getStartTime().equals(another.getStartTime())) && 
		(getEndTime().after(another.getEndTime()) || getEndTime().equals(another.getEndTime()));
		//return getDate().get(Calendar.YEAR) == another.getDate().get(Calendar.YEAR) && getDate().get(Calendar.MONTH) == another.getDate().get(Calendar.MONTH) && getDate().get(Calendar.DAY_OF_MONTH) == another.getDate().get(Calendar.DAY_OF_MONTH) && getStartTime().get(Calendar.HOUR_OF_DAY) >= another.getStartTime().get(Calendar.HOUR_OF_DAY) && getStartTime().get(Calendar.MINUTE) >= another.getStartTime().get(Calendar.MINUTE) && getEndTime().get(Calendar.HOUR_OF_DAY) <= another.getEndTime().get(Calendar.HOUR_OF_DAY) && getEndTime().get(Calendar.MINUTE) <= another.getEndTime().get(Calendar.MINUTE);
	}
	
	// TODO: Auf append(Lesson) umstellen und mit LessonCode usw. auch machen
	
	public void appendLists(List<SchoolClass> schoolClasses, List<SchoolTeacher> schoolTeachers, List<SchoolSubject> schoolSubjects, List<SchoolRoom> schoolRooms) {
		for(SchoolClass schoolClass : schoolClasses) {
			if(!containsSchoolClass(schoolClass)) {
				this.schoolClasses.add(schoolClass);
			}
		}
		
		for(SchoolTeacher schoolTeacher : schoolTeachers) {
			if(!containsSchoolTeacher(schoolTeacher)) {
				this.schoolTeachers.add(schoolTeacher);
			}
		}
		
		for(SchoolSubject schoolSubject : schoolSubjects) {
			if(!containsSchoolSubject(schoolSubject)) {
				this.schoolSubjects.add(schoolSubject);
			}
		}
		
		for(SchoolRoom schoolRoom : schoolRooms) {
			if(!containsSchoolRoom(schoolRoom)) {
				this.schoolRooms.add(schoolRoom);
			}
		}
		sortLists();
	}
	
	private boolean containsSchoolClass(SchoolClass newSchoolClass) {
		for(SchoolClass schoolClass : this.schoolClasses) {
			if(schoolClass.getName().equals(newSchoolClass.getName())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean containsSchoolTeacher(SchoolTeacher newSchoolTeacher) {
		for(SchoolTeacher schoolTeacher : this.schoolTeachers) {
			if(schoolTeacher.getName().equals(newSchoolTeacher.getName())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean containsSchoolSubject(SchoolSubject newSchoolSubject) {
		for(SchoolSubject schoolSubject : this.schoolSubjects) {
			if(schoolSubject.getName().equals(newSchoolSubject.getName())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean containsSchoolRoom(SchoolRoom newSchoolRoom) {
		for(SchoolRoom schoolRoom : this.schoolRooms) {
			if(schoolRoom.getName().equals(newSchoolRoom.getName())) {
				return true;
			}
		}
		return false;
	}
	
	private void sortLists() {
		Collections.sort(schoolClasses);
		Collections.sort(schoolTeachers);
		Collections.sort(schoolSubjects);
		Collections.sort(schoolRooms);
	}
}
