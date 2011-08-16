/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
						Gerald Schreiber <mail@gerald-schreiber.at>
						Philip Woelfel <philip@woelfel.at>
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

package edu.htl3r.schoolplanner.gui.timetableviews;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;
import edu.htl3r.schoolplanner.gui.ExtrasStrings;
import edu.htl3r.schoolplanner.gui.elements.ViewHeader;
import edu.htl3r.schoolplanner.gui.elements.ViewHourInfo;
import edu.htl3r.schoolplanner.gui.elements.ViewLesson;

public class WeekView extends ViewActivity {
	public WeekView() {
		settingsId = 1;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingsId = 1;
	}

	@Override
	public View createLayout() {
		ScrollView sv = new ScrollView(this);
		// sv.setScrollBarStyle(ScrollView.SCROLLBARS_OUTSIDE_OVERLAY);
		RelativeLayout container = new RelativeLayout(this);
		TextView header = new TextView(this);
		Calendar[] week = getWeek(currentDate);
		// currentDate = week[0];
		// String datum = getDateString(week[0]);
		String datum = week[0].get(Calendar.DAY_OF_MONTH) + "." + (week[0].get(Calendar.MONTH) + 1) + ". - " + week[week.length - 1].get(Calendar.DAY_OF_MONTH) + "." + (week[week.length - 1].get(Calendar.MONTH) + 1) + "." + week[week.length - 1].get(Calendar.YEAR);
		header.setText(datum + " - " + getSelectedViewType().getName());
		header.setId(1337);
		header.setTextSize(20);
		header.setGravity(Gravity.CENTER);
		Display display = getWindowManager().getDefaultDisplay();
		header.setWidth(display.getWidth());
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSelectViewTypePopup();
			}
		});
		container.addView(header);

		// Log.d("Philip", "currentDate " + currentDate);
		LinearLayout weekList = new LinearLayout(this);
		weekList.setOrientation(LinearLayout.HORIZONTAL);
		weekList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

		Map<String, List<Lesson>> lessons = null;
		try {
			lessons = app.getData().getMergedLessons(getSelectedViewType(), week[0], week[week.length - 1], refresh_lessons);
		} catch (IOException e) {
			sendErrorMessageToHandler("Exception ("+getClass().getSimpleName()+") at lessons: "+e.getMessage());
			Log.w("Network", e.getMessage(),e);
		}
		if (lessons != null) {
			if (lessons.size() == 0) {
				Log.w("Philip", getClass().getSimpleName() + ": lesson size 0");
				// handler.sendEmptyMessage(HANDLER_NOLESSON);
				Message msg = new Message();
				msg.what = HANDLER_NOLESSON;
				Bundle data = new Bundle();
				data.putSerializable(ExtrasStrings.DATE, week[0]);
				msg.setData(data);
				handler.sendMessage(msg);
			}
			else {
				String emptyDates = "";
				String failDates = "";

				boolean showZero = prefs.zerohourEnabled();
				Timegrid tg = null;

				try {
					tg = app.getData().getTimegrid();
				} catch (IOException e) {
					sendErrorMessageToHandler("Exception ("+getClass().getSimpleName()+") at timegrid: "+e.getMessage());
					Log.w("Network", e.getMessage(),e);
				}
				List<TimegridUnit> tulist = null;

				for (int j = 0; j < week.length; j++) {
					List<Lesson> leslist = lessons.get(CalendarUtils.getCalendarAs8601String(week[j]));
					if (tg != null) {
						tulist = tg.getTimegridForCalendarDay(week[j].get(Calendar.DAY_OF_WEEK));
					}
					if (!showZero && leslist != null && leslist.size() > 0 && tulist != null && tulist.size() > 0) {
						Collections.sort(leslist);
						Lesson nulltestunde = leslist.get(0);
						TimegridUnit nulltezeit = tulist.get(0);
						boolean what = (nulltestunde.getStartTime().get(Calendar.HOUR_OF_DAY) <= nulltezeit.getBegin().get(Calendar.HOUR_OF_DAY) && nulltestunde.getStartTime().get(Calendar.MINUTE) <= nulltezeit.getBegin().get(Calendar.MINUTE));
						if (what) {
							showZero = true;
							break;
						}
					}
				}
				
				if(prefs.isTimegridEnabled()){ 
					LinearLayout tgList = new LinearLayout(this);
					tgList.setOrientation(LinearLayout.VERTICAL);
					TextView dummy = new TextView(this);
					dummy.setTextSize(getResources().getDimension(R.dimen.weekHeader));
					dummy.setText((prefs.headerDatesEnabled()&&prefs.headerDaynamesEnabled())?" \n ":"");
					dummy.setBackgroundColor(prefs.getHeaderColor());
					if(!prefs.headerDatesEnabled()&&!prefs.headerDaynamesEnabled()){
						dummy.setHeight(0);
					}
					LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
					hp.setMargins(0, 0, 0, (!prefs.headerDatesEnabled()&&!prefs.headerDaynamesEnabled()?0:10));
					tgList.addView(dummy, hp);
					if(tg!=null){
						List<TimegridUnit> tus = tg.getTimegridForCalendarDay(Calendar.MONDAY);
						for(int i=(showZero?0:1);i<tus.size();i++){
							TimegridUnit tu = tus.get(i);
							ViewHourInfo vi = new ViewHourInfo(this);
							vi.setTu(tu);
							vi.setHour(i);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
							if (i > 0) {
								int margin = getMarginBetweenCalendars(tus.get(i-1).getEnd(), tu.getBegin());
								params.setMargins(0, margin, 0, 0);
							}
							
							tgList.addView(vi,params);
							
							
						}
					}
					weekList.addView(tgList);
				}
				
				for (int j = 0; j < week.length; j++) {
					Log.d("Philip", "week[" + j + "]: " + CalendarUtils.getTimeStr(week[j], true) + " " + CalendarUtils.getDateString(week[j], false));
					LinearLayout dayList = new LinearLayout(this);
					dayList.setOrientation(LinearLayout.VERTICAL);
					float div = (prefs.isSaturdayEnabled() ? 6 : 5);
					int headerWidth = (int) ((display.getWidth() / div)-(prefs.isTimegridEnabled()?20f/div:0));
					if (prefs.headerDatesEnabled() || prefs.headerDaynamesEnabled()) {
						ViewHeader vHeader = new ViewHeader(this);
						// vHeader.setText(getDateString(week[j]));
						vHeader.setDate(week[j], false);
						vHeader.setOnClickListener(this);
						vHeader.setWidth(headerWidth);
						LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
						hp.setMargins(0, 0, 0, 10);
						dayList.addView(vHeader, hp);
					}
					else {
						TextView dummy = new TextView(this);
						dummy.setText("");
						dummy.setWidth(headerWidth);
						dummy.setHeight(0);
						dayList.addView(dummy);

					}

					List<Lesson> leslist = lessons.get(CalendarUtils.getCalendarAs8601String(week[j]));
					// Log.d("Philip", getClass().getName() + ": leslist: " +leslist);

					if (leslist != null) {
						if (leslist.size() == 0) {
							Log.w("Philip", getClass().getSimpleName() + ": leslist size 0");
							// handler.sendEmptyMessage(HANDLER_NOLESSON);
							if (emptyDates.equals("")) {
								emptyDates = CalendarUtils.normalizeDate(week[j].get(Calendar.DAY_OF_MONTH) + "") + "." + CalendarUtils.normalizeDate((week[j].get(Calendar.MONTH) + 1) + "") + ".";
							}
							else {
								emptyDates += ", " + CalendarUtils.normalizeDate(week[j].get(Calendar.DAY_OF_MONTH) + "") + "." + CalendarUtils.normalizeDate((week[j].get(Calendar.MONTH) + 1) + "") + ".";
							}
						}
						else {
							Collections.sort(leslist);
							for (int i = 0; i < leslist.size(); i++) {
								ViewLesson vl = new ViewLesson(this);
								registerForContextMenu(vl);
								vl.setOnClickListener(this);
								vl.setLesson(leslist.get(i), getSelectedViewType());
								vl.setId(i + 1);
								
								LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
								int margin;
								if (i > 0) {
									margin = getMarginBetweenCalendars(leslist.get(i - 1).getEndTime(), leslist.get(i).getStartTime());
								}
								else {
									margin = getMarginSinceStart(leslist.get(i).getStartTime(), showZero ? 0 : 1);
								}
								params.setMargins(0, margin, 0, 0);
								dayList.addView(vl, params);
							}
						}
					}
					else {
						// TextView tv = new TextView(this);
						// tv.setText(getString(R.string.error_laoddata));
						// tv.setTextColor(Color.RED);
						// weekList.addView(tv);
						if (failDates.equals("")) {
							failDates = CalendarUtils.normalizeDate(week[j].get(Calendar.DAY_OF_MONTH) + "") + "." + CalendarUtils.normalizeDate((week[j].get(Calendar.MONTH) + 1) + "") + ".";
						}
						else {
							failDates += ", " + CalendarUtils.normalizeDate(week[j].get(Calendar.DAY_OF_MONTH) + "") + "." + CalendarUtils.normalizeDate((week[j].get(Calendar.MONTH) + 1) + "") + ".";
						}
						Log.w("Philip", getClass().getSimpleName() + ": keine leslist " + CalendarUtils.getDateString(week[j], true));
					}
					weekList.addView(dayList);

				}

				// keine nachrichten wenn keine stunden an gewissen tagen
				/*
				 * if (!emptyDates.equals("")) { Message msg = new Message(); msg.what = HANDLER_NOLESSON; Bundle data = new Bundle(); data.putString(ExtrasStrings.DATELIST, emptyDates);
				 * msg.setData(data); handler.sendMessage(msg); }
				 */
				if (!failDates.equals("")) {
					Message msg = new Message();
					msg.what = HANDLER_ERRORLESSON;
					Bundle data = new Bundle();
					data.putSerializable(ExtrasStrings.DATELIST, failDates);
					msg.setData(data);
					handler.sendMessage(msg);
				}
			}
		}
		else {
			Message msg = new Message();
			msg.what = HANDLER_ERRORLESSON;
			Bundle data = new Bundle();
			data.putSerializable(ExtrasStrings.DATE, week[0]);
			msg.setData(data);
			handler.sendMessage(msg);
			Log.w("Philip", getClass().getSimpleName() + ": keine lessons");
		}
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		lp.addRule(RelativeLayout.BELOW, header.getId());
		container.addView(weekList, lp);
		sv.addView(container);
		return sv;

	}

	/**
	 * gibt ein Array von Calendar-Objekten zurueck, die die Wochentage, der Woche in der {@code date} vorkommt, representieren
	 * @param date das Datum von dem die Woche berechnet werden soll
	 * @return ein Array mit Calendar Objekten
	 */
	protected Calendar[] getWeek(Calendar date) {

		// cal.set( cal.get(dayofmonth) - offset )
		int offset = 0;
		switch (date.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.MONDAY:
				offset = 0;
				break;

			case Calendar.TUESDAY:
				offset = 1;
				break;

			case Calendar.WEDNESDAY:
				offset = 2;
				break;

			case Calendar.THURSDAY:
				offset = 3;
				break;

			case Calendar.FRIDAY:
				offset = 4;
				break;

			case Calendar.SATURDAY:
				offset = 5;
				break;

			case Calendar.SUNDAY:
				offset = -1;
				break;
			default:
				break;
		}
		Calendar[] woche;
		if (prefs.isSaturdayEnabled()) {
			woche = new Calendar[6];
		}
		else {
			woche = new Calendar[5];
		}

		for (int i = 0; i < woche.length; i++) {
			Calendar tmp = (Calendar) date.clone();
			tmp.add(Calendar.DAY_OF_MONTH, -offset);
			woche[i] = tmp;
			offset--;
		}
		return woche;
	}

	@Override
	public void nextDate() {
		getDate().add(Calendar.WEEK_OF_YEAR, 1);
	}

	@Override
	public void prevDate() {
		getDate().add(Calendar.WEEK_OF_YEAR, -1);
	}
}
