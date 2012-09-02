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
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.timetable.TransportClasses.LastRefreshTransferObject;

public class BARefreshAction extends BAAction {

	private ProgressBar progressbar;
	private TextView lastRefreshText;

	public BARefreshAction(Context context) {
		super(context);
	}

	public BARefreshAction(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BARefreshAction(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void initProgressBar() {
		progressbar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(CENTER_VERTICAL);
		layoutParams.addRule(CENTER_HORIZONTAL);
		addView(progressbar, layoutParams);
	}

	public void initTextView() {
		lastRefreshText = new TextView(getContext());
		lastRefreshText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(ALIGN_PARENT_BOTTOM);
		layoutParams.addRule(ALIGN_PARENT_RIGHT);
		layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.gui_actionbar_action_refresh_text_margin_bottom));

		addView(lastRefreshText, layoutParams);
	}

	public void startProgressBar(boolean scroll) {
		if (scroll) {
			progressbar.setVisibility(VISIBLE);
			icon.setVisibility(INVISIBLE);
		} else {
			icon.setVisibility(VISIBLE);
			progressbar.setVisibility(INVISIBLE);
		}
	}

	public void setLastRefresh(LastRefreshTransferObject lastRefreshTransferObject) {
		Log.d("basti", "lasR: " + lastRefreshTransferObject.getDiffernceForDisply());
		lastRefreshText.setTextColor(lastRefreshTransferObject.getTextColor());
		lastRefreshText.setText(lastRefreshTransferObject.getDiffernceForDisply());
	}

}
