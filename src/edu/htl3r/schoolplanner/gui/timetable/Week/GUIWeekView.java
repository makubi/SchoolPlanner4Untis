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
package edu.htl3r.schoolplanner.gui.timetable.Week;

import android.content.Context;
import android.widget.RelativeLayout;

public class GUIWeekView extends RelativeLayout{

	
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

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
	    super.onLayout(changed,l, t, r, b);
	}
	
}
