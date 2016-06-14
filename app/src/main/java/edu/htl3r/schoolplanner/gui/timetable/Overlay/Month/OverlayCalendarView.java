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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;

public class OverlayCalendarView extends View implements OnTouchListener {

	private final int DEAD_DAY = -1;

	private DateTime firstDay;
	private int width, height;
	private int colwidth, rowheight;
	private int textpadding;
	
	private TextPaint tp;
	private OverlayMonth overlaymonth;
	
	private List<Integer> holidays;

	private int highx, highy;
	private boolean showhighlight = false;

	private ArrayList<ArrayList<Integer>> buffer = new ArrayList<ArrayList<Integer>>();
	
	private Resources resources = getResources();

	public OverlayCalendarView(Context context, OverlayMonth om) {
		super(context);
		overlaymonth = om;
		tp = new TextPaint();
		tp.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_2));
		tp.setAntiAlias(true);
		tp.setTextSize(getResources().getDimension(R.dimen.gui_overlay_month_text_size));
		tp.setColor(Color.WHITE);
		setOnTouchListener(this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getMeasuredWidth();
		height = getMeasuredHeight();
		
		colwidth = width / 8;
		
		if(height < width){
			rowheight = height/7;
		}else{
			rowheight = colwidth;
			height = rowheight * 7;
		}
		
		int txtsize = getResources().getDimensionPixelSize(R.dimen.gui_overlay_month_text_size);
		textpadding = (rowheight - txtsize)/2;
		
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (showhighlight) {
			showhighlight = false;
			showHighLight(highx, highy, canvas);
		}
		
		canvas.save();
		paintFirstRow(canvas);
		canvas.restore();

		
		canvas.save();
		paintDays(canvas);
		canvas.restore();
		
		canvas.save();
		paintWeekNumber(canvas);
		canvas.restore();

	}

	private void paintFirstRow(Canvas canvas) {
		tp.setColor(resources.getColor(R.color.month_overlay_legend));
		
		String title[] = getResources().getStringArray(R.array.timetable_overlay_month_header_name);
		for (int i = 0; i < title.length; i++) {
			StaticLayout s = new StaticLayout(title[i], tp, colwidth, Layout.Alignment.ALIGN_CENTER, 1, 0, false);
			s.draw(canvas);
			canvas.translate(colwidth, 0);
		}
		canvas.translate(-(colwidth * title.length), -textpadding);
		canvas.drawLine(0, rowheight, width, rowheight, tp);
	}

	private void showHighLight(int x, int y, Canvas canvas) {

		tp.setColor(resources.getColor(R.color.month_overlay_on_touch));
		tp.setStyle(Style.FILL);
		RectF r = null;
		
		if(colwidth == rowheight){
			r = new RectF(x*colwidth, y*rowheight, (x+1)*colwidth, (y+1)*rowheight);
		} else if(colwidth > rowheight){
			r = new RectF((x*colwidth) + ((colwidth/2) - (rowheight/2)) , y*rowheight, (x*colwidth) + ((colwidth/2) + (rowheight/2)), (y+1)*rowheight);
		}
		
		canvas.drawArc(r, 0, 360, true, tp);
		
	}

	private void paintDays(Canvas canvas) {

		canvas.translate(getOffsetFaktor() * colwidth, textpadding + rowheight);
		DateTime tmp = firstDay.clone();

		tp.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_3));
		
		int count = 0;
		boolean firstrow = true;
		ArrayList<Integer> line = new ArrayList<Integer>();
		while (tmp.getMonth() == firstDay.getMonth()) {

			if (tmp.getWeekDay() == Time.SUNDAY) {
	
				holidays.add(tmp.getDay());
				paintDay(tmp.getDay(), tp, canvas);
				
				if (firstrow) {
					for (int i = line.size(); i < 6; i++)
						line.add(0, DEAD_DAY);
				}
				line.add(tmp.getDay());
				buffer.add(line);
				line = new ArrayList<Integer>();

				if (!firstrow) {
					canvas.translate(-(colwidth * count), rowheight);
				} else {
					canvas.translate(-(colwidth * (count + getOffsetFaktor() - 1)), rowheight);
					firstrow = false;
				}
				count = 0;

			} else {

				line.add(tmp.getDay());
				paintDay(tmp.getDay(), tp, canvas);
				canvas.translate(colwidth, 0);
				count++;
			}

			tmp.increaseDay();
		}

		for (int i = line.size(); i < 7; i++)
			line.add(DEAD_DAY);
		buffer.add(line);
		tp.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_2));
	}
	
	private void paintDay(int day, TextPaint tp, Canvas canvas){
		StaticLayout s = null;
		s = new StaticLayout(day + "", tp, colwidth, Layout.Alignment.ALIGN_CENTER, 1, 0, false);

		DateTime today = new DateTime();
		today.getAndroidTime().setToNow();
		
		if(firstDay.getYear() == today.getYear() && firstDay.getMonth() == today.getMonth() && day == today.getDay()) {
			tp.setColor(resources.getColor(R.color.month_overlay_today));
			s.draw(canvas, null, tp, 5);
		}
		else if(holidays.contains(day)){
			tp.setColor(resources.getColor(R.color.month_overlay_holiday));
			s.draw(canvas, null, tp, 5);
		}else{
			tp.setColor(resources.getColor(R.color.month_overlay_day));
			s.draw(canvas);	
		}
		
		
	}

	private void paintWeekNumber(Canvas canvas) {

		tp.setColor(resources.getColor(R.color.month_overlay_legend));
		tp.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_3));
		
		DateTime tmp = firstDay.clone();

		canvas.translate(0, rowheight + textpadding);
		int weeknumber = -1;
		StaticLayout s = null;

		while (tmp.getMonth() == firstDay.getMonth()) {
			if (weeknumber != tmp.getAndroidTime().getWeekNumber()) {
				weeknumber = tmp.getAndroidTime().getWeekNumber();
				s = new StaticLayout(weeknumber + "", tp, colwidth, Layout.Alignment.ALIGN_CENTER, 1, 0, false);
				s.draw(canvas);
				canvas.translate(0, rowheight);
			}
			tmp.increaseDay();
		}
	}

	private int getOffsetFaktor() {

		int calc = 1;
		switch (firstDay.getWeekDay()) {
		case Time.MONDAY:
			calc = 1;
			break;
		case Time.TUESDAY:
			calc = 2;
			break;
		case Time.WEDNESDAY:
			calc = 3;
			break;
		case Time.THURSDAY:
			calc = 4;
			break;
		case Time.FRIDAY:
			calc = 5;
			break;
		case Time.SATURDAY:
			calc = 6;
			break;
		case Time.SUNDAY:
			calc = 7;
			break;
		}
		return calc;
	}

	public void setFirstDay(DateTime d, List<Integer> holidays) {
		firstDay = d;
		buffer.clear();
		this.holidays = holidays;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x, y;
			x = event.getX() - colwidth;
			y = event.getY() - rowheight;
			Log.d("basti", "x: " + x + " y: " + y);
			if (x > 0 && y > 0) {
				x = (float) Math.ceil(x / colwidth);
				y = (float) Math.ceil(y / rowheight);
				if (y < buffer.size() + 1 && x < 8) {
					if ((buffer.get((int) y - 1)).get((int) x - 1) != DEAD_DAY)
						showhighlight = true;
					highx = (int) x;
					highy = (int) y;
					invalidate();
					
					overlaymonth.displayChoosenWeek((buffer.get((int) y - 1)).get((int) x - 1));
				}
			}

		}
		return false;
	}

	@SuppressWarnings("unused")
	private void printBuffer() {
		String tmp = "";
		for (ArrayList<Integer> in : buffer) {
			for (int j : in) {
				tmp += j + " ";
			}
			Log.d("basti", tmp);
			tmp = "";
		}
	}
}