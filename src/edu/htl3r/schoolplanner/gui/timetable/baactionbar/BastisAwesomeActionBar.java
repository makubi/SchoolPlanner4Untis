package edu.htl3r.schoolplanner.gui.timetable.baactionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;

public class BastisAwesomeActionBar extends RelativeLayout {

	private BADropdown dropdown;
	private BAAction month;
	private BAHomeAction home;
	private BAAction dropdown_action;
	private TextView title;

	public BastisAwesomeActionBar(Context context) {
		super(context);
	}

	public BastisAwesomeActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BastisAwesomeActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(BADropdown dropdown) {
		this.dropdown = dropdown;
		title = (TextView)findViewById(R.id.baactionbar_title);
		setHomeIcon();
		setMonthIcon();
		setDropDownIcon();
		initActionListenerDropDown();
	}

	private void setHomeIcon() {
		home = (BAHomeAction) findViewById(R.id.baactionbar_home);
		home.setIcon(getResources().getDrawable(R.drawable.logo));
		home.initProgressBar();
	}

	private void setMonthIcon() {
		month = (BAAction) findViewById(R.id.baactionbar_month);
		month.setIcon(getResources().getDrawable(R.drawable.ic_menu_month));
	}

	private void setDropDownIcon() {
		dropdown_action = (BAAction) findViewById(R.id.baactionbar_dropdown_action);
		dropdown_action.setIcon(getResources().getDrawable(R.drawable.ic_actionbar_dropdown_list));
	}

	private void initActionListenerDropDown() {
		dropdown_action.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dropdown.setVisibility((dropdown.isShown()) ? View.GONE : View.VISIBLE);
			}
		});
		
		dropdown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("basti", "autsch");				
			}
		});

	}
	
	public void setText(ViewType vt){
		title.setText(vt.getName());
	}

	public void setProgress(boolean active) {
		home.startProgressBar(active);
	}

}
