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
package edu.htl3r.schoolplanner.gui.listener;

import java.io.Serializable;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.ErrorMessage;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.gui.AsyncTaskProgress;
import edu.htl3r.schoolplanner.gui.BundleConstants;
import edu.htl3r.schoolplanner.gui.SelectScreen;
import edu.htl3r.schoolplanner.gui.WelcomeScreen;

public class LoginListener implements OnItemClickListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WelcomeScreen welcomescreen;

	public LoginListener(WelcomeScreen ws) {
		welcomescreen = ws;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		final LoginSet selectedEntry = welcomescreen.getLoginManager().getLoginSetOnPosition(position);
		performLogin(selectedEntry);
	}
	
	public void performLogin(final LoginSet selectedEntry) {
		AsyncTask<Void, AsyncTaskProgress, Void> task = new AsyncTask<Void, AsyncTaskProgress, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				
				SchoolPlannerApp app = (SchoolPlannerApp) welcomescreen.getApplication();
				
				// Beim Login die Daten im RAM leeren, damit aus der Datenbank die passenden Daten geladen werden
				app.getData().clearInternalCache();
				
				app.getData().setLoginCredentials(selectedEntry);
				DataFacade<Boolean> authenticate = app.getData().authenticate();
				if(authenticate.isSuccessful()) {
					boolean auth = authenticate.getData();
					if(auth) {
						Intent t = new Intent(welcomescreen, SelectScreen.class);
						Bundle bundle = new Bundle();
						
						AsyncTaskProgress schoolClassProgress = new AsyncTaskProgress();
						schoolClassProgress.setProgressMessage(welcomescreen.getString(R.string.loading_school_classes));
						schoolClassProgress.setShowProgressWheel(true);
						
						publishProgress(schoolClassProgress);
						bundle.putSerializable(BundleConstants.SCHOOL_CLASS_LIST, app.getData().getSchoolClassList());
						
						
						AsyncTaskProgress schoolTeacherProgress = new AsyncTaskProgress();
						schoolClassProgress.setProgressMessage(welcomescreen.getString(R.string.loading_school_teacher));
						schoolClassProgress.setShowProgressWheel(true);
						
						publishProgress(schoolTeacherProgress);
						bundle.putSerializable(BundleConstants.SCHOOL_TEACHER_LIST, app.getData().getSchoolTeacherList());

						
						AsyncTaskProgress schoolRoomProgress = new AsyncTaskProgress();
						schoolClassProgress.setProgressMessage(welcomescreen.getString(R.string.loading_school_rooms));
						schoolClassProgress.setShowProgressWheel(true);
						
						publishProgress(schoolRoomProgress);
						bundle.putSerializable(BundleConstants.SCHOOL_ROOM_LIST, app.getData().getSchoolRoomList());
						
						
						AsyncTaskProgress schoolSubjectProgress = new AsyncTaskProgress();
						schoolClassProgress.setProgressMessage(welcomescreen.getString(R.string.loading_school_subjects));
						schoolClassProgress.setShowProgressWheel(true);
						
						publishProgress(schoolSubjectProgress);
						bundle.putSerializable(BundleConstants.SCHOOL_SUBJECT_LIST, app.getData().getSchoolSubjectList());
						
						
						AsyncTaskProgress loginFinished = new AsyncTaskProgress();
						schoolClassProgress.setProgressMessage(welcomescreen.getString(R.string.loading_next_screen));
						loginFinished.setShowProgressWheel(true);
						
						publishProgress(loginFinished);
						
						t.putExtras(bundle);
						welcomescreen.startActivity(t);						
					}
					else {
						AsyncTaskProgress loginFailed = new AsyncTaskProgress();
						loginFailed.setToastMessage(welcomescreen.getString(R.string.login_data_wrong));
						publishProgress(loginFailed);
					}
				}
				else {
					AsyncTaskProgress loginError = new AsyncTaskProgress();
					loginError.setToastMessage(welcomescreen.getString(R.string.error_occurred));
					publishProgress(loginError);
					
					ErrorMessage error = authenticate.getErrorMessage();
					String additionalInfo = error.getAdditionalInfo();
					int errorCode = error.getErrorCode();
					Throwable exception = error.getException();
					
					Log.e("login","========== ERROR");
					Log.e("login","info: "+additionalInfo);
					Log.e("login","code: "+errorCode);
					Log.e("login","e: "+exception.getMessage(),exception);
				}
				
				
			
				return null;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				welcomescreen.setInProgress(welcomescreen.getString(R.string.login_in_progress), true);
			}
			
			@Override
			protected void onProgressUpdate(AsyncTaskProgress... values) {
				super.onProgressUpdate(values);
				
				AsyncTaskProgress progress = values[0];
				String progressMessage = progress.getProgressMessage();
				String toastMessage = progress.getToastMessage();
				
				if(progressMessage != null) {
					welcomescreen.setInProgress(progressMessage, progress.isShowProgressWheel());
				}
				if(toastMessage != null) {
					welcomescreen.showToastMessage(toastMessage);
				}
				
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				welcomescreen.setInProgress("", false);
			}
		};

		task.execute();
	}

}
