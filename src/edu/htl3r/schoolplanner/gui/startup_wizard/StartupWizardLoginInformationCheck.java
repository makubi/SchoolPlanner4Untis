package edu.htl3r.schoolplanner.gui.startup_wizard;

import android.content.Intent;
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
import edu.htl3r.schoolplanner.gui.welcomeScreen.LoginListener;
import edu.htl3r.schoolplanner.gui.welcomeScreen.OnLoginListenerFinishedListener;

/**
 * Startup-Assistent Seite 3, welche den Login mit den vorher eingegebenen Daten testet.
 */
public class StartupWizardLoginInformationCheck extends SchoolPlannerActivity implements OnLoginListenerFinishedListener{

	private Button finishButton;
	private TextView statusText;
	private ProgressBar progressWheel;
	
	private LoginListener loginListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_wizard_login_information_check);
		
		statusText = ((TextView) findViewById(R.id.startup_wizard_login_information_check_text));
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
	}
	
	@Override
	public void setInProgress(String message, boolean active) {
		statusText.setText(message);
		progressWheel.setVisibility(active ? View.VISIBLE : View.GONE);
	}

	@Override
	public void loginListenerFinished(Bundle data) {}

	@Override
	public void onPostLoginListenerFinished(boolean success) {
		statusText.setText(success ? "success" : "no success");
		progressWheel.setVisibility(View.GONE);
	}
	
	@Override
	public void showToastMessage(String message) {
		statusText.setText(message);
	}

}
