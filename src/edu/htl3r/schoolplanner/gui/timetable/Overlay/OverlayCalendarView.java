package edu.htl3r.schoolplanner.gui.timetable.Overlay;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
	private TextPaint tp;
	private OverlayMonth overlaymonth;
	
	private List<Integer> holidays;

	private int highx, highy;
	private boolean showhighlight = false;

	private ArrayList<ArrayList<Integer>> buffer = new ArrayList<ArrayList<Integer>>();

	public OverlayCalendarView(Context context, OverlayMonth om) {
		super(context);
		overlaymonth = om;
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

		canvas.save();

		if (showhighlight) {
			showhighlight = false;
			showHighLight(highx, highy, canvas);
		}

		canvas.restore();

		paintFirstRow(canvas);
		paintDays(canvas);
		paintWeekNumber(canvas);

		canvas.restore();

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

	private void showHighLight(int x, int y, Canvas canvas) {

		tp.setColor(Color.parseColor("#2955ce"));
		tp.setStyle(Style.FILL);
		
		RectF r = new RectF(x*colwidth, y*rowheight, (x+1)*colwidth, (y+1)*rowheight);

		canvas.drawArc(r, 0, 360, true, tp);
		
	}

	private void paintDays(Canvas canvas) {

		canvas.save();
		canvas.translate(getOffsetFaktor() * colwidth, 15 + rowheight);
		DateTime tmp = firstDay.clone();

		tp.setColor(Color.WHITE);
		tp.setStrokeWidth(3);
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

		canvas.restore();
	}
	
	private void paintDay(int day, TextPaint tp, Canvas canvas){
		StaticLayout s = null;
		s = new StaticLayout(day + "", tp, colwidth, Layout.Alignment.ALIGN_CENTER, 0, 0, false);

		if(holidays.contains(day)){
			tp.setColor(Color.GREEN);
			s.draw(canvas, null, tp, 5);
		}else{
			tp.setColor(Color.WHITE);
			s.draw(canvas);	
		}
		
		
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

	public void setFirstDay(DateTime d, List<Integer> holidays) {
		firstDay = d;
		buffer.clear();
		this.holidays = holidays;
	}

	@SuppressWarnings("static-access")
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