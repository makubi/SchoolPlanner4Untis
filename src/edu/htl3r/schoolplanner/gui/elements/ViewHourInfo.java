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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.view.Gravity;
import android.widget.TextView;
import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;
import edu.htl3r.schoolplanner.gui.timetableviews.DayView;
import edu.htl3r.schoolplanner.gui.timetableviews.MonthView;
import edu.htl3r.schoolplanner.gui.timetableviews.ViewActivity;
import edu.htl3r.schoolplanner.gui.timetableviews.WeekView;

public class ViewHourInfo extends TextView {
	private SchoolPlannerApp app;
	private TimegridUnit tu;
	private float textsize;
	private int hour = -1;
	
	public ViewHourInfo(Context context) {
		super(context);
		app = (SchoolPlannerApp) ((ViewActivity) getContext()).getApplication();
		setGravity(Gravity.CENTER);
		if (context instanceof DayView) {
			textsize = getResources().getDimension(R.dimen.dayTextSize);
		}
		else if (context instanceof WeekView) {
			textsize = getResources().getDimension(R.dimen.weekTextSize);
		}
		else if (context instanceof MonthView) {
			textsize = getResources().getDimension(R.dimen.monthTextSize);
		}
		setTextSize(textsize);
		setTextColor(Color.WHITE);
		setBackgroundColor(app.getPrefs().getHeaderColor());
		setClickable(false);
	}

	public TimegridUnit getTu() {
		return tu;
	}

	public void setTu(TimegridUnit tu) {
		this.tu = tu;
		String timestr = CalendarUtils.getTimeStr(tu.getBegin(),false) +"\n" +CalendarUtils.getTimeStr(tu.getEnd(), false);
		setText(timestr);
		long min = CalendarUtils.minBetween(tu.getBegin(), tu.getEnd());
		int height = (int) (min*getResources().getDisplayMetrics().density);
		setHeight(height);
		Paint paint = new Paint();
		paint.setTextSize(textsize);
		Rect rect = new Rect();
		paint.getTextBounds(timestr, 0, timestr.length(), rect);
		setWidth(rect.width()+20);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		Rect maxRect = new Rect(getPaddingLeft(), getPaddingTop(), getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
		paint.setColor(app.getPrefs().getBorderColor());
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5);
		canvas.drawRect(maxRect, paint);
	}

	/**
	 * @return the hour
	 */
	public int getHour() {
		return hour;
	}

	/**
	 * @param hour the hour to set
	 */
	public void setHour(int hour) {
		this.hour = hour;
		setText(""+hour);
		setWidth(20);
	}
	
	

}
