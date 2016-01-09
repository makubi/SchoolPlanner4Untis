/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
			Sebastian Chlan <sebastian@schoolplanner.at>
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package edu.htl3r.schoolplanner.gui.timetable.GUIData;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.preferences.Settings;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.gui.timetable.TransportClasses.LastRefreshTransferObject;

public class GUIContentManager{
	
	public static int WEEK = 55187;
	public static int DAY = 78155;
	
	private GUIContentProvider datacenter;
	private Context context;
	private ViewType viewtype;
	private int viewlength = WEEK;
	private Settings settings;
	
	public GUIContentManager(){}
	
	public void setNeededData(Context context, GUIContentProvider provider){
		this.context = context;
		datacenter = provider;
	}
	
	public void setViewType(ViewType vt){
		viewtype = vt;
	}
	
	public void setViewLength(int len){
		viewlength = len;
	}
	
	public GUIWeek getTimeTable4GUI(DateTime start, boolean forceNetWork){
		
		if(viewlength == WEEK){
			RenderInfoWeekTable weekinfo = new RenderInfoWeekTable();
			DateTime end = new DateTime();
			
			if(settings.isDisplaySaturday())
				end.set(start.getDay()+5, start.getMonth(), start.getYear());
			else
				end.set(start.getDay()+4, start.getMonth(), start.getYear());

			DataFromNetwork dataFromNetwork = null;
			
			if(forceNetWork || !settings.isCachingEnabled()){
				dataFromNetwork = datacenter.getLessonsForSomeTimeFromNetwork(viewtype, start, end);
			} else{
				dataFromNetwork = datacenter.getLessonsForSomeTime(viewtype, start, end);
			}
			
			LastRefreshTransferObject lastRefreshTransferObject = new LastRefreshTransferObject(dataFromNetwork.getLastRefresh());
			
			if(settings.isCachingEnabled() && settings.getCacheLifeTimeInHours() < lastRefreshTransferObject.getDiffernceInHours()){
				Log.d("basti", "Daten zu alt, lade neu "+ lastRefreshTransferObject.getDiffernceInHours());
				dataFromNetwork = datacenter.getLessonsForSomeTimeFromNetwork(viewtype, start, end);
				lastRefreshTransferObject = new LastRefreshTransferObject(dataFromNetwork.getLastRefresh());
			}
			
			
			Map<String, List<Lesson>> lessons = dataFromNetwork.getLessons();
			if(lessons.size() != 0 && lessons != null){
				weekinfo.setWeekData(lessons);
				weekinfo.setHolidays(datacenter.getAllSchoolHolidays());
				weekinfo.setTimeGrid(datacenter.getTimeGrid());
				weekinfo.setViewType(viewtype);
				weekinfo.setSettings(settings);
				weekinfo.setLastRefresh(lastRefreshTransferObject);
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

	public Context getContext() {
		return context;
	}
}
