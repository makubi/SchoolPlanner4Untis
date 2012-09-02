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

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.timetable.TransportClasses.LastRefreshTransferObject;

public class BastisAwesomeActionBar extends RelativeLayout {

	public final static int REFRESH = 1;
	public final static int MONTH = 2;
	public final static int HOME = 3;
	public final static int DROPDOWN = 4;
	public final static int LIST_CLASS = 41;
	public final static int LIST_TEACHER = 42;
	public final static int LIST_ROOMS = 43;
	public final static int LIST_SUBJECTS = 44;
	public final static int TEXT = 5;
	
	private BADropdown dropdown;
	private BAAction month;
	private BAAction home;
	private BAAction dropdown_action;
	private BARefreshAction refresh;
	private TextView title;
	private View everything;

	private ArrayList<BAActoinBarEvent> actionbarevent = new ArrayList<BastisAwesomeActionBar.BAActoinBarEvent>();

	public BastisAwesomeActionBar(Context context) {
		super(context);
	}

	public BastisAwesomeActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BastisAwesomeActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(BADropdown dropdown, View everything) {
		this.dropdown = dropdown;
		dropdown.setActionBar(this);
		this.everything = everything;
		title = (TextView) findViewById(R.id.baactionbar_title);
		setHomeIcon();
		setMonthIcon();
		setDropDownIcon();
		setRefreshIcon();
		initActionListenerDropDown();
		initText();
	}

	private void initText(){
		title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				closeDropDown();
				fireActionBarEvent(TEXT);
			}
		});
	}
	
	private void setHomeIcon() {
		home = (BAAction) findViewById(R.id.baactionbar_home);
		home.setIcon(getResources().getDrawable(R.drawable.logo));

		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closeDropDown();
				fireActionBarEvent(HOME);
			}
		});
	}

	private void setMonthIcon() {
		month = (BAAction) findViewById(R.id.baactionbar_month);
		month.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_month));

		month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closeDropDown();
				fireActionBarEvent(MONTH);
			}
		});
	}

	private void setDropDownIcon() {
		dropdown_action = (BAAction) findViewById(R.id.baactionbar_dropdown_action);
		dropdown_action.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_dropdown_list));
	}

	private void setRefreshIcon() {
		refresh = (BARefreshAction) findViewById(R.id.baactionbar_refresh);
		refresh.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_refresh));
		refresh.initProgressBar();
		refresh.initTextView();

		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closeDropDown();
				fireActionBarEvent(REFRESH);
			}
		});
	}

	private void initActionListenerDropDown() {
		dropdown_action.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dropdown.setVisibility((dropdown.isShown()) ? View.GONE : View.VISIBLE);
				fireActionBarEvent(DROPDOWN);
			}
		});

		everything.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					closeDropDown();
				return false;
			}
		});
	}

	public void setText(ViewType vt) {
		title.setText(vt.getName());
	}

	public void setProgress(boolean active) {
		refresh.startProgressBar(active);
	}

	public void closeDropDown() {
		dropdown.setVisibility((dropdown.isShown()) ? View.GONE : View.GONE);
	}

	public void addBAActionBarEvent(BAActoinBarEvent listener) {
		actionbarevent.add(listener);
	}

	public void removeBAActionBarEvent(BAActoinBarEvent listener) {
		actionbarevent.remove(listener);
	}

	public void fireActionBarEvent(int ID) {
		for (BAActoinBarEvent a : actionbarevent) {
			a.onBAActionbarActionClicked(ID);
		}
	}
	
	public void setLastRefresh(LastRefreshTransferObject lastRefreshTransferObject){
		refresh.setLastRefresh(lastRefreshTransferObject);
	}

	public interface BAActoinBarEvent {
		public void onBAActionbarActionClicked(int ID);
	}
}
