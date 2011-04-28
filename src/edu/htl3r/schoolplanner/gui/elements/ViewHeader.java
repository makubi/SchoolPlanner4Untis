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

package edu.htl3r.schoolplanner.gui.elements;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;
import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.gui.timetableviews.ViewActivity;

public class ViewHeader extends TextView {
	private SchoolPlannerApp app;
	private Calendar date;

	public ViewHeader(Context context) {
		super(context);
		app = (SchoolPlannerApp) ((ViewActivity) getContext()).getApplication();
		setGravity(Gravity.CENTER);
		setTextSize(getResources().getDimension(R.dimen.weekHeader));
		setTextColor(Color.WHITE);
		setTextColor(getResources().getColorStateList(R.color.weekheader));
		setBackgroundColor(app.getPrefs().getHeaderColor());
		setClickable(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			setBackgroundColor(app.getPrefs().getTouchColor());
		}
		else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
			setBackgroundColor(app.getPrefs().getHeaderColor());
		}
		return super.onTouchEvent(event);
	}

	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * setzt das Datum neu und updatet den Text
	 * @param date the date to set
	 * @param forceDaynameOnly TODO
	 */
	public void setDate(Calendar date, boolean forceDaynameOnly) {
		this.date = date;
		if(forceDaynameOnly){
			setText(CalendarUtils.getWeekdayString(date.get(Calendar.DAY_OF_WEEK)));
		}
		else{
			boolean datesEna = app.getPrefs().headerDatesEnabled();
			boolean namesEna = app.getPrefs().headerDaynamesEnabled();
			setText((datesEna ? CalendarUtils.getDateStringNoYears(date, false) : "") + (datesEna && namesEna ? "\n" : "") + (namesEna ? CalendarUtils.getShortWeekday(date.get(Calendar.DAY_OF_WEEK)) : ""));
		}
	}

}
