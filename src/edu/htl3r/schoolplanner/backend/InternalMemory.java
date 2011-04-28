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

package edu.htl3r.schoolplanner.backend;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTest;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTestType;
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
public class InternalMemory implements DataStore, DataProvider {

	private List<SchoolClass> schoolClassList;
	private List<SchoolTeacher> schoolTeacherList;
	private List<SchoolSubject> schoolSubjectList;
	private List<SchoolRoom> schoolRoomList;

	private List<SchoolHoliday> schoolHolidayList;

	private Timegrid timegrid;

	private List<SchoolTestType> schoolTestTypeList;
	@Deprecated private List<SchoolTest> schoolTestList;

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
	public List<SchoolTestType> getSchoolTestTypeList() {
		return schoolTestTypeList;
	}

	@Override
	public void setSchoolTestTypeList(List<SchoolTestType> schoolTestTypeList) {
		this.schoolTestTypeList = schoolTestTypeList;
	}

	@Override
	@Deprecated
	public List<SchoolTest> getSchoolTestList() {
		return schoolTestList;
	}

	@Override
	public List<Lesson> getLessons(ViewType view, Calendar date)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLessons(ViewType view, List<Lesson> lessons) {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, List<Lesson>> getLessons(ViewType view,
			Calendar startDate, Calendar endDate) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLessons(ViewType view, Calendar startDate, Calendar endDate,
			Map<String, List<Lesson>> lessonList) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Lesson> getMergedLessons(ViewType view, Calendar date)
			throws IOException {
		return timetable.get(view, CalendarUtils.getCalendarAs8601String(date));
	}

	@Override
	public Map<String, List<Lesson>> getMergedLessons(ViewType view,
			Calendar startDate, Calendar endDate) throws IOException {
		Calendar tmpStartCalendar = (Calendar) startDate.clone();
		Calendar tmpEndCalendar = (Calendar) endDate.clone();
		tmpEndCalendar.add(Calendar.DATE, 1);

		Map<String, List<Lesson>> lessonMap = new HashMap<String, List<Lesson>>();
		while (tmpStartCalendar.before(tmpEndCalendar)) {
			String date = CalendarUtils
					.getCalendarAs8601String(tmpStartCalendar);
			List<Lesson> lessonList = timetable.get(view, date);
			if (lessonList != null) {
				lessonMap.put(date, lessonList);
			}
			tmpStartCalendar.add(Calendar.DATE, 1);
		}
		return lessonMap.size() != 0 ? lessonMap : null;
	}

	@Override
	public void setMergedLessons(ViewType view, Calendar date,
			List<Lesson> lessonList) {
		timetable.put(view, CalendarUtils.getCalendarAs8601String(date),
				lessonList);
	}

	@Override
	public void setMergedLessons(ViewType view, Calendar startDate,
			Calendar endDate, Map<String, List<Lesson>> lessonMap) {
		for (String date : lessonMap.keySet()) {
			List<Lesson> lessonList = lessonMap.get(date);
			timetable.put(view, date, lessonList);
		}
	}

	@Override
	public List<SchoolTest> getSchoolTestList(ViewType view, Calendar startDate,
			Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveSchoolTest(SchoolTest schoolTest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSchoolTestList(List<SchoolTest> schoolTestList) {
		for(SchoolTest schoolTest : schoolTestList) {
			saveSchoolTest(schoolTest);
		}
	}
}
