package edu.htl3r.schoolplanner.gui.basti.Week;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.ViewGroup;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.basti.Eyecandy.WeekHeader;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIDay;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUILessonContainer;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIWeek;
import edu.htl3r.schoolplanner.gui.basti.Lessons.LessonView;

public class WeekLayout extends ViewGroup {

	private final int HEADER_HEIGHT = 80;
	private final int BORDERWIDTH = 2;

	private GUIWeek weekdata;

	private Paint paint;

	private int width, height;
	private int days, hours;
	private float widthlesson, heightlesson;
	private Context context;

	private WeekHeader weekheader;

	public WeekLayout(Context context) {
		super(context);
		this.context = context;
		weekheader = new WeekHeader(context);
		initDrawingStuff();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		for (int i = getChildCount() - 1; i >= 0; i--) {
			drawChild(canvas, getChildAt(i), 0);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		zeichneGatter(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		widthlesson = width / days;
		heightlesson = (widthlesson / 5) * 4;
		height = (int) (heightlesson * hours) + HEADER_HEIGHT;


		for (int i = 0; i < getChildCount(); i++) {
			GUIView c = (GUIView) getChildAt(i);

			switch (c.getId()) {
			case GUIView.LESSON_ID:
				measureChild(c, MeasureSpec.makeMeasureSpec((int) widthlesson - 4, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) heightlesson - 4, MeasureSpec.EXACTLY));
				break;
			case GUIView.HEADER_ID:
				measureChild(c, MeasureSpec.makeMeasureSpec((int) width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) HEADER_HEIGHT, MeasureSpec.EXACTLY));
				break;
			}
		}
		this.setMeasuredDimension(width, height);

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		int l = 0;
		int t = HEADER_HEIGHT;
		int r = (int) (l + widthlesson);
		int b = (int) (t + heightlesson);
		DateTime now = null;
		DateTime old = null;

		for (int i = 0; i < getChildCount(); i++) {

			GUIView gv = (GUIView) getChildAt(i);

			switch (gv.getId()) {
			case GUIView.LESSON_ID:
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
				break;

			case GUIView.HEADER_ID:
				gv.layout(0, 0, width, HEADER_HEIGHT);
				break;
			}

		}
	}

	private void zeichneGatter(Canvas canvas) {
		int tposx = 0, tposy = HEADER_HEIGHT;
		
		for (int i = 0; i < days - 1; ++i) {
			tposx += widthlesson;
			canvas.drawLine(tposx, tposy, tposx, height, paint);
		}

		tposx = 0;
		tposy = HEADER_HEIGHT;
		for (int i = 0; i < hours - 1; i++) {
			tposy += heightlesson;
			canvas.drawLine(0, tposy, width, tposy, paint);
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
		weekheader.setMonday(datum.get(0));
		this.addView(weekheader);

		for (int i = 0; i < datum.size(); i++) {
			GUIDay day = week.getDay(datum.get(i));

			ArrayList<DateTime> sortDates = day.getSortDates();

			for (int j = 0; j < sortDates.size(); j++) {
				GUILessonContainer lessonsContainer = day.getLessonsContainer(sortDates.get(j));
				LessonView lv = new LessonView(context);
				lv.setNeededData(lessonsContainer, week.getViewType());
				this.addView(lv);
			}
		}
	}

}
