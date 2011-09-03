package edu.htl3r.schoolplanner.gui.basti.Week;

import android.content.Context;
import android.view.View;

public class GUIWeekView extends View{

	
	public final static int LESSON_ID = 0;
	public final static int HEADER_ID = 1;
	public final static int TIMGRID_ID = 2;  
	
	private int id = LESSON_ID;
	
	public GUIWeekView(Context context) {
		super(context);
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}

	
}
