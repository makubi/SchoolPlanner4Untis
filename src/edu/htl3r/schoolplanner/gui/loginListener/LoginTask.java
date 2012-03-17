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
package edu.htl3r.schoolplanner.gui.loginListener;

import java.io.Serializable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.ErrorMessage;
import edu.htl3r.schoolplanner.backend.network.ErrorCodes;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.gui.AsyncTaskProgress;
import edu.htl3r.schoolplanner.gui.BundleConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;

public class LoginTask implements OnItemClickListener, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private SchoolPlannerActivity parent;
	private OnLoginTaskUpdateListener loginListenerFinishedListener;
	
	private AsyncTask<Void, AsyncTaskProgress, Boolean> loginTask;
	
	private SchoolPlannerApp app;
	
	public LoginTask(SchoolPlannerActivity parent) {
		this.parent = parent;
		loginListenerFinishedListener = (OnLoginTaskUpdateListener) parent;
		app = (SchoolPlannerApp) parent.getApplication();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final LoginSet selectedEntry = app.getLoginSetManager().getLoginSetOnPosition((int) id);
		performLogin(selectedEntry);
	}
	
	public void performLogin(final LoginSet selectedEntry) {
		loginTask = new AsyncTask<Void, AsyncTaskProgress, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				Cache cache = app.getData();
				
				// Beim Login die Daten im RAM leeren, damit aus der Datenbank die passenden Daten geladen werden
				cache.clearInternalCache();

				cache.setLoginCredentials(selectedEntry);
				app.getLoginSetManager().setActiveLoginSet(selectedEntry);
				
				if(!isCancelled()) {
					DataFacade<Boolean> authenticate = cache.authenticate();
				if(authenticate.isSuccessful()) {
					boolean auth = authenticate.getData();
					if(auth) {
						Bundle bundle = new Bundle();
						
						AsyncTaskProgress loginProgress = new AsyncTaskProgress();
						loginProgress.setStatus(LoginTaskStatus.LOGIN_SUCCESS);
						publishProgress(loginProgress);
						
						if(!isCancelled()) {
							AsyncTaskProgress schoolClassProgress = new AsyncTaskProgress();
							schoolClassProgress.setProgressMessage(parent.getString(R.string.loading_school_classes));
							schoolClassProgress.setShowProgressWheel(true);
							schoolClassProgress.setStatus(LoginTaskStatus.CLASSLIST_SUCCESS);
							
							publishProgress(schoolClassProgress);
							bundle.putSerializable(BundleConstants.SCHOOL_CLASS_LIST, cache.getSchoolClassList());
						}
						
						if(!isCancelled()) {
							AsyncTaskProgress schoolTeacherProgress = new AsyncTaskProgress();
							schoolTeacherProgress.setProgressMessage(parent.getString(R.string.loading_school_teacher));
							schoolTeacherProgress.setShowProgressWheel(true);
							schoolTeacherProgress.setStatus(LoginTaskStatus.TEACHERLIST_SUCCESS);
							
							publishProgress(schoolTeacherProgress);
							bundle.putSerializable(BundleConstants.SCHOOL_TEACHER_LIST, cache.getSchoolTeacherList());
						}
						
						if(!isCancelled()) {
							AsyncTaskProgress schoolRoomProgress = new AsyncTaskProgress();
							schoolRoomProgress.setProgressMessage(parent.getString(R.string.loading_school_rooms));
							schoolRoomProgress.setShowProgressWheel(true);
							schoolRoomProgress.setStatus(LoginTaskStatus.ROOMLIST_SUCCESS);
							
							publishProgress(schoolRoomProgress);
							bundle.putSerializable(BundleConstants.SCHOOL_ROOM_LIST, cache.getSchoolRoomList());
						}
						
						if(!isCancelled()) {
							AsyncTaskProgress schoolSubjectProgress = new AsyncTaskProgress();
							schoolSubjectProgress.setProgressMessage(parent.getString(R.string.loading_school_subjects));
							schoolSubjectProgress.setShowProgressWheel(true);
							schoolSubjectProgress.setStatus(LoginTaskStatus.SUBJECTLIST_SUCCESS);
							
							publishProgress(schoolSubjectProgress);
							bundle.putSerializable(BundleConstants.SCHOOL_SUBJECT_LIST, cache.getSchoolSubjectList());
						}
						
						if(!isCancelled()) {
							AsyncTaskProgress loginFinished = new AsyncTaskProgress();
							loginFinished.setProgressMessage(parent.getString(R.string.loading_next_screen));
							loginFinished.setShowProgressWheel(true);
							loginFinished.setStatus(LoginTaskStatus.MASTERDATA_SUCCESS);
							
							publishProgress(loginFinished);
							
							loginListenerFinishedListener.loginTaskFinished(bundle);
							return true;
						}
					}
					else {
						AsyncTaskProgress loginFailed = new AsyncTaskProgress();
						loginFailed.setToastMessage(parent.getString(R.string.login_data_wrong));
						loginFailed.setStatus(LoginTaskStatus.LOGIN_BAD_CREDENTIALS);
						publishProgress(loginFailed);
					}
				}
				else {
					AsyncTaskProgress loginError = new AsyncTaskProgress();
					String errorMessage;
					
					ErrorMessage error = authenticate.getErrorMessage();
					String additionalInfo = error.getAdditionalInfo();
					int errorCode = error.getErrorCode();
					Throwable exception = error.getException();
					
					switch (errorCode) {
					
					case ErrorCodes.HTTP_HOST_CONNECTION_EXCEPTION:
						errorMessage = getString(R.string.error_connection_refused) + " " + selectedEntry.getServerUrl();
						break;
						
					case ErrorCodes.UNKNOWN_HOST_EXCEPTION:
						errorMessage = getString(R.string.error_unknown_host) + " " + selectedEntry.getServerUrl();
						break;
					case ErrorCodes.SOCKET_TIMEOUT_EXCEPTION:
						errorMessage = getString(R.string.error_socket_timeout);
						break;
						
					case ErrorCodes.SSL_FORCED_BUT_UNAVAILABLE:
						errorMessage = getString(R.string.error_ssl_forced_but_unavailable) + " " + selectedEntry.getServerUrl();
						break;
						
					case ErrorCodes.WEBUNTIS_SERVICE_EXCEPTION:
						errorMessage = getString(R.string.webuntis_error_occurred) + " " + errorCode+":"+additionalInfo;
						break;
							
					case ErrorCodes.JSON_EXCEPTION:
						errorMessage = getString(R.string.json_exception);
						break;
						
					case ErrorCodes.NETWORK_NEEDED_BUT_UNAVAILABLE:
						errorMessage = getString(R.string.error_network_needed_but_unavailable);
						break;
						
					default:
						errorMessage = getString(R.string.error_occurred) + " "+errorCode+" ::: "+additionalInfo;
						Log.e("login","========== ERROR");
						Log.e("login","info: "+additionalInfo);
						Log.e("login","code: "+errorCode);
						if(exception != null) {
							errorMessage += " ::: "+exception;
							Log.e("login","e: "+exception.getMessage(),exception);
						}
						break;
					}
					
					loginError.setToastMessage(errorMessage);
					publishProgress(loginError);
				}
				
				}
				loginListenerFinishedListener.loginTaskFinished(null);
				return false;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				parent.setInProgress(parent.getString(R.string.login_in_progress), true);
			}
			
			@Override
			protected void onProgressUpdate(AsyncTaskProgress... values) {
				super.onProgressUpdate(values);
				
				AsyncTaskProgress progress = values[0];
				String progressMessage = progress.getProgressMessage();
				String toastMessage = progress.getToastMessage();
				String status = progress.getStatus();
				
				if(progressMessage != null) {
					parent.setInProgress(progressMessage, progress.isShowProgressWheel());
				}
				if(toastMessage != null) {
					parent.showToastMessage(toastMessage);
				}
				
				if(status != null) {
					loginListenerFinishedListener.statusChanged(status);
				}
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if(!result)
					parent.setInProgress("", false);
			}
			
			@Override
			protected void onCancelled() {
				super.onCancelled();
				parent.setInProgress("", false);
			}
		};

		loginTask.execute();
	}

	public AsyncTask<Void, AsyncTaskProgress, Boolean> getAsyncTask() {
		return loginTask;
	}
	
	private String getString(int resId) {
		return parent.getString(resId);
	}

}
