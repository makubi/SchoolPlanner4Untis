package edu.htl3r.schoolplanner.gui.basti.Eyecandy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.basti.Week.GUIWeekView;

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
		canvas.drawLine(width, 0, width-3, height, paint);
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
		tp.setStyle(Style.FILL_AND_STROKE);
		tp.setTextSize(getResources().getDimension(R.dimen.gui_header_line1_size));
		canvas.translate(0, offsettop+(((height-offsettop)/hours)/4));
		int padding_right = getResources().getDimensionPixelSize(R.dimen.gui_timegrid_padding_right);
		
		for(int i=0; i<hours; i++){
		//	canvas.drawText(i+"", 5, 10, tp);
			StaticLayout s = new StaticLayout(i+"", tp, width-padding_right, Layout.Alignment.ALIGN_OPPOSITE, 0, 0, false);
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
