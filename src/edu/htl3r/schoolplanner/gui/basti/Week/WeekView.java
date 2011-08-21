package edu.htl3r.schoolplanner.gui.basti.Week;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.gui.DummyBackend;
import edu.htl3r.schoolplanner.gui.basti.Lessons.LessonView;

public class WeekView extends ViewGroup {

	private DummyBackend data;
	private Paint paint;

	private int width, height;
	private int days, hours;
	private float widthlesson, heightlesson;

	private final int border = 4;

	public WeekView(Context context, AttributeSet attrs) {
		super(context);
		data = new DummyBackend();
		init();
	}

	public WeekView(Context context, DummyBackend d) {
		super(context);
		data = d;
		init();
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
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

		int l = 0;
		int t = 0;
		int r = (int) (l + widthlesson);
		int b = (int) (t + heightlesson);
		for (int i = 0; i < getChildCount(); i++) {

			LessonView c = (LessonView) getChildAt(i);
			c.measure(MeasureSpec.makeMeasureSpec((int) widthlesson - 4, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) heightlesson - 4, MeasureSpec.EXACTLY));

			c.layout(l + 2, t + 2, r - 2, b - 2);
			if ((i % days) == 0 && i != 0) {
				l = 0;
				t += heightlesson;
				b += heightlesson;
				r = (int) (l + widthlesson);
			} else {
				l += widthlesson;
				r = (int) (l + widthlesson);
			}
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

	private void init() {
		setBackgroundColor(getResources().getColor(R.color.background_light));

		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(border);
		paint.setStyle(Style.STROKE);
		days = data.getNonMultipleDummyLessonsForWeek().size();
		hours = 20;

		addView(new LessonView(getContext()));
		
	}

}
