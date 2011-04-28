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

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCode;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeCancelled;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import edu.htl3r.schoolplanner.gui.timetableviews.DayView;
import edu.htl3r.schoolplanner.gui.timetableviews.MonthView;
import edu.htl3r.schoolplanner.gui.timetableviews.ViewActivity;
import edu.htl3r.schoolplanner.gui.timetableviews.WeekView;

public class ViewLesson extends View {
	private Lesson lesson;
	private ViewType viewType;
	private SchoolPlannerApp app = ((SchoolPlannerApp) ((ViewActivity) getContext()).getApplication());
	private int bgColor;
	final float scale = getContext().getResources().getDisplayMetrics().density;

	public ViewLesson(Context context) {
		super(context);

	}

	/**
	 * @return the lesson
	 */
	public Lesson getLesson() {
		return lesson;
	}

	/**
	 * @param lesson the lesson to set
	 */
	public void setLesson(Lesson lesson, ViewType requestingViewType) {
		this.lesson = lesson;
		this.viewType = requestingViewType;
		bgColor = getBGColor();
		requestLayout();
		invalidate();
	}

	/**
	 * @return the viewType
	 */
	public ViewType getViewType() {
		return viewType;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int specWidth = MeasureSpec.getSize(widthMeasureSpec);
		Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
		int dispWidth = display.getWidth();

		int width = getPaddingLeft() + getPaddingRight();
		int height = getPaddingTop() + getPaddingBottom();
		if (lesson != null) {
			Calendar startTime = null;
			Calendar endTime = null;
			try {
				List<TimegridUnit> tulist = app.getData().getTimegrid().getTimegridForCalendarDay(lesson.getStartTime().get(Calendar.DAY_OF_WEEK));
				Calendar nulltestunde = tulist.get(0).getBegin();
				startTime = (Calendar) nulltestunde.clone();
				startTime.set(Calendar.HOUR_OF_DAY, lesson.getStartTime().get(Calendar.HOUR_OF_DAY));
				startTime.set(Calendar.MINUTE, lesson.getStartTime().get(Calendar.MINUTE));

				// Log.d("Philip", getClass().getSimpleName() + ": nullte: " +CalendarUtils.dateToStr(nulltestunde));
				// Log.d("Philip", getClass().getSimpleName() + ": start: " +CalendarUtils.dateToStr(startTime));
				if (startTime.before(nulltestunde)) {
					// Log.d("Philip", getClass().getSimpleName() + ": true 1");
					startTime.set(Calendar.HOUR_OF_DAY, nulltestunde.get(Calendar.HOUR_OF_DAY));
					startTime.set(Calendar.MINUTE, nulltestunde.get(Calendar.MINUTE));
				}

				Calendar letztetestunde = tulist.get(tulist.size() - 1).getEnd();
				endTime = (Calendar) letztetestunde.clone();
				endTime.set(Calendar.HOUR_OF_DAY, lesson.getEndTime().get(Calendar.HOUR_OF_DAY));
				endTime.set(Calendar.MINUTE, lesson.getEndTime().get(Calendar.MINUTE));

				// Log.d("Philip", getClass().getSimpleName() + ": letzte: " +CalendarUtils.dateToStr(letztetestunde));
				// Log.d("Philip", getClass().getSimpleName() + ": end: " +CalendarUtils.dateToStr(endTime));
				if (endTime.after(letztetestunde)) {
					// Log.d("Philip", getClass().getSimpleName() + ": true 2");
					endTime.set(Calendar.HOUR_OF_DAY, letztetestunde.get(Calendar.HOUR_OF_DAY));
					endTime.set(Calendar.MINUTE, letztetestunde.get(Calendar.MINUTE));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long minutes = CalendarUtils.minBetween(startTime, endTime);
			height += (int) (scale * minutes);
		}

		// Log.d("Philip", getClass().getSimpleName() + ": parentWidth: " + parentWidth);
		// Log.d("Philip", getClass().getSimpleName() + ": lesson: " +lesson.getSchoolSubjects());
		// int mode = MeasureSpec.getMode(widthMeasureSpec);
		// Log.d("Philip", getClass().getSimpleName() + ": mode exact: " +(mode==MeasureSpec.EXACTLY));
		// Log.d("Philip", getClass().getSimpleName() + ": mode atmost: " +(mode==MeasureSpec.AT_MOST));
		// Log.d("Philip", getClass().getSimpleName() + ": mode unspec: " +(mode==MeasureSpec.UNSPECIFIED));
		if (getContext() instanceof DayView) {
			width += specWidth;
		}
		else if (getContext() instanceof WeekView) {
			float div = (app.getPrefs().isSaturdayEnabled() ? 6 : 5);
			int myWidth = (int) ((display.getWidth() / div)-(app.getPrefs().isTimegridEnabled()?20f/div:0));
			width += myWidth;

		}
		setMeasuredDimension(width, height);
		// Log.d("Philip", "measure width: " +widthMeasureSpec +" height: " +heightMeasureSpec +" spec: " +MeasureSpec.AT_MOST);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float ecke = 7*scale;
		Paint paint = new Paint();
		Rect maxRect = new Rect(getPaddingLeft(), getPaddingTop(), getMeasuredWidth() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom());
		RectF maxRectF = new RectF(maxRect);
		Resources res = getContext().getResources();
		
		
		
		paint.setColor(bgColor);
		paint.setStyle(Style.FILL);
		canvas.drawRect(maxRect, paint);
		//canvas.drawRoundRect(maxRectF, ecke, ecke, paint);
		
		paint.setColor(app.getPrefs().getBorderColor());
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(3);
		canvas.drawRect(maxRect, paint);
		//canvas.drawRoundRect(maxRectF, ecke, ecke, paint);

		
		
		paint = new Paint();
		if (getContext() instanceof DayView) {
			paint.setTextSize(res.getDimension(R.dimen.dayTextSize));
		}
		else if (getContext() instanceof WeekView) {
			paint.setTextSize(res.getDimension(R.dimen.weekTextSize));
		}
		else if (getContext() instanceof MonthView) {
			paint.setTextSize(res.getDimension(R.dimen.monthTextSize));
		}

		paint.setAntiAlias(true);

		if (lesson != null) {
			// Log.d("Philip", getClass().getSimpleName() + ": " +lesson);
			LessonCode lsCode = lesson.getLessonCode();
			
			if (lsCode instanceof LessonCodeCancelled) {
				paint.setStrikeThruText(true);
			}

			int y = (int) (getPaddingTop() + 15 * scale);

			if (!(viewType instanceof SchoolClass)) {
				y = drawList(paint, canvas, y, lesson.getSchoolClasses());
			}
			if (!(viewType instanceof SchoolTeacher)) {
				y = drawList(paint, canvas, y, lesson.getSchoolTeachers());
			}
			if (!(viewType instanceof SchoolRoom)) {
				y = drawList(paint, canvas, y, lesson.getSchoolRooms());
			}
			if (!(viewType instanceof SchoolSubject)) {
				y = drawList(paint, canvas, y, lesson.getSchoolSubjects());
			}

		}
		else {
			paint.setColor(Color.RED);
			canvas.drawText(getContext().getString(R.string.lessonview_error), 10, 20, paint);
		}
	}

	/**
	 * Wenn LessonCode und LessonType gesetzt sind, wird die Vordergrundfarbe des LessonCodes verwendet.
	 * @param paint
	 * @param canvas
	 * @param y
	 * @param list
	 * @return
	 */
	private int drawList(Paint paint, Canvas canvas, int y, List<? extends ViewType> list) {
		if (list.size() > 0) {
			int ypadding = (int) (4 * scale);
			String name = "";
			for (int i = 0; i < list.size(); i++) {
				ViewType clas = list.get(i);
				
				// Wenn LessonCode und LessonType gesetzt sind, zieht hier der LessonCode
				paint.setColor(lesson.getLessonType() == null ? clas.getForeColor() : lesson.getLessonType().getFgColor());
				paint.setColor(lesson.getLessonCode() == null ? paint.getColor() : lesson.getLessonCode().getFgColor());
				
				String add = "";
				if (name.equals("")) {
					add = clas.getName();
				}
				else {
					add = ", " + clas.getName();
				}
				Rect textSize = new Rect();
				paint.getTextBounds(name + add, 0, name.length() + add.length(), textSize);
				if (!(textSize.width() > getWidth())) {
					name += add;
				}
				else {
					if (name.equals("")) {
						name = trimString(add, getWidth() - 2, paint);
					}
					Paint kr = new Paint();
					kr.setColor(Color.RED);
					kr.setStyle(Style.FILL);
					canvas.drawCircle(5, 5, 3, kr);
					break;
				}
			}
			// name = trimString(name, getWidth(), paint);
			Rect textSize = new Rect();
			paint.getTextBounds(name, 0, name.length(), textSize);
			int x = getPaddingLeft() + ((getMeasuredWidth() / 2) - (textSize.width() / 2));
			canvas.drawText(name, x, y, paint);

			// versuch die hintergrundfarbe zu zeichnen, aber das waere meistens weis auf weis
			// paint.setColor(list.get(0).getBackColor());
			// textSize.set(x, y, x+textSize.width(), y+textSize.height());
			// canvas.drawRect(textSize, paint);
			y += textSize.height() + ypadding;
		}
		return y;
	}

	private String trimString(String str, int length, Paint paint) {
		Rect textSize = new Rect();
		for (int i = str.length(); i > 0; i--) {
			paint.getTextBounds(str, 0, i, textSize);
			if (textSize.width() < length) {
				return str.substring(0, i);
			}
		}

		return "";
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// Log.d("Philip", "x: " +event.getX() +", y: " +event.getY());
		float x = event.getX();
		float y = event.getY();

		SchoolPlannerApp app = ((SchoolPlannerApp) ((ViewActivity) getContext()).getApplication());
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (x > getPaddingLeft() && x < getMeasuredWidth() - getPaddingRight() && y > getPaddingTop() && y < getMeasuredHeight() - getPaddingBottom()) {
				Log.d("Philip", "viewlesson down");
				bgColor = app.getPrefs().getTouchColor();
				invalidate();
			}
		}
		else {
			if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
				Log.d("Philip", "viewlesson up");
				bgColor = getBGColor();
				invalidate();
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected ContextMenuInfo getContextMenuInfo() {
		ViewLessonContextInfo ci = new ViewLessonContextInfo(getLesson());
		return ci;
	}

	/**
	 * Wenn LessonCode und LessonType gesetzt sind, zieht hier die Farbe des LessonCodes.
	 * @return
	 */
	protected int getBGColor() {
		if (lesson != null) {
			// Log.d("Philip", getClass().getSimpleName() + ": lesson: " +lesson);
			LessonCode lessonCode = lesson.getLessonCode();
			LessonType lessonType = lesson.getLessonType();
			
			// Wenn LessonCode und LessonType gesetzt sind, zieht hier der LessonCode
			
			if(lessonCode != null) {
				if (lessonCode instanceof LessonCodeSubstitute) {
					SchoolTeacher originSchoolTeacher = ((LessonCodeSubstitute) lessonCode).getOriginSchoolTeacher();
					if(originSchoolTeacher != null && originSchoolTeacher.getId() == viewType.getId()) {
						return new LessonCodeCancelled().getBgColor();
					}
				}
				return lessonCode.getBgColor();
			}
			else if(lessonType != null) {
				return lessonType.getBgColor();
			}
		}
		return app.getPrefs().getBgColor();
	}
}
