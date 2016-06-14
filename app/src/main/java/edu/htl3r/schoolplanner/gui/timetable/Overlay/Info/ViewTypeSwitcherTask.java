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
package edu.htl3r.schoolplanner.gui.timetable.Overlay.Info;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.widget.ScrollView;
import android.widget.Toast;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.timetable.LoadDataTask;
import edu.htl3r.schoolplanner.gui.timetable.WeekView;
import edu.htl3r.schoolplanner.gui.timetable.WeekViewPageAdapter;

public class ViewTypeSwitcherTask extends AsyncTask<Void, Void, Void>{

	private WeekView weekView;
	
	private ViewPager myViewPager;
	private WeekViewPageAdapter wvpageadapter;
	private LoadDataTask loadweekdata;
	private ViewType vt;
	
	public ViewTypeSwitcherTask(WeekView weekView, ViewPager myViewPager, WeekViewPageAdapter wvpageadapter, LoadDataTask loadweekdata, ViewType vt, boolean forceNetwork) {
		this.weekView = weekView;
		this.myViewPager = myViewPager;
		this.wvpageadapter = wvpageadapter;
		this.loadweekdata = loadweekdata;
		this.vt = vt;
		
		if(forceNetwork)
			loadweekdata.forceNetwork();
	}

	@Override
	protected Void doInBackground(Void... params) {
		loadweekdata.changeViewType(vt);
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		wvpageadapter.reset(weekView,weekView.getSettings());

		for(int i=0; i<myViewPager.getChildCount(); i++){
			ScrollView scr = (ScrollView)myViewPager.getChildAt(i);
			scr.removeAllViews();
		}
		myViewPager.removeAllViews();
		wvpageadapter.notifyDataSetChanged();
		
		Toast.makeText(weekView, vt.getName(), Toast.LENGTH_SHORT).show();
	}
	
}
