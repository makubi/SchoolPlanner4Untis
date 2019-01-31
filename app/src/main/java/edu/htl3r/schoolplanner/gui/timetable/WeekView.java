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

import java.util.List;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.viewpager.widget.ViewPager;
import android.text.format.Time;
import android.widget.Toast;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.preferences.Settings;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import edu.htl3r.schoolplanner.gui.BundleConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import edu.htl3r.schoolplanner.gui.timetable.Overlay.Info.ViewTypeSwitcherTask;
import edu.htl3r.schoolplanner.gui.timetable.Overlay.Month.OverlayMonth;
import edu.htl3r.schoolplanner.gui.timetable.TransportClasses.OutputTransferObject;
import edu.htl3r.schoolplanner.gui.timetable.baactionbar.BADropdown;
import edu.htl3r.schoolplanner.gui.timetable.baactionbar.BastisAwesomeActionBar;

public class WeekView extends SchoolPlannerActivity implements BastisAwesomeActionBar.BAActoinBarEvent {

	private ViewPager myViewPager;
	private WeekViewPageAdapter wvpageadapter;
	private ViewPagerEventDistributer viewPagerEventDistributer;
		
	private LoadDataTask loadweekdata;
	private ViewPagerIndicator indicator;
	private ViewType viewtype;
	private OverlayMonth overlaymonth;
	private Settings settings;

	private List<SchoolHoliday> holidays;
	private ViewTypeListDialog viewtypedialog;

	public BlockingDownloadQueue downloadschlange = new BlockingDownloadQueue();
	private BastisAwesomeActionBar actionbar;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = ((SchoolPlannerApp) getApplication()).getSettings();
		setContentView(R.layout.basti_weekview);

		initActionBar();
		initViewPager();

