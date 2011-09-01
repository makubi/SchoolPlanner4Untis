/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
			Sebastian Chlan <sebastian@schoolplanner.at>
			Christian Pascher <christian@schoolplanner.at>
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
package edu.htl3r.schoolplanner.gui.basti;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.BundleConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIContentManager;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIWeek;
import edu.htl3r.schoolplanner.gui.basti.Week.WeekView;

public class ViewBasti extends SchoolPlannerActivity {
	
	private Cache cache;
	private GUIContentManager contentmanager = new GUIContentManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    

		
		DateTime date = new DateTime();
		date.set(16, 5, 2011);

		
		cache = ((SchoolPlannerApp)getApplication()).getData();
		contentmanager.setNeededData(this, cache);
		contentmanager.setViewType((ViewType)getIntent().getExtras().getSerializable(BundleConstants.SELECTED_VIEW_TYPE));
		
		GUIWeek week = contentmanager.getTimeTable4GUI(date);
		WeekView weekview = new WeekView(this);
		Log.d("basti",week.toString());
		weekview.setWeekData(week);
		setContentView(R.layout.basti_weekview);
		
		
		RelativeLayout r = (RelativeLayout)findViewById(R.id.week_rel);
		r.addView(weekview);
	}
}