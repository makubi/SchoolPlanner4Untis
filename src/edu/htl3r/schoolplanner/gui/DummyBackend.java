/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
			Sebastian Chlan <sebastian@schoolplanner.at>
			Christian Pascher <christian@schoolplanner.at>
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
package edu.htl3r.schoolplanner.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.format.Time;

import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class DummyBackend {

	public Map<Integer, List<Lesson>> getNonMultipleDummyLessonsForWeek() {
		Map<Integer, List<Lesson>> data = new HashMap<Integer, List<Lesson>>();
		
		List<SchoolClass> schoolClasses = new ArrayList<SchoolClass>();
		List<SchoolTeacher> schoolTeachers = new ArrayList<SchoolTeacher>();
		List<SchoolRoom> schoolRooms = new ArrayList<SchoolRoom>();
		List<SchoolSubject> schoolSubjects = new ArrayList<SchoolSubject>();
		
		for(int i = 0; i < 4; i++) {
			SchoolClass sc = new SchoolClass();
			sc.setId(i);
			sc.setName("Name "+i);
			sc.setLongName("LognName "+i);
			sc.setForeColor("#ff0000");
			sc.setBackColor("#000000");
			
			schoolClasses.add(sc);
			
			SchoolTeacher st = new SchoolTeacher();
			st.setId(i);
			st.setName("Name "+i);
			st.setLongName("LognName "+i);
			st.setForeColor("#ff0000");
			st.setBackColor("#000000");
			
			schoolTeachers.add(st);
		
			SchoolRoom sr = new SchoolRoom();
			sr.setId(i);
			sr.setName("Name "+i);
			sr.setLongName("LognName "+i);
			sr.setForeColor("#ff0000");
			sr.setBackColor("#000000");
			
			schoolRooms.add(sr);
		
			SchoolSubject ss = new SchoolSubject();
			ss.setId(i);
			ss.setName("Name "+i);
			ss.setLongName("LognName "+i);
			ss.setForeColor("#ff0000");
			ss.setBackColor("#000000");
			
			schoolSubjects.add(ss);
		}
		
		for(int i = 1; i <= 7; i++) {
			Time now = new Time();
			now.setToNow();
			
			List<Lesson> lessonList = new ArrayList<Lesson>();
			for(int j = 1; j <= 10; j++) {
				
				Lesson lesson = new Lesson();
				lesson.setDate(now.year, now.month, now.monthDay);
				lesson.setStartTime(j, (j*10)%60);
				lesson.setEndTime(j+1, (j*10)%60);
				lesson.setId((i*10)+j);
				lesson.setSchoolClasses(schoolClasses);
				lesson.setSchoolTeachers(schoolTeachers);
				lesson.setSchoolRooms(schoolRooms);
				lesson.setSchoolSubjects(schoolSubjects);
				
				lessonList.add(lesson);
			}
			data.put(i, lessonList);
		}
		return data;
	}
}
