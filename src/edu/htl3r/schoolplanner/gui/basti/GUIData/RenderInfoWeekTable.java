
package edu.htl3r.schoolplanner.gui.basti.GUIData;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.util.Log;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;

public class RenderInfoWeekTable {

	private Map<String,List<Lesson>> weekdata ;
	
	private boolean dispsat, dispdate, dispweekdaynames, dispzerolesson;
	private Timegrid timegrid;
	private List<SchoolHoliday> holidays;
	
	
	
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
	
	public void setHolidays(List<SchoolHoliday> hol){
		holidays = hol;
	}
	
	public void setTimeGrid(Timegrid t){
		timegrid = t;
	}
	
	public void analyse(){

		
		
		Set<String> keySet = weekdata.keySet();
		TreeSet<DateTime> datum = new TreeSet<DateTime>();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			datum.add(DateTimeUtils.iso8601StringToDateTime(string));
		}
		
		GUIWeek week = new GUIWeek();
		
		for (Iterator iterator = datum.iterator(); iterator.hasNext();) {
			DateTime dateTime = (DateTime) iterator.next();
			Log.d("basti", "day: " +analyseDay(dateTime, weekdata.get(DateTimeUtils.toISO8601Date(dateTime)))+"");
		}
	}
	
	List<TimegridUnit> timegridForDateTimeDay;
	
	private GUIDay analyseDay(DateTime date, List<Lesson> lessons){
		GUIDay day = new GUIDay();
		day.setDate(date);
		
		Log.d("basti",lessons.toString());
		
		if(timegridForDateTimeDay == null){
			timegridForDateTimeDay = timegrid.getTimegridForDateTimeDay(date.getDay());
		}
		
		
		for (TimegridUnit timegridUnit : timegridForDateTimeDay) {
			GUILessonContainer lessoncon = new GUILessonContainer();
			lessoncon.setTime(timegridUnit.getStart(), timegridUnit.getEnd());
			
			Log.d("basti", "Timgrid: "+ timegridUnit.getStart().toString());
			
			for(Lesson lesson : lessons){
				Log.d("basti", "Lesson: "+lesson.getStartTime().toString());
				
				if(lesson.getStartTime().getMinute() == timegridUnit.getStart().getMinute()
						&& lesson.getStartTime().getHour() == timegridUnit.getStart().getHour()
						&& lesson.getEndTime().getMinute() == timegridUnit.getEnd().getMinute()
						&& lesson.getEndTime().getHour() == timegridUnit.getEnd().getHour()){
					lessoncon.addLesson(lesson);
				}
			}
			day.addLessonContainer(timegridUnit.getStart(),lessoncon);
		}
		
		
		return day;
	}
	
	
}
