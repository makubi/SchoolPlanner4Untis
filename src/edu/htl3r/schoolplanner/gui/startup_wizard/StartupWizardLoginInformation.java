package edu.htl3r.schoolplanner.gui.startup_wizard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;
import edu.htl3r.schoolplanner.gui.BundleConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;

/**
 * Startup-Assistent Seite 2, welche den Benutzer auffordert, Login-Informationen anzugeben.
 */
public class StartupWizardLoginInformation extends SchoolPlannerActivity {

	private EditText nameInput;
	private EditText serverUrlInput;
	private EditText schoolInput;
	private EditText usernameInput;
	private EditText passwordInput;
	
	private Button nextButton;
	
	private Activity thisActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_wizard_login_information);
		
		nameInput = (EditText) findViewById(R.id.startup_wizard_login_information_name);
		serverUrlInput= (EditText) findViewById(R.id.startup_wizard_login_information_server_url);
		schoolInput = (EditText) findViewById(R.id.startup_wizard_login_information_school);
		usernameInput = (EditText) findViewById(R.id.startup_wizard_login_information_username);
		passwordInput = (EditText) findViewById(R.id.startup_wizard_login_information_password);
		
		nextButton = (Button) findViewById(R.id.startup_wizard_login_information_next_button);
		nextButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(requiredDataEntered()) {
					Intent nextScreen = new Intent(thisActivity, StartupWizardLoginInformationCheck.class);
					putEnteredLoginInformation(nextScreen);
					
					startActivity(nextScreen);
				}
				else {
					showToastMessage("enter all needed info");
				}
			}
			
		});
	}
	
	private boolean requiredDataEntered() {
		return nameInput.getText().toString().length() > 0 && serverUrlInput.getText().toString().length() > 0 && schoolInput.getText().toString().length() > 0 && usernameInput.getText().toString().length() > 0;
	}
	
	private void putEnteredLoginInformation(Intent intent) {
		intent.putExtra(LoginSetConstants.nameKey, nameInput.getText().toString());
		intent.putExtra(LoginSetConstants.serverUrlKey, serverUrlInput.getText().toString());
		intent.putExtra(LoginSetConstants.schoolKey, schoolInput.getText().toString());
		intent.putExtra(LoginSetConstants.usernameKey, usernameInput.getText().toString());
		intent.putExtra(LoginSetConstants.passwordKey, passwordInput.getText().toString());
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		thisActivity = this;
	}
}
