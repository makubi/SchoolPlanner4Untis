package edu.htl3r.schoolplanner.gui.timetable.Overlay.Info;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCode;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeCancelled;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeIrregular;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class OverlayInfoLesson extends View {

	private Lesson lesson;
	private LessonCode lcode;
	private ViewType viewtype;
	private int width, height;

	public OverlayInfoLesson(Context context) {
		super(context);
	}

	
	public void setData(Lesson l, ViewType vt){
		lesson = l;
		lcode = lesson.getLessonCode();
		viewtype = vt;
		setBackground();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = getResources().getDimensionPixelSize(R.dimen.gui_overlay_lesson_width);
		height = getResources().getDimensionPixelSize(R.dimen.gui_overlay_lesson_height);
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint p = new Paint();
		p.setStrokeWidth(10);
		p.setColor(Color.BLACK);
		p.setTextSize(getResources().getDimension(R.dimen.gui_overlay_text_size_medium));
		p.setAntiAlias(true);
		
		if(lcode!=null)
			p.setColor(Color.RED);
		
		
		canvas.drawLine(0, 0, width, 0, p);
		canvas.drawLine(0, 0, 0, height, p);
		canvas.drawLine(width, 0, width, height, p);
		canvas.drawLine(width, height, 0, height, p);
		
		
		
		if(lcode instanceof LessonCodeCancelled){
			p.setStrokeWidth(5);
			canvas.drawLine(0, 0, width, height, p);
		}
		
		if(lcode != null){
			int marginleft = getResources().getDimensionPixelSize(R.dimen.gui_overlay_footer_margin_left);
			int marginbottom = getResources().getDimensionPixelSize(R.dimen.gui_overlay_footer_margin_bottom);
			if(lcode instanceof LessonCodeCancelled){
				canvas.drawText(getResources().getString(R.string.timetable_overlay_lesson_can), marginleft, height-marginbottom, p);
			}else if(lcode instanceof LessonCodeSubstitute){
				canvas.drawText(getResources().getString(R.string.timetable_overlay_lesson_sub), marginleft, height-marginbottom, p);
			}if(lcode instanceof LessonCodeIrregular){
				canvas.drawText(getResources().getString(R.string.timetable_overlay_lesson_irr), marginleft, height-marginbottom, p);
			}
		}
		
		paintText(canvas);
		
		super.onDraw(canvas);
	}

	private void setBackground() {
		List<? extends ViewType> vt = null;
		if (viewtype instanceof SchoolClass) {
			vt = lesson.getSchoolSubjects();
		} else if (viewtype instanceof SchoolTeacher) {
			vt = lesson.getSchoolClasses();
		} else if (viewtype instanceof SchoolRoom) {
			vt = lesson.getSchoolClasses();
		} else if (viewtype instanceof SchoolSubject) {
			vt = lesson.getSchoolTeachers();
		}

		if (vt.size() != 0) {
			String bcolor = vt.get(0).getBackColor();
			if (!bcolor.equalsIgnoreCase("")) {
				setBackgroundColor(Color.parseColor("#55" + bcolor));
			}
		}	

	}

	private void paintText(Canvas canvas){
		List<SchoolClass> schoolClasses = lesson.getSchoolClasses();
		List<SchoolRoom> schoolRooms = lesson.getSchoolRooms();
		List<SchoolSubject> schoolSubjects = lesson.getSchoolSubjects();
		List<SchoolTeacher> schoolTeachers = lesson.getSchoolTeachers();
		
		TextPaint tp = new TextPaint();
		tp.setColor(Color.BLACK);
		tp.setAntiAlias(true);
		tp.setTextSize(getResources().getDimension(R.dimen.gui_overlay_text_size_big));
		
		StringBuilder classes = new StringBuilder();
		StringBuilder rooms = new StringBuilder();
		StringBuilder subjects = new StringBuilder();
		StringBuilder teacher = new StringBuilder();
		
			
		
		for(SchoolClass s : schoolClasses){
			classes.append(s.getName()+" ");
		}
		for(SchoolRoom s : schoolRooms){
			rooms.append(s.getName()+" ");
		}
		for(SchoolSubject s : schoolSubjects){
			subjects.append(s.getName()+" ");
		}
		for(SchoolTeacher s : schoolTeachers){
			teacher.append(s.getName()+" ");
		}
		
		if(lcode instanceof LessonCodeSubstitute){
			LessonCodeSubstitute lcs = (LessonCodeSubstitute)lcode;
			SchoolRoom originSchoolRoom = lcs.getOriginSchoolRoom();
			SchoolTeacher originSchoolTeacher = lcs.getOriginSchoolTeacher();
			if(originSchoolRoom!= null)
				rooms.append("("+originSchoolRoom.getName()+")");
			if(originSchoolTeacher != null)
				teacher.append("("+originSchoolTeacher.getName()+")");
		}
		
		
		ArrayList<String> splitString = splitString(classes.toString(), tp);
		StaticLayout sl = null;
		
		int margintop = getResources().getDimensionPixelOffset(R.dimen.gui_overlay_margin_top);
		int linespace = getResources().getDimensionPixelOffset(R.dimen.gui_overlay_line_space);

		canvas.translate(0, margintop);

		for (String line : splitString){
			sl = new StaticLayout(line.trim(), tp, width, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
			sl.draw(canvas);
			canvas.translate(0, linespace-margintop);

		}

		canvas.translate(0, margintop);
		sl = new StaticLayout(teacher.toString(), tp, width, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
		sl.draw(canvas);
		
		canvas.translate(0, linespace);
		sl = new StaticLayout(subjects.toString(), tp, width, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
		sl.draw(canvas);
		
		canvas.translate(0, linespace);
		sl = new StaticLayout(rooms.toString(), tp, width, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
		sl.draw(canvas);
		
		
		tp.setTextSize(getResources().getDimension(R.dimen.gui_overlay_text_size_small));
		
		sl = new StaticLayout(lesson.getDate().getDay()+"."+lesson.getDate().getMonth()+"."+lesson.getDate().getYear(), tp, width, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
		canvas.translate(0, linespace);
		sl.draw(canvas);
		String start = lesson.getStartTime().getHour() +":" +(((lesson.getStartTime().getMinute()+"").length()==2)?lesson.getStartTime().getMinute()+"":"0"+lesson.getStartTime().getMinute()+"");
		String end = lesson.getEndTime().getHour() +":" +(((lesson.getEndTime().getMinute()+"").length()==2)?lesson.getEndTime().getMinute()+"":"0"+lesson.getEndTime().getMinute()+"");
		sl = new StaticLayout(start + " - " + end, tp, width, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
		canvas.translate(0, linespace/2);
		sl.draw(canvas);
		
	}
	
	private ArrayList<String> splitString(String s, TextPaint tp){
		
		String [] tmp = s.split(" ");
		StringBuilder sb = new StringBuilder();
		ArrayList<String> ret = new ArrayList<String>();
		
		for (int i = 0; i < tmp.length; i++) {	
			if(StaticLayout.getDesiredWidth(sb.toString()+" "+tmp[i], tp) > width){
				ret.add(sb.toString().trim());
				sb = new StringBuilder();
				sb.append(tmp[i]);
			}else{
				sb.append(" " + tmp[i]);
			}			
		}
		ret.add(sb.toString());
		return ret;
	}

}
