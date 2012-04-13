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
package edu.htl3r.schoolplanner.gui.startup_wizard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import edu.htl3r.schoolplanner.R;

public class DrawableCheckBox extends ImageView {
	
	private boolean checked;
	private final int BUTTON_UNCHECKED_RESOURCE = R.drawable.btn_check_buttonless_off;
	private final int BUTTON_CHECKED_ON_RESOURCE = R.drawable.btn_check_buttonless_on;
	private final int BUTTON_ERROR_RESOURCE = R.drawable.ic_delete;
	
	public static enum Status {
		CHECKED, UNCHECKED, ERROR
	}
	
	private Status status = Status.UNCHECKED;
	
	public DrawableCheckBox(Context context) {
		super(context);
		
		initView();
	}
	
	public DrawableCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initView();
	}

	public DrawableCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initView();
	}
	
	private void initView() {
		checked = false;
		setImageResource(BUTTON_UNCHECKED_RESOURCE);
	}
	
	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
		setImageResource(checked ? BUTTON_CHECKED_ON_RESOURCE : BUTTON_UNCHECKED_RESOURCE);
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
		
		switch (status) {
		case CHECKED:
			setImageResource(BUTTON_CHECKED_ON_RESOURCE);
			break;
		case UNCHECKED:
			setImageResource(BUTTON_UNCHECKED_RESOURCE);
			break;
		case ERROR:
			setImageResource(BUTTON_ERROR_RESOURCE);
			break;
		default:
			break;
		}
	}
}
