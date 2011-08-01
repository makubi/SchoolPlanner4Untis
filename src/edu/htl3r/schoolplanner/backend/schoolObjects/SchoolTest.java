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

package edu.htl3r.schoolplanner.backend.schoolObjects;

import java.util.Calendar;
import java.util.List;

import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class SchoolTest {

	private String id;
	private String title;
	private String description;
	private Calendar date;
	private Calendar start;
	private Calendar end;
	@Deprecated
	private int teacher;
	private List<SchoolTeacher> schoolTeachers;
	@Deprecated
	private int room;
	private List<SchoolRoom> schoolRooms;
	@Deprecated
	private int clazz;
	private List<SchoolClass> schoolClasses;
	@Deprecated
	private int subject;
	private List<SchoolSubject> schoolSubjects;
	private SchoolTestType type;
	private long last_update;
	private boolean local;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public Calendar getStart() {
		return start;
	}

	public void setStart(Calendar start) {
		this.start = start;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	@Deprecated
	public int getTeacher() {
		return teacher;
	}

	@Deprecated
	public void setTeacher(int teacher) {
		this.teacher = teacher;
	}

	public List<SchoolTeacher> getSchoolTeachers() {
		return schoolTeachers;
	}

	public void setSchoolTeachers(List<SchoolTeacher> schoolTeachers) {
		this.schoolTeachers = schoolTeachers;
	}

	@Deprecated
	public int getRoom() {
		return room;
	}

	@Deprecated
	public void setRoom(int room) {
		this.room = room;
	}

	public List<SchoolRoom> getSchoolRooms() {
		return schoolRooms;
	}

	public void setSchoolRooms(List<SchoolRoom> schoolRooms) {
		this.schoolRooms = schoolRooms;
	}

	@Deprecated
	public int getClazz() {
		return clazz;
	}

	@Deprecated
	public void setClazz(int clazz) {
		this.clazz = clazz;
	}

	public List<SchoolClass> getSchoolClasses() {
		return schoolClasses;
	}

	public void setSchoolClasses(List<SchoolClass> schoolClasses) {
		this.schoolClasses = schoolClasses;
	}

	@Deprecated
	public int getSubject() {
		return subject;
	}

	@Deprecated
	public void setSubject(int subject) {
		this.subject = subject;
	}

	public List<SchoolSubject> getSchoolSubjects() {
		return schoolSubjects;
	}

	public void setSchoolSubjects(List<SchoolSubject> schoolSubjects) {
		this.schoolSubjects = schoolSubjects;
	}


	/**
	 * @return the type
	 */
	public SchoolTestType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(SchoolTestType type) {
		this.type = type;
	}

	public long getLast_update() {
		return last_update;
	}

	public void setLast_update(long lastUpdate) {
		last_update = lastUpdate;
	}

	public void setLocal(boolean local) {
		this.local = local;
	}

	public boolean isLocal() {
		return local;
	}
}