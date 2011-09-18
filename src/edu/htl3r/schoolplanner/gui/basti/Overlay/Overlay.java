package edu.htl3r.schoolplanner.gui.basti.Overlay;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.widget.LinearLayout;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUILessonContainer;

public class Overlay extends Dialog{

	
	private GUILessonContainer lessons;
	private LinearLayout container;
	private ArrayList<OverlayLesson> overlaylessons = new ArrayList<OverlayLesson>();
	
	public Overlay(Context context) {
		super(context);
		setContentView(R.layout.overlay);
		setCanceledOnTouchOutside(true);
		container = (LinearLayout)findViewById(R.id.overlay_container);
		setTitle(context.getString(R.string.timetable_overlay_title));
	}


	public void setData(GUILessonContainer lessonsContainer, ViewType viewType) {
		lessons = lessonsContainer;
		
		container.removeAllViews();
		overlaylessons.clear();
		
		ArrayList<Lesson> allLessons = lessons.getAllLessons();
		for(Lesson l :  allLessons){
			OverlayLesson ol = new OverlayLesson(getContext());
			ol.setData(l,viewType);
			overlaylessons.add(ol);
			container.addView(ol);
		}		
	}

}
