package edu.htl3r.schoolplanner.gui.basti.GUIData;

import java.util.List;
import java.util.Map;

import android.content.Context;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;

public class GUIContentManager {
	
	public static int WEEK = 1;
	public static int DAY = 2;
	
	private GUIContentProviderSpez datacenter;
	private Context context;
	private ViewType viewtype;
	private int viewlength = WEEK;
	
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
		if(viewtype == null || datacenter == null || context == null)
			return null;	//TODO Nice Error Message
		
		if(viewlength == WEEK){
			RenderInfoWeekTable weekinfo = new RenderInfoWeekTable();
			DateTime end = new DateTime();
			end.set(17, 9, 2010);
			Map<String, List<Lesson>> lessonsForSomeTime = datacenter.getLessonsForSomeTime(viewtype, start, end);
			if(lessonsForSomeTime.size() != 0 && lessonsForSomeTime != null){
				weekinfo.setWeekData(lessonsForSomeTime);
				weekinfo.setTimeGrid(datacenter.getTimeGrid());
				weekinfo.setViewType(viewtype);
				return weekinfo.analyse();
			}else{
				return null;
			}
		}
		
		return null;
	}
	
}
