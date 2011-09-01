package edu.htl3r.schoolplanner.gui.basti.Lessons;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.Log;
import android.view.View;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUILessonContainer;

public class LessonView extends View{

	private GUILessonContainer lessoncontainer;
	private Paint paint;
	private int width,height;
	private String name;
	private ViewType viewtype;
	
	private DateTime time;
	
	public LessonView(Context context) {
		super(context);
		
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(5);
		paint.setStyle(Style.FILL);
		paint.setTextSize(20);
		paint.setAntiAlias(true);
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		List<Lesson> lessons = lessoncontainer.getLessons();
		String color = "";
		float [] radiin = {5,5,5,5,5,5,5,5};
		float [] radiout = {0,0,0,0,0,0,0,0};
		
		for(Lesson l : lessons){
			
			if(viewtype instanceof SchoolClass){
				List<SchoolSubject> schoolClasses = l.getSchoolSubjects();
				if(schoolClasses.size() != 0){
					color = schoolClasses.get(0).getBackColor();
				}
				RoundRectShape rrs = new RoundRectShape(radiout, new RectF(1, 1, 1, 1), radiin);
				Log.d("basti",color);
				LessonShape ls = new LessonShape(rrs,Color.parseColor("#"+color),getResources().getColor(R.color.background_stundenplan));
				setBackgroundDrawable(ls);
				
				String blub = "";
				for(SchoolSubject s : schoolClasses){
					blub+=s.getName()+ " ";
				}
				
				canvas.drawText(blub,8, (height/2)+10,paint);
				
			}
			if(viewtype instanceof SchoolRoom){
			}
			if(viewtype instanceof SchoolSubject){
			}
			if(viewtype instanceof SchoolTeacher){
				List<SchoolClass> schoolClasses = l.getSchoolClasses();
				if(schoolClasses.size() != 0){
					color = schoolClasses.get(0).getBackColor();
				}
				RoundRectShape rrs = new RoundRectShape(radiout, new RectF(1, 1, 1, 1), radiin);
				Log.d("basti","teacher : " +color);
				LessonShape ls = new LessonShape(rrs,Color.parseColor("#"+color),getResources().getColor(R.color.background_stundenplan));
				setBackgroundDrawable(ls);
				
				String blub = "";
				for(SchoolClass s : schoolClasses){
					blub+=s.getName()+ " ";
				}
				
				canvas.drawText(blub,8, (height/2)+10,paint);
			}
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
		lessoncontainer = l;
		time = lessoncontainer.getDate();
		
//		if(l.getLessonsCount() != 0){
//			setBackgroundResource(R.drawable.border_lesson);
//		}
		
	}

	public DateTime getTime() {
		return time;
	}
	
	public void setViewType(ViewType vt){
		viewtype = vt;
	}
	
}