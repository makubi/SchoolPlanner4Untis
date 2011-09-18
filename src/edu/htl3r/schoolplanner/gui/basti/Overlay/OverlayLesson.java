package edu.htl3r.schoolplanner.gui.basti.Overlay;

import java.util.List;

import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout.Alignment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;

public class OverlayLesson extends View {

	private Lesson lesson;
	private ViewType viewtype;
	int width, height;

	public OverlayLesson(Context context) {
		super(context);
	}

	
	public void setData(Lesson l, ViewType vt){
		lesson = l;
		viewtype = vt;
		setBackground();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		width = getResources().getDimensionPixelSize(R.dimen.gui_overlay_lesson_width);
		height = getResources().getDimensionPixelSize(R.dimen.gui_overlay_lesson_height);
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint p = new Paint();
		p.setStrokeWidth(7);
		p.setColor(Color.BLACK);
		
		canvas.drawLine(0, 0, width, 0, p);
		canvas.drawLine(0, 0, 0, height, p);
		canvas.drawLine(width, 0, width, height, p);
		canvas.drawLine(width, height, 0, height, p);
		
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
		StringBuilder line = new StringBuilder();
		
		TextPaint tp = new TextPaint();
		tp.setColor(Color.BLACK);
		tp.setAntiAlias(true);
		tp.setTextSize(30);
		
		for(SchoolClass s : schoolClasses){
			line.append(s.getName()+" ");
		}
		canvas.translate(0, 40);
		StaticLayout sl = new StaticLayout(line.toString(), tp, width, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
		sl.draw(canvas);
		line = new StringBuilder();
		
		for(SchoolRoom s : schoolRooms){
			line.append(s.getName()+" ");
		}
		
		canvas.translate(0, 40);
		sl = new StaticLayout(line.toString(), tp, width, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
		sl.draw(canvas);
		line = new StringBuilder();
		
		for(SchoolSubject s : schoolSubjects){
			line.append(s.getName()+" ");
		}
		canvas.translate(0, 40);
		sl = new StaticLayout(line.toString(), tp, width, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
		sl.draw(canvas);
		line = new StringBuilder();
		
		for(SchoolTeacher s : schoolTeachers){
			line.append(s.getName()+" ");
		}
		canvas.translate(0, 40);
		sl = new StaticLayout(line.toString(), tp, width, Layout.Alignment.ALIGN_CENTER, 0, 0, false);
		sl.draw(canvas);
		line = new StringBuilder();
		
	}

}
