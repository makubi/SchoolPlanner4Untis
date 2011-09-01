package edu.htl3r.schoolplanner.gui.basti.GUIData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import edu.htl3r.schoolplanner.DateTime;

public class GUIDay {
	
	
	private DateTime date;
	private Map<DateTime,GUILessonContainer> lessons = new HashMap<DateTime,GUILessonContainer>();
	
	public GUIDay(){}
	
	
	public void setDate(DateTime date){
		this.date = date;
	}
	
	
	public void addLessonContainer(DateTime start, GUILessonContainer con){
		lessons.put(start, con);
	}
	
	public ArrayList<DateTime> getSortDates(){
		Set<DateTime> keySet = lessons.keySet();
		TreeSet<DateTime> dates = new TreeSet<DateTime>();
		ArrayList<DateTime> ret = new ArrayList<DateTime>();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			DateTime dateTime = (DateTime) iterator.next();
			dates.add(dateTime);
		}
		
		for (Iterator iterator = dates.iterator(); iterator.hasNext();) {
			DateTime dateTime = (DateTime) iterator.next();
			ret.add(dateTime);
		}
		return ret;
	}
	
	@Override
	public String toString() {
		Set<DateTime> keySet = lessons.keySet();
		StringBuilder sb = new StringBuilder();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			DateTime dateTime = (DateTime) iterator.next();
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

}
