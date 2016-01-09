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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;

public class ViewTypeBox extends View {

	private ViewType viewtype;
	private int width, height;
	private TextPaint paint;
	private int background;
	private StaticLayout s;
	private boolean substiviewtype = false;

	public ViewTypeBox(Context context, ViewType vt, int background, boolean subs) {
		super(context);
		viewtype = vt;
		this.substiviewtype = subs;
		this.background = background;
		paint = new TextPaint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setTextSize(getResources().getDimension(R.dimen.gui_overlay_text_size_big));

		s = new StaticLayout(toString(), paint, getDesiredWidth(),Alignment.ALIGN_CENTER, 1, 0, true);
		
		setBackground(false);

	}
	
	public void setBackground(boolean touch){
		int colors [] = new int[2];
		if(background != Color.WHITE){
			int startg = Color.argb(10, Color.red(background),Color.green(background), Color.blue(background));
			int endg = Color.argb(100, Color.red(background),Color.green(background), Color.blue(background));
			
			if(!touch){
				 colors[0] = endg;
				 colors[1] = startg;
			}else{
				 colors[1] = 17170457;
				 colors[0] = 17170457;
			}
			
			GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
			// 	drawable.setAlpha(80);
			setBackgroundDrawable(drawable);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		s.draw(canvas);

		if (background == Color.WHITE) {
			canvas.drawLine(0, 0, width, 0, paint);
			canvas.drawLine(0, height, width, height, paint);
			canvas.drawLine(0, 0, 0, height, paint);
			canvas.drawLine(width, 0, width, height, paint);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	public ViewType getViewType() {
		return viewtype;
	}

	public int getDesiredWidth() {
		return (int) StaticLayout.getDesiredWidth(toString(), paint);
	}

	public int getDesiredHeight() {
		return (int) paint.getTextSize() + getResources().getDimensionPixelSize(R.dimen.gui_overlay_vtb_height);
	}

	@Override
	public String toString() {
		if(!substiviewtype)
			return viewtype.getName();
		else
			return "("+viewtype.getName()+")";
	}
	
}
