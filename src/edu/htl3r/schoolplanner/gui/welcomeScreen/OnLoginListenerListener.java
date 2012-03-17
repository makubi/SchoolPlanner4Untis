package edu.htl3r.schoolplanner.gui.welcomeScreen;

import android.os.Bundle;

public interface OnLoginListenerListener {
	public void statusChanged(String status);
	public void loginListenerFinished(Bundle data);
	public void onPostLoginListenerFinished(boolean success);
}
