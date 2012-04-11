package edu.htl3r.schoolplanner.gui.welcomeScreen;

import android.content.Intent;
import android.os.Bundle;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.SelectScreen;
import edu.htl3r.schoolplanner.gui.WelcomeScreen;
import edu.htl3r.schoolplanner.gui.loginTask.AsyncTaskProgress;
import edu.htl3r.schoolplanner.gui.loginTask.OnUpdatePublishingAsyncTaskListener;

public class WelcomeScreenLoginTaskListener implements OnUpdatePublishingAsyncTaskListener<Bundle> {

	private WelcomeScreen welcomeScreen;
	
	public WelcomeScreenLoginTaskListener(WelcomeScreen welcomeScreen) {
		this.welcomeScreen = welcomeScreen;
	}
	
	@Override
	public void onStatusUpdated(String status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelled() {
		welcomeScreen.setInProgress("", false);
	}

	@Override
	public void onPostExecute(Bundle result) {
		if(result != null) {
			Intent t = new Intent(welcomeScreen, SelectScreen.class);
			t.putExtras(result);
			welcomeScreen.startActivity(t);
		}
		else {
			welcomeScreen.setInProgress("", false);
		}
	}

	@Override
	public void onProgressUpdate(AsyncTaskProgress progress) {
		String progressMessage = progress.getProgressMessage();
		String toastMessage = progress.getToastMessage();
		String status = progress.getStatus();
		
		if(progressMessage != null) {
			welcomeScreen.setInProgress(progressMessage, progress.isShowProgressWheel());
		}
		if(toastMessage != null) {
			welcomeScreen.showToastMessage(toastMessage);
		}
		
		if(status != null) {
			onStatusUpdated(status);
		}
	}

	@Override
	public void onPreExecute() {
		welcomeScreen.setInProgress(welcomeScreen.getString(R.string.login_in_progress), true);
	}

}