		initViewTypeDialog();
		loadViewType();
		initDownloadQueue();
	}

	private void initViewTypeDialog() {
		viewtypedialog = new ViewTypeListDialog(this);
		viewtypedialog.setData(((SchoolPlannerApp) getApplication()).getData(), this, this);
	}

	private void initActionBar() {
		actionbar = (BastisAwesomeActionBar) findViewById(R.id.baactionbar);
		actionbar.init(((BADropdown) findViewById(R.id.baactionbar_dropdown)), findViewById(R.id.week_container));
		actionbar.addBAActionBarEvent(this);
	}

	private void loadViewType() {
		final Object data = getLastNonConfigurationInstance();

		if (data == null) {
			viewtype = (ViewType) getIntent().getExtras().getSerializable(BundleConstants.SELECTED_VIEW_TYPE);
		} else {
			viewtype = (ViewType) data;
		}
		actionbar.setText(viewtype);
	}

	private void initDownloadQueue() {
		loadweekdata = new LoadDataTask();
		loadweekdata.setData(this, ((SchoolPlannerApp) getApplication()).getData(), this, viewtype, downloadschlange, settings);
		loadweekdata.execute();
	}

	private DateTime getMonday() {
		DateTime d = DateTimeUtils.getNow();

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
			
			if(result.getWeek() != null && myViewPager.getCurrentItem() == result.getPos()){
				actionbar.setLastRefresh(result.getWeek().getLastRefresh());
			}
			
			if (result.getWeek() != null && holidays == null)
				holidays = result.getWeek().getHolidays();
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

	@Override
	protected void onResume() {
		if (downloadschlange.isInterrupted()) {
			downloadschlange.reset();
			initDownloadQueue();
		}
		super.onResume();
	}

	@Override
	protected void onRestart() {
		if (downloadschlange.isInterrupted()) {
			downloadschlange.reset();
			initDownloadQueue();
		}
		super.onRestart();
	}

	private void initViewPager() {

		myViewPager = (ViewPager) findViewById(R.id.week_pager);
		indicator = (ViewPagerIndicator) findViewById(R.id.week_indicator);
		wvpageadapter = new WeekViewPageAdapter();
		viewPagerEventDistributer = new ViewPagerEventDistributer();

		wvpageadapter.setContext(this, downloadschlange, this, ((SchoolPlannerApp) getApplication()).getSettings());
		wvpageadapter.setDate(getMonday());
		myViewPager.setAdapter(wvpageadapter);
		
		viewPagerEventDistributer.addViewPagerEventDistributer(indicator);
		viewPagerEventDistributer.addViewPagerEventDistributer(new ViewPagerEventDistributerOnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if(arg1 == 0 && arg2 == 0){
					actionbar.setLastRefresh(wvpageadapter.getLastRefresh(arg0));
				}
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		myViewPager.setOnPageChangeListener(viewPagerEventDistributer);
		
		
				
		indicator.init(50, wvpageadapter.getCount(), wvpageadapter);

		Resources res = getResources();
		Drawable prev = res.getDrawable(R.drawable.indicator_prev_arrow);
		Drawable next = res.getDrawable(R.drawable.indicator_next_arrow);

		indicator.setArrows(prev, next);
		myViewPager.setCurrentItem(50);
	}

	public void setDateforDialog(DateTime date) {
		DateTime d = date.clone();
		d.setHour(0);
		d.setMinute(0);
		d.setSecond(0);

		Toast.makeText(this, date.getDay() + "." + date.getMonth() + "." + date.getYear(), Toast.LENGTH_SHORT).show();
		if (d.getWeekDay() == Time.SUNDAY) {
			d.increaseDay();
		} else {
			while (d.getWeekDay() != Time.MONDAY) {
				d.decreaseDay();
			}
		}

		DateTime now = getMonday().clone();
		now.setHour(0);
		now.setMinute(0);
		now.setSecond(0);

		int count = 0;

		if (d.compareTo(now) < 0) {
			while (d.compareTo(now) != 0) {
				now.set(now.getDay() - 7, now.getMonth(), now.getYear());
				count--;
			}
		} else if (d.compareTo(now) > 0) {
			while (d.compareTo(now) != 0) {
				now.set(now.getDay() + 7, now.getMonth(), now.getYear());
				count++;
			}
		}
		myViewPager.setCurrentItem(50 + count);
	}

	public void changeViewType(ViewType vt) {
		viewtype = vt;
		actionbar.setText(viewtype);

		ViewTypeSwitcherTask viewTypeSwitcher = new ViewTypeSwitcherTask(this, myViewPager, wvpageadapter, loadweekdata, vt,false);
		viewTypeSwitcher.execute();
	}
	
	public void refreshTimeTable(){
		ViewTypeSwitcherTask viewTypeSwitcher = new ViewTypeSwitcherTask(this, myViewPager, wvpageadapter, loadweekdata, viewtype,true);
		viewTypeSwitcher.execute();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return viewtype;
	}

	@Override
	public void setInProgress(String message, boolean active) {
		actionbar.setProgress(active);
	}

	public Settings getSettings() {
		return settings;
	}

	public void notifyActionBarTouch() {
		actionbar.closeDropDown();
	}

	@Override
	public void onBAActionbarActionClicked(int ID) {
		switch (ID) {
		case BastisAwesomeActionBar.REFRESH:
			refreshTimeTable();
			break;
		case BastisAwesomeActionBar.MONTH:
			if (holidays != null) {
				overlaymonth = new OverlayMonth(this, this, holidays);
				overlaymonth.setDate(DateTimeUtils.getNow());
				overlaymonth.show();
			}
			break;
		case BastisAwesomeActionBar.HOME:
			finish();
			break;
		case BastisAwesomeActionBar.LIST_CLASS:
			viewtypedialog.setList(BastisAwesomeActionBar.LIST_CLASS);
			viewtypedialog.show();
			break;
		case BastisAwesomeActionBar.LIST_ROOMS:
			viewtypedialog.setList(BastisAwesomeActionBar.LIST_ROOMS);
			viewtypedialog.show();
			break;
		case BastisAwesomeActionBar.LIST_SUBJECTS:
			viewtypedialog.setList(BastisAwesomeActionBar.LIST_SUBJECTS);
			viewtypedialog.show();
			break;
		case BastisAwesomeActionBar.LIST_TEACHER:
			viewtypedialog.setList(BastisAwesomeActionBar.LIST_TEACHER);
			viewtypedialog.show();
			break;
		case BastisAwesomeActionBar.TEXT:
			if (viewtype instanceof SchoolClass) {
				viewtypedialog.setList(BastisAwesomeActionBar.LIST_CLASS);
			} else if (viewtype instanceof SchoolRoom) {
				viewtypedialog.setList(BastisAwesomeActionBar.LIST_ROOMS);
			} else if (viewtype instanceof SchoolSubject) {
				viewtypedialog.setList(BastisAwesomeActionBar.LIST_SUBJECTS);
			} else if (viewtype instanceof SchoolTeacher) {
				viewtypedialog.setList(BastisAwesomeActionBar.LIST_TEACHER);
			}
			viewtypedialog.show();
			break;
		}
	}

}