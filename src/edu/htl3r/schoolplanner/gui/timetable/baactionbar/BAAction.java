package edu.htl3r.schoolplanner.gui.timetable.baactionbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BAAction extends RelativeLayout{
	
	private ImageView icon;
	
	public BAAction(Context context) {
		super(context);
	}
	
	public BAAction(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public BAAction(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	public void setIcon(Drawable drawable){
		icon = new ImageView(getContext());
		icon.setImageDrawable(drawable);
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layout.addRule(CENTER_VERTICAL);
		layout.addRule(CENTER_HORIZONTAL);
		addView(icon, layout);
	}

	
}
