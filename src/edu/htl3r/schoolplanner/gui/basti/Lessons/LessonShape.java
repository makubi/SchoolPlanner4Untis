package edu.htl3r.schoolplanner.gui.basti.Lessons;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class LessonShape extends ShapeDrawable {

	Paint fillpaint, strokepaint;
	private static final int WIDTH = 4;

	public LessonShape(Shape s, int strokecolor, int fillcolor) {
		super(s);
		
		fillpaint = this.getPaint();
		strokepaint = new Paint();
		
		fillpaint.setStyle(Paint.Style.FILL);
		fillpaint.setColor(fillcolor);
		
		strokepaint.setStyle(Paint.Style.FILL_AND_STROKE);
		strokepaint.setStrokeWidth(WIDTH);
		strokepaint.setColor(strokecolor);
		strokepaint.setAlpha(255);
	}

	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint fillpaint) {
		shape.draw(canvas, fillpaint);
		shape.draw(canvas, strokepaint);
	}

}
