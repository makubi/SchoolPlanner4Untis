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

package edu.htl3r.schoolplanner.backend.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.backend.InternalMemoryTimetableDataProvider;
import edu.htl3r.schoolplanner.backend.InternalMemoryTimetableDataStore;
import edu.htl3r.schoolplanner.backend.MasterdataProvider;
import edu.htl3r.schoolplanner.backend.MasterdataStore;
import edu.htl3r.schoolplanner.backend.Timetable;
import edu.htl3r.schoolplanner.backend.TimetableDay;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

/**
 * Diese Klasse repraesentiert den internen, fluechtigen Speicher und haelt
 * Daten, um sie bei Gebrauch schneller und direkt aus dem RAM laden zu koennen.
 */
public class InternalMemory implements MasterdataProvider, MasterdataStore, InternalMemoryTimetableDataProvider, InternalMemoryTimetableDataStore {

	private List<SchoolClass> schoolClassList;
	private List<SchoolTeacher> schoolTeacherList;
	private List<SchoolSubject> schoolSubjectList;
	private List<SchoolRoom> schoolRoomList;

	private List<SchoolHoliday> schoolHolidayList;

	private Timegrid timegrid;
	
	private Timetable timetable = new Timetable();
	
	@Override
	public List<SchoolClass> getSchoolClassList() {
		return schoolClassList;
	}

	@Override
	public void setSchoolClassList(List<SchoolClass> schoolClassList) {
		this.schoolClassList = schoolClassList;
	}

	@Override
	public List<SchoolTeacher> getSchoolTeacherList() {
		return schoolTeacherList;
	}

	@Override
	public void setSchoolTeacherList(List<SchoolTeacher> schoolTeacherList) {
		this.schoolTeacherList = schoolTeacherList;
	}

	@Override
	public List<SchoolSubject> getSchoolSubjectList() {
		return schoolSubjectList;
	}

	@Override
	public void setSchoolSubjectList(List<SchoolSubject> schoolSubjectList) {
		this.schoolSubjectList = schoolSubjectList;
	}

	@Override
	public List<SchoolRoom> getSchoolRoomList() {
		return schoolRoomList;
	}

	@Override
	public void setSchoolRoomList(List<SchoolRoom> schoolRoomList) {
		this.schoolRoomList = schoolRoomList;
	}

	@Override
	public List<SchoolHoliday> getSchoolHolidayList() {
		return schoolHolidayList;
	}

	@Override
	public void setSchoolHolidayList(List<SchoolHoliday> schoolHolidayList) {
		this.schoolHolidayList = schoolHolidayList;
	}

	@Override
	public Timegrid getTimegrid() {
		return timegrid;
	}

	@Override
	public void setTimegrid(Timegrid timegrid) {
		this.timegrid = timegrid;
	}
	
	@Override
	public TimetableDay getTimetableForDay(ViewType view, DateTime date) {
		return getTimetableForDay(view, date);
	}

	@Override
	public Map<String, TimetableDay> getTimetableForTimePeriod(ViewType view,
			DateTime startDate, DateTime endDate) {
		Map<String, TimetableDay> lessonMap = new HashMap<String, TimetableDay>();
		DateTime tmpDate = startDate.clone();
		
		while(tmpDate.beforeOrEquals(endDate)) {
			String date = DateTimeUtils.toISO8601Date(tmpDate);
			TimetableDay list = timetable.getTimetableForDay(view, date);
			
			// If any day was not set yet, return null.
			// This can happen, if e.g. you retrieved timetable for
			// monday, tuesday, wednesday, thursday and then
			// request the timetable for the whole week.
			if(list == null) return null;
			
			lessonMap.put(date, list);
			tmpDate.increaseDay();
		}
		
		return lessonMap;
	}

	@Override
	public void setLessonsForDay(ViewType view, DateTime date,
			List<Lesson> lessons, DateTime lastRefreshTime) {
		timetable.putTimetableForDay(view, DateTimeUtils.toISO8601Date(date), lessons, lastRefreshTime);
	}

	@Override
	public void setLessonsForDay(ViewType view, DateTime startDate,
			DateTime endDate, Map<String, List<Lesson>> lessonMap,
			DateTime lastRefreshTime) {
		DateTime tmpDate = startDate.clone();
		
		while(tmpDate.beforeOrEquals(endDate)) {
			String date = DateTimeUtils.toISO8601Date(tmpDate);
			
			timetable.putTimetableForDay(view, date, lessonMap.get(date), lastRefreshTime);
			tmpDate.increaseDay();
		}
	}
	
}
