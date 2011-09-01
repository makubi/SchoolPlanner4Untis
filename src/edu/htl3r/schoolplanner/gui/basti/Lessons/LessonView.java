package edu.htl3r.schoolplanner.gui.basti.Lessons;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUILessonContainer;

public class LessonView extends View{

	private GUILessonContainer lesson;
	private Paint paint;
	private int width,height;
	private String name;
	
	private DateTime time;
	
	public LessonView(Context context) {
		super(context);
		
		paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStrokeWidth(5);
		paint.setStyle(Style.FILL);
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		canvas.drawText(getTime().toString(), 8, 10, paint);
		canvas.drawText(lesson.getStart().toString(), 8, 25, paint);
		canvas.drawText(lesson.getEnd().toString(), 8, 40, paint);
		
		if(lesson.getLessonsCount() == 1){
			List<SchoolSubject> schoolSubjects = lesson.getLessons().get(0).getSchoolSubjects();
			if(schoolSubjects.size() != 0){
				SchoolSubject schoolClass = schoolSubjects.get(0);
				canvas.drawText(schoolClass.getName(), 8, 55, paint);
				canvas.drawText(schoolClass.getLongName(), 8, 70, paint);

			}
		}
	}
	
	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}
	
	public void setLesson(GUILessonContainer l){
		lesson = l;
		time = lesson.getDate();
	}

	public DateTime getTime() {
		return time;
	}
	
}