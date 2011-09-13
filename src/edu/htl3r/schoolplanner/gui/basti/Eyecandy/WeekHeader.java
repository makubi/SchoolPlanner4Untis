package edu.htl3r.schoolplanner.gui.basti.Eyecandy;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.basti.Week.GUIWeekView;

public class WeekHeader extends GUIWeekView {

	private int width, height;
	private int days, lessonwidth;
	private Paint paint;

	private String weekdays[] = { "Mo", "Di", "Mi", "Do", "Fr", "Sa" };
	private ArrayList<DateTime> datum = new ArrayList<DateTime>();

	public WeekHeader(Context context) {
		super(context);
		setID(HEADER_ID);

		int color = getResources().getColor(R.color.header_background);
		setBackgroundColor(Color.argb(200, Color.red(color), Color.green(color), Color.blue(color)));

		paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(4);
	}

	public void setMonday(ArrayList<DateTime> dt) {
		datum = dt;
		days = datum.size();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		zeichenGatter(canvas);
		zeichenInfos(canvas);
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
		for (int i = 0; i < width; i += lessonwidth) {
			canvas.drawLine(i, 0, i, height, paint);
		}
		canvas.drawLine(0, height, width, height, paint);
	}

	private void zeichenInfos(Canvas canvas) {
		paint.setColor(getResources().getColor(R.color.background_stundenplan));
		TextPaint tp = new TextPaint(paint);
		tp.setStrokeWidth(2);
		tp.setTextSize(getResources().getDimension(R.dimen.gui_header_line1_size));
		tp.setStyle(Style.FILL_AND_STROKE);
		
		TextPaint tp2 = new TextPaint(paint);
		tp2.setStrokeWidth(1);
		tp2.setTextSize(getResources().getDimension(R.dimen.gui_header_line2_size));
		tp2.setTypeface(Typeface.DEFAULT);
		tp2.setStyle(Style.FILL_AND_STROKE);
		
		int paddint_top = getResources().getDimensionPixelSize(R.dimen.gui_header_paddting_top);
		int padding_bottom = getResources().getDimensionPixelSize(R.dimen.gui_header_line1_line1_padding);
		for (int i = 0; i < days; i++) {
			canvas.translate(0, paddint_top);
			
			StaticLayout s = new StaticLayout(weekdays[i], tp, lessonwidth, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
			s.draw(canvas);
			canvas.translate(0, padding_bottom);

			s = new StaticLayout(datum.get(i).getDay() + "." + datum.get(i).getMonth() + "." + datum.get(i).getYear(), tp2, lessonwidth, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
			s.draw(canvas);
			canvas.translate(lessonwidth, -(padding_bottom+paddint_top));
		}
	}

}
