package edu.htl3r.schoolplanner.gui.basti.Week;

import java.util.ArrayList;
import java.util.Iterator;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.ViewGroup;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIDay;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUILessonContainer;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIWeek;
import edu.htl3r.schoolplanner.gui.basti.Lessons.LessonView;

public class WeekView extends ViewGroup {

	private GUIWeek weekdata;

	private Paint paint;

	private int width, height;
	private int days, hours;
	private float widthlesson, heightlesson;
	private Context context;
	private final int border = 4;

	public WeekView(Context context) {
		super(context);
		this.context = context;
		initDrawingStuff();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		for (int i = getChildCount() - 1; i >= 0; i--) {
			drawChild(canvas, getChildAt(i), 0);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		zeichneGatter(canvas);
		super.onDraw(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		widthlesson = width / days;
		heightlesson = (widthlesson / 5) * 4;
		setMeasuredDimension(width, 1500);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {

		int l = 0;
		int t = 0;
		int r = (int) (l + widthlesson);
		int b = (int) (t + heightlesson);
		DateTime now = null;
		DateTime old = null;
		for (int i = 0; i < getChildCount(); i++) {

			LessonView c = (LessonView) getChildAt(i);

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
					t = 0;
					b = (int) (t + heightlesson);
				}
			}

			c.measure(MeasureSpec.makeMeasureSpec((int) widthlesson - 4,
					MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
					(int) heightlesson - 4, MeasureSpec.EXACTLY));
			c.layout(l + 2, t + 2, r - 2, b - 2);

		}
	}

	private void zeichneGatter(Canvas canvas) {
		int tposx = 0, tposy = 0;
		for (int i = 0; i < days - 1; i++) {
			tposx += widthlesson;
			canvas.drawLine(tposx, 0, tposx, height, paint);
		}

		tposx = 0;
		tposy = 0;
		for (int i = 0; i < hours - 1; i++) {
			tposy += heightlesson;
			canvas.drawLine(0, tposy, width, tposy, paint);
		}
	}

	private void initDrawingStuff() {
		setBackgroundColor(getResources().getColor(R.color.background_light));
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(border);
		paint.setStyle(Style.STROKE);
	}

	public void setWeekData(GUIWeek week) {
		weekdata = week;
		days = weekdata.getCountDays();
		hours = weekdata.getMaxHours();

		ArrayList<DateTime> datum = weekdata.getSortDates();

		for (int i = 0; i < datum.size(); i++) {
			GUIDay day = week.getDay(datum.get(i));

			ArrayList<DateTime> sortDates = day.getSortDates();

			for (int j = 0; j < sortDates.size(); j++) {
				GUILessonContainer lessonsContainer = day
						.getLessonsContainer(sortDates.get(j));
				LessonView lv = new LessonView(context);
				lv.setLesson(lessonsContainer);
				this.addView(lv);
			}
		}

	}

}
