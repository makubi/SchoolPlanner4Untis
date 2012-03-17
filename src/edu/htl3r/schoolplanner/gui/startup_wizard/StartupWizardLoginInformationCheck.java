package edu.htl3r.schoolplanner.gui.startup_wizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import edu.htl3r.schoolplanner.gui.WelcomeScreen;
import edu.htl3r.schoolplanner.gui.welcomeScreen.LoginListener;
import edu.htl3r.schoolplanner.gui.welcomeScreen.LoginListenerStatus;
import edu.htl3r.schoolplanner.gui.welcomeScreen.OnLoginListenerListener;

/**
 * Startup-Assistent Seite 3, welche den Login mit den vorher eingegebenen Daten testet.
 */
public class StartupWizardLoginInformationCheck extends SchoolPlannerActivity implements OnLoginListenerListener{

	private ProgressBar progressWheel;
	
	private TextView loginText;
	private ImageView loginImage;
	
	private TextView classListText;
	private ImageView classListImage;
	
	private TextView teacherListText;
	private ImageView teacherListImage;
	
	private TextView roomListText;
	private ImageView roomListImage;
	
	private TextView subjectListText;
	private ImageView subjectListImage;
	
	private Button finishButton;
	
	private LoginListener loginListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_wizard_login_information_check);
		
		loginText = (TextView) findViewById(R.id.startup_wizard_login_information_check_login_text);
		loginImage = (ImageView) findViewById(R.id.startup_wizard_login_information_check_login_image);
		
		classListText = (TextView) findViewById(R.id.startup_wizard_login_information_check_classlist_text);
		classListImage = (ImageView) findViewById(R.id.startup_wizard_login_information_check_classlist_image);
		
		teacherListText = (TextView) findViewById(R.id.startup_wizard_login_information_check_teacherlist_text);
		teacherListImage = (ImageView) findViewById(R.id.startup_wizard_login_information_check_teacherlist_image);
		
		roomListText = (TextView) findViewById(R.id.startup_wizard_login_information_check_roomlist_text);
		roomListImage = (ImageView) findViewById(R.id.startup_wizard_login_information_check_roomlist_image);
		
		subjectListText = (TextView) findViewById(R.id.startup_wizard_login_information_check_subjectlist_text);
		subjectListImage = (ImageView) findViewById(R.id.startup_wizard_login_information_check_subjectlist_image);
		
		progressWheel = (ProgressBar) findViewById(R.id.startup_wizard_login_information_check_progress);
		
		finishButton = (Button) findViewById(R.id.startup_wizard_login_information_check_finish_button);
		finishButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		loginListener = new LoginListener(this);
		Bundle extras = getIntent().getExtras();
		LoginSet loginSet = new LoginSet(extras.getString(LoginSetConstants.nameKey), extras.getString(LoginSetConstants.serverUrlKey), extras.getString(LoginSetConstants.schoolKey), extras.getString(LoginSetConstants.usernameKey), extras.getString(LoginSetConstants.passwordKey), false);
		((SchoolPlannerApp) getApplication()).getLoginSetManager().addLoginSet(loginSet);
		loginListener.performLogin(loginSet);
		
		loginText.setTextColor(R.color.text);
	}
	
	@Override
	public void setInProgress(String message, boolean active) {
		progressWheel.setVisibility(active ? View.VISIBLE : View.GONE);
	}

	@Override
	public void loginListenerFinished(Bundle data) {}

	@Override
	public void onPostLoginListenerFinished(boolean success) {
		
		progressWheel.setVisibility(View.INVISIBLE);
	}
	
//	@Override
//	public void showToastMessage(String message) {
//		
//	}
	
	// FIXME: erste seite back --> liste reloaden

	@Override
	public void statusChanged(String status) {
		if(status.equals(LoginListenerStatus.LOGIN_SUCCESS)) {
			loginImage.setImageResource(R.drawable.btn_check_buttonless_on);
			classListText.setTextColor(R.color.text);
		}
		else if(status.equals(LoginListenerStatus.CLASSLIST_SUCCESS)) {
			classListImage.setImageResource(R.drawable.btn_check_buttonless_on);
			teacherListText.setTextColor(R.color.text);
		}
		else if(status.equals(LoginListenerStatus.TEACHERLIST_SUCCESS)) {
			teacherListImage.setImageResource(R.drawable.btn_check_buttonless_on);
			roomListText.setTextColor(R.color.text);
		}
		else if(status.equals(LoginListenerStatus.ROOMLIST_SUCCESS)) {
			roomListImage.setImageResource(R.drawable.btn_check_buttonless_on);
			subjectListText.setTextColor(R.color.text);
		}
		else if(status.equals(LoginListenerStatus.SUBJECTLIST_SUCCESS)) {
			subjectListImage.setImageResource(R.drawable.btn_check_buttonless_on);
		}
		else if(status.equals(LoginListenerStatus.MASTERDATA_SUCCESS)) {
			finishButton.setVisibility(View.VISIBLE);
		}
		
		else if(status.equals(LoginListenerStatus.LOGIN_BAD_CREDENTIALS)) {
			loginImage.setImageResource(R.drawable.ic_delete);
		}
	}

}
