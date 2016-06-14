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
package edu.htl3r.schoolplanner.gui.selectScreen;

import java.util.List;

import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class SelectScreenInstanceBundle {
	
	private DataFacade<List<SchoolClass>> classData;
	private DataFacade<List<SchoolTeacher>> teacherData;
	private DataFacade<List<SchoolRoom>> roomData;
	private DataFacade<List<SchoolSubject>> subjectData;
	
	private boolean autoSelectDone;
	
	public DataFacade<List<SchoolClass>> getClassData() {
		return classData;
	}
	public void setClassData(DataFacade<List<SchoolClass>> classData) {
		this.classData = classData;
	}
	public DataFacade<List<SchoolTeacher>> getTeacherData() {
		return teacherData;
	}
	public void setTeacherData(DataFacade<List<SchoolTeacher>> teacherData) {
		this.teacherData = teacherData;
	}
	public DataFacade<List<SchoolRoom>> getRoomData() {
		return roomData;
	}
	public void setRoomData(DataFacade<List<SchoolRoom>> roomData) {
		this.roomData = roomData;
	}
	public DataFacade<List<SchoolSubject>> getSubjectData() {
		return subjectData;
	}
	public void setSubjectData(DataFacade<List<SchoolSubject>> subjectData) {
		this.subjectData = subjectData;
	}
	public boolean isAutoSelectDone() {
		return autoSelectDone;
	}
	public void setAutoSelectDone(boolean autoSelectDone) {
		this.autoSelectDone = autoSelectDone;
	}
}
