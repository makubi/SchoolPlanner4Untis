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

import android.app.Dialog;
import android.content.Context;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.gui.timetable.WeekView;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUILessonContainer;

public class OverlayInfo extends Dialog{

	
	private GUILessonContainer lessons;
	private LinearLayout container;
	private HorizontalScrollView scrollview;
	private ArrayList<OverlayInfoLesson> overlaylessons = new ArrayList<OverlayInfoLesson>();
	private OverlayInfoViewTypeChangeListener changevtlistener = new OverlayInfoViewTypeChangeListener();
	
	public OverlayInfo(Context context) {
		super(context);
		setContentView(R.layout.overlay);
		setCanceledOnTouchOutside(true);
		container = (LinearLayout)findViewById(R.id.overlay_container);
		scrollview = (HorizontalScrollView)findViewById(R.id.overlay_scroll);
		setTitle("");
	}


	public void setData(GUILessonContainer lessonsContainer, ViewType viewType, WeekView wv) {
		lessons = lessonsContainer;
		scrollview.scrollTo(0, 0);
		DateTime start = lessons.getStart();
		DateTime end = lessons.getEnd();
		
		String mins = ((start.getMinute()+"").length()==1) ? "0"+start.getMinute() : start.getMinute()+"";
		String mine = ((end.getMinute()+"").length()==1) ? "0"+end.getMinute() : end.getMinute()+"";

		setTitle(start.getHour()+":"+ mins + " - " + end.getHour() + ":" + mine);

		container.removeAllViews();
		overlaylessons.clear();
		
		changevtlistener.setData(this, wv);
		
		ArrayList<Lesson> allLessons = lessons.getAllLessons();
		for(Lesson l :  allLessons){
			OverlayInfoLesson ol = new OverlayInfoLesson(getContext());
			ol.setData(l,viewType,changevtlistener);
			overlaylessons.add(ol);
			ScrollView scr = new ScrollView(getContext());
			scr.addView(ol);
			container.addView(scr);
		}		
	}

}
