package edu.htl3r.schoolplanner.gui.basti.GUIData;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.util.Log;
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
		TreeSet<DateTime> datum = new TreeSet<DateTime>();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			datum.add(DateTimeUtils.iso8601StringToDateTime(string));
		}
		
		for (Iterator iterator = datum.iterator(); iterator.hasNext();) {
			DateTime dateTime = (DateTime) iterator.next();
			analyseDay(dateTime, weekdata.get(DateTimeUtils.toISO8601Date(dateTime)));
		}
		
		
	}
	
	private void analyseDay(DateTime date, List<Lesson> lessons){
		Log.d("basti",DateTimeUtils.toISO8601Date(date) + ": " + lessons+"");
	}
	
	
}
