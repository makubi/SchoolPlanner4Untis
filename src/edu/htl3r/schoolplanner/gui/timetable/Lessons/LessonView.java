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
package edu.htl3r.schoolplanner.gui.timetable.Lessons;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
	private int width, height;
	private ViewType viewtype;

	private DateTime time;
	
	private boolean cancelhour = false;

	private TextView head;
	private TextView line1;

	
	private ArrayList<String> firstline = new ArrayList<String>();
	private ArrayList<String> secondline = new ArrayList<String>();
	private ArrayList<String> thirdline = new ArrayList<String>();
	
	private Paint p;
	
	public LessonView(Context context) {
		super(context);
		setID(LESSON_ID);
		
		p = new Paint();
		p.setColor(Color.RED);
		p.setAlpha(100);
		p.setAntiAlias(true);
        p.setDither(true);
        p.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_4));
	}
	
	public void setNeededData(GUILessonContainer l, ViewType vt) {
		lessoncontainer = l;
		viewtype = vt;
		time = lessoncontainer.getDate();
		if(!lessoncontainer.isEmpty()){
			View.inflate(getContext(), R.layout.timetable_lesson_view, this);
			initData();
			initLessonView();	
		}
	}

	private void initLessonView(){
		
		int background = setBackgroundColor();
		
		
		if(lessoncontainer.isSomethinStrange() == GUILessonContainer.STRANGE || lessoncontainer.isSomethinStrange() == GUILessonContainer.STRANGE_NORMAL){
			GradientDrawable back = (GradientDrawable) getResources().getDrawable(R.drawable.border_lesson);
			back.setColor(background);
			setBackgroundDrawable(back);
		}
		
		if (lessoncontainer.allCancelled() && lessoncontainer.isSomethinStrange() != GUILessonContainer.STRANGE_NORMAL) {
			cancelhour = true;
		}
		
		int txtcolor = setTextColor(background);

		head = (TextView) findViewById(R.id.timetable_lesson_title);
		head.setTextColor(txtcolor);
		head.setText(prepareListForDisplay(firstline));
		
		line1 = (TextView)findViewById(R.id.timetable_lesson_line_1);
		line1.setTextColor(txtcolor);
		line1.setText(prepareListForDisplay(secondline));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(cancelhour)
			canvas.drawLine(0, 0, width, height, p);
	}
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	

	
	private void initData(){
		List<? extends ViewType> vtfirstline = null;
		List<? extends ViewType> vtsecondline = null;
		List<? extends ViewType> vtthirdline = null;
		List<Lesson> lessons = giveMeTheCorrectList();
		
		for (Lesson l : lessons) {

			if (viewtype instanceof SchoolClass) {
				vtfirstline = l.getSchoolSubjects();
				vtsecondline = l.getSchoolTeachers();
				vtthirdline = l.getSchoolRooms();

				if (l.getLessonCode() instanceof LessonCodeSubstitute) {
					secondline.add(substituteLessonTeacherString((LessonCodeSubstitute) l.getLessonCode()));
					thirdline.add(substituteLessonRoomString((LessonCodeSubstitute) l.getLessonCode()));
				}

			} else if (viewtype instanceof SchoolTeacher) {
				vtfirstline = l.getSchoolClasses();
				vtsecondline = l.getSchoolSubjects();
				vtthirdline = l.getSchoolRooms();

				if (l.getLessonCode() instanceof LessonCodeSubstitute) {
					thirdline.add(substituteLessonRoomString((LessonCodeSubstitute) l.getLessonCode()));
				}

			} else if (viewtype instanceof SchoolRoom) {
				vtfirstline = l.getSchoolClasses();
				vtsecondline = l.getSchoolTeachers();
				vtthirdline = l.getSchoolSubjects();

				if (l.getLessonCode() instanceof LessonCodeSubstitute) {
					secondline.add(substituteLessonTeacherString((LessonCodeSubstitute) l.getLessonCode()));
				}

			} else if (viewtype instanceof SchoolSubject) {
				vtfirstline = l.getSchoolTeachers();
				vtsecondline = l.getSchoolClasses();
				vtthirdline = l.getSchoolRooms();

				if (l.getLessonCode() instanceof LessonCodeSubstitute) {
					firstline.add(substituteLessonTeacherString((LessonCodeSubstitute) l.getLessonCode()));
					thirdline.add(substituteLessonRoomString((LessonCodeSubstitute) l.getLessonCode()));
				}
			}

			for (ViewType s : vtfirstline) {
				if (!firstline.contains(s.getName()))
					firstline.add(s.getName());
			}
			for (ViewType s : vtsecondline) {
				if (!secondline.contains(s.getName()))
					secondline.add(s.getName());
			}
//			if (extendedView) {
//				for (ViewType s : vtthirdline) {
//					if (!thirdline.contains(s.getName()))
//						thirdline.add(s.getName());
//				}
//			}

		}
	}
	
	
	private String substituteLessonTeacherString(LessonCodeSubstitute lcs) {
		SchoolTeacher originSchoolTeacher = lcs.getOriginSchoolTeacher();
		if (originSchoolTeacher != null)
			return "(" + originSchoolTeacher.getName() + ")";
		return "";
	}

	private String substituteLessonRoomString(LessonCodeSubstitute lcs) {
		SchoolRoom getOriginSchoolRoom = lcs.getOriginSchoolRoom();
		if (getOriginSchoolRoom != null)
			return "(" + getOriginSchoolRoom.getName() + ")";
		return "";
	}

	private String prepareListForDisplay(ArrayList<String> input) {
		StringBuilder sb = new StringBuilder();
		String tmp = "";

		for(String s : input){
			sb.append(s + ",\u00A0");
		}
		tmp = sb.toString();
		if(tmp.length() < 2 )
			return tmp;
		else
			return tmp.substring(0,tmp.length()-2);
	}


	private int setBackgroundColor() {

		List<Lesson> lessons = giveMeTheCorrectList();

		List<? extends ViewType> vt = null;

		if (lessons.size() != 0) {
			if (viewtype instanceof SchoolClass) {
				vt = lessons.get(0).getSchoolSubjects();
			} else if (viewtype instanceof SchoolTeacher) {
				//vt = lessons.get(0).getSchoolClasses();
				vt = lessons.get(0).getSchoolSubjects();
			} else if (viewtype instanceof SchoolRoom) {
				vt = lessons.get(0).getSchoolClasses();
			} else if (viewtype instanceof SchoolSubject) {
				vt = lessons.get(0).getSchoolTeachers();
			}

			if (vt.size() != 0) {
				String bcolor = vt.get(0).getBackColor();
				if (!bcolor.equalsIgnoreCase("")) {
					setBackgroundColor(Color.parseColor("#55" + bcolor));
					return Color.parseColor("#55" + bcolor);
				}
			}
		}
		return Color.WHITE;
	}
	
	private int setTextColor(int color){
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		
		
		// Magisch Formel von: http://www.nbdtech.com/Blog/archive/2008/04/27/Calculating-the-Perceived-Brightness-of-a-Color.aspx
		double brightness =  Math.sqrt( red * red * .241 + green * green * .691 +  blue * blue * .068);
		
		if(brightness > 100){
			return Color.BLACK;
		}else{
			return Color.WHITE;

		}
	}

	private void paintRedBorder(Canvas c) {
		Paint p = new Paint();
		p.setColor(Color.RED);
		p.setAlpha(100);
		p.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_8));
		p.setStyle(Style.STROKE);
		p.setAntiAlias(true);

		int halfborder = getResources().getDimensionPixelSize(R.dimen.gui_stroke_width_4)/2;
		c.drawLine(0, 0, width + halfborder, 0, p);
		c.drawLine(0, 0, 0, height + halfborder, p);
		c.drawLine(0, height + halfborder, width + halfborder, height + halfborder, p);
		c.drawLine(width + halfborder, 0, width + halfborder, height + halfborder, p);

		
		setBackgroundDrawable(getResources().getDrawable(R.drawable.border_lesson));
