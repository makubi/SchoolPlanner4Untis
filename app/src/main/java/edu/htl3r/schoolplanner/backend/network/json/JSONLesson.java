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

package edu.htl3r.schoolplanner.backend.network.json;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JSONLesson {

	private int id;

	private long date;
	private int startTime;
	private int endTime;
	
	private List<ViewTypeId> schoolClasses = new ArrayList<ViewTypeId>();
	private List<ViewTypeId> schoolTeachers = new ArrayList<ViewTypeId>();
	private List<ViewTypeId> schoolSubjects = new ArrayList<ViewTypeId>();
	private List<ViewTypeId> schoolRooms = new ArrayList<ViewTypeId>();

	private String lessonType;
	private String lessonCode;
		
	protected int getId() {
		return id;
	}

	protected long getDate() {
		return date;
	}

	protected int getStartTime() {
		return startTime;
	}

	protected int getEndTime() {
		return endTime;
	}

	protected List<Integer> getSchoolClasses() {
		return toIntegerList(schoolClasses);
	}

	protected List<Integer> getSchoolTeachers() {
		return toIntegerList(schoolTeachers);
	}

	protected List<Integer> getSchoolSubjects() {
		return toIntegerList(schoolSubjects);
	}

	protected List<Integer> getSchoolRooms() {
		return toIntegerList(schoolRooms);
	}

	protected String getLessonType() {
		return lessonType;
	}

	protected String getLessonCode() {
		return lessonCode;
	}

	@JsonProperty(value="id")
	protected void setId(int id) {
		this.id = id;
	}

	@JsonProperty(value="date")
	protected void setDate(long date) {
		this.date = date;
	}

	@JsonProperty(value="startTime")
	protected void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	@JsonProperty(value="endTime")
	protected void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	@JsonProperty(value="kl")
	protected void setSchoolClasses(List<ViewTypeId> schoolClasses) {
		this.schoolClasses = schoolClasses;
	}

	@JsonProperty(value="te")
	protected void setSchoolTeachers(List<ViewTypeId> schoolTeachers) {
		this.schoolTeachers = schoolTeachers;
	}

	@JsonProperty(value="su")
	protected void setSchoolSubjects(List<ViewTypeId> schoolSubjects) {
		this.schoolSubjects = schoolSubjects;
	}

	@JsonProperty(value="ro")
	protected void setSchoolRooms(List<ViewTypeId> schoolRooms) {
		this.schoolRooms = schoolRooms;
	}

	@JsonProperty(value="lstype")
	protected void setLessonType(String lessonType) {
		this.lessonType = lessonType;
	}

	@JsonProperty(value="code")
	protected void setLessonCode(String lessonCode) {
		this.lessonCode = lessonCode;
	}
	
	private List<Integer> toIntegerList(List<ViewTypeId> viewTypeIdList) {
		List<Integer> list = new ArrayList<Integer>();
		
		for(ViewTypeId viewTypeId : viewTypeIdList) list.add(viewTypeId.getId());
		
		return list;
	}
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ViewTypeId {
	
	private int id;

	protected int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}
}