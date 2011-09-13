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

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
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
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.network.WebUntis;
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
	private ViewPagerIndicator indicator;
	private ViewType viewtype;

	public BlockingDownloadQueue downloadschlange = new BlockingDownloadQueue();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basti_weekview);

		

		myViewPager = (ViewPager) findViewById(R.id.week_pager);
		indicator = (ViewPagerIndicator) findViewById(R.id.week_indicator);

		wvpageadapter = new WeekViewPageAdapter();
		wvpageadapter.setContext(this);

		viewtype = (ViewType) getIntent().getExtras().getSerializable(BundleConstants.SELECTED_VIEW_TYPE);

		wvpageadapter.setDate(getMonday());

		myViewPager.setAdapter(wvpageadapter);

		myViewPager.setOnPageChangeListener(indicator);
		indicator.init(50, wvpageadapter.getCount(), wvpageadapter);

		Resources res = getResources();
		Drawable prev = res.getDrawable(R.drawable.indicator_prev_arrow);
		Drawable next = res.getDrawable(R.drawable.indicator_next_arrow);

		// Set images for previous and next arrows.
		indicator.setArrows(prev, next);
		myViewPager.setCurrentItem(50);


		loadweekdata = new LoadWeekData();
		loadweekdata.setContext(this);
		
		loadweekdata.execute();

	}

	private DateTime getMonday(){
		DateTime d = new DateTime();
		Calendar c = Calendar.getInstance();
		d.set(c.get(Calendar.DATE), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR));
		
		
		if(d.getWeekDay() == Time.SUNDAY){
			d.increaseDay();
			return d;
		}
			
		while(d.getWeekDay() != Time.MONDAY){
			d.decreaseDay();
		}
		
		return d;
	}
	@Override
	protected void onStop() {
		downloadschlange.interrupt();
		super.onStop();
	}
	
	public Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			wvpageadapter.setWeeData((GUIWeek) msg.obj, msg.arg1);
			super.handleMessage(msg);
		}
	};

	private class LoadWeekData extends AsyncTask<Void, String, Void> {

		private Context context;
		private int pos;

		@Override
		protected void onPreExecute() {
			Log.d("basti", "1. hole cache");
			publishProgress("Erzeuge Objekte", "true");
			cache = ((SchoolPlannerApp) getApplication()).getData();
			
			Log.d("basti", "2. gibt dem contentmanager suessigkeiten");
			contentmanager.setNeededData(context, cache);
			
			Log.d("basti", "3. gibt mehr dem contentmanager suessigkeiten");
			contentmanager.setViewType(viewtype);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			Random r = new Random();
			int i = r.nextInt();
			while (!downloadschlange.isInterrupted()) {
				DateTime d[] = null;
				try {
					d = downloadschlange.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(d.equals(BlockingDownloadQueue.INTERRUPT))
					return null;
				
				publishProgress("Lade Daten", "true");
				GUIWeek timeTable4GUI = contentmanager.getTimeTable4GUI(d[0]);
				pos = d[1].getYear();

				publishProgress("zaubere UI", "true");

				Message m = new Message();
				m.arg1 = pos;
				m.obj = timeTable4GUI;
				h.sendMessage(m);
				publishProgress("", "false");
			}
			return null;

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

	public class WeekViewPageAdapter extends PagerAdapter implements ViewPagerIndicator.PageInfoProvider {

		public final static int NUM_SCREENS = 100;

		private DateTime date;
		private Context context;
		private int oldpos = NUM_SCREENS / 2;
		private WeekLayout view_cach[] = new WeekLayout[NUM_SCREENS];

		public void setDate(DateTime dt) {
			date = dt;
		}

		public synchronized void setWeeData(GUIWeek data, int pos) {
			if (!view_cach[pos].isDataHere()) {
				view_cach[pos].setWeekData(data);
			}
		}

		public void setContext(Context c) {
			context = c;
			for (int i = 0; i < view_cach.length; i++) {
				view_cach[i] = new WeekLayout(context, i);
			}
		}

		@Override
		public void destroyItem(View collection, int arg1, Object view) {
			ScrollView s = (ScrollView) view;
			s.removeAllViews();
			((ViewPager) collection).removeView(s);
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public int getCount() {
			return NUM_SCREENS;
		}

		@Override
		public synchronized View instantiateItem(View collection, int position) {

			int di = position - 50;

			DateTime ad = new DateTime();
			ad.set(date.getDay() + (di * 7), date.getMonth(), date.getYear());

			DateTime dummy = new DateTime();
			dummy.set(1, 1, position);

			DateTime[] blub = { ad, dummy };

			if (!view_cach[position].isDataHere() && !downloadschlange.contains(blub)) {
				downloadschlange.add(blub);
			}

			ViewPager tmp = (ViewPager) collection;

			tmp.removeView((ScrollView) view_cach[position].getParent());

			oldpos = position;

			ScrollView scr = new ScrollView(context);
			scr.addView(view_cach[position]);
			tmp.addView(scr);

			return scr;

		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (View) object;
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
			return oldpos - 1;
		}

		@Override
		public String getTitle(int pos) {
			int di = pos - 50;
			DateTime ad = new DateTime();
			ad.set(date.getDay() + (di * 7), date.getMonth(), date.getYear());
			return ad.getDay() + "." + ad.getMonth() + "." + ad.getYear();
		}

	}

}