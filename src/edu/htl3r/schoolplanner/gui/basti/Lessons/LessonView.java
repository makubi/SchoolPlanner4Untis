package edu.htl3r.schoolplanner.gui.basti.Lessons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;

public class LessonView extends View{

	private Lesson lesson;
	private Paint paint;
	private int width,height;
	private String name;
	
	public LessonView(Context context) {
		super(context);
		
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAlpha(120);
		paint.setStrokeWidth(5);
		paint.setStyle(Style.FILL);
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
	}
	
	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		Log.d("basti", width + " " + height);
		setMeasuredDimension(width, height);
	}
	
	public void setLesson(Lesson l){
		lesson = l;
	}
	
}