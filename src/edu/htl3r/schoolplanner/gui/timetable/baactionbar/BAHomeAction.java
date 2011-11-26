package edu.htl3r.schoolplanner.gui.timetable.baactionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;
import edu.htl3r.schoolplanner.R;

public class BAHomeAction extends BAAction{

	
	private ProgressBar progressbar;
	
	public BAHomeAction(Context context) {
		super(context);
	}
	
	public BAHomeAction(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public BAHomeAction(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	
	public void initProgressBar(){
		progressbar = (ProgressBar)findViewById(R.id.baactionbar_home_progress);
	}
	
	public void startProgressBar(boolean scroll){
		progressbar.bringToFront();
		if(scroll){
			progressbar.setVisibility(VISIBLE);
		}else{
			progressbar.setVisibility(INVISIBLE);
		}
	}
	
}
