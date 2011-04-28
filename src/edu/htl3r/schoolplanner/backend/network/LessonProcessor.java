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

package edu.htl3r.schoolplanner.backend.network;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCode;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeCancelled;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class LessonProcessor {

	public Map<String, List<Lesson>> mergeLessons(
			Map<String, List<Lesson>> lessonMap) {
		Map<String, List<Lesson>> mergedLessonMap = lessonMap;
		for(String key : lessonMap.keySet()) {
			mergedLessonMap.put(key, mergeLessons(lessonMap.get(key)));
		}
				
		return mergedLessonMap;
	}

	public  List<Lesson> mergeLessons(List<Lesson> unmergedLessons) {
		List<Lesson> mergedLessons = unmergedLessons;
		
		for(int i = 0; i < mergedLessons.size()-1; i++) {
			Lesson primaryLesson = mergedLessons.get(i);
			for(int j = i+1; j < mergedLessons.size(); j++) {	
				Lesson secondaryLesson = mergedLessons.get(j);
				
				// Wenn zwei Stunden ueberschneiden
				if(primaryLesson.inLesson(secondaryLesson) || secondaryLesson.inLesson(primaryLesson)) {
					LessonCode pLessonCode = primaryLesson.getLessonCode();
					LessonCode sLessonCode = secondaryLesson.getLessonCode();
					
					// Wenn beide Stunden Entfallstunden sind
					if(pLessonCode instanceof LessonCodeCancelled && sLessonCode instanceof LessonCodeCancelled) {
						// Entferne die zweite Stunde, damit die erste Stunde als Entfallstunde angezeigt wird
						mergedLessons.remove(secondaryLesson);
						j = j >= 0 ? j-1 : j;
					}
					
					// Wenn die erste Stunde eine Entfallstunde ist
					else if(pLessonCode instanceof LessonCodeCancelled) {
						// Erzeuge Supplierungs-LessonCode und setze original Lehrer + Raum
						LessonCodeSubstitute newLessonCode = new LessonCodeSubstitute();
						
						// Setze supplierten Lehrer nur, wenn er nicht in der Liste der aktuellen Lehrer vorkommt
						for(SchoolTeacher st : primaryLesson.getSchoolTeachers()) {
							if (!secondaryLesson.getSchoolTeachers().contains(st)) {
								newLessonCode.setOriginSchoolTeacher(st);
							}
						}
						
						// Setze supplierten Raum nur, wenn er nicht in der Liste der aktuellen Raeume vorkommt
						for(SchoolRoom sr : primaryLesson.getSchoolRooms()) {
							if (!secondaryLesson.getSchoolRooms().contains(sr)) {
								newLessonCode.setOriginSchoolRoom(sr);
							}
						}
						
						// Setze den LessonCode der verwendeten Stunde auf Supplierung
						secondaryLesson.setLessonCode(newLessonCode);
						
						// Entferne nicht-benoetigte Stunde
						mergedLessons.remove(primaryLesson);
						i = i >= 0 ? i-1 : i;
					}
					
					// Wenn die zweite Stunde eine Entfallstunde ist
					else if (sLessonCode instanceof LessonCodeCancelled) {
						// Erzeuge Supplierungs-LessonCode und setze original Lehrer + Raum
						LessonCodeSubstitute newLessonCode = new LessonCodeSubstitute();
						
						// Setze supplierten Lehrer nur, wenn er nicht in der Liste der aktuellen Lehrer vorkommt
						for(SchoolTeacher st : secondaryLesson.getSchoolTeachers()) {
							if (!primaryLesson.getSchoolTeachers().contains(st)) {
								newLessonCode.setOriginSchoolTeacher(st);
							}
						}
						
						// Setze supplierten Raum nur, wenn er nicht in der Liste der aktuellen Raeume vorkommt
						for(SchoolRoom sr : secondaryLesson.getSchoolRooms()) {
							if (!primaryLesson.getSchoolRooms().contains(sr)) {
								newLessonCode.setOriginSchoolRoom(sr);
							}
						}
						
						// Setze den LessonCode der verwendeten Stunde auf Supplierung
						primaryLesson.setLessonCode(newLessonCode);
						
						// Entferne nicht-benoetigte Stunde
						mergedLessons.remove(secondaryLesson);
						j = j >= 0 ? j-1 : j;
					}
					
					// Andere Stundenkombinationen
					else {
						// Fuege Listen der Stunden (Klassen, Lehrer, Faecher, Raeume) zusammen
						primaryLesson.appendLists(secondaryLesson.getSchoolClasses(), secondaryLesson.getSchoolTeachers(), secondaryLesson.getSchoolSubjects(), secondaryLesson.getSchoolRooms());
						
						// Setze LessonCode, falls dieser nicht leer ist
						if (sLessonCode != null) {
							primaryLesson.setLessonCode(sLessonCode);
						}
						
						// Setze LessonTyp, falls dieser nicht leer ist
						if(secondaryLesson.getLessonType() != null) {
							primaryLesson.setLessonType(secondaryLesson.getLessonType());
						}
						
						// Entfernen nicht mehr benoetigte Stunde
						mergedLessons.remove(secondaryLesson);
						j = j >= 0 ? j-1 : j;
					}
				}
			}
		}
		return mergedLessons;
	}

	/**
	 * Fuegt fuer jeden Tag, der zwischen Start- und Enddatum liegen und an dem keine Stunden stattfinden, eine leere ArrayList hinzu.
	 * @param lessonMap Map, zu der die leeren ArrayListen hinzugefuegt werden sollen
	 * @param startDate Startdatum
	 * @param endDate Enddatum
	 * @return Map mit leeren Listen an den Tagen, an denen kein Unterricht stattfindet
	 */
	public Map<String, List<Lesson>> addEmptyDaysToLessonMap(Map<String, List<Lesson>> lessonMap, Calendar startDate, Calendar endDate) {
		Map<String, List<Lesson>> updatedLessonMap = lessonMap;
		Calendar tmpStartCalendar = (Calendar) startDate.clone();
		Calendar tmpEndCalendar = (Calendar) endDate.clone();
		tmpEndCalendar.add(Calendar.DATE, 1);
		
		while(tmpStartCalendar.before(tmpEndCalendar)) {			
			if(!updatedLessonMap.containsKey(CalendarUtils.getCalendarAs8601String(tmpStartCalendar))) {
				updatedLessonMap.put(CalendarUtils.getCalendarAs8601String(tmpStartCalendar), new ArrayList<Lesson>());
			}
			tmpStartCalendar.add(Calendar.DATE, 1);
		}
		return updatedLessonMap;
	}

}
