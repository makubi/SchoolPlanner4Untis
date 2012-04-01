package edu.htl3r.schoolplanner.gui.startup_wizard;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;
import edu.htl3r.schoolplanner.gui.AsyncTaskProgress;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import edu.htl3r.schoolplanner.gui.WelcomeScreen;
import edu.htl3r.schoolplanner.gui.loginTask.LoginTask;
import edu.htl3r.schoolplanner.gui.loginTask.LoginTaskStatus;
import edu.htl3r.schoolplanner.gui.loginTask.OnLoginTaskUpdateListener;

/**
 * Startup-Assistent Seite 3, welche den Login mit den vorher eingegebenen Daten testet.
 */
public class StartupWizardLoginInformationCheck extends SchoolPlannerActivity implements OnLoginTaskUpdateListener{

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
	private static final String SAVED_INSTANCE_KEY_PROGRESS_VISIBILITY = "progressVisible";
	private static final String SAVED_INSTANCE_KEY_LOGIN_CHECKED = "loginChecked";
	private static final String SAVED_INSTANCE_KEY_CLASS_CHECKED = "classChecked";
	private static final String SAVED_INSTANCE_KEY_TEACHER_CHECKED = "teacherChecked";
	private static final String SAVED_INSTANCE_KEY_ROOMS_CHECKED = "roomsChecked";
	private static final String SAVED_INSTANCE_KEY_SUBJECTS_CHECKED = "subjectsChecked";

	private static final String SAVED_INSTANCE_KEY_LOGIN_TASK_FINISHED = "loginTaskFinished";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_wizard_login_information_check);
		
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
	public void loginTaskFinished(Bundle data) {
		loginTaskFinished = true;
	}
	
	@Override
	public void showToastMessage(String message) {
		infoText.setText(message);
		// TODO: auch mit status changed
		backButton.setVisibility(View.VISIBLE);
	}

	@Override
	public void statusChanged(String status) {
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
			loginImage.setImageResource(R.drawable.ic_delete);
		}
	}

	
	private void setLoginSuccess() {
		loginImage.setChecked(true);
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
		AsyncTask<Void, AsyncTaskProgress, Boolean> loginTask = loginListener.getAsyncTask();
		if(loginTask != null && !loginTask.isCancelled() && !(loginTask.getStatus() == Status.FINISHED)) {
			loginTask.cancel(true);
		}
		
		super.onBackPressed();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		outState.putInt(SAVED_INSTANCE_KEY_PROGRESS_VISIBILITY, progressWheel.getVisibility());
		outState.putBoolean(SAVED_INSTANCE_KEY_LOGIN_CHECKED, loginImage.getChecked());
		outState.putBoolean(SAVED_INSTANCE_KEY_CLASS_CHECKED, classListImage.getChecked());
		outState.putBoolean(SAVED_INSTANCE_KEY_TEACHER_CHECKED, teacherListImage.getChecked());
		outState.putBoolean(SAVED_INSTANCE_KEY_ROOMS_CHECKED, roomListImage.getChecked());
		outState.putBoolean(SAVED_INSTANCE_KEY_SUBJECTS_CHECKED, subjectListImage.getChecked());
		
		outState.putString(SAVED_INSTANCE_KEY_INFO_TEXT, infoText.getText().toString());
		
		outState.putBoolean(SAVED_INSTANCE_KEY_LOGIN_TASK_FINISHED, loginTaskFinished);
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		progressWheel.setVisibility(savedInstanceState.getInt(SAVED_INSTANCE_KEY_PROGRESS_VISIBILITY));
		if(savedInstanceState.getBoolean(SAVED_INSTANCE_KEY_LOGIN_CHECKED)) setLoginSuccess();
		if(savedInstanceState.getBoolean(SAVED_INSTANCE_KEY_CLASS_CHECKED)) setClassListSuccess();
		if(savedInstanceState.getBoolean(SAVED_INSTANCE_KEY_TEACHER_CHECKED)) setTeacherListSuccess();
		if(savedInstanceState.getBoolean(SAVED_INSTANCE_KEY_ROOMS_CHECKED)) setRoomListSuccess();
		if(savedInstanceState.getBoolean(SAVED_INSTANCE_KEY_SUBJECTS_CHECKED)) setSubjectListSuccess();
		infoText.setText(savedInstanceState.getString(SAVED_INSTANCE_KEY_INFO_TEXT));
		
		loginTaskFinished = savedInstanceState.getBoolean(SAVED_INSTANCE_KEY_LOGIN_TASK_FINISHED);
		
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		AsyncTask<Void, AsyncTaskProgress, Boolean> loginTask = loginListener.getAsyncTask();
		if(loginTask != null && !loginTask.isCancelled() && !(loginTask.getStatus() == Status.FINISHED)) {
			loginTask.cancel(true);
		}
		super.onDestroy();
	}
	
	private int getColor(int resId) {
		return getResources().getColor(resId);
	}

}
