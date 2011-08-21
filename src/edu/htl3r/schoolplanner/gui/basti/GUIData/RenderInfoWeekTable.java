package edu.htl3r.schoolplanner.gui.basti.GUIData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;

public class RenderInfoWeekTable {

	private Map<String,List<Lesson>> weekdata ;
	
	private boolean dispsat, dispdate, dispweekdaynames, dispzerolesson;
	
	
	
	public RenderInfoWeekTable(){
		
		//TODO Lese das aus den Preffernces aus!!!!
		dispsat = true;
		dispdate = true;
		dispweekdaynames = true;
		dispzerolesson = true;
	}
	
	
	public void setWeekData(Map<String,List<Lesson>> w){
		weekdata = w;
	}
	
	public void analyse(){
		Set<String> keySet = weekdata.keySet();
		ArrayList<DateTime> datum = new ArrayList<DateTime>();
		
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			datum.add(DateTimeUtils.iso8601StringToDateTime(string));
		}
		
	}
	
	
}
