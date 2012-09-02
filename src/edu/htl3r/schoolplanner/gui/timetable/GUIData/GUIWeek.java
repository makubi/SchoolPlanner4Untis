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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;
import edu.htl3r.schoolplanner.gui.timetable.TransportClasses.LastRefreshTransferObject;

public class GUIWeek{
	
	
	private Map<DateTime, GUIDay> week = new HashMap<DateTime, GUIDay>();
	private ViewType viewtype;
	private List<TimegridUnit> timegrid;
	private List<SchoolHoliday> holidays;
	private LastRefreshTransferObject lastRefresh;

	
	public void setTimegrid(List <TimegridUnit> time){
		timegrid = time;
	}
	
	public void setGUIDay(DateTime day, GUIDay g){
		week.put(day, g);
	}
	
	public void setViewType(ViewType vt){
		viewtype = vt;
	}
	
	public void setLastRefreshDate(LastRefreshTransferObject lastRefresh2) {
		this.lastRefresh = lastRefresh2;
	}
	
	public ViewType getViewType(){
		return viewtype;
	}
	
	public int getCountDays(){
		return week.keySet().size();
	}
	
	public List<TimegridUnit> getTimeGrid(){
		return timegrid;
	}
	

	public LastRefreshTransferObject getLastRefresh() {
		return lastRefresh;
	}	
	
	public List<SchoolHoliday> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<SchoolHoliday> holidays) {
		this.holidays = holidays;
	}

	public ArrayList<DateTime> getSortDates(){
		Set<DateTime> keySet = week.keySet();
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
	
	public int getMaxHours(){
		Collection<GUIDay> values = week.values();
		int max = 0;
		for (Object element : values) {
			GUIDay guiDay = (GUIDay) element;
			if(guiDay.getLessonCount() > max){
				max = guiDay.getLessonCount();
			}
		}
		return max;
	}
	
	public GUIDay getDay(DateTime d){
		return week.get(d);
	}
	
	@Override
	public String toString() {
		Set<DateTime> keySet = week.keySet();
		StringBuilder sb = new StringBuilder();
		for (Object element : keySet) {
			DateTime dateTime = (DateTime) element;
			sb.append(dateTime + ": " + week.get(dateTime)+"\n");
		}
		return sb.toString();
	}


}
