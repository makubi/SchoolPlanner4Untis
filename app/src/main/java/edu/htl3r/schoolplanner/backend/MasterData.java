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

import java.util.List;

import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTestType;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

/**
 * Repraesentiert eine Klasse, die alle Stammdaten haelt.
 */
public class MasterData {

	private List<SchoolClass> schoolClassList;
	private List<SchoolTeacher> schoolTeacherList;
	private List<SchoolRoom> schoolRoomList;
	private List<SchoolSubject> schoolSubjectList;
	
	private List<SchoolHoliday> schoolHolidayList;
	private List<SchoolTestType> schoolTestTypeList;
	
	private Timegrid timegrid;
	
	public List<SchoolClass> getSchoolClassList() {
		return schoolClassList;
	}

	public void setSchoolClassList(List<SchoolClass> schoolClassList) {
		this.schoolClassList = schoolClassList;
	}

	public List<SchoolTeacher> getSchoolTeacherList() {
		return schoolTeacherList;
	}

	public void setSchoolTeacherList(List<SchoolTeacher> schoolTeacherList) {
		this.schoolTeacherList = schoolTeacherList;
	}

	public List<SchoolRoom> getSchoolRoomList() {
		return schoolRoomList;
	}

	public void setSchoolRoomList(List<SchoolRoom> schoolRoomList) {
		this.schoolRoomList = schoolRoomList;
	}

	public List<SchoolSubject> getSchoolSubjectList() {
		return schoolSubjectList;
	}

	public void setSchoolSubjectList(List<SchoolSubject> schoolSubjectList) {
		this.schoolSubjectList = schoolSubjectList;
	}

	public Timegrid getTimegrid() {
		return timegrid;
	}

	public void setTimegrid(Timegrid timegrid) {
		this.timegrid = timegrid;
	}

	public List<SchoolHoliday> getSchoolHolidayList() {
		return schoolHolidayList;
	}

	public void setSchoolHolidayList(List<SchoolHoliday> schoolHolidayList) {
		this.schoolHolidayList = schoolHolidayList;
	}

	/**
	 * Not implemented yet! 
	 * @return null
	 */
	public List<SchoolTestType> getSchoolTestTypeList() {
		return schoolTestTypeList;
	}

	/**
	 * Not implemented yet! 
	 * @param schoolTestTypeList
	 */
	public void setSchoolTestTypeList(List<SchoolTestType> schoolTestTypeList) {
		this.schoolTestTypeList = schoolTestTypeList;
	}
	
}