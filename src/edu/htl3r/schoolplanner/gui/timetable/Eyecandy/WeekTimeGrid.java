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
package edu.htl3r.schoolplanner.gui.timetable.Eyecandy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;
import edu.htl3r.schoolplanner.gui.timetable.Week.GUIWeekView;

public class WeekTimeGrid extends GUIWeekView implements OnTouchListener{

	
	private int width,height,hours,offsettop,lessongheight;
	private Paint paint;
	private List<TimegridUnit> timegrid = new ArrayList<TimegridUnit>();
	private boolean landscape = false;
	private boolean highlight;
	
	
	public WeekTimeGrid(Context context, boolean highlight) {
		super(context);
		setID(TIMGRID_ID);
		this.highlight = highlight;
		
		//int color = getResources().getColor(R.color.header_background);
		//setBackgroundColor(Color.argb(200, Color.red(color), Color.green(color), Color.blue(color)));
		
//		int color = getResources().getColor(R.color.element_background);
		setBackgroundColor(Color.parseColor("#D3D3D3"));
		
		paint = new Paint();
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_2));
		paint.setColor( getResources().getColor(R.color.background_stundenplan));
		setOnTouchListener(this);
		
		int h = getResources().getDisplayMetrics().heightPixels;
		int w = getResources().getDisplayMetrics().widthPixels;
		if(h < w)
			landscape = true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.save();
		if(highlight)
			highlight(canvas);
		canvas.restore();
		zeichenInfos(canvas);
		canvas.restore();
		zeichenGatter(canvas);
		super.onDraw(canvas);
	}
	
	private void zeichenGatter(Canvas canvas){
		for(int i=offsettop+lessongheight; i<height; i+=lessongheight){
			canvas.drawLine(0, i, width, i, paint);
		}
		Paint p = new Paint(paint);
		int stroke = getResources().getDimensionPixelSize(R.dimen.gui_stroke_width_4);
		p.setStrokeWidth(stroke);
		canvas.drawLine(width, 0, width, height, p);
		canvas.drawLine(0, offsettop-stroke/2, width, offsettop-stroke/2, p);

	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		lessongheight = (height-offsettop)/ (hours == 0 ? 1 : hours);
		setMeasuredDimension(width, height);
	}
	
	private void zeichenInfos(Canvas canvas){
		paint.setColor(Color.BLACK);

		
		TextPaint tp = new TextPaint(paint);
		tp.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_2));
		tp.setStyle(Style.FILL_AND_STROKE);
		tp.setTextSize(getResources().getDimension(R.dimen.gui_header_line1_size));
		
		TextPaint tp2 = new TextPaint(paint);
		tp2.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_1));
		tp2.setStyle(Style.FILL_AND_STROKE);
		tp2.setTextSize(getResources().getDimension(R.dimen.gui_header_line2_size));
		
		int padding_right = getResources().getDimensionPixelSize(R.dimen.gui_timegrid_padding_right);
		int padding_top = getResources().getDimensionPixelSize(R.dimen.gui_timegrid_padding_top);
		canvas.translate(0, offsettop+padding_top);

		
		for(int i=0; i<hours; i++){
			
			String anz = (timegrid.get(i).getName()).length() <=2 ? (timegrid.get(i).getName()) : (timegrid.get(i).getName()).substring(0, 2);
			StaticLayout s = new StaticLayout(anz, tp, width-padding_right, Layout.Alignment.ALIGN_OPPOSITE, 1, 0, false);
			s.draw(canvas);
			
			if(landscape){
				String start = timegrid.get(i).getStart().getHour() + ":" + ((timegrid.get(i).getStart().getMinute()+"").length()==1 ? "0"+ timegrid.get(i).getStart().getMinute(): timegrid.get(i).getStart().getMinute()+"");
				String end = timegrid.get(i).getEnd().getHour() + ":" + ((timegrid.get(i).getEnd().getMinute()+"").length()==1 ? "0"+ timegrid.get(i).getEnd().getMinute(): timegrid.get(i).getEnd().getMinute()+"");
				
				canvas.translate(0, getResources().getDimension(R.dimen.gui_header_line1_line1_padding));
				s = new StaticLayout(start, tp2, width-padding_right, Layout.Alignment.ALIGN_OPPOSITE, 1, 0, false);
				s.draw(canvas);
				
				canvas.translate(0, getResources().getDimension(R.dimen.gui_timegrid_padding_line2_line3));
				
				s = new StaticLayout(end, tp2, width-padding_right, Layout.Alignment.ALIGN_OPPOSITE, 1, 0, false);
				s.draw(canvas);
				
				canvas.translate(0, -(getResources().getDimension(R.dimen.gui_header_line1_line1_padding) + 
						getResources().getDimension(R.dimen.gui_timegrid_padding_line2_line3)));
				
			}
			canvas.translate(0, (height-offsettop)/hours);
		}
	}
	
	private void setHours(int h){
		hours = h;
	}
	
	private void highlight(Canvas canvas){
		DateTime now = new DateTime();
		now.setHour(DateTimeUtils.getNow().getHour());
		now.setMinute(DateTimeUtils.getNow().getMinute());
			
		Collections.sort(timegrid);
		int position = -1;
		
		for(TimegridUnit tu : timegrid){
			DateTime start = new DateTime();
			start.setHour(tu.getStart().getHour());
			start.setMinute(tu.getStart().getMinute());
			DateTime end = new DateTime();
			end.setHour(tu.getEnd().getHour());
			end.setMinute(tu.getEnd().getMinute());

			if(now.compareTo(start) >= 0 && now.compareTo(end) <= 0){
				position = timegrid.indexOf(tu);
				break;
			}
		}
				
		
		if(position != -1){
			int border = getResources().getDimensionPixelSize(R.dimen.gui_stroke_width_2)/2;
			
			Rect r = new Rect(0, offsettop+(position*lessongheight)+border, width-border, ((position+1)*lessongheight)+offsettop-border);
			Paint p = new Paint();
			p.setStyle(Style.FILL);
			p.setColor(getResources().getColor(R.color.month_overlay_today));
			canvas.drawRect(r, p);	
		}
	}
	
	
	public void setTimeGrid(List<TimegridUnit> time, int off){
		timegrid = time;
		setHours(time.size());
		offsettop = off;
	}
	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			float y = event.getY();
			if(y > offsettop && y < height){
				y-=offsettop;
				y= (float) Math.ceil(y/((height-offsettop)/hours));
				y--;
				if(y >= timegrid.size())
					return true;
				String start = timegrid.get((int)y).getStart().getHour() + ":" + ((timegrid.get((int)y).getStart().getMinute()+"").length()==1 ? "0"+ timegrid.get((int)y).getStart().getMinute(): timegrid.get((int)y).getStart().getMinute()+"");
				String end = timegrid.get((int)y).getEnd().getHour() + ":" + ((timegrid.get((int)y).getEnd().getMinute()+"").length()==1 ? "0"+ timegrid.get((int)y).getEnd().getMinute(): timegrid.get((int)y).getEnd().getMinute()+"");
				String name = timegrid.get((int)y).getName();
				Toast.makeText(getContext(), name + ": "+start +" - "+end, Toast.LENGTH_SHORT).show();
			}
		}
		return true;
	}
	
	

}
