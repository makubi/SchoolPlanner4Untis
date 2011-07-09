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
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.util.MonthDisplayHelper;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.gui.elements.ViewMonthDay;

public class MonthView extends ViewActivity {
	private List<SchoolHoliday> frei = null;

	public MonthView() {
		settingsId = 2;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settingsId = 2;

		try {
			frei = app.getData().getSchoolHolidayList();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Log.d("Philip", getClass().getSimpleName() + ": frei: " + frei);
	}

	@Override
	public View createLayout() {
		ScrollView sv = new ScrollView(this);
		LinearLayout container = new LinearLayout(this);
		container.setOrientation(LinearLayout.VERTICAL);
		TextView header = new TextView(this);
		String datum = CalendarUtils.getMonthString(currentDate.get(Calendar.MONTH)) + " " + currentDate.get(Calendar.YEAR);
		header.setText(datum + " - " + getSelectedViewType().getName());
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

		MonthDisplayHelper mdhelper = new MonthDisplayHelper(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), Calendar.MONDAY);

		TableLayout table = new TableLayout(this);
		table.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
		TableRow currentRow = new TableRow(this);

		int[] calvars = { Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY };
		for (int i = 0; i < calvars.length; i++) {
			TextView headert = new TextView(this);
			headert.setBackgroundColor(prefs.getHeaderColor());
			headert.setText(CalendarUtils.getShortWeekday(calvars[i]));
			headert.setTextColor(Color.WHITE);
			headert.setWidth(display.getWidth() / 7);
			headert.setGravity(Gravity.CENTER);
			headert.setTextSize(getResources().getDimension(R.dimen.monthHeader));
			currentRow.addView(headert);
		}
		table.addView(currentRow);
		currentRow = new TableRow(this);

		int daysInRow = 0;

		for (int i = 0; i < mdhelper.getOffset(); i++) {
			ViewMonthDay day = new ViewMonthDay(this);
			day.setText(mdhelper.getDayAt(0, i) + "");
			day.setPreviousMonth(true);
			day.setHoliday(giveMeHoliday(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH) - 1, mdhelper.getDayAt(0, i)));
			currentRow.addView(day);
			daysInRow++;
		}

		int rowcount = 0;
		for (int i = 0; i < mdhelper.getNumberOfDaysInMonth(); i++) {
			ViewMonthDay day = new ViewMonthDay(this);
			day.setText((i + 1) + "");
			day.setHoliday(giveMeHoliday(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), i + 1));
			currentRow.addView(day);
			daysInRow++;
			if(day.isToday()) {
				day.setBackgroundColor(Color.parseColor("#FF8C00"));
			}
			if (daysInRow > 6) {
				table.addView(currentRow);
				currentRow = new TableRow(this);
				daysInRow = 0;
				rowcount++;
			}
		}
		if (currentRow.getChildCount() > 0) {
			table.addView(currentRow);
		}

		for (int i = daysInRow; i < 7; i++) {
			ViewMonthDay day = new ViewMonthDay(this);
			day.setText(mdhelper.getDayAt(rowcount, i) + "");
			day.setNextMonth(true);
			day.setHoliday(giveMeHoliday(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH) + 1, mdhelper.getDayAt(0, i)));
			currentRow.addView(day);
			daysInRow++;
		}
		container.addView(table);
		sv.addView(container);

		return sv;

	}

	private SchoolHoliday giveMeHoliday(int year, int month, int day) {
		Calendar cal = CalendarUtils.getEmptyInstance();
		cal.set(year, month, day);
		SchoolHoliday sh = findHolidayInList(frei, cal);
		return sh;
	}

	private SchoolHoliday findHolidayInList(List<SchoolHoliday> list, Calendar date) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				if (CalendarUtils.betweenCalendars(list.get(i).getStartDate(), list.get(i).getEndDate(), date)) {
					return list.get(i);
				}
			}
		}

		return null;
	}

	@Override
	public void nextDate() {
		getDate().add(Calendar.MONTH, 1);
	}

	@Override
	public void prevDate() {
		getDate().add(Calendar.MONTH, -1);
	}
}