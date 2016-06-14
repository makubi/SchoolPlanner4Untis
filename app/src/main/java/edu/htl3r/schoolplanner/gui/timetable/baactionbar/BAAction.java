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
package edu.htl3r.schoolplanner.gui.timetable.baactionbar;

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
import edu.htl3r.schoolplanner.R;

public class BAAction extends RelativeLayout implements OnTouchListener{
	
	protected ImageView icon;
	
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
