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
		
		if(progressMessage != null) {
			welcomeScreen.setInProgress(progressMessage, progress.isShowProgressWheel());
		}
		if(toastMessage != null) {
			welcomeScreen.showToastMessage(toastMessage);
		}
	}

	@Override
	public void onPreExecute() {
		welcomeScreen.setInProgress(welcomeScreen.getString(R.string.login_in_progress), true);
	}

}
