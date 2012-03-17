package edu.htl3r.schoolplanner.gui.loginListener;

import android.os.Bundle;

public interface OnLoginTaskUpdateListener {
	public void statusChanged(String status);
	public void loginTaskFinished(Bundle data);
}
