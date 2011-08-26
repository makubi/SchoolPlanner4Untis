package edu.htl3r.schoolplanner.gui.basti.GUIData;

import java.util.HashMap;
import java.util.Map;

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
	
	@Override
	public String toString() {
		return lessons + "";
	}

}
