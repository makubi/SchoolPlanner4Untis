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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.util.Log;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.backend.network.WebUntis;
import edu.htl3r.schoolplanner.backend.preferences.Settings;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeCancelled;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeIrregular;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;

public class RenderInfoWeekTable implements WebUntis {

	private Map<String, List<Lesson>> weekdata;

	private Timegrid timegrid;
	private List<SchoolHoliday> holidays;
	private ViewType viewtype;

	private Settings settings;

	public RenderInfoWeekTable() {

	}

	public void setWeekData(Map<String, List<Lesson>> w) {
		weekdata = w;
	}

	public void setHolidays(List<SchoolHoliday> hol) {
		holidays = hol;
	}

	public void setTimeGrid(Timegrid t) {
		timegrid = t;
	}

	public void setViewType(ViewType vt) {
		viewtype = vt;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public GUIWeek analyse() {
		// TODO Alles da?
		Set<String> keySet = weekdata.keySet();
		TreeSet<DateTime> dates = new TreeSet<DateTime>();

		for (Object element : keySet) {
			String string = (String) element;
			dates.add(DateTimeUtils.iso8601StringToDateTime(string));
		}

		GUIWeek week = new GUIWeek();

		for (Object element : dates) {
			DateTime dateTime = (DateTime) element;
			List<Lesson> lessons = weekdata.get(DateTimeUtils.toISO8601Date(dateTime));
			GUIDay d = analyseDay(dateTime, lessons);
			week.setGUIDay(dateTime, d);
		}
		week.setViewType(viewtype);
		week.setTimegrid(timegridForDateTimeDay);
		week.setHolidays(holidays);
		return week;
	}

	private List<TimegridUnit> timegridForDateTimeDay;
	private TimegridUnit zerolesson = null;
	private List<TimegridUnit> timegridForDay = null;

	private GUIDay analyseDay(DateTime date, List<Lesson> lessons) {
		GUIDay day = new GUIDay();
		day.setDate(date);
		boolean dispZerolesson = settings.isDisplayZerothLesson();

		timegridForDateTimeDay = new ArrayList<TimegridUnit>();
		
		// Der Montag ist sicher nie null ansonsten stimmt was anders nicht
		// Ist jedoch zB. der Samstag null dann wird das TimeGrid von Freitag benutzt
		if(timegrid.getTimegridForDateTimeDay(date.getWeekDay()) != null){		
			timegridForDay = timegrid.getTimegridForDateTimeDay(date.getWeekDay());
		}

		// FIXME Gruber and Petters!
		// An mich:
		// Hier wird die Einstellung Display Zero Lesson implementiert
		// Das Problem ist folgendes, es gibt Schulen deren Timegrid namen
		// alle 0 sind - das ist bloed. Deswegen ueberpruefe ich hier ob die
		// stunde eh vor 8:00 uhr aufhoert - taddaaa
		// workaround done

		for (TimegridUnit timegridUnit : timegridForDay) {
			if (!dispZerolesson) {
				if (timegridUnit.getName().equals("0") && timegridUnit.getEnd().getHour() <= 8 && timegridUnit.getEnd().getMinute() == 0) {
					zerolesson = timegridUnit;
				} else {
					timegridForDateTimeDay.add(timegridUnit);
				}
			} else {
				timegridForDateTimeDay.add(timegridUnit);
			}
		}
		
		if (lessons.size() == 0) {
			for (TimegridUnit timegridUnit : timegridForDateTimeDay) {
				GUILessonContainer lessoncon = new GUILessonContainer();
				lessoncon.setTime(timegridUnit.getStart(), timegridUnit.getEnd());
				lessoncon.setDate(date);
				day.addLessonContainer(timegridUnit.getStart(), lessoncon);
			}
			return day;
		}
		
		
		webuntisOnlyZeroTimegridUnitsHack(timegridForDateTimeDay);


		if (zerolesson != null)
			Log.d("basti", "zero: " + zerolesson.getStart() + " " + zerolesson.getEnd() + " " + date.toString());

	

		for (int i = 0; i < timegridForDateTimeDay.size(); i++) {
			TimegridUnit timegridUnit = timegridForDateTimeDay.get(i);

			GUILessonContainer lessoncon = new GUILessonContainer();
			lessoncon.setTime(timegridUnit.getStart(), timegridUnit.getEnd());
			lessoncon.setDate(date);

			for (int j = 0; j < lessons.size(); j++) {
				Lesson lesson = lessons.get(j);

				// Normale Stunden
				if (lesson.getStartTime().getMinute() == timegridUnit.getStart().getMinute() && lesson.getStartTime().getHour() == timegridUnit.getStart().getHour()
						&& lesson.getEndTime().getMinute() == timegridUnit.getEnd().getMinute() && lesson.getEndTime().getHour() == timegridUnit.getEnd().getHour()) {

					if (lesson.getLessonCode() instanceof LessonCodeIrregular || lesson.getLessonCode() instanceof LessonCodeCancelled || lesson.getLessonCode() instanceof LessonCodeSubstitute) {
						lessoncon.addSpecialLesson(lesson);
					} else {
						lessoncon.addStandardLesson(lesson);
					}

				}
				// Ist die Anzeige der 0ten Stunden deaktiviert, wird sie hier
				// aus verbannt
				else if (!dispZerolesson && zerolesson != null && lessonsSameStartTime(lesson, zerolesson) && lessonSameEndTime(lesson, zerolesson)) {
					lessons.remove(j);
				}
				// Hier werden Ueberlange Stunden zerhackt
				else if (lesson.getStartTime().getMinute() == timegridUnit.getStart().getMinute() && lesson.getStartTime().getHour() == timegridUnit.getStart().getHour()
						&& (lesson.getEndTime().getMinute() != timegridUnit.getEnd().getMinute() || lesson.getEndTime().getHour() != timegridUnit.getEnd().getHour())) {

					lessons.remove(j);

					Lesson tmp1 = new Lesson();
					tmp1.setDate(lesson.getDate().getYear(), lesson.getDate().getMonth(), lesson.getDate().getDay());
					tmp1.setStartTime(lesson.getStartTime().getHour(), lesson.getStartTime().getMinute());
					tmp1.setEndTime(timegridUnit.getEnd().getHour(), timegridUnit.getEnd().getMinute());
					tmp1.setId(lesson.getId());
					tmp1.setLessonCode(lesson.getLessonCode());
					tmp1.setLessonType(lesson.getLessonType());
					tmp1.setSchoolClasses(lesson.getSchoolClasses());
					tmp1.setSchoolRooms(lesson.getSchoolRooms());
					tmp1.setSchoolSubjects(lesson.getSchoolSubjects());
					tmp1.setSchoolTeachers(lesson.getSchoolTeachers());

					if (i != timegridForDateTimeDay.size() - 1) {
						Lesson tmp2 = new Lesson();
						tmp2.setDate(lesson.getDate().getYear(), lesson.getDate().getMonth(), lesson.getDate().getDay());
						tmp2.setStartTime(timegridForDateTimeDay.get(i + 1).getStart().getHour(), timegridForDateTimeDay.get(i + 1).getStart().getMinute());
						tmp2.setEndTime(lesson.getEndTime().getHour(), lesson.getEndTime().getMinute());
						tmp2.setId(lesson.getId());
						tmp2.setLessonCode(lesson.getLessonCode());
						tmp2.setLessonType(lesson.getLessonType());
						tmp2.setSchoolClasses(lesson.getSchoolClasses());
						tmp2.setSchoolRooms(lesson.getSchoolRooms());
						tmp2.setSchoolSubjects(lesson.getSchoolSubjects());
						tmp2.setSchoolTeachers(lesson.getSchoolTeachers());

						lessons.add(tmp2);
					}
					lessons.add(tmp1);

				}
				// Stunden die frueher beginnen als die erste TimeGridUnit
				// werden hier auf das erste TimeGridUnit gesynct
				else if ((lesson.getStartTime().getHour() < timegridForDateTimeDay.get(0).getStart().getHour())) {

					lessons.remove(j);

					Lesson tmp1 = new Lesson();
					tmp1.setDate(lesson.getDate().getYear(), lesson.getDate().getMonth(), lesson.getDate().getDay());
					tmp1.setStartTime(timegridForDateTimeDay.get(0).getStart().getHour(), timegridForDateTimeDay.get(0).getStart().getMinute());
					tmp1.setEndTime(lesson.getEndTime().getHour(), lesson.getEndTime().getMinute());
					tmp1.setId(lesson.getId());
					tmp1.setLessonCode(lesson.getLessonCode());
					tmp1.setLessonType(lesson.getLessonType());
					tmp1.setSchoolClasses(lesson.getSchoolClasses());
					tmp1.setSchoolRooms(lesson.getSchoolRooms());
					tmp1.setSchoolSubjects(lesson.getSchoolSubjects());
					tmp1.setSchoolTeachers(lesson.getSchoolTeachers());
					lessons.add(tmp1);

				}
				// Hier werden Stunden behandelt, die nicht genau dem TimeGrid
				// entsprechen d.h. doof sind
				else if (lesson.getStartTime().getMinute() >= timegridUnit.getStart().getMinute() && lesson.getStartTime().getHour() >= timegridUnit.getStart().getHour()
						&& lesson.getEndTime().getMinute() <= timegridUnit.getEnd().getMinute() && lesson.getEndTime().getHour() <= timegridUnit.getEnd().getHour()) {
					if (lesson.getLessonCode() instanceof LessonCodeIrregular || lesson.getLessonCode() instanceof LessonCodeCancelled || lesson.getLessonCode() instanceof LessonCodeSubstitute) {
						lessoncon.addSpecialLesson(lesson);
					} else {
						lessoncon.addStandardLesson(lesson);
					}
				}

			}
			day.addLessonContainer(timegridUnit.getStart(), lessoncon);
		}
		return day;
	}

	private boolean lessonsSameStartTime(Lesson l1, TimegridUnit l2) {
		return ((l1.getStartTime().getHour() == l2.getStart().getHour()) && (l1.getStartTime().getMinute() == l2.getStart().getMinute()));
	}

	private boolean lessonSameEndTime(Lesson l1, TimegridUnit l2) {
		return ((l1.getEndTime().getHour() == l2.getEnd().getHour()) && (l1.getEndTime().getMinute() == l2.getEnd().getMinute()));
	}

	/*
	 * private boolean lessonsSameStartTime(Lesson l1, Lesson l2){ return
	 * ((l1.getStartTime().getHour() == l2.getStartTime().getHour())
	 * &&(l1.getStartTime().getMinute() == l2.getStartTime().getMinute())); }
	 * private boolean lessonSameEndTime(Lesson l1, Lesson l2){ return
	 * ((l1.getEndTime().getHour() == l2.getEndTime().getHour())
	 * &&(l1.getEndTime().getMinute() == l2.getEndTime().getMinute())); }
	 */
	/**
	 * Temporaer, bis von WebUntis bei Standardeinstellungen die
	 * Stundenbezeichnungen passen geliefert werden.
	 * 
	 * @param timegridForDay
	 */
	private void webuntisOnlyZeroTimegridUnitsHack(List<TimegridUnit> timegridForDay) {
		final String zeroString = "0";

		int zeroCount = 0;
		for (TimegridUnit timegridUnit : timegridForDay) {
			if (timegridUnit.getName().equals(zeroString)) {
				zeroCount++;
			}
		}

		if (zeroCount > 1) {
			Collections.sort(timegridForDay);
			for (int i = 0; i < timegridForDay.size(); i++) {
				// Standard is to start at lesson '1'
				timegridForDay.get(i).setName("" + (i + 1));
			}
		}
	}

}
