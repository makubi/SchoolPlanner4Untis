package edu.htl3r.schoolplanner.gui.basti.GUIData;

import java.util.List;
import java.util.Map;

import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
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

	public List<Lesson> getLessonsForDate(ViewType vt, DateTime start);
	public Map<String,List<Lesson>> getLessonsForSomeTime(ViewType vt, DateTime start, DateTime end);
}
