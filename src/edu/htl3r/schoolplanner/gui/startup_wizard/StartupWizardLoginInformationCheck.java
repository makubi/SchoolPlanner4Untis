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
package edu.htl3r.schoolplanner.gui.startup_wizard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import edu.htl3r.schoolplanner.gui.WelcomeScreen;
import edu.htl3r.schoolplanner.gui.loginTask.AsyncTaskProgress;
import edu.htl3r.schoolplanner.gui.loginTask.LoginTask;
import edu.htl3r.schoolplanner.gui.loginTask.LoginTaskStatus;
import edu.htl3r.schoolplanner.gui.loginTask.OnUpdatePublishingAsyncTaskListener;

/**
 * Startup-Assistent Seite 3, welche den Login mit den vorher eingegebenen Daten testet.
 */
public class StartupWizardLoginInformationCheck extends SchoolPlannerActivity implements OnUpdatePublishingAsyncTaskListener<Bundle>{

	private ProgressBar progressWheel;
	
	private TextView loginText;
	private DrawableCheckBox loginImage;
	
	private TextView classListText;
	private DrawableCheckBox classListImage;
	
	private TextView teacherListText;
	private DrawableCheckBox teacherListImage;
	
	private TextView roomListText;
	private DrawableCheckBox roomListImage;
	
	private TextView subjectListText;
	private DrawableCheckBox subjectListImage;
	
	private TextView infoText;
	
	private Button finishButton;
	private Button backButton;
	
	private LoginTask loginListener;
	
	private LoginSet activeSet;

	private boolean loginTaskFinished = false;
	
	private static final String SAVED_INSTANCE_KEY_INFO_TEXT = "infoText";
	private static final String SAVED_INSTANCE_KEY_PROGRESS_VISIBILITY = "progressVisibility";
	private static final String SAVED_INSTANCE_KEY_LOGIN_CHECKED = "loginChecked";
	private static final String SAVED_INSTANCE_KEY_CLASS_CHECKED = "classChecked";
	private static final String SAVED_INSTANCE_KEY_TEACHER_CHECKED = "teacherChecked";
	private static final String SAVED_INSTANCE_KEY_ROOMS_CHECKED = "roomsChecked";
	private static final String SAVED_INSTANCE_KEY_SUBJECTS_CHECKED = "subjectsChecked";

	private static final String SAVED_INSTANCE_KEY_LOGIN_TASK_FINISHED = "loginTaskFinished";

