package edu.htl3r.schoolplanner.gui.basti.GUIData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;

public class GUIWeek implements DataGUItoGraphicGUI{
	
	
	private Map<DateTime, GUIDay> week = new HashMap<DateTime, GUIDay>();
	private ViewType viewtype;
	
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
	
	public ArrayList<DateTime> getSortDates(){
		Set<DateTime> keySet = week.keySet();
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
	
	public int getMaxHours(){
		Collection<GUIDay> values = week.values();
		int max = 0;
		for (Iterator iterator = values.iterator(); iterator.hasNext();) {
			GUIDay guiDay = (GUIDay) iterator.next();
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
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			DateTime dateTime = (DateTime) iterator.next();
			sb.append(dateTime + ": " + week.get(dateTime)+"\n");
		}
		return sb.toString();
	}
}
