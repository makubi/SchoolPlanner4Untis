package edu.htl3r.schoolplanner.gui.basti.Lessons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
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
		paint.setColor(Color.RED);
		paint.setStrokeWidth(5);
		paint.setStyle(Style.FILL);
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(viewtype instanceof SchoolClass){
		}
		if(viewtype instanceof SchoolRoom){
		}
		if(viewtype instanceof SchoolSubject){
		}
		if(viewtype instanceof SchoolTeacher){
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
	}

	public DateTime getTime() {
		return time;
	}
	
	public void setViewType(ViewType vt){
		viewtype = vt;
	}
	
}