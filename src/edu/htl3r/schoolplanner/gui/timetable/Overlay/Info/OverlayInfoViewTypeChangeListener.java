package edu.htl3r.schoolplanner.gui.timetable.Overlay.Info;

import android.view.View;
import android.view.View.OnClickListener;
import edu.htl3r.schoolplanner.gui.timetable.WeekView;

public class OverlayInfoViewTypeChangeListener implements OnClickListener{

	private OverlayInfo overlay;
	private WeekView weekview;
	
	public void setData(OverlayInfo oim, WeekView wv){
		overlay = oim;
		weekview = wv;
	}
	
	@Override
	public void onClick(View v) {
			ViewTypeBox vtb = (ViewTypeBox)v;
			vtb.setBackground(true);
			overlay.dismiss();
			weekview.changeViewType(vtb.getViewType());
	}

}
