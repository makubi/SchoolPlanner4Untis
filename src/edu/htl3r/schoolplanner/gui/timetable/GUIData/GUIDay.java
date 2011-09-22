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
