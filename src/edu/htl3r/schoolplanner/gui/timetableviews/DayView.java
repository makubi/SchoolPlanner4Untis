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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;
import edu.htl3r.schoolplanner.gui.elements.ViewHourInfo;
import edu.htl3r.schoolplanner.gui.elements.ViewLesson;

/**
 * Anzeige eines Tages des Stundenplans
 * 
 * @author Philip Woelfel <philip@woelfel.at>
 * 
 */
public class DayView extends ViewActivity {
	/**
	 * normaler Konstruktor damit in den Settings das richtige ausgewaehlt wird
	 */
	public DayView() {
		settingsId = 0;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingsId = 0;
	}

	@Override
	public View createLayout() {
		ScrollView sv = new ScrollView(this);
		LinearLayout lessonList = new LinearLayout(this);
		lessonList.setOrientation(LinearLayout.VERTICAL);
		TextView header = new TextView(this);
		String datum = CalendarUtils.getDateString(currentDate, true);
		header.setText(datum + " - " + getSelectedViewType().getName());
		header.setId(1337);
		header.setTextSize(20); // TODO dimensions verwenden
		header.setGravity(Gravity.CENTER);
		Display display = getWindowManager().getDefaultDisplay();
		header.setWidth(display.getWidth());
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSelectViewTypePopup();
			}
		});
		
		

		boolean showInfo = prefs.isTimegridEnabled();
		boolean showZero = prefs.zerohourEnabled();

		List<Lesson> leslist = getLessonList(currentDate);

		Timegrid tg = null;

		try {
			tg = app.getData().getTimegrid();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<TimegridUnit> tulist = null;
		if (tg != null) {
			tulist = tg.getTimegridForCalendarDay(currentDate.get(Calendar.DAY_OF_WEEK));
		}

		
		if (!showZero && leslist != null && leslist.size() > 0 && tulist != null && tulist.size() > 0) {
			Collections.sort(leslist);
			Lesson nulltestunde = leslist.get(0);
			TimegridUnit nulltezeit = tulist.get(0);
			boolean what = (nulltestunde.getStartTime().get(Calendar.HOUR_OF_DAY)==nulltezeit.getBegin().get(Calendar.HOUR_OF_DAY) && nulltestunde.getStartTime().get(Calendar.MINUTE)==nulltezeit.getBegin().get(Calendar.MINUTE));
			if (what) {
				showZero=true;
			}
		}

		if (showInfo && tulist != null) {
			LinearLayout containerH = new LinearLayout(this);
			containerH.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout containerV = new LinearLayout(this);
			containerV.setOrientation(LinearLayout.VERTICAL);
			LinearLayout infoList = new LinearLayout(this);
			infoList.setOrientation(LinearLayout.VERTICAL);

			List<ViewHourInfo> views = new ArrayList<ViewHourInfo>();
			// Log.d("Philip", getClass().getSimpleName() + ": tulist: " + tulist);
			for (int i = 0; i < tulist.size(); i++) {
				TimegridUnit tu = tulist.get(i);
				ViewHourInfo vi = new ViewHourInfo(this);
				vi.setTu(tu);
				views.add(vi);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				if (i > 0) {
					int margin = getMarginBetweenCalendars(views.get(i - 1).getTu().getEnd(), tu.getBegin());
					params.setMargins(0, margin, 0, 0);
					infoList.addView(vi, params);
				}
				else {
					if(showZero){
						params.setMargins(0, 0, 0, 0);
						infoList.addView(vi, params);
					}
				}
				
			}

			containerV.addView(header);
			containerV.addView(containerH);
			containerH.addView(infoList);
			containerH.addView(lessonList);
			sv.addView(containerV);
		}
		else {
			lessonList.addView(header);
			sv.addView(lessonList);
		}

		// Log.d("Philip", getClass().getName() + ": leslist: " +leslist);
		if (leslist != null) {
			if (leslist.size() == 0) {
				TextView tv = new TextView(this);
				tv.setText(getString(R.string.no_lessons));
				lessonList.addView(tv);
			}
			else {
				Collections.sort(leslist);
				// Log.d("Philip", getClass().getSimpleName() + ": leslist: " + leslist);
				List<ViewLesson> vlist = new ArrayList<ViewLesson>();

				for (int i = 0; i < leslist.size(); i++) {
					ViewLesson vl = new ViewLesson(this);
					vl.setOnClickListener(this);
					registerForContextMenu(vl);
					vl.setLesson(leslist.get(i), getSelectedViewType());
					vl.setId(i + 1);

					// vl.setPadding(0, getPaddingSinceStart(leslist.get(i).getStartTime()) + 50 + 50/* <--fuer header */, 0, 0);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
					int margin;
					if (i > 0) {
						margin = getMarginBetweenCalendars(leslist.get(i - 1).getEndTime(), leslist.get(i).getStartTime());
					}
					else {
						if(showZero){
							margin = getMarginSinceStart(leslist.get(i).getStartTime(), 0);
						}
						else{
							margin = getMarginSinceStart(leslist.get(i).getStartTime(), 1);
						}
					}
					params.setMargins(0, margin, 0, 0);
					lessonList.addView(vl, params);
					vlist.add(vl);

				}
			}

		}
		else {
			TextView tv = new TextView(this);
			tv.setText(getString(R.string.error_laoddata));
			tv.setTextColor(Color.RED);
			lessonList.addView(tv);
			Log.w("Philip", getClass().getSimpleName() + ": keine lessons");
		}

		return sv;

	}

	@Override
	public void nextDate() {
		getDate().add(Calendar.DAY_OF_MONTH, 1);
		int dayofweek = getDate().get(Calendar.DAY_OF_WEEK);
		if(dayofweek==Calendar.SATURDAY && !prefs.isSaturdayEnabled()){
			getDate().add(Calendar.DAY_OF_MONTH, 2);
		}
		else if (dayofweek == Calendar.SUNDAY) {
			getDate().add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	@Override
	public void prevDate() {
		getDate().add(Calendar.DAY_OF_MONTH, -1);
		int dayofweek = getDate().get(Calendar.DAY_OF_WEEK);
		if (dayofweek == Calendar.SUNDAY) {
			if(prefs.isSaturdayEnabled()){
				getDate().add(Calendar.DAY_OF_MONTH, -1);
			}
			else{
				getDate().add(Calendar.DAY_OF_MONTH, -2);
			}
		}
	}
}
