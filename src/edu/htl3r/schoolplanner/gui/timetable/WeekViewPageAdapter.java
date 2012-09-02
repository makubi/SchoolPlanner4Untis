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
package edu.htl3r.schoolplanner.gui.timetable;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ScrollView;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.preferences.Settings;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUIWeek;
import edu.htl3r.schoolplanner.gui.timetable.TransportClasses.InputTransferObject;
import edu.htl3r.schoolplanner.gui.timetable.TransportClasses.LastRefreshTransferObject;
import edu.htl3r.schoolplanner.gui.timetable.Week.WeekLayout;

public class WeekViewPageAdapter extends PagerAdapter implements ViewPagerIndicator.PageInfoProvider {

	public final static int NUM_SCREENS = 100;
	

	private DateTime date;
	private Context context;
	private int oldpos = NUM_SCREENS / 2;
	private WeekLayout view_cach[] = new WeekLayout[NUM_SCREENS];
	private BlockingDownloadQueue downloadschlange = new BlockingDownloadQueue();
	
	public void setDate(DateTime dt) {
		date = dt;
	}
	
	public synchronized void setWeeData(GUIWeek data, int pos) {
		if (!view_cach[pos].isDataHere() && data != null) {
			view_cach[pos].setWeekData(data);
		}
	}

	public void setContext(Context c, BlockingDownloadQueue bd, WeekView wv, Settings settings) {
		context = c;
		downloadschlange = bd;
		for (int i = 0; i < view_cach.length; i++) {
			view_cach[i] = new WeekLayout(context, i, wv, settings);
		}
	}

	@Override
	public void destroyItem(View collection, int arg1, Object view) {
		ScrollView s = (ScrollView) view;
		s.removeAllViews();
		((ViewPager) collection).removeView(s);
	}

	@Override
	public void finishUpdate(View arg0) {}

	@Override
	public int getCount() {
		return NUM_SCREENS;
	}

	@Override
	public synchronized View instantiateItem(View collection, int position) {

		int di = position - 50;

		DateTime ad = new DateTime();
		ad.set(date.getDay() + (di * 7), date.getMonth(), date.getYear());

		InputTransferObject input = new InputTransferObject(ad, position);

		if (!view_cach[position].isDataHere() ){
			downloadschlange.add(input);
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
	public void restoreState(Parcelable arg0, ClassLoader arg1) {	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {}
	

	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	
	@Override
	public String getTitle(int pos) {
		int di = pos - 50;
		DateTime ad = new DateTime();
		ad.set(date.getDay() + (di * 7), date.getMonth(), date.getYear());
		return ad.getDay() + "." + ad.getMonth() + "." + ad.getYear();
	}
	
	public void reset(WeekView wv, Settings settings){
		view_cach = new WeekLayout[NUM_SCREENS];
		for (int i = 0; i < view_cach.length; i++) {
			view_cach[i] = new WeekLayout(context, i, wv, settings);
		}
	}

	public LastRefreshTransferObject getLastRefresh(int position) {
		return view_cach[position].getLastRefresh();
	}


	

}