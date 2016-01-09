/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
			Sebastian Chlan <sebastian@schoolplanner.at>
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.htl3r.schoolplanner.gui.timetable.Overlay.Info;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.ViewGroup;
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

public class OverlayInfoLesson extends ViewGroup {

	private Lesson lesson;
	private LessonCode lcode;
	private ViewType viewtype;
	private int width, height;
	private OverlayInfoViewTypeChangeListener changevtlistener;

	public OverlayInfoLesson(Context context) {
		super(context);
	}

	public void setData(Lesson l, ViewType vt, OverlayInfoViewTypeChangeListener listener) {
		lesson = l;
		lcode = lesson.getLessonCode();
		viewtype = vt;
		changevtlistener = listener;
		setBackground();
		paintText();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		width = getResources().getDimensionPixelSize(R.dimen.gui_overlay_lesson_width);
		height = getResources().getDimensionPixelSize(R.dimen.gui_overlay_lesson_height);
		
		for (int i = 0; i < getChildCount(); i++) {
			if(!(getChildAt(i) instanceof ViewTypeBox)){
				continue;
			}
			ViewTypeBox c = (ViewTypeBox) getChildAt(i);
			measureChild(c, MeasureSpec.makeMeasureSpec(c.getDesiredWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(c.getDesiredHeight(), MeasureSpec.EXACTLY));
		}
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		for (int i = 0; i < getChildCount(); i++) {
			if(!(getChildAt(i) instanceof ViewTypeBox)){
				continue;
			}
			drawChild(canvas, getChildAt(i), 0);
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint p = new Paint();
		p.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_10));
		p.setColor(Color.BLACK);
		p.setTextSize(getResources().getDimension(R.dimen.gui_overlay_text_size_medium));
		p.setAntiAlias(true);

		if (lcode != null)
			p.setColor(Color.RED);

		canvas.drawLine(0, 0, width, 0, p);
		canvas.drawLine(0, 0, 0, height, p);
		canvas.drawLine(width, 0, width, height, p);
		canvas.drawLine(width, height, 0, height, p);

		if (lcode instanceof LessonCodeCancelled) {
			p.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_5));
			canvas.drawLine(0, 0, width, height, p);
		}
		int marginleft = getResources().getDimensionPixelSize(R.dimen.gui_overlay_footer_margin_left);
		int marginbottom = getResources().getDimensionPixelSize(R.dimen.gui_overlay_footer_margin_bottom);

		if (lcode != null) {
			if (lcode instanceof LessonCodeCancelled) {
				canvas.drawText(getResources().getString(R.string.timetable_overlay_lesson_can), marginleft, height - marginbottom, p);
			} else if (lcode instanceof LessonCodeSubstitute) {
				canvas.drawText(getResources().getString(R.string.timetable_overlay_lesson_sub), marginleft, height - marginbottom, p);
			}
			if (lcode instanceof LessonCodeIrregular) {
				canvas.drawText(getResources().getString(R.string.timetable_overlay_lesson_irr), marginleft, height - marginbottom, p);
			}
		}
		
		
		int linespace = getResources().getDimensionPixelOffset(R.dimen.gui_overlay_line_space);
		p.setTextSize(getResources().getDimension(R.dimen.gui_overlay_text_size_small));
		String line1 = lesson.getDate().getDay()+"."+lesson.getDate().getMonth()+"."+lesson.getDate().getYear();
		String start = lesson.getStartTime().getHour() +":" +(((lesson.getStartTime().getMinute()+"").length()==2)?lesson.getStartTime().getMinute()+"":"0"+lesson.getStartTime().getMinute()+"");
		String end = lesson.getEndTime().getHour() +":" +(((lesson.getEndTime().getMinute()+"").length()==2)?lesson.getEndTime().getMinute()+"":"0"+lesson.getEndTime().getMinute()+"");
		String line2 = start + " - " + end;
		canvas.drawText(line1, width - marginleft - StaticLayout.getDesiredWidth(line1, new TextPaint(p)), height-marginbottom-(linespace/2), p);
		canvas.drawText(line2, width - marginleft - StaticLayout.getDesiredWidth(line2, new TextPaint(p)), height-marginbottom, p);
	}

	private void setBackground() {
		setBackgroundColor(getBackgroundColor());
	}
	
	private int getBackgroundColor(){
		List<? extends ViewType> vt = null;
		if (viewtype instanceof SchoolClass) {
			vt = lesson.getSchoolSubjects();
		} else if (viewtype instanceof SchoolTeacher) {
			// vt = lesson.getSchoolClasses();
			vt = lesson.getSchoolSubjects();
		} else if (viewtype instanceof SchoolRoom) {
			vt = lesson.getSchoolClasses();
		} else if (viewtype instanceof SchoolSubject) {
			vt = lesson.getSchoolTeachers();
		}
		if (vt.size() != 0) {
			String bcolor = vt.get(0).getBackColor();
			if (!bcolor.equalsIgnoreCase("")) {

				int color;
				try {
					color = Color.parseColor("#" + bcolor);
				} catch (RuntimeException e) {
					return Color.WHITE;
				}
				color = Color.argb(85, Color.red(color), Color.green(color),Color.blue(color));
				setBackgroundColor(color);
				return color;
			}
		}
		return Color.WHITE;
	}

	private void paintText() {
			
		List<SchoolClass> schoolClasses = lesson.getSchoolClasses();
		List<SchoolRoom> schoolRooms = lesson.getSchoolRooms();
		List<SchoolSubject> schoolSubjects = lesson.getSchoolSubjects();
		List<SchoolTeacher> schoolTeachers = lesson.getSchoolTeachers();
		int color = getBackgroundColor();
		ViewTypeBox vtb;
		
		for (SchoolClass s : schoolClasses) {
			vtb = new ViewTypeBox(getContext(), s,color,false);
			vtb.setOnClickListener(changevtlistener);
			vtb.setOnTouchListener(changevtlistener);
			addView(vtb);
		}
		for (SchoolRoom s : schoolRooms) {
			vtb = new ViewTypeBox(getContext(), s,color,false);
			vtb.setOnClickListener(changevtlistener);
			vtb.setOnTouchListener(changevtlistener);
			addView(vtb);
		}
		for (SchoolSubject s : schoolSubjects) {
			vtb = new ViewTypeBox(getContext(), s,color,false);
			vtb.setOnClickListener(changevtlistener);
			vtb.setOnTouchListener(changevtlistener);
			addView(vtb);
		}
		for (SchoolTeacher s : schoolTeachers) {
			vtb = new ViewTypeBox(getContext(), s,color,false);
			vtb.setOnClickListener(changevtlistener);
			vtb.setOnTouchListener(changevtlistener);
			addView(vtb);
		}
		
		
		
		if(lesson.getLessonCode() instanceof LessonCodeSubstitute){
			LessonCodeSubstitute lc = (LessonCodeSubstitute) lesson.getLessonCode();
			if(lc.getOriginSchoolRoom() != null){
				vtb = new ViewTypeBox(getContext(), lc.getOriginSchoolRoom(), color, true);
				vtb.setOnClickListener(changevtlistener);
				addView(vtb);
			}
			if(lc.getOriginSchoolTeacher() != null){
				vtb = new ViewTypeBox(getContext(), lc.getOriginSchoolTeacher(), color, true);
				vtb.setOnClickListener(changevtlistener);
				addView(vtb);
			}
			
		}
		
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		int cc = getChildCount();
		ArrayList<ViewTypeBox> classes = new ArrayList<ViewTypeBox>();
		ArrayList<ViewTypeBox> rooms = new ArrayList<ViewTypeBox>();
		ArrayList<ViewTypeBox> teacher = new ArrayList<ViewTypeBox>();
		ArrayList<ViewTypeBox> subjects = new ArrayList<ViewTypeBox>();


		int height = 0;
		
		for (int i = 0; i < cc; i++) {
			if(!(getChildAt(i) instanceof ViewTypeBox)){
				continue;
			}
			ViewTypeBox vtb = (ViewTypeBox) getChildAt(i);
			if (vtb.getViewType() instanceof SchoolClass) {
				classes.add(vtb);
			} else if (vtb.getViewType() instanceof SchoolRoom) {
				rooms.add(vtb);
			} else if (vtb.getViewType() instanceof SchoolTeacher) {
				teacher.add(vtb);
			} else if (vtb.getViewType() instanceof SchoolSubject) {
				subjects.add(vtb);
			}
			height = vtb.getDesiredHeight();
		}
		
		
		int margintop = getResources().getDimensionPixelOffset(R.dimen.gui_overlay_margin_top);
		margintop = layoutViewTypeBoxes(classes, margintop);
		margintop = layoutViewTypeBoxes(rooms, margintop+10+height);
		margintop = layoutViewTypeBoxes(teacher, margintop+10+height);
		layoutViewTypeBoxes(subjects, margintop+10+height);
		
	}
	
	private int layoutViewTypeBoxes(ArrayList<ViewTypeBox> vtbs, int margintop){
		int marginleft = getResources().getDimensionPixelOffset(R.dimen.gui_stroke_width_10);

		ArrayList<Integer> lines = getLineLength(vtbs);
		marginleft = (width-lines.get(0))/2;
		int count = 1;
		for(ViewTypeBox vtb : vtbs){
			if(marginleft+vtb.getDesiredWidth() > width && count < lines.size()){
				marginleft = (width-lines.get(count))/2;
				margintop += vtb.getDesiredHeight(); 
				count++;
			}
			vtb.layout(marginleft,margintop, marginleft+vtb.getDesiredWidth(), margintop+vtb.getDesiredHeight());
			marginleft+=vtb.getDesiredWidth()+getResources().getDimensionPixelSize(R.dimen.gui_overlay_vtb_abstand_left);
		}
		return margintop;
	}
	
	private ArrayList<Integer> getLineLength(ArrayList<ViewTypeBox> vtbs){
		ArrayList<Integer> lines = new ArrayList<Integer>();
		
		int tmplength =0;
		
		if((getLength(vtbs)) < width){
			lines.add(getLength(vtbs));
			return lines;
		}
		for(ViewTypeBox vt : vtbs){
			if((vt.getDesiredWidth()+ 20 + tmplength) > width){
				lines.add(tmplength);
				tmplength=0;
			}
			tmplength+=vt.getDesiredWidth()+ 20;
		}
		lines.add(tmplength);
		return lines;
	}
	
	private int getLength(ArrayList<ViewTypeBox> vtb){
		int len = 0;
		for(ViewTypeBox v : vtb){
			len+=v.getDesiredWidth()+20;
		}
		return len;
	}

}
