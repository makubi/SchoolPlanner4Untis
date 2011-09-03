package edu.htl3r.schoolplanner.gui.basti.Week;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.R;

public class WeekTimeGrid extends GUIWeekView{

	
	int width,height,hours,offsettop;
	
	private Paint paint;

	
	public WeekTimeGrid(Context context) {
		super(context);
		setID(TIMGRID_ID);
		int color = getResources().getColor(R.color.header_background);
		setBackgroundColor(Color.argb(200, Color.red(color), Color.green(color), Color.blue(color)));
		
		paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(4);
		paint.setColor( getResources().getColor(R.color.background_stundenplan));

	}
	
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		zeichenGatter(canvas);
		zeichenInfos(canvas);
		super.onDraw(canvas);
	}
	
	private void zeichenGatter(Canvas canvas){
		for(int i=offsettop; i<height; i+=((height-offsettop)/hours)){
			canvas.drawLine(0, i, width, i, paint);
		}
		canvas.drawLine(width, 0, width, height, paint);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}
	
	private void zeichenInfos(Canvas canvas){
		paint.setColor( getResources().getColor(R.color.background_stundenplan));
		TextPaint tp = new TextPaint(paint);
		tp.setStrokeWidth(2);
		tp.setTextSize(25);
		canvas.translate(0, offsettop+(((height-offsettop)/hours)/4));
		for(int i=0; i<hours; i++){
			StaticLayout s = new StaticLayout(i+"", tp,width, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
			s.draw(canvas);
			canvas.translate(0, (height-offsettop)/hours);
		}
	}
	
	public void setHours(int h){
		hours = h;
	}
	public void setOffsetTop(int off){
		offsettop = off;
	}

}
