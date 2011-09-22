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

public class GUIWeek implements DataGUItoGraphicGUI{
	
	
	private Map<DateTime, GUIDay> week = new HashMap<DateTime, GUIDay>();
	private ViewType viewtype;
	private List<TimegridUnit> timegrid;
	private List<SchoolHoliday> holidays;

	
	public void setTimegrid(List <TimegridUnit> time){
		timegrid = time;
	}
	
	public void setGUIDay(DateTime day, GUIDay g){
		week.put(day, g);
	}
	
	public void setViewType(ViewType vt){
		viewtype = vt;
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
