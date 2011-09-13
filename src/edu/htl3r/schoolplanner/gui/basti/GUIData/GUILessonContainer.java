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

package edu.htl3r.schoolplanner.gui.basti.GUIData;

import java.util.ArrayList;
import java.util.List;

import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;

public class GUILessonContainer {
	
	
	private DateTime start, end,date;

	private List<Lesson> standardLessons = new ArrayList<Lesson>();
	private List<Lesson> extraLongLessons = new ArrayList<Lesson>();
	private List<Lesson> specialLessons = new ArrayList<Lesson>();
	private List<Lesson> extraLongSpecialLessons = new ArrayList<Lesson>();

	

	public GUILessonContainer(){}
	
	public void setTime(DateTime start, DateTime end){
		this.start = start;
		this.end = end;
	}
	
//	public int getStandardLessonsCount(){
//		return standardLessons.size();
//	}
	
	public void addStandardLesson(Lesson l){
		standardLessons.add(l);
	}
	
	public void addExtraLongLesson(Lesson l){
		extraLongLessons.add(l);
	}
	
	public void addSpecialLesson(Lesson l){
		specialLessons.add(l);
	}
	
	public void addExtraLongSpecialLesson(Lesson l){
		extraLongSpecialLessons.add(l);
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
	
	public List<Lesson> getExtraLongLessons() {
		return extraLongLessons;
	}

	public List<Lesson> getSpecialLessons() {
		return specialLessons;
	}

	public List<Lesson> getExtraLongSpecialLessons() {
		return extraLongSpecialLessons;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public boolean isEmpty(){
		return (standardLessons.isEmpty() && specialLessons.isEmpty() && extraLongLessons.isEmpty() && extraLongSpecialLessons.isEmpty())? true:false;
	}
		
}
