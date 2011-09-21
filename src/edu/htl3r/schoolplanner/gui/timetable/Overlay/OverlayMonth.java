package edu.htl3r.schoolplanner.gui.timetable.Overlay;

import javax.print.attribute.standard.Finishings;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.timetable.ViewBasti;

public class OverlayMonth extends Dialog implements OnClickListener {

	private ImageButton next, prev;
	private TextView title;
	private DateTime date;

	private DateTime start, end;

	private OverlayCalendarView calendar;
	private RelativeLayout month_container;
	
	private ViewBasti viewbasti;

	public OverlayMonth(Context context, ViewBasti vb) {
		super(context);
		viewbasti = vb;
		init();
	}

	private void init() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.overlay_month);

		calendar = new OverlayCalendarView(getContext(),this);

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

		if (start == null && end == null) {
			start = new DateTime();
			start.set(1 - (50 * 7), d.getMonth(), d.getYear());
			end = new DateTime();
			end.set(1 + (50 * 7), d.getMonth(), d.getYear());
		}
		addCalendar();
	}

	@Override
	public void onClick(View v) {
		DateTime tmp = new DateTime();
		if (v == next) {
			tmp.set(date.getDay(), date.getMonth() + 1, date.getYear());

			if (tmp.getMonth() == end.getMonth() - 1 && tmp.getYear() == end.getYear()) {
				next.setEnabled(false);
			}
			
			if(!prev.isEnabled())
				prev.setEnabled(true);
				
		} else if (v == prev) {
			tmp.set(date.getDay(), date.getMonth() - 1, date.getYear());

			if (tmp.getMonth() == start.getMonth() + 1 && tmp.getYear() == start.getYear()) {
				prev.setEnabled(false);
			}
			
			if(!next.isEnabled())
				next.setEnabled(true);
		}
		setDate(tmp);
	}

	private void setHeader() {
		String header = DateTimeUtils.getMonthName(date) + ", " + date.getYear();
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
	
	public void displayChoosenWeek(int day){
		DateTime ret = new DateTime();
		ret.set(day, date.getMonth(), date.getYear());
		viewbasti.setDateforDialog(ret);
		dismiss();
	}

}
