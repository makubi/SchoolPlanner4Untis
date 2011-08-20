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
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.ErrorMessage;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.gui.BundleConstants;
import edu.htl3r.schoolplanner.gui.DummyBackend;
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
		//final LoginSet selectedEntry = welcomescreen.getLoginManager().getLoginSetOnPosition(position);

		AsyncTask<Void, String, Void> task = new AsyncTask<Void, String, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				
				// TODO: Do login here
				/*SchoolPlannerApp app = (SchoolPlannerApp) welcomescreen.getApplication();
				
				app.getData().setLoginCredentials(selectedEntry);
				DataFacade<Boolean> authenticate = app.getData().authenticate();
				if(authenticate.isSuccessful()) {
					boolean auth = authenticate.getData();
					if(auth) {
						Log.d("Misc","Authentication successful");
					}
					else {
						Log.d("Misc","Authentication not successful");
					}
				}
				else {
					ErrorMessage error = authenticate.getErrorMessage();
					String additionalInfo = error.getAdditionalInfo();
					int errorCode = error.getErrorCode();
					Throwable exception = error.getException();
					Log.d("Misc","info: "+additionalInfo);
					Log.d("Misc","code: "+errorCode);
					Log.d("Misc","e: "+exception.getMessage());
				}*/
				
				Intent t = new Intent(welcomescreen, SelectScreen.class);
				Bundle bundle = new Bundle();
				DummyBackend dummy = new DummyBackend();
				publishProgress("Retrieving school classes...");
				//bundle.putSerializable("schoolClassList", app.getData().getSchoolClassList());
				bundle.putSerializable(BundleConstants.SCHOOL_CLASS_LIST, dummy.getSchoolClassList());
				
				publishProgress("Retrieving school teacher...");
				//bundle.putSerializable("schoolTeacherList", app.getData().getSchoolTeacherList());
				bundle.putSerializable(BundleConstants.SCHOOL_TEACHER_LIST, dummy.getSchoolTeacherList());

				publishProgress("Retrieving school rooms...");
				//bundle.putSerializable("schoolRoomList", app.getData().getSchoolRoomList());
				bundle.putSerializable(BundleConstants.SCHOOL_ROOM_LIST, dummy.getSchoolRoomList());
				
				publishProgress("Retrieving school subjects...");
				//bundle.putSerializable("schoolSubjectList", app.getData().getSchoolSubjectList());
				bundle.putSerializable(BundleConstants.SCHOOL_SUBJECT_LIST, dummy.getSchoolSubjectList());
				
				t.putExtras(bundle);
				welcomescreen.startActivity(t);
			
				return null;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				welcomescreen.setInProgress("Login in progress...", true);
			}
			
			@Override
			protected void onProgressUpdate(String... values) {
				super.onProgressUpdate(values);
				welcomescreen.setInProgress(values[0], true);
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
