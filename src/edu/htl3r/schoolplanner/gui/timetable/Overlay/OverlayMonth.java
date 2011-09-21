package edu.htl3r.schoolplanner.gui.timetable.Overlay;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;

public class OverlayMonth extends Dialog implements OnClickListener {

	private ImageButton next, prev;
	private TextView title;
	private DateTime date;
	private OverlayCalendarView calendar;
	private RelativeLayout month_container;

	public OverlayMonth(Context context) {
		super(context);
		init();
	}

	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.overlay_month);

		calendar = new OverlayCalendarView(getContext());

		month_container = (RelativeLayout) findViewById(R.id.overlay_month_container);
		next = (ImageButton) findViewById(R.id.overlay_month_next);
		prev = (ImageButton) findViewById(R.id.overlay_month_prev);
		title = (TextView) findViewById(R.id.overlay_month_title);

		next.setOnClickListener(this);
		prev.setOnClickListener(this);

	}

	public void setDate(DateTime d) {
		date = new DateTime();
		date.set(1, d.getMonth(), d.getYear());
		addCalendar();
	}

	@Override
	public void onClick(View v) {
		if (v == next) {
			date.set(date.getDay(), date.getMonth() + 1, date.getYear());
		} else if (v == prev) {
			date.set(date.getDay(), date.getMonth() - 1, date.getYear());
		}
		addCalendar();
	}
	
	private void setHeader() {
		String header = monthToString(date.getMonth()) + ", " + date.getYear();
		title.setText(header);
	}

	private void addCalendar() {
		calendar.setFirstDay(date);
		month_container.removeView(calendar);
		
		RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		layout.addRule(RelativeLayout.BELOW, R.id.overlay_mont_header);
		layout.setMargins(0, 20, 0, 0);
		calendar.setLayoutParams(layout);
		month_container.addView(calendar);
		setHeader();
	}

	@Deprecated
	private String monthToString(int mon) {				//FIXME MATTHIAS MACH DAS RICHTIG MIT RESSOURCES UND DATETIMEUTILS
		switch (mon - 1) {
		case 0:
			return "January";
		case 1:
			return "February";
		case 2:
			return "March";
		case 3:
			return "April";
		case 4:
			return "May";
		case 5:
			return "June";
		case 6:
			return "July";
		case 7:
			return "August";
		case 8:
			return "September";
		case 9:
			return "October";
		case 10:
			return "November";
		case 11:
			return "Dezember";
		}
		return "Fuck";
	}
}
