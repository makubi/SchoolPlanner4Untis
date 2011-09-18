package edu.htl3r.schoolplanner.gui.timetable.GUIData;

import java.util.List;
import java.util.Map;

import android.content.Context;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.preferences.Settings;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;

public class GUIContentManager {
	
	public static int WEEK = 55187;
	public static int DAY = 78155;
	
	private GUIContentProviderSpez datacenter;
	private Context context;
	private ViewType viewtype;
	private int viewlength = WEEK;
	private Settings settings;
	
	public GUIContentManager(){}
	
	public void setNeededData(Context context, Cache cache){
		this.context = context;
		datacenter = new GUIContentProvider(cache, context);
	}
	
	public void setViewType(ViewType vt){
		viewtype = vt;
	}
	
	public void setViewLength(int len){
		viewlength = len;
	}
	
	public GUIWeek getTimeTable4GUI(DateTime start){
		
		if(viewlength == WEEK){
			RenderInfoWeekTable weekinfo = new RenderInfoWeekTable();
			DateTime end = new DateTime();
			
			if(settings.isDisplaySaturday())
				end.set(start.getDay()+5, start.getMonth(), start.getYear());
			else
				end.set(start.getDay()+4, start.getMonth(), start.getYear());

			
			Map<String, List<Lesson>> lessonsForSomeTime = datacenter.getLessonsForSomeTime(viewtype, start, end);
			if(lessonsForSomeTime.size() != 0 && lessonsForSomeTime != null){
				weekinfo.setWeekData(lessonsForSomeTime);
				weekinfo.setTimeGrid(datacenter.getTimeGrid());
				weekinfo.setViewType(viewtype);
				weekinfo.setSettings(settings);
				return weekinfo.analyse();
			}else{
				return null;
			}
		}
		
		return null;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
}
