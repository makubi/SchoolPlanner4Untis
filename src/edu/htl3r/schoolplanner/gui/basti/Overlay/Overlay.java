package edu.htl3r.schoolplanner.gui.basti.Overlay;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUILessonContainer;

public class Overlay extends Dialog{

	
	private GUILessonContainer lessons;
	private LinearLayout container;
	private HorizontalScrollView scrollview;
	private ArrayList<OverlayLesson> overlaylessons = new ArrayList<OverlayLesson>();
	
	public Overlay(Context context) {
		super(context);
		setContentView(R.layout.overlay);
		setCanceledOnTouchOutside(true);
		container = (LinearLayout)findViewById(R.id.overlay_container);
		scrollview = (HorizontalScrollView)findViewById(R.id.overlay_scroll);
		setTitle("");
	}


	public void setData(GUILessonContainer lessonsContainer, ViewType viewType) {
		lessons = lessonsContainer;
		scrollview.scrollTo(0, 0);
		DateTime start = lessons.getStart();
		DateTime end = lessons.getEnd();
		setTitle(start.getHour()+":"+start.getMinute() + " - " + end.getHour() + ":" + end.getMinute());

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
