package edu.htl3r.schoolplanner.gui.timetableLessons;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUILessonContainer;
import edu.htl3r.schoolplanner.gui.timetable.Week.GUIWeekView;

public class LessonView extends GUIWeekView {

	private GUILessonContainer lessoncontainer;
	private Paint paint;
	private int width, height;
	private ViewType viewtype;

	private DateTime time;
	

	public LessonView(Context context) {
		super(context);

		setID(LESSON_ID);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(5);
		paint.setStyle(Style.FILL);
		paint.setTextSize(23);
		paint.setAntiAlias(true);
		
	}



	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		List<Lesson> lessons = new ArrayList<Lesson>();
		
		if(lessoncontainer.isSomethinStrange()){
			__paintRedBorder(canvas);
			
			if(lessoncontainer.allCancelled()){
				lessons = lessoncontainer.getSpecialLessons();
			}else if(lessoncontainer.containsSubsituteLesson()){
				lessons = lessoncontainer.getAllLessons();
			}else{
				lessons = lessoncontainer.getIrregularLessons();
			}
			
		}else{
			lessons = lessoncontainer.getStandardLessons();
		}
		
		
		
		ArrayList<String> firstline = new ArrayList<String>();
		ArrayList<String> secondline = new ArrayList<String>();
		ArrayList<String> thirdline = new ArrayList<String>();


		List<? extends ViewType> vtfirstline = null;
		List<? extends ViewType> vtsecondline = null;
		List<? extends ViewType> vtthirdline = null;


		
		for (Lesson l : lessons) {
						
			if (viewtype instanceof SchoolClass) {
				vtfirstline = l.getSchoolSubjects();
				vtsecondline = l.getSchoolTeachers();
				vtthirdline = l.getSchoolRooms();
				
				if(l.getLessonCode() instanceof LessonCodeSubstitute){
					secondline.add(substituteLessonTeacherString((LessonCodeSubstitute)l.getLessonCode()));
					thirdline.add(substituteLessonRoomString((LessonCodeSubstitute)l.getLessonCode()));
				}
				
			} else if (viewtype instanceof SchoolTeacher) {
				vtfirstline = l.getSchoolClasses();
				vtsecondline = l.getSchoolSubjects();
				vtthirdline = l.getSchoolRooms();
				
				if(l.getLessonCode() instanceof LessonCodeSubstitute){
					thirdline.add(substituteLessonRoomString((LessonCodeSubstitute)l.getLessonCode()));
				}

			} else if (viewtype instanceof SchoolRoom) {
				vtfirstline = l.getSchoolClasses();
				vtsecondline = l.getSchoolTeachers();
				vtthirdline = l.getSchoolSubjects();

				if(l.getLessonCode() instanceof LessonCodeSubstitute){
					secondline.add(substituteLessonTeacherString((LessonCodeSubstitute)l.getLessonCode()));
				}
				
			} else if (viewtype instanceof SchoolSubject) {
				vtfirstline = l.getSchoolTeachers();
				vtsecondline = l.getSchoolClasses();
				vtthirdline = l.getSchoolRooms();
				
				if(l.getLessonCode() instanceof LessonCodeSubstitute){
					firstline.add(substituteLessonTeacherString((LessonCodeSubstitute)l.getLessonCode()));
					thirdline.add(substituteLessonRoomString((LessonCodeSubstitute)l.getLessonCode()));
				}
			}
			
			
			for (ViewType s : vtfirstline) {
				if(!firstline.contains(s.getName()))
					firstline.add(s.getName());
			}
			for (ViewType s : vtsecondline) {
				if(!secondline.contains(s.getName()))
					secondline.add(s.getName());
			}
			for (ViewType s : vtthirdline) {
				if(!thirdline.contains(s.getName()))
					thirdline.add(s.getName());
			}
			
		}
		
	
		int left = getResources().getDimensionPixelSize(R.dimen.gui_lesson_padding_left);
		int top = getResources().getDimensionPixelSize(R.dimen.gui_lesson_padding_top);
		int l1l2p = getResources().getDimensionPixelSize(R.dimen.gui_lesson_line1_line1_padding);

		TextPaint tp = new TextPaint(paint);
		tp.setTypeface(Typeface.DEFAULT_BOLD); 
		tp.setTextSize(getResources().getDimension(R.dimen.gui_lesson_line1_size));
		
		
		String line1 = prepareListForDisplay(firstline,tp);
		canvas.drawText(line1, left, top, tp);
		
		tp.setTextSize(getResources().getDimension(R.dimen.gui_lesson_line2_size));
		tp.setTypeface(Typeface.DEFAULT);
		String line2 = prepareListForDisplay(secondline,tp);
		canvas.drawText(line2, left, l1l2p+top, tp);
		
		
		int all = top + l1l2p*2;
		Log.d("basti", all + " " + height);
		if( all < height ){
			String line3 = prepareListForDisplay(thirdline,tp);
			canvas.drawText(line3, left, l1l2p*2+top, tp);
		}
	}
	
	private String substituteLessonTeacherString(LessonCodeSubstitute lcs){
		SchoolTeacher originSchoolTeacher = lcs.getOriginSchoolTeacher();
		if(originSchoolTeacher != null)
			return "("+originSchoolTeacher.getName()+")";	
		return "";
	}
	
	private String substituteLessonRoomString(LessonCodeSubstitute lcs){
		SchoolRoom getOriginSchoolRoom = lcs.getOriginSchoolRoom();
		if(getOriginSchoolRoom != null)
			return "("+getOriginSchoolRoom.getName()+")";	
		return "";
	}
	
	private String prepareListForDisplay(ArrayList<String> input, TextPaint tp){
		StringBuilder sb = new StringBuilder();
		String tmp = "";

		for (String c : input) {
			
			if(c.equals(""))
				continue;
			
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
		
		List<Lesson> lessons = lessoncontainer.getStandardLessons();
		
		if(lessons.size() == 0){
			if(lessoncontainer.allCancelled()){
				lessons = lessoncontainer.getSpecialLessons();
			}else{
				lessons = lessoncontainer.getIrregularLessons();
			}
		}
		
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
	
	private void __paintRedBorder(Canvas c){
		Paint p = new Paint();
		p.setColor(Color.RED);
		p.setAlpha(100);
		p.setStrokeWidth(8);
		p.setStyle(Style.STROKE);
		p.setAntiAlias(true);
		
		c.drawLine(0, 0, width+2, 0, p);
		c.drawLine(0, 0, 0, height+2, p);
		c.drawLine(0, height+2, width+2, height+2, p);
		c.drawLine(width+2, 0, width+2, height+2, p);
		
		if(lessoncontainer.allCancelled()){
			p.setStrokeWidth(4);
			c.drawLine(0, 0, width+2, height+2, p);
		}
	}
	
	public DateTime getTime() {
		return time;
	}
	
	public GUILessonContainer getLessonsContainer(){
		return lessoncontainer;
	}
	
	public ViewType getViewType(){
		return viewtype;
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
