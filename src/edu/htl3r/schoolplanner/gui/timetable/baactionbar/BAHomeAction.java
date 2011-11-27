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
import android.util.AttributeSet;
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
