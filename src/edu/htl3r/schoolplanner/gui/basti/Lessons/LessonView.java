package edu.htl3r.schoolplanner.gui.basti.Lessons;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
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
		
	/*	canvas.drawText(getTime().toString(), 8, 10, paint);
		canvas.drawText(lesson.getStart().toString(), 8, 25, paint);
		canvas.drawText(lesson.getEnd().toString(), 8, 40, paint);*/
		
		List<Lesson> lessons = lesson.getLessons();
		
		for(Lesson l : lessons){
			List<SchoolRoom> schoolRooms = l.getSchoolRooms();
			List<SchoolClass> schoolClasses = l.getSchoolClasses();
			List<SchoolSubject> schoolSubjects = l.getSchoolSubjects();
			List<SchoolTeacher> schoolTeachers = l.getSchoolTeachers();
			
			StringBuilder sb  = new StringBuilder();
			for(SchoolClass sc : schoolClasses){
				sb.append(sc.getName() + " ");
			}
			canvas.drawText(sb.toString(), 8, 10, paint);
			sb  = new StringBuilder();
			
			for(SchoolRoom sr : schoolRooms){
				sb.append(sr.getName() + " ");
			}
			canvas.drawText(sb.toString(), 8, 25, paint);
			sb  = new StringBuilder();
			
			for(SchoolSubject ss : schoolSubjects){
				sb.append(ss.getName() + " ");
			}	
			canvas.drawText(sb.toString(), 8, 40, paint);
			sb  = new StringBuilder();
			
			for(SchoolTeacher st : schoolTeachers){
				sb.append(st.getName() + " ");
			}	
			canvas.drawText(sb.toString(), 8, 55, paint);
			sb  = new StringBuilder();

			break;
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