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

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.BundleConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIContentManager;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIWeek;
import edu.htl3r.schoolplanner.gui.basti.Week.WeekLayout;

public class ViewBasti extends SchoolPlannerActivity {

	private Cache cache;
	private GUIContentManager contentmanager = new GUIContentManager();
	private ViewPager myViewPager;
	private WeekViewPageAdapter wvpageadapter;
	private LoadWeekData loadweekdata;
	
	private boolean isTaskUpdateing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basti_weekview);

		myViewPager = (ViewPager) findViewById(R.id.awesomepager);

		wvpageadapter = new WeekViewPageAdapter();
		wvpageadapter.setContext(this);
		

		DateTime d = new DateTime();
		d.set(14, 2, 2011);

		wvpageadapter.setDate(d);
		
		myViewPager.setAdapter(wvpageadapter);
		myViewPager.setCurrentItem(50);

	}

	private class LoadWeekData extends AsyncTask<DateTime, String, GUIWeek> {

		private Context context;

		@Override
		protected void onPreExecute() {
			publishProgress("Erzeuge Objekte", "true");

			cache = ((SchoolPlannerApp) getApplication()).getData();
			contentmanager.setNeededData(context, cache);
			contentmanager.setViewType((ViewType) getIntent().getExtras().getSerializable(BundleConstants.SELECTED_VIEW_TYPE));
			isTaskUpdateing = true;
			super.onPreExecute();
		}

		@Override
		protected GUIWeek doInBackground(DateTime... date) {
			
			DateTime d = date[0];
			publishProgress("Lade Daten", "true");
			GUIWeek timeTable4GUI = contentmanager.getTimeTable4GUI(d);
	
			return timeTable4GUI;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			setInProgress(values[0], Boolean.parseBoolean(values[1]));
		}

		@Override
		protected void onPostExecute(GUIWeek result) {
			publishProgress("zaubere UI", "true");
			wvpageadapter.setWeeData(result);
			wvpageadapter.notifyDataSetChanged();
			publishProgress("", "false");
			isTaskUpdateing = false;
			super.onPostExecute(result);
		}

		public void setContext(Context context) {
			this.context = context;
		}

	}

	public class WeekViewPageAdapter extends PagerAdapter {

		DateTime date;
		Context context;
		WeekLayout wl;
		int oldpos = 51;

		GUIWeek data = null;

		public void setDate(DateTime dt) {
			date = dt;
		}

		public synchronized void setWeeData(GUIWeek data) {
			wl.setWeekData(data);
			wl.invalidate();
		}

		public void setContext(Context c) {
			context = c;
		}

		@Override
		public void destroyItem(View collection, int arg1, Object view) {
			((ViewPager) collection).removeView((WeekLayout) view);
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public int getCount() {
			return 100;
		}

		@Override
		public synchronized WeekLayout instantiateItem(View collection, int position) {
			
			if(oldpos < position){
				date.set(date.getDay()+7, date.getMonth(), date.getYear());
			}
			if(oldpos > position){
				date.set(date.getDay()-7, date.getMonth(), date.getYear());
			}
			oldpos = position;
			
			if(!isTaskUpdateing){
				loadweekdata = new LoadWeekData();
				loadweekdata.setContext(context);
				loadweekdata.execute(date);
			}
			
			wl = new WeekLayout(context);
			((ViewPager)collection).addView(wl);
			
		
			return wl;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (WeekLayout)object;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
		
		public int getItemPosition(Object object) {
		    return oldpos;
		}

	}

}