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
package edu.htl3r.schoolplanner.gui.timetable.Overlay.Month;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.gui.timetable.WeekView;

public class OverlayMonth extends Dialog implements OnClickListener {

	private ImageButton next, prev;
	private TextView title;
	private DateTime date;

	private DateTime start, end;

	private OverlayCalendarView calendar;
	private RelativeLayout month_container;

	private WeekView viewbasti;

	private List<SchoolHoliday> holidays;
	private List<Integer> holidays_day = new ArrayList<Integer>();

	public OverlayMonth(Context context, WeekView vb, List<SchoolHoliday> hol) {
		super(context);
		viewbasti = vb;
		holidays = hol;
		init();
	}

	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.overlay_month);

		calendar = new OverlayCalendarView(getContext(), this);

		month_container = (RelativeLayout) findViewById(R.id.overlay_month_container);
		next = (ImageButton) findViewById(R.id.overlay_month_next);
		prev = (ImageButton) findViewById(R.id.overlay_month_prev);
		title = (TextView) findViewById(R.id.overlay_month_title);

		next.setOnClickListener(this);
		prev.setOnClickListener(this);

	}

	public void setDate(DateTime d) {
		date = new DateTime();
		date.set(1, d.getMonth(), d.getYear());
		
		

		if (start == null && end == null) {
			start = new DateTime();
			start.set(1 - (50 * 7), d.getMonth(), d.getYear());
			end = new DateTime();
			end.set(1 + (50 * 7), d.getMonth(), d.getYear());
		}

		holidays_day = new ArrayList<Integer>();
		for (SchoolHoliday s : holidays) {
			Log.d("basti", "Holidays: " + s.getStartDate() + " " + s.getEndDate());

			DateTime hstart = s.getStartDate().clone();
			DateTime hend = s.getEndDate().clone();
			if (hstart.compareTo(hend) == 0) {
				if (hstart.getMonth() == date.getMonth() && hstart.getYear() == date.getYear()) {
					holidays_day.add(hstart.getDay());
				}
			} else {

				if (hend.getMonth() == date.getMonth() && hend.getYear() == date.getYear() && 
						(hstart.getMonth() != date.getMonth() || hstart.getYear() != date.getYear())) {
					hstart.set(1, hstart.getMonth() + 1, hstart.getYear());
				}
				boolean wasinloop = false;
				while (hstart.compareTo(hend) < 0 && hstart.getMonth() == date.getMonth()) {
					holidays_day.add(hstart.getDay());
					wasinloop = true;
					hstart.increaseDay();
				}
				if(wasinloop && hstart.getMonth() == date.getMonth())
					holidays_day.add(hstart.getDay());
					
			}
		}

		addCalendar();
	}

	@Override
	public void onClick(View v) {
		DateTime tmp = new DateTime();
		if (v == next) {
			tmp.set(date.getDay(), date.getMonth() + 1, date.getYear());

			if (tmp.getMonth() == end.getMonth() - 1 && tmp.getYear() == end.getYear()) {
				next.setEnabled(false);
			}

			if (!prev.isEnabled())
				prev.setEnabled(true);

		} else if (v == prev) {
			tmp.set(date.getDay(), date.getMonth() - 1, date.getYear());

			if (tmp.getMonth() == start.getMonth() + 1 && tmp.getYear() == start.getYear()) {
				prev.setEnabled(false);
			}

			if (!next.isEnabled())
				next.setEnabled(true);
		}
		setDate(tmp);
	}

	private void setHeader() {
		String header = DateTimeUtils.getMonthName(date) + ", " + date.getYear();
		title.setText(header);
	}

	private void addCalendar() {
		calendar.setFirstDay(date, holidays_day);
		month_container.removeView(calendar);

		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		layout.addRule(RelativeLayout.BELOW, R.id.overlay_mont_header);
		
		int top = getContext().getResources().getDimensionPixelSize(R.dimen.gui_overlay_month_marign_top);
		layout.setMargins(0, top, 0, 0);
		calendar.setLayoutParams(layout);
		month_container.addView(calendar);
		setHeader();
	}

	public void displayChoosenWeek(int day) {
		DateTime ret = new DateTime();
		ret.set(day, date.getMonth(), date.getYear());
		viewbasti.setDateforDialog(ret);
		dismiss();
	}

}
