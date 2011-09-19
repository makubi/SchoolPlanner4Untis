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

package edu.htl3r.schoolplanner.gui.timetable.GUIData;

import java.util.ArrayList;
import java.util.List;

import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeCancelled;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeIrregular;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;

public class GUILessonContainer {

	private DateTime start, end, date;

	private List<Lesson> standardLessons = new ArrayList<Lesson>();
	private List<Lesson> specialLessons = new ArrayList<Lesson>();


	public GUILessonContainer() {
	}

	public void setTime(DateTime start, DateTime end) {
		this.start = start;
		this.end = end;
	}

	public void addStandardLesson(Lesson l) {
		standardLessons.add(l);
	}

	public void addSpecialLesson(Lesson l) {
		specialLessons.add(l);
	}

	@Override
	public String toString() {
		return standardLessons.toString();
	}

	public DateTime getStart() {
		return start;
	}

	public DateTime getEnd() {
		return end;
	}

	public List<Lesson> getStandardLessons() {
		return standardLessons;
	}

	public List<Lesson> getSpecialLessons() {
		return specialLessons;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	/**
	 * Sind Lessons fuer diese Einheit vorhanden
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return (standardLessons.isEmpty() && specialLessons.isEmpty()) ? true : false;
	}

	/**
	 * Gibt es irregulaere Stunden
	 * 
	 * @return
	 */
	public boolean isSomethinStrange() {
		return (specialLessons.isEmpty()) ? false : true;
	}

	public ArrayList<Lesson> getAllLessons() {
		ArrayList<Lesson> ret = new ArrayList<Lesson>();
		for (Lesson l : standardLessons) {
			ret.add(l);
		}
		for (Lesson l : specialLessons) {
			ret.add(l);
		}
		return ret;
	}
	
	public boolean allCancelled(){
		if(isEmpty())
			return false;
		if(!isSomethinStrange())
			return false;
		
		
		for(Lesson l : specialLessons){
			if(!(l.getLessonCode() instanceof LessonCodeCancelled)){
				return false;
			}
		}
		return true;
	}
	
	public List<Lesson> getIrregularLessons(){
		List<Lesson> ret = new ArrayList<Lesson>();
		for(Lesson l : specialLessons){
			if((l.getLessonCode() instanceof LessonCodeIrregular)){
				ret.add(l);
			}
		}
		return ret;
	}

	public boolean containsSubsituteLesson(){
		if(isEmpty())
			return false;
		if(!isSomethinStrange())
			return false;
		
		
		for(Lesson l : specialLessons){
			if(l.getLessonCode() instanceof LessonCodeSubstitute){
				return true;
			}
		}
		return false;
	}
}
