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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;
import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.gui.ExtrasStrings;
import edu.htl3r.schoolplanner.gui.timetableviews.DayView;
import edu.htl3r.schoolplanner.gui.timetableviews.ViewActivity;

public class ViewMonthDay extends TextView {
	private SchoolPlannerApp app;
	private float textsize;

	private boolean previousMonth = false;
	private boolean nextMonth = false;
	private SchoolHoliday holiday = null;
	
	public ViewMonthDay(Context context) {
		super(context);
		app = (SchoolPlannerApp) ((ViewActivity) getContext()).getApplication();
		setGravity(Gravity.CENTER);
		textsize = getResources().getDimension(R.dimen.monthTextSize);
		setTextSize(textsize);
		setTextColor(Color.WHITE);
		setBackgroundColor(getBGColor());
		setClickable(false);
		Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
		setWidth(display.getWidth() / 7);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Log.d("Philip", "x: " +event.getX() +", y: " +event.getY());
		float x = event.getX();
		float y = event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			if (x > getPaddingLeft() && x < getMeasuredWidth() - getPaddingRight() && y > getPaddingTop() && y < getMeasuredHeight() - getPaddingBottom()) {
				Log.d("Philip", getClass().getSimpleName() + " down");
				setBackgroundColor(app.getPrefs().getTouchColor());
				invalidate();
				return true;
//			}
		}
		else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
			Log.d("Philip", getClass().getSimpleName() + " up");
			setBackgroundColor(getBGColor());
			invalidate();
			if(event.getAction() == MotionEvent.ACTION_UP){
				ViewActivity par = (ViewActivity)getContext();
				Calendar cal = getCalendar();
				Log.d("Philip", getClass().getSimpleName() + ": CalendarUtils.dateToStr(cal): " + CalendarUtils.dateToStr(cal));
				Intent in = new Intent(getContext(), DayView.class);
				in.putExtra(ExtrasStrings.DATE, cal);
				in.putExtra(ExtrasStrings.VIEWTYPE, par.getSelectedViewType());
				in.putExtra(ExtrasStrings.PREVCLASS, par.getClass());
				par.startActivity(in);
				app.setCurrentView(DayView.class);
			}
			return true;
		}
		
		return super.onTouchEvent(event);
	}

	protected int getBGColor() {
		if(holiday!=null){
			return Color.RED;
		}
		if(previousMonth || nextMonth){
			return Color.BLACK;
		}
		
		return app.getPrefs().getBgColor();
	}
	
	private Calendar getCalendar(){
		ViewActivity par = (ViewActivity)getContext();
		Calendar cal = (Calendar) par.getDate().clone();
		if(previousMonth){
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1);
		}
		else if(nextMonth){
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
		}
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(getText()+""));
		return cal;
	}
	
	public boolean isToday() {
		return CalendarUtils.compareCalendarDates(Calendar.getInstance(), getCalendar());
	}

	/**
	 * @return the previousMonth
	 */
	public boolean isPreviousMonth() {
		return previousMonth;
	}

	/**
	 * @param previousMonth the previousMonth to set
	 */
	public void setPreviousMonth(boolean previousMonth) {
		this.previousMonth = previousMonth;
		if(previousMonth){
			setTextColor(Color.GRAY);
			setBackgroundColor(Color.BLACK);
		}
	}

	/**
	 * @return the nextMonth
	 */
	public boolean isNextMonth() {
		return nextMonth;
	}

	/**
	 * @param nextMonth the nextMonth to set
	 */
	public void setNextMonth(boolean nextMonth) {
		this.nextMonth = nextMonth;
		if(nextMonth){
			setTextColor(Color.GRAY);
			setBackgroundColor(Color.BLACK);
		}
	}

	/**
	 * @return the holiday
	 */
	public SchoolHoliday isHoliday() {
		return holiday;
	}

	/**
	 * @param holiday the holiday to set
	 */
	public void setHoliday(SchoolHoliday holiday) {
		this.holiday = holiday;
		setBackgroundColor(getBGColor());
	}

}
