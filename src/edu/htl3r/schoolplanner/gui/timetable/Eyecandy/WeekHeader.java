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
package edu.htl3r.schoolplanner.gui.timetable.Eyecandy;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.timetable.Week.GUIWeekView;

public class WeekHeader extends GUIWeekView {

	private int width, height;
	private int days, lessonwidth;
	private Paint paint;

	private ArrayList<DateTime> datum = new ArrayList<DateTime>();
	private boolean highlight;

	public WeekHeader(Context context, boolean highlight) {
		super(context);
		this.highlight = highlight;

		setID(HEADER_ID);

		// int color = getResources().getColor(R.color.header_background);
		// setBackgroundColor(Color.argb(200, Color.red(color),
		// Color.green(color), Color.blue(color)));

		setBackgroundColor(Color.parseColor("#D3D3D3"));

		paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_2));
		paint.setColor(Color.BLACK);
	}

	public void setMonday(ArrayList<DateTime> dt) {
		datum = dt;
		days = datum.size();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		if (highlight)
			highlightToday(canvas);
		zeichenInfos(canvas);
		canvas.restore();
		zeichenGatter(canvas);
		super.onDraw(canvas);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		lessonwidth = width / days;
		setMeasuredDimension(width, height);
	}

	private void zeichenGatter(Canvas canvas) {
		
		for (int i = lessonwidth; i < width; i += lessonwidth) {
			canvas.drawLine(i, 0, i, height, paint);
		}
		int strokewidth = getResources().getDimensionPixelSize(R.dimen.gui_stroke_width_4);
		Paint p = new Paint(paint);
		p.setStrokeWidth(strokewidth);
		canvas.drawLine(0, 0, 0, height, p);
		canvas.drawLine(0, height-strokewidth/2, width, height-strokewidth/2, p);
	}

	private void zeichenInfos(Canvas canvas) {
		TextPaint tp = new TextPaint(paint);
		tp.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_2));
		tp.setTextSize(getResources().getDimension(R.dimen.gui_header_line1_size));
		tp.setStyle(Style.FILL_AND_STROKE);

		TextPaint tp2 = new TextPaint(paint);
		tp2.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_1));
		tp2.setTextSize(getResources().getDimension(R.dimen.gui_header_line2_size));
		tp2.setTypeface(Typeface.DEFAULT);
		tp2.setStyle(Style.FILL_AND_STROKE);

		int paddint_top = getResources().getDimensionPixelSize(R.dimen.gui_header_paddting_top);
		int padding_bottom = getResources().getDimensionPixelSize(R.dimen.gui_header_line1_line1_padding);
		for (int i = 0; i < days; i++) {
			canvas.translate(0, paddint_top);

			DateTime dateTime = datum.get(i);

			StaticLayout s = new StaticLayout(DateTimeUtils.getShortWeekDayName(dateTime), tp, lessonwidth, Layout.Alignment.ALIGN_CENTER, 1, 0, false);
			s.draw(canvas);
			
			canvas.translate(0, padding_bottom);

			s = new StaticLayout(dateTime.getDay() + "." + dateTime.getMonth() + "." + dateTime.getYear(), tp2, lessonwidth, Layout.Alignment.ALIGN_CENTER, 1,
					0, false);
			s.draw(canvas);
			canvas.translate(lessonwidth, -(padding_bottom + paddint_top));
		}
	}

	private void highlightToday(Canvas canvas) {
		DateTime today = DateTimeUtils.getNow();
		int position = -1;
		for (DateTime d : datum) {
			if (d.getYear() == today.getYear() && d.getMonth() == today.getMonth() && d.getDay() == today.getDay()) {
				position = datum.indexOf(d);
				break;
			}
		}
		if (position != -1) {
			int border = getResources().getDimensionPixelSize(R.dimen.gui_stroke_width_2) / 2;
			Rect r = new Rect(border + lessonwidth * position, 0, (lessonwidth * position) + lessonwidth, height - border);
			Paint p = new Paint();
			p.setStyle(Style.FILL);
			p.setColor(getResources().getColor(R.color.month_overlay_today));
			canvas.drawRect(r, p);
		}
	}

}
