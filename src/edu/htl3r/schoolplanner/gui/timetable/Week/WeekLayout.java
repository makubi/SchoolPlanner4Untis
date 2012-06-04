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
package edu.htl3r.schoolplanner.gui.timetable.Week;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.Settings;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.timetable.WeekView;
import edu.htl3r.schoolplanner.gui.timetable.Eyecandy.WeekHeader;
import edu.htl3r.schoolplanner.gui.timetable.Eyecandy.WeekTimeGrid;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUIDay;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUILessonContainer;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUIWeek;
import edu.htl3r.schoolplanner.gui.timetable.Lessons.LessonView;
import edu.htl3r.schoolplanner.gui.timetable.Overlay.Info.OverlayInfo;

public class WeekLayout extends ViewGroup{

	private final int HEADER_HEIGHT = getResources().getDimensionPixelSize(R.dimen.gui_header_height);
	private final int TIMEGRID_WIDTH = getResources().getDimensionPixelSize(R.dimen.gui_timegrid_width);

	private final int BORDERWIDTH = getResources().getDimensionPixelSize(R.dimen.gui_stroke_width_2);

	private int ID;

	private GUIWeek weekdata;

	private Paint paint;

	private int width, height;
	private int days, hours;
	private float widthlesson, heightlesson;
	private Context context;

	private WeekHeader weekheader;
	private WeekTimeGrid weektimegrid;

	private boolean isDataHere = false;
	
	private OnLessonsClickListener clicklistener;
	private OverlayInfo weekoverlay;
	
	private WeekView weekview;
	
	private Settings settings;

	public WeekLayout(Context context, int id, WeekView wv, Settings settings) {
		super(context);
		this.context = context;
		this.ID = id;
		this.weekview = wv;
		this.settings = settings;
		
		boolean highlightCurrentHour = settings.isHighlightCurrentLesson();
		clicklistener = new OnLessonsClickListener();
		weekoverlay = new OverlayInfo(context);
		
		weekheader = new WeekHeader(context,highlightCurrentHour);
		weektimegrid = new WeekTimeGrid(context,highlightCurrentHour);
		initDrawingStuff();

		weekdata = new GUIWeek();
		days = 5;
		hours = 11;
		
		setOnTouchListener(clicklistener);		
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		for (int i = getChildCount() - 1; i >= 0; i--) {
			drawChild(canvas, getChildAt(i), 0);
		}
		if (getChildCount() != 0) {
			zeichneGatter(canvas);
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (getChildCount() != 0) {
			zeichneGatter(canvas);
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		width = MeasureSpec.getSize(widthMeasureSpec);

		widthlesson = (width - TIMEGRID_WIDTH) / days;
		heightlesson = (widthlesson / 5) * 4;
		height = (int) (heightlesson * hours) + HEADER_HEIGHT;

		for (int i = 0; i < getChildCount(); i++) {
			GUIWeekView c = (GUIWeekView) getChildAt(i);

			switch (c.getId()) {
			case GUIWeekView.LESSON_ID:
				measureChild(c, MeasureSpec.makeMeasureSpec((int) widthlesson - (BORDERWIDTH*2), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) heightlesson - (BORDERWIDTH*2), MeasureSpec.EXACTLY));
				break;
			case GUIWeekView.HEADER_ID:
				measureChild(c, MeasureSpec.makeMeasureSpec((int) width - TIMEGRID_WIDTH, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) HEADER_HEIGHT, MeasureSpec.EXACTLY));
				break;
			case GUIWeekView.TIMGRID_ID:
				measureChild(c, MeasureSpec.makeMeasureSpec((int) TIMEGRID_WIDTH, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) height, MeasureSpec.EXACTLY));
				break;
			}
		}

		this.setMeasuredDimension(width, height);

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		int l = TIMEGRID_WIDTH;
		int t = HEADER_HEIGHT;
		int r = (int) (l + widthlesson);
		int b = (int) (t + heightlesson);
		DateTime now = null;
		DateTime old = null;

