package edu.htl3r.schoolplanner.gui.timetable.TransportClasses;

import edu.htl3r.schoolplanner.DateTime;
import android.graphics.Color;

public class LastRefreshTransferObject {

	private DateTime lastRefresh;
	private long differenceInMilliSeconds = 0;
	private int textColor = Color.WHITE;
	private String differnceForDisplay = "-";

	private final String DAY = "d";
	private final String HOUR = "h";
	private final String MINUTE = "m";

	public LastRefreshTransferObject(DateTime lastRefresh) {
		this.lastRefresh = lastRefresh;
		init();
	}
	
	public LastRefreshTransferObject(){
	}
	
	
	private void init() {
		differenceInMilliSeconds = System.currentTimeMillis() - lastRefresh.getAndroidTime().toMillis(true);
		float diff = differenceInMilliSeconds;
		diff = diff / (1000 * 60);
		differnceForDisplay = MINUTE;
		if (diff >= 60) {
			diff = diff / 60;
			differnceForDisplay = HOUR;
			if (diff >= 24) {
				diff /= 24;
				differnceForDisplay = DAY;
				textColor = Color.RED;
			}
		}
		differnceForDisplay = ((int) diff) + differnceForDisplay;
	}

	public long getDiffernceInMilliSeconds() {
		return differenceInMilliSeconds;
	}

	public float getDiffernceInHours() {
		return getDiffernceInMilliSeconds() / (1000 * 60 * 60);
	}

	public int getTextColor() {
		return textColor;
	}

	public String getDiffernceForDisply() {
		return differnceForDisplay;
	}
}