//		if (lessoncontainer.allCancelled() && lessoncontainer.isSomethinStrange() != GUILessonContainer.STRANGE_NORMAL) {
//			p.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_4));
//			c.drawLine(0, 0, width + 2, height + 2, p);
//		}
	}

	public DateTime getTime() {
		return time;
	}

	public GUILessonContainer getLessonsContainer() {
		return lessoncontainer;
	}

	public ViewType getViewType() {
		return viewtype;
	}
	
	private List<Lesson> giveMeTheCorrectList(){
		List<Lesson> lessons = new ArrayList<Lesson>();
		
		switch(lessoncontainer.isSomethinStrange()){
		case GUILessonContainer.NORMAL:
			lessons = lessoncontainer.getStandardLessons();
			break;
		case GUILessonContainer.STRANGE:
			if (lessoncontainer.allCancelled()) {					//Wurde alle Stunden gestrichen?
				lessons = lessoncontainer.getSpecialLessons();
			} else if (lessoncontainer.containsSubsituteLesson()) {	// Gibt es Supplierstunden?
				lessons = lessoncontainer.getAllLessons();
			} else {												//Zeige die 
				lessons = lessoncontainer.getIrregularLessons();
			}
			break;
		case GUILessonContainer.STRANGE_NORMAL:
			lessons = lessoncontainer.getAllLessons();
			break;
		}
		
		return lessons;
	}

}