	private static final String SAVED_INSTANCE_KEY_FINISH_BUTTON_VISIBILITY = "finishButtonVisibility";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_wizard_login_information_check);
		initTitle(getResources().getString(R.string.startup_wizard_header));

		
		loginText = (TextView) findViewById(R.id.startup_wizard_login_information_check_login_text);
		loginImage = (DrawableCheckBox) findViewById(R.id.startup_wizard_login_information_check_login_image);
		
		classListText = (TextView) findViewById(R.id.startup_wizard_login_information_check_classlist_text);
		classListImage = (DrawableCheckBox) findViewById(R.id.startup_wizard_login_information_check_classlist_image);
		
		teacherListText = (TextView) findViewById(R.id.startup_wizard_login_information_check_teacherlist_text);
		teacherListImage = (DrawableCheckBox) findViewById(R.id.startup_wizard_login_information_check_teacherlist_image);
		
		roomListText = (TextView) findViewById(R.id.startup_wizard_login_information_check_roomlist_text);
		roomListImage = (DrawableCheckBox) findViewById(R.id.startup_wizard_login_information_check_roomlist_image);
		
		subjectListText = (TextView) findViewById(R.id.startup_wizard_login_information_check_subjectlist_text);
		subjectListImage = (DrawableCheckBox) findViewById(R.id.startup_wizard_login_information_check_subjectlist_image);
		
		progressWheel = (ProgressBar) findViewById(R.id.startup_wizard_login_information_check_progress);
		
		infoText = (TextView) findViewById(R.id.startup_wizard_login_information_check_info_text);
		
		finishButton = (Button) findViewById(R.id.startup_wizard_login_information_check_finish_button);
		finishButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((SchoolPlannerApp) getApplication()).getLoginSetManager().addLoginSet(activeSet);
				
				Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		backButton = (Button) findViewById(R.id.startup_wizard_login_information_check_back_button);
		backButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
			
		});
		
		Bundle extras = getIntent().getExtras();
		activeSet = new LoginSet(extras.getString(LoginSetConstants.nameKey), extras.getString(LoginSetConstants.serverUrlKey), extras.getString(LoginSetConstants.schoolKey), extras.getString(LoginSetConstants.usernameKey), extras.getString(LoginSetConstants.passwordKey), extras.getBoolean(LoginSetConstants.sslOnlyKey));
		loginListener = new LoginTask(this);
		loginListener.addListener(this);
		
		
		loginText.setTextColor(getColor(R.color.text));
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		
		if(!loginTaskFinished) {
			loginListener.performLogin(activeSet);
		}
		
		super.onPostCreate(savedInstanceState);
	}
	
	@Override
	public void setInProgress(String message, boolean active) {
		progressWheel.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
	}
	
	@Override
	public void showToastMessage(String message) {
		infoText.setText(message);
		// TODO: auch mit status changed
		backButton.setVisibility(View.VISIBLE);
	}

	private void setLoginSuccess() {
		loginImage.setChecked(true);
		loginImage.setStatus(edu.htl3r.schoolplanner.gui.startup_wizard.DrawableCheckBox.Status.CHECKED);
		loginListener.skipLogin();
		classListText.setTextColor(getColor(R.color.text));
	}
	
	private void setClassListSuccess() {
		classListImage.setChecked(true);
		loginListener.skipClassListLoading();
		teacherListText.setTextColor(getColor(R.color.text));
	}
	
	private void setTeacherListSuccess() {
		teacherListImage.setChecked(true);
		loginListener.skipTeacherListLoading();
		roomListText.setTextColor(getColor(R.color.text));
	}
	
	private void setRoomListSuccess() {
		roomListImage.setChecked(true);
		loginListener.skipRoomListLoading();
		subjectListText.setTextColor(getColor(R.color.text));
	}
	
	private void setSubjectListSuccess() {
		loginListener.skipSubjectListLoading();
		subjectListImage.setChecked(true);
	}
	
	@Override
	public void onBackPressed() {
		AsyncTask<?, ?, ?> loginTask = loginListener.getAsyncTask();
		if(loginTask != null && !loginTask.isCancelled() && !(loginTask.getStatus() == Status.FINISHED)) {
			loginTask.cancel(true);
		}
		
		super.onBackPressed();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(SAVED_INSTANCE_KEY_PROGRESS_VISIBILITY, progressWheel.getVisibility());
		
		outState.putSerializable(SAVED_INSTANCE_KEY_LOGIN_CHECKED, loginImage.getStatus());
		
		outState.putBoolean(SAVED_INSTANCE_KEY_CLASS_CHECKED, classListImage.getChecked());
		outState.putBoolean(SAVED_INSTANCE_KEY_TEACHER_CHECKED, teacherListImage.getChecked());
		outState.putBoolean(SAVED_INSTANCE_KEY_ROOMS_CHECKED, roomListImage.getChecked());
		outState.putBoolean(SAVED_INSTANCE_KEY_SUBJECTS_CHECKED, subjectListImage.getChecked());
		
		outState.putString(SAVED_INSTANCE_KEY_INFO_TEXT, infoText.getText().toString());
		
		outState.putBoolean(SAVED_INSTANCE_KEY_LOGIN_TASK_FINISHED, loginTaskFinished);
		
		outState.putInt(SAVED_INSTANCE_KEY_FINISH_BUTTON_VISIBILITY, finishButton.getVisibility());
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		progressWheel.setVisibility(savedInstanceState.getInt(SAVED_INSTANCE_KEY_PROGRESS_VISIBILITY));
		
		edu.htl3r.schoolplanner.gui.startup_wizard.DrawableCheckBox.Status loginImageStatus = (edu.htl3r.schoolplanner.gui.startup_wizard.DrawableCheckBox.Status) savedInstanceState.getSerializable(SAVED_INSTANCE_KEY_LOGIN_CHECKED);
		if(loginImageStatus == edu.htl3r.schoolplanner.gui.startup_wizard.DrawableCheckBox.Status.CHECKED) setLoginSuccess();
		loginImage.setStatus(loginImageStatus);
		
		if(savedInstanceState.getBoolean(SAVED_INSTANCE_KEY_CLASS_CHECKED)) setClassListSuccess();
		if(savedInstanceState.getBoolean(SAVED_INSTANCE_KEY_TEACHER_CHECKED)) setTeacherListSuccess();
		if(savedInstanceState.getBoolean(SAVED_INSTANCE_KEY_ROOMS_CHECKED)) setRoomListSuccess();
		if(savedInstanceState.getBoolean(SAVED_INSTANCE_KEY_SUBJECTS_CHECKED)) setSubjectListSuccess();
		infoText.setText(savedInstanceState.getString(SAVED_INSTANCE_KEY_INFO_TEXT));
		
		loginTaskFinished = savedInstanceState.getBoolean(SAVED_INSTANCE_KEY_LOGIN_TASK_FINISHED);
		
		finishButton.setVisibility(savedInstanceState.getInt(SAVED_INSTANCE_KEY_FINISH_BUTTON_VISIBILITY));
		
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		AsyncTask<?, ?, ?> loginTask = loginListener.getAsyncTask();
		if(loginTask != null && !loginTask.isCancelled() && !(loginTask.getStatus() == Status.FINISHED)) {
			loginTask.cancel(true);
		}
		super.onDestroy();
	}
	
	private int getColor(int resId) {
		return getResources().getColor(resId);
	}

	public void onStatusUpdated(String status) {
		if(status.equals(LoginTaskStatus.LOGIN_SUCCESS)) {
			setLoginSuccess();
		}
		else if(status.equals(LoginTaskStatus.CLASSLIST_SUCCESS)) {
			setClassListSuccess();
		}
		else if(status.equals(LoginTaskStatus.TEACHERLIST_SUCCESS)) {
			setTeacherListSuccess();
		}
		else if(status.equals(LoginTaskStatus.ROOMLIST_SUCCESS)) {
			setRoomListSuccess();
		}
		else if(status.equals(LoginTaskStatus.SUBJECTLIST_SUCCESS)) {
			setSubjectListSuccess();
		}
		else if(status.equals(LoginTaskStatus.MASTERDATA_SUCCESS)) {
			progressWheel.setVisibility(View.INVISIBLE);
			finishButton.setVisibility(View.VISIBLE);
		}
	
		else if(status.equals(LoginTaskStatus.LOGIN_BAD_CREDENTIALS)) {
			loginImage.setStatus(edu.htl3r.schoolplanner.gui.startup_wizard.DrawableCheckBox.Status.ERROR);
		}
	}

	@Override
	public void onCancelled() {
		setInProgress("", false);
	}

	@Override
	public void onPostExecute(Bundle result) {
		setInProgress("", false);
		loginTaskFinished = true;
	}

	@Override
	public void onProgressUpdate(AsyncTaskProgress progress) {
		
		String progressMessage = progress.getProgressMessage();
		String toastMessage = progress.getToastMessage();
		String status = progress.getStatus();
		
		if(progressMessage != null) {
			setInProgress(progressMessage, progress.isShowProgressWheel());
		}
		if(toastMessage != null) {
			showToastMessage(toastMessage);
		}
		
		if(status != null) {
			onStatusUpdated(status);
		}
	}

	@Override
	public void onPreExecute() {
		setInProgress(getString(R.string.login_in_progress), true);
	}

}
