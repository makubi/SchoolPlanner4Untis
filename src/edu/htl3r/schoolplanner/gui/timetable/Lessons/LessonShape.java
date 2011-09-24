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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class LessonShape extends ShapeDrawable {

	Paint fillpaint, strokepaint;
	private static final int WIDTH = 4;

	public LessonShape(Shape s, int strokecolor, int fillcolor) {
		super(s);
		
		fillpaint = this.getPaint();
		strokepaint = new Paint();
		
		fillpaint.setStyle(Paint.Style.FILL);
		fillpaint.setColor(fillcolor);
		
		strokepaint.setStyle(Paint.Style.FILL_AND_STROKE);
		strokepaint.setStrokeWidth(WIDTH);
		strokepaint.setColor(strokecolor);
		strokepaint.setAlpha(255);
	}

	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint fillpaint) {
		shape.draw(canvas, fillpaint);
		shape.draw(canvas, strokepaint);
	}

}
