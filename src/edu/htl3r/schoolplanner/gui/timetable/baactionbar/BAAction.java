package edu.htl3r.schoolplanner.gui.timetable.baactionbar;

import edu.htl3r.schoolplanner.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BAAction extends RelativeLayout implements OnTouchListener{
	
	private ImageView icon;
	
	public BAAction(Context context) {
		super(context);
		init();
	}
	
	public BAAction(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public BAAction(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		setOnTouchListener(this);
	}
	
	public void setIcon(Drawable drawable){
		icon = new ImageView(getContext());
		icon.setImageDrawable(drawable);
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layout.addRule(CENTER_VERTICAL);
		layout.addRule(CENTER_HORIZONTAL);
		addView(icon, layout);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			setBackgroundColor(getResources().getColor(R.color.month_overlay_today));
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			setBackgroundColor(Color.parseColor("#00000000"));
		}
		return false;
	}

	
}
