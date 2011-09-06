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

import java.util.concurrent.LinkedBlockingQueue;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.BundleConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import edu.htl3r.schoolplanner.gui.basti.ViewPagerIndicator.PageInfoProvider;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIContentManager;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIWeek;
import edu.htl3r.schoolplanner.gui.basti.Week.WeekLayout;

public class ViewBasti extends SchoolPlannerActivity {

	private Cache cache;
	private GUIContentManager contentmanager = new GUIContentManager();
	private ViewPager myViewPager;
	private WeekViewPageAdapter wvpageadapter;
	private LoadWeekData loadweekdata;
	private RelativeLayout week_container;
	private ViewPagerIndicator indicator;

	
	public LinkedBlockingQueue<DateTime[]> downloadschlange = new LinkedBlockingQueue<DateTime[]>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basti_weekview);

		myViewPager = (ViewPager) findViewById(R.id.week_pager);
		week_container = (RelativeLayout) findViewById(R.id.week_container);
		indicator = (ViewPagerIndicator) findViewById(R.id.week_indicator);
		
		wvpageadapter = new WeekViewPageAdapter();
		wvpageadapter.setContext(this);
		
	

		DateTime d = new DateTime();
		d.set(12, 9, 2011);

		wvpageadapter.setDate(d);
		
		myViewPager.setAdapter(wvpageadapter);
		myViewPager.setCurrentItem(50);
		
		myViewPager.setOnPageChangeListener(indicator);
		indicator.init(100, wvpageadapter.getCount(), wvpageadapter);
		
		Resources res = getResources();
		Drawable prev = res.getDrawable(R.drawable.indicator_prev_arrow);
		Drawable next = res.getDrawable(R.drawable.indicator_next_arrow);
		
		// Set images for previous and next arrows.
		indicator.setArrows(prev, next);
		
		loadweekdata = new LoadWeekData();
		loadweekdata.setContext(this);
		loadweekdata.execute();

	}
	
	public Handler h = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			wvpageadapter.setWeeData((GUIWeek) msg.obj,msg.arg1);
			super.handleMessage(msg);
		}
	};

	private class LoadWeekData extends AsyncTask<Void, String, Void> {

		private Context context;
		private int pos;

		@Override
		protected void onPreExecute() {
			publishProgress("Erzeuge Objekte", "true");
			cache = ((SchoolPlannerApp) getApplication()).getData();
			contentmanager.setNeededData(context, cache);
			contentmanager.setViewType((ViewType) getIntent().getExtras().getSerializable(BundleConstants.SELECTED_VIEW_TYPE));
			week_container.setClickable(false);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			while(true){
				DateTime d[] = null;
				try {
					d = downloadschlange.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				publishProgress("Lade Daten", "true");
				GUIWeek timeTable4GUI = contentmanager.getTimeTable4GUI(d[0]);
				pos = d[1].getYear();
				
				Log.d("basti", "Thread " + pos);
				
				publishProgress("zaubere UI", "true");
				
				Message m = new Message();
				m.arg1 = pos;
				m.obj = timeTable4GUI;
				h.sendMessage(m);
				publishProgress("", "false");
			}
			
			
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			setInProgress(values[0], Boolean.parseBoolean(values[1]));
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}

		public void setContext(Context context) {
			this.context = context;
		}

	}

	public class WeekViewPageAdapter extends PagerAdapter implements ViewPagerIndicator.PageInfoProvider{

		DateTime date;
		Context context;
		WeekLayout wl;
		int oldpos = 50;

		private WeekLayout view_cach []= new WeekLayout[100];

		
		//GUIWeek data = null;

		public void setDate(DateTime dt) {
			date = dt;
		}

		public synchronized void setWeeData(GUIWeek data, int pos) {
			view_cach[pos].setWeekData(data);
			Log.d("basti", "setWeekData: " + pos);
		}
		


		public void setContext(Context c) {
			context = c;
			for (int i = 0; i < view_cach.length; i++) {
				view_cach[i] = new WeekLayout(context, i);
			}
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
			
			int di = position - 50;
			
			DateTime ad = new DateTime();
			ad.set(date.getDay() + (di*7), date.getMonth(), date.getYear());
			
			DateTime dummy = new DateTime();
			dummy.set(1, 1, position);
		
			DateTime[] blub = {ad,dummy};
			
			if(!view_cach[position].isDataHere && !downloadschlange.contains(blub))
				downloadschlange.add(blub);

	
			
			Log.d("basti", "init: " + position +" ");
			
			ViewPager tmp = (ViewPager)collection;
			
			
			boolean vorhanden = false;
			
			for(int i=0; i<tmp.getChildCount(); i++){
				if(((WeekLayout)tmp.getChildAt(i)).equals(view_cach[position])){
					vorhanden = true;
					break;
				}
			}
			
			if(!vorhanden)
				tmp.addView(view_cach[position]);
		
			
			oldpos = position;

			return view_cach[position];
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
		    return oldpos-1;
		}

		@Override
		public String getTitle(int pos) {
			int di = pos - 50;
			DateTime ad = new DateTime();
			ad.set(date.getDay() + (di*7), date.getMonth(), date.getYear());
			return ad.getDay() + "." + ad.getMonth() + "." + ad.getYear();
		}

	}

}