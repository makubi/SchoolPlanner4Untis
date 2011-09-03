package edu.htl3r.schoolplanner.gui.basti.Lessons;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
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

public class LessonView extends View {

	private GUILessonContainer lessoncontainer;
	private Paint paint;
	private int width, height;
	private String name;
	private ViewType viewtype;

	private DateTime time;

	public LessonView(Context context) {
		super(context);

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

		ArrayList<String> title = new ArrayList<String>();
		List<? extends ViewType> vts = null;
		
		for (Lesson l : lessons) {
			if (viewtype instanceof SchoolClass) {
				vts = l.getSchoolSubjects();
			} else if (viewtype instanceof SchoolTeacher) {
				vts = l.getSchoolClasses();
			} else if (viewtype instanceof SchoolRoom) {
				vts = l.getSchoolClasses();
			} else if (viewtype instanceof SchoolSubject) {
				vts = l.getSchoolTeachers();
			}
						
			for (ViewType s : vts) {
				title.add(s.getName());
			}
		}

		StringBuilder sb = new StringBuilder();
		String tmp = "";

		for (String c : title) {
			tmp = c + sb.toString() + " ";

			if (StaticLayout.getDesiredWidth(tmp, new TextPaint(paint)) > width) {
				sb.append(" ...");
				break;
			}

			if (sb.length() == 0)
				sb.append(c);
			else
				sb.append(", " + c);
		}
		StaticLayout s = new StaticLayout(sb.toString(), new TextPaint(paint),width, Layout.Alignment.ALIGN_NORMAL, 0, 0, false);
		s.draw(canvas);
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
