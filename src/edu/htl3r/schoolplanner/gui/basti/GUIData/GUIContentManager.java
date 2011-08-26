package edu.htl3r.schoolplanner.gui.basti.GUIData;

import android.content.Context;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;

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
			end.set(start.getDay()+4, 9, 2011);
			weekinfo.setWeekData(datacenter.getLessonsForSomeTime(viewtype, start, end));
			weekinfo.setTimeGrid(datacenter.getTimeGrid());
			weekinfo.analyse();
		}
		
		return null;
	}
	
}