		for (int i = 0; i < getChildCount(); i++) {

			GUIWeekView gv = (GUIWeekView) getChildAt(i);

			switch (gv.getId()) {
			case GUIWeekView.LESSON_ID:
				LessonView c = (LessonView) gv;
				now = c.getTime();

				if (old == null) {
					old = c.getTime();
				} else {

					if (old.compareTo(now) == 0) {
						t += heightlesson;
						b += heightlesson;
					} else {
						old = now;
						l += widthlesson;
						r += widthlesson;
						t = HEADER_HEIGHT;
						b = (int) (t + heightlesson);
					}
				}
				c.layout(l + (BORDERWIDTH / 2), t + (BORDERWIDTH / 2), r - (BORDERWIDTH / 2), b - (BORDERWIDTH / 2));
				//c.layout(l + (BORDERWIDTH), t + (BORDERWIDTH), r - (BORDERWIDTH), b - (BORDERWIDTH));
				break;

			case GUIWeekView.HEADER_ID:
				gv.layout(TIMEGRID_WIDTH, 0, width, HEADER_HEIGHT);
				break;

			case GUIWeekView.TIMGRID_ID:
				gv.layout(0, 0, TIMEGRID_WIDTH, height);
				break;
			}
		}
	}

	private void zeichneGatter(Canvas canvas) {
		int tposx = TIMEGRID_WIDTH, tposy = HEADER_HEIGHT;

		for (int i = 0; i < days - 1; ++i) {
			tposx += widthlesson;
			canvas.drawLine(tposx, tposy, tposx, height, paint);
		}

		tposx = TIMEGRID_WIDTH;
		tposy = HEADER_HEIGHT;
		for (int i = 0; i < hours; i++) {
			tposy += heightlesson;
			canvas.drawLine(tposx, tposy, width, tposy, paint);
		}
	}

	private void initDrawingStuff() {
		setBackgroundColor(getResources().getColor(R.color.background));
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(BORDERWIDTH);
		paint.setStyle(Style.STROKE);
	}

	public void setWeekData(GUIWeek week) {
		weekdata = week;
		days = weekdata.getCountDays();
		hours = weekdata.getMaxHours();

		ArrayList<DateTime> datum = weekdata.getSortDates();

		weekheader.setMonday(datum);
		weektimegrid.setTimeGrid(week.getTimeGrid(),HEADER_HEIGHT);
		

		this.addView(weekheader);
		this.addView(weektimegrid);

		for (int i = 0; i < datum.size(); i++) {
			GUIDay day = week.getDay(datum.get(i));

			ArrayList<DateTime> sortDates = day.getSortDates();

			for (int j = 0; j < sortDates.size(); j++) {
				GUILessonContainer lessonsContainer = day.getLessonsContainer(sortDates.get(j));
				LessonView lv = new LessonView(context);
				lv.setNeededData(lessonsContainer, week.getViewType());
				
				
				if(!lessonsContainer.isEmpty()){
					lv.setOnClickListener(clicklistener);
				}
				
				this.addView(lv);
			}
		}
		isDataHere = true;
	}

	public int getID() {
		return ID;
	}

	@Override
	public boolean equals(Object obj) {
		return (this.getID() == ((WeekLayout) obj).getID()) ? true : false;
	}

	public boolean isDataHere() {
		return isDataHere;
	}

	public DateTime getWeekDate() {
		return (isDataHere()) ? weekdata.getSortDates().get(0) : new DateTime();
	}
	
	public ViewType getVT(){
		return weekdata.getViewType();
	}
	
	private class OnLessonsClickListener implements OnClickListener, OnTouchListener{
		@Override
		public void onClick(View v) {
			weekview.notifyActionBarTouch();
			LessonView l = (LessonView)v;
			weekoverlay.setData(l.getLessonsContainer(),l.getViewType(),weekview);
			weekoverlay.show();
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(MotionEvent.ACTION_DOWN == event.getAction())
				weekview.notifyActionBarTouch();
			return false;
		}
	}
		
}
