package edu.htl3r.schoolplanner.gui.basti.GUIData;

import java.util.HashMap;
import java.util.Map;

import edu.htl3r.schoolplanner.DateTime;

public class GUIWeek implements GUIDataStatics{
	
	
	private Map<DateTime, GUIDay> week = new HashMap<DateTime, GUIDay>();
	
	public void setGUIDay(DateTime day, GUIDay g){
		week.put(day, g);
	}
	
}
