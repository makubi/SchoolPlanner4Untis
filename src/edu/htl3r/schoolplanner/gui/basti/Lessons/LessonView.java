package edu.htl3r.schoolplanner.gui.basti.Lessons;

import java.util.ArrayList;
import java.util.List;

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
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUILessonContainer;
import edu.htl3r.schoolplanner.gui.basti.Week.GUIWeekView;

public class LessonView extends GUIWeekView {

	private GUILessonContainer lessoncontainer;
	private Paint paint;
	private int width, height;
	private String name;
	private ViewType viewtype;

	private DateTime time;
	
	private boolean background = false;

	public LessonView(Context context) {
		super(context);

		setID(LESSON_ID);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(5);
		paint.setStyle(Style.FILL);
		paint.setTextSize(25);
		paint.setAntiAlias(true);

	}

	public String getName() {
		return name;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		

		
		List<Lesson> lessons = lessoncontainer.getLessons();

		ArrayList<String> firstline = new ArrayList<String>();
		ArrayList<String> secondline = new ArrayList<String>();

		List<? extends ViewType> vtfirstline = null;
		List<? extends ViewType> vtsecondline = null;

		
		for (Lesson l : lessons) {
			
			if (viewtype instanceof SchoolClass) {
				vtfirstline = l.getSchoolSubjects();
				vtsecondline = l.getSchoolTeachers();
			} else if (viewtype instanceof SchoolTeacher) {
				vtfirstline = l.getSchoolClasses();
				vtsecondline = l.getSchoolSubjects();
			} else if (viewtype instanceof SchoolRoom) {
				vtfirstline = l.getSchoolClasses();
				vtsecondline = l.getSchoolTeachers();
			} else if (viewtype instanceof SchoolSubject) {
				vtfirstline = l.getSchoolTeachers();
				vtsecondline = l.getSchoolClasses();
			}
						
			for (ViewType s : vtfirstline) {
				if(!firstline.contains(s.getName()))
					firstline.add(s.getName());
			}
			for (ViewType s : vtsecondline) {
				if(!secondline.contains(s.getName()))
					secondline.add(s.getName());
			}
			
			
		}
		
	
		TextPaint tp = new TextPaint(paint);
		tp.setTypeface(Typeface.DEFAULT_BOLD); 
		String line1 = prepareListForDisplay(firstline,tp);
			

		canvas.drawText(line1, 5, 25, tp);
		
		tp.setTextSize(18);
		tp.setTypeface(Typeface.DEFAULT);
		String line2 = prepareListForDisplay(secondline,tp);

		canvas.drawText(line2, 5, 50, tp);
	}
	
	private String prepareListForDisplay(ArrayList<String> input, TextPaint tp){
		StringBuilder sb = new StringBuilder();
		String tmp = "";

		for (String c : input) {
			tmp = c + sb.toString() + ", ";

			if (StaticLayout.getDesiredWidth(tmp, tp) > width) {
				if(StaticLayout.getDesiredWidth(sb.toString()+" ...", tp) < width){
					sb.append(" ...");
				}else{
					sb.append(" .");
				}
				break;
			}

			if (sb.length() == 0)
				sb.append(c);
			else
				sb.append(", " + c);
		}
		return sb.toString();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	public void setNeededData(GUILessonContainer l, ViewType vt) {
		lessoncontainer = l;
		viewtype = vt;
		time = lessoncontainer.getDate();
		__setBackgroundColor();
	}

	private void __setBackgroundColor() {
		List<Lesson> lessons = lessoncontainer.getLessons();
		List<? extends ViewType> vt = null;

		if (lessons.size() != 0) {

			if (viewtype instanceof SchoolClass) {
				vt = lessons.get(0).getSchoolSubjects();
			} else if (viewtype instanceof SchoolTeacher) {
				vt = lessons.get(0).getSchoolClasses();
			} else if (viewtype instanceof SchoolRoom) {
				vt = lessons.get(0).getSchoolClasses();
			} else if (viewtype instanceof SchoolSubject) {
				vt = lessons.get(0).getSchoolTeachers();
			}
			
			
			
			if (vt.size() != 0) {
				String bcolor = vt.get(0).getBackColor();
				if (!bcolor.equalsIgnoreCase("")) {
					setBackgroundColor(Color.parseColor("#55"+bcolor));
				}
			}
		}
	}

	public DateTime getTime() {
		return time;
	}

}

// RoundRectShape rrs = new RoundRectShape(radiout, new RectF(1, 1, 1, 1),
// radiin);
// Log.d("basti",color);
// LessonShape ls = new
// LessonShape(rrs,Color.parseColor("#"+color),getResources().getColor(R.color.background_stundenplan));

// TODO setBackgroundDrawable ruft anscheinend invalidate auf --> onDraw wird
// erneut ausgefuehrt
// setBackgroundDrawable(ls);
// float [] radiin = {5,5,5,5,5,5,5,5};
// float [] radiout = {0,0,0,0,0,0,0,0};
