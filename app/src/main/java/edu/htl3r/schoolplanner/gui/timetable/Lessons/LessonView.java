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
package edu.htl3r.schoolplanner.gui.timetable.Lessons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUILessonContainer;
import edu.htl3r.schoolplanner.gui.timetable.Week.GUIWeekView;

public class LessonView extends GUIWeekView {

	private int width, height;
	private LessonInfoManager infomanager;
	
	private boolean cancelhour = false;

	private TextView head, line1, line2;

	private Paint p;
	
	public LessonView(Context context) {
		super(context);
		setID(LESSON_ID);
		
		p = new Paint();
		p.setColor(Color.RED);
		p.setAlpha(100);
		p.setAntiAlias(true);
        p.setDither(true);
        p.setStrokeWidth(getResources().getDimension(R.dimen.gui_stroke_width_4));
	}

	
	public void setNeededData(GUILessonContainer l, ViewType vt) {
		infomanager = new LessonInfoManager(l, vt);
		
		if(!infomanager.getLessonContainer().isEmpty()){
			View.inflate(getContext(), R.layout.timetable_lesson_view, this);
			initLessonView();	
		}
	}

	private void initLessonView(){
		
		int background = infomanager.getBackgroundColor();
		setBackgroundColor(background);
		
		if(infomanager.getLessonContainer().isSomethinStrange() == GUILessonContainer.STRANGE || infomanager.getLessonContainer().isSomethinStrange() == GUILessonContainer.STRANGE_NORMAL){
			GradientDrawable back = (GradientDrawable) getResources().getDrawable(R.drawable.border_lesson);
			back.setColor(background);
			setBackgroundDrawable(back);
		}
		
		if (infomanager.getLessonContainer().allCancelled() && infomanager.getLessonContainer().isSomethinStrange() != GUILessonContainer.STRANGE_NORMAL) {
			cancelhour = true;
		}
		
		int txtcolor = setTextColor(background);

		head = (TextView) findViewById(R.id.timetable_lesson_title);
		head.setTextColor(txtcolor);
		head.setText(infomanager.getFirstLine());
		
		line1 = (TextView)findViewById(R.id.timetable_lesson_line_1);
		line1.setTextColor(txtcolor);
		line1.setText(infomanager.getSecondLine());
		
		line2 = (TextView)findViewById(R.id.timetable_lesson_line_2);
		
		if(line2 != null){
			line2.setTextColor(txtcolor);
			line2.setText(infomanager.getThirdLine());
		}
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(cancelhour)
			canvas.drawLine(0, 0, width, height, p);
	}
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = MeasureSpec.getSize(widthMeasureSpec);
		height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}
	
	private int setTextColor(int color){
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		
		// Magisch Formel von: http://www.nbdtech.com/Blog/archive/2008/04/27/Calculating-the-Perceived-Brightness-of-a-Color.aspx
		double brightness =  Math.sqrt( red * red * .241 + green * green * .691 +  blue * blue * .068);
		
		if(brightness > 100){
			return Color.BLACK;
		}else{
			return Color.WHITE;
		}
	}

	public DateTime getTime() {
		return infomanager.getDateTime();
	}

	public GUILessonContainer getLessonsContainer() {
		return infomanager.getLessonContainer();
	}

	public ViewType getViewType() {
		return infomanager.getVT();
	}

}
