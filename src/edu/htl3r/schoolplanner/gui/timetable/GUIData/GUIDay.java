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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import edu.htl3r.schoolplanner.DateTime;

public class GUIDay {
	
	
	private Map<DateTime,GUILessonContainer> lessons = new HashMap<DateTime,GUILessonContainer>();
	private DateTime date;
	
	public GUIDay(){}
	
	public void setDate(DateTime d){
		date = d;
	}
	

	public void addLessonContainer(DateTime start, GUILessonContainer con){
		lessons.put(start, con);
	}
	
	public ArrayList<DateTime> getSortDates(){
		Set<DateTime> keySet = lessons.keySet();
		TreeSet<DateTime> dates = new TreeSet<DateTime>();
		ArrayList<DateTime> ret = new ArrayList<DateTime>();
		for (Object element : keySet) {
			DateTime dateTime = (DateTime) element;
			dates.add(dateTime);
		}
		
		for (Object element : dates) {
			DateTime dateTime = (DateTime) element;
			ret.add(dateTime);
		}
		return ret;
	}
	
	@Override
	public String toString() {
		Set<DateTime> keySet = lessons.keySet();
		StringBuilder sb = new StringBuilder();
		for (Object element : keySet) {
			DateTime dateTime = (DateTime) element;
			sb.append("    " + dateTime.toString() + ": " + lessons.get(dateTime)+"\n");
		}
		return sb.toString();
	}
	
	public int getLessonCount(){
		return lessons.size();
	}
	
	public GUILessonContainer getLessonsContainer(DateTime d){
		return lessons.get(d);
	}

	public DateTime getDate() {
		return date;
	}
	
	
}
