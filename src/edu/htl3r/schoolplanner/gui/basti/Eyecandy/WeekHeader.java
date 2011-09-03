package edu.htl3r.schoolplanner.gui.basti.Eyecandy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.gui.basti.Week.GUIView;

public class WeekHeader extends GUIView{

	private DateTime monday;
	private int width,height;
	private int lessonwidth;
	
	public WeekHeader(Context context) {
		super(context);
		setID(HEADER_ID);
		setBackgroundColor(Color.CYAN);
	}

	public void setMonday(DateTime dt){
		monday = dt;
	}
	
	public void setLessonWidth(int lwidth){
		lessonwidth = lwidth;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}
	
}
