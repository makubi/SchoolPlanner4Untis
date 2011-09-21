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
package edu.htl3r.schoolplanner.gui.timetable;

import java.util.Calendar;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.BundleConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import edu.htl3r.schoolplanner.gui.timetable.Overlay.OverlayMonth;

public class ViewBasti extends SchoolPlannerActivity {

	private ViewPager myViewPager;
	private WeekViewPageAdapter wvpageadapter;
	private LoadDataTask loadweekdata;
	private ViewPagerIndicator indicator;
	private ViewType viewtype;
	private OverlayMonth overlaymonth;

	public BlockingDownloadQueue downloadschlange = new BlockingDownloadQueue();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basti_weekview);
		initViewPager();

		viewtype = (ViewType) getIntent().getExtras().getSerializable(BundleConstants.SELECTED_VIEW_TYPE);
		loadweekdata = new LoadDataTask();
		loadweekdata.setData(this, ((SchoolPlannerApp) getApplication()).getData(), this, viewtype, downloadschlange, ((SchoolPlannerApp) getApplication()).getSettings());
		loadweekdata.execute();
	}

	private DateTime getMonday() {
		DateTime d = new DateTime();
		Calendar c = Calendar.getInstance();
		d.set(c.get(Calendar.DATE), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));

		if (d.getWeekDay() == Time.SUNDAY) {
			d.increaseDay();
			return d;
		}

		while (d.getWeekDay() != Time.MONDAY) {
			d.decreaseDay();
		}
		return d;
	}

	public Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			OutputTransferObject result = (OutputTransferObject) msg.obj;
			wvpageadapter.setWeeData(result.getWeek(), result.getPos());
		}
	};

	@Override
	protected void onStop() {
		downloadschlange.interrupt();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		downloadschlange.interrupt();
		super.onDestroy();
	}

	private void initViewPager() {
		myViewPager = (ViewPager) findViewById(R.id.week_pager);
		indicator = (ViewPagerIndicator) findViewById(R.id.week_indicator);

		wvpageadapter = new WeekViewPageAdapter();
		wvpageadapter.setContext(this, downloadschlange);
		wvpageadapter.setDate(getMonday());

		myViewPager.setAdapter(wvpageadapter);

		myViewPager.setOnPageChangeListener(indicator);
		indicator.init(50, wvpageadapter.getCount(), wvpageadapter);

		Resources res = getResources();
		Drawable prev = res.getDrawable(R.drawable.indicator_prev_arrow);
		Drawable next = res.getDrawable(R.drawable.indicator_next_arrow);

		indicator.setArrows(prev, next);
		myViewPager.setCurrentItem(50);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.timetable_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.timetable_month:
			Toast.makeText(this, "Month", Toast.LENGTH_SHORT).show();
			overlaymonth = new OverlayMonth(this, this);
			overlaymonth.setDate(getMonday());
			overlaymonth.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setDateforDialog(DateTime date) {
		DateTime d = date.clone();

		Log.d("basti", "Gewaehltes Datum : " + date);
		if (d.getWeekDay() == Time.SUNDAY) {
			d.increaseDay();
		} else {

			while (d.getWeekDay() != Time.MONDAY) {
				d.decreaseDay();
			}
		}
		DateTime now = getMonday().clone();

		int count = 0;
		
		if(d.compareTo(now) < 0){
			
			while(d.compareTo(now) != 0){
				now.set(now.getDay()-7, now.getMonth(), now.getYear());
				count--;
			}
		}else if (d.compareTo(now)  > 0){
			while(d.compareTo(now) != 0){
				now.set(now.getDay()+7, now.getMonth(), now.getYear());
				count++;
			}
		}
		myViewPager.setCurrentItem(50+count);
	}

}