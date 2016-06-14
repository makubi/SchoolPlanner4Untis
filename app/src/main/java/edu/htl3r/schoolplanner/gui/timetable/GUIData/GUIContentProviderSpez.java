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
package edu.htl3r.schoolplanner.gui.timetable.GUIData;

import java.util.List;

import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public interface GUIContentProviderSpez {
	
	public List<SchoolRoom> getAllSchoolRooms();
	public List<SchoolClass> getAllSchoolClasses();
	public List<SchoolSubject> getAllSchoolSubjects();
	public List<SchoolTeacher> getAllSchoolTeachers();
	public List<SchoolHoliday> getAllSchoolHolidays();
 	public Timegrid getTimeGrid();

 	
	//public List<Lesson> getLessonsForDate(ViewType vt, DateTime start);
	public DataFromNetwork getLessonsForSomeTime(ViewType vt, DateTime start, DateTime end);
	public DataFromNetwork getLessonsForSomeTimeFromNetwork(ViewType vt, DateTime start, DateTime end);
	
}
