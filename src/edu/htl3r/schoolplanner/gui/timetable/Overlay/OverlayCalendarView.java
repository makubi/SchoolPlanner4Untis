package edu.htl3r.schoolplanner.gui.timetable.Overlay;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
	private TextPaint tp;

	private ArrayList<ArrayList<Integer>> buffer = new ArrayList<ArrayList<Integer>>();

	public OverlayCalendarView(Context context) {
		super(context);

		tp = new TextPaint();
		tp.setStrokeWidth(2);
		tp.setAntiAlias(true);
		tp.setTextSize(25);
		tp.setColor(Color.WHITE);
		setOnTouchListener(this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getMeasuredWidth();
		colwidth = width / 8;
		rowheight = colwidth;
		height = rowheight * 7;
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paintFirstRow(canvas);
		paintDays(canvas);
		paintWeekNumber(canvas);
	}

	private void paintFirstRow(Canvas canvas) {
		tp.setColor(Color.parseColor("#4a494a"));
		tp.setStrokeWidth(2);

		String title[] = getResources().getStringArray(R.array.timetable_overlay_month_header_name);
		canvas.translate(0, 15);
		for (int i = 0; i < title.length; i++) {
			StaticLayout s = new StaticLayout(title[i], tp, colwidth, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
			s.draw(canvas);
			canvas.translate(colwidth, 0);
		}
		canvas.translate(-(colwidth * title.length), -15);
		canvas.drawLine(0, rowheight, width, rowheight, tp);
	}

	private void paintDays(Canvas canvas) {

		canvas.save();
		canvas.translate(getOffsetFaktor() * colwidth, 15 + rowheight);
		DateTime tmp = firstDay.clone();
		StaticLayout s = null;

		tp.setColor(Color.WHITE);
		tp.setStrokeWidth(3);
		int count = 0;
		boolean firstrow = true;
		ArrayList<Integer> line = new ArrayList<Integer>();
		while (tmp.getMonth() == firstDay.getMonth()) {

			if (tmp.getWeekDay() == Time.SUNDAY) {
				s = new StaticLayout(tmp.getDay() + "", tp, colwidth, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
				s.draw(canvas);

				if(firstrow){
					for(int i=line.size(); i<6; i++)
						line.add(0,DEAD_DAY);
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
				s = new StaticLayout(tmp.getDay() + "", tp, colwidth, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
				s.draw(canvas);
				canvas.translate(colwidth, 0);
				count++;
			}

			tmp.increaseDay();
		}

		for (int i = line.size(); i < 7; i++)
			line.add(DEAD_DAY);
		buffer.add(line);

		canvas.restore();
	}

	private void paintWeekNumber(Canvas canvas) {

		tp.setColor(Color.parseColor("#4a494a"));
		tp.setStrokeWidth(2);

		DateTime tmp = firstDay.clone();
		canvas.save();

		canvas.translate(0, rowheight + 15);
		int weeknumber = -1;
		StaticLayout s = null;

		while (tmp.getMonth() == firstDay.getMonth()) {
			if (weeknumber != tmp.getAndroidTime().getWeekNumber()) {
				weeknumber = tmp.getAndroidTime().getWeekNumber();
				s = new StaticLayout(weeknumber + "", tp, colwidth, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
				s.draw(canvas);
				canvas.translate(0, rowheight);
			}
			tmp.increaseDay();
		}

		printBuffer();
		canvas.restore();
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

	public void setFirstDay(DateTime d) {
		firstDay = d;
		buffer.clear();
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
					Log.d("basti", "spalte: " + x + " zeile: " + y);
					Log.d("basti", "Tag: " + (buffer.get((int) y - 1)).get((int) x - 1));
				}
			}

		}
		return false;
	}

	private void printBuffer() {
		String tmp ="";
		for (ArrayList<Integer> in : buffer) {
			for (int j : in) {
				tmp+=j + " ";
			}
			Log.d("basti",tmp);
			tmp="";
		}
	}
}