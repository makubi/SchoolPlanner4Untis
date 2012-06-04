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
package edu.htl3r.schoolplanner.gui.timetable.Lessons;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUILessonContainer;

public class LessonInfoManager {

	private ArrayList<String> firstline = new ArrayList<String>();
	private ArrayList<String> secondline = new ArrayList<String>();
	private ArrayList<String> thirdline = new ArrayList<String>();

	private ViewType viewtype;
	private GUILessonContainer lessoncontainer;

	public LessonInfoManager(GUILessonContainer container, ViewType viewtype) {
		this.viewtype = viewtype;
		this.lessoncontainer = container;
		initData(); 
	}

	private void initData() {
		List<? extends ViewType> vtfirstline = null;
		List<? extends ViewType> vtsecondline = null;
		List<? extends ViewType> vtthirdline = null;
		List<Lesson> lessons = giveMeTheCorrectList();

		for (Lesson l : lessons) {

			if (viewtype instanceof SchoolClass) {
				vtfirstline = l.getSchoolSubjects();
				vtsecondline = l.getSchoolTeachers();
				vtthirdline = l.getSchoolRooms();

				if (l.getLessonCode() instanceof LessonCodeSubstitute) {
					secondline.add(substituteLessonTeacherString((LessonCodeSubstitute) l.getLessonCode()));
					thirdline.add(substituteLessonRoomString((LessonCodeSubstitute) l.getLessonCode()));
				}

			} else if (viewtype instanceof SchoolTeacher) {
				vtfirstline = l.getSchoolClasses();
				vtsecondline = l.getSchoolSubjects();
				vtthirdline = l.getSchoolRooms();

				if (l.getLessonCode() instanceof LessonCodeSubstitute) {
					thirdline.add(substituteLessonRoomString((LessonCodeSubstitute) l.getLessonCode()));
				}

			} else if (viewtype instanceof SchoolRoom) {
				vtfirstline = l.getSchoolClasses();
				vtsecondline = l.getSchoolTeachers();
				vtthirdline = l.getSchoolSubjects();

				if (l.getLessonCode() instanceof LessonCodeSubstitute) {
					secondline.add(substituteLessonTeacherString((LessonCodeSubstitute) l.getLessonCode()));
				}

			} else if (viewtype instanceof SchoolSubject) {
				vtfirstline = l.getSchoolTeachers();
				vtsecondline = l.getSchoolClasses();
				vtthirdline = l.getSchoolRooms();

				if (l.getLessonCode() instanceof LessonCodeSubstitute) {
					firstline.add(substituteLessonTeacherString((LessonCodeSubstitute) l.getLessonCode()));
					thirdline.add(substituteLessonRoomString((LessonCodeSubstitute) l.getLessonCode()));
				}
			}

			for (ViewType s : vtfirstline) {
				if (!firstline.contains(s.getName()))
					firstline.add(s.getName());
			}
			for (ViewType s : vtsecondline) {
				if (!secondline.contains(s.getName()))
					secondline.add(s.getName());
			}
			for (ViewType s : vtthirdline) {
				if (!thirdline.contains(s.getName()))
					thirdline.add(s.getName());
			}

		}
	}

	private List<Lesson> giveMeTheCorrectList() {
		List<Lesson> lessons = new ArrayList<Lesson>();

		switch (lessoncontainer.isSomethinStrange()) {
		case GUILessonContainer.NORMAL:
			lessons = lessoncontainer.getStandardLessons();
			break;
		case GUILessonContainer.STRANGE:
			if (lessoncontainer.allCancelled()) { // Wurde alle Stunden
													// gestrichen?
				lessons = lessoncontainer.getSpecialLessons();
			} else if (lessoncontainer.containsSubsituteLesson()) { // Gibt es
																	// Supplierstunden?
				lessons = lessoncontainer.getAllLessons();
			} else { // Zeige die
				lessons = lessoncontainer.getIrregularLessons();
			}
			break;
		case GUILessonContainer.STRANGE_NORMAL:
			lessons = lessoncontainer.getAllLessons();
			break;
		}

		return lessons;
	}

	private String substituteLessonTeacherString(LessonCodeSubstitute lcs) {
		SchoolTeacher originSchoolTeacher = lcs.getOriginSchoolTeacher();
		if (originSchoolTeacher != null)
			return "(" + originSchoolTeacher.getName() + ")";
		return "";
	}

	private String substituteLessonRoomString(LessonCodeSubstitute lcs) {
		SchoolRoom getOriginSchoolRoom = lcs.getOriginSchoolRoom();
		if (getOriginSchoolRoom != null)
			return "(" + getOriginSchoolRoom.getName() + ")";
		return "";
	}

	private String prepareListForDisplay(ArrayList<String> input) {
		StringBuilder sb = new StringBuilder();
		String tmp = "";

		for (String s : input) {
			sb.append(s + ",\u00A0");
		}
		tmp = sb.toString();
		if (tmp.length() < 2)
			return tmp;
		else
			return tmp.substring(0, tmp.length() - 2);
	}
	
	public int getBackgroundColor() {

		List<Lesson> lessons = giveMeTheCorrectList();

		List<? extends ViewType> vt = null;

		if (lessons.size() != 0) {
			if (getVT() instanceof SchoolClass) {
				vt = lessons.get(0).getSchoolSubjects();
			} else if (getVT() instanceof SchoolTeacher) {
				//vt = lessons.get(0).getSchoolClasses();
				vt = lessons.get(0).getSchoolSubjects();
			} else if (getVT() instanceof SchoolRoom) {
				vt = lessons.get(0).getSchoolClasses();
			} else if (getVT() instanceof SchoolSubject) {
				vt = lessons.get(0).getSchoolTeachers();
			}

			if (vt.size() != 0) {
				String bcolor = vt.get(0).getBackColor();
				if (!bcolor.equalsIgnoreCase("")) {
					return Color.parseColor("#55" + bcolor);
				}
			}
		}
		return Color.WHITE;
	}
	
	
	
	public String getFirstLine(){
		return prepareListForDisplay(firstline);
	}

	public String getSecondLine(){
		return prepareListForDisplay(secondline);
	}
	
	public String getThirdLine(){
		return prepareListForDisplay(thirdline);
	}
	
	public ViewType getVT(){
		return viewtype;
	}
	
	public DateTime getDateTime(){
		return lessoncontainer.getDate();
	}
	
	public GUILessonContainer getLessonContainer(){
		return lessoncontainer;
	}
}
