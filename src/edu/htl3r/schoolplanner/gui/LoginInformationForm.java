package edu.htl3r.schoolplanner.gui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import edu.htl3r.schoolplanner.R;

/**
 * Startup-Assistent Seite 2, welche den Benutzer auffordert, Login-Informationen anzugeben.
 */
public abstract class LoginInformationForm extends SchoolPlannerActivity {

	private EditText nameInput;
	private EditText serverUrlInput;
	private EditText schoolInput;
	private EditText usernameInput;
	private EditText passwordInput;
	
	private CheckBox sslOnlyInput;
	
	private Button nextButton;
	private Button backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_wizard_login_information);
		
		nameInput = (EditText) findViewById(R.id.startup_wizard_login_information_name);
		serverUrlInput= (EditText) findViewById(R.id.startup_wizard_login_information_server_url);
		schoolInput = (EditText) findViewById(R.id.startup_wizard_login_information_school);
		usernameInput = (EditText) findViewById(R.id.startup_wizard_login_information_username);
		passwordInput = (EditText) findViewById(R.id.startup_wizard_login_information_password);
		
		sslOnlyInput = (CheckBox) findViewById(R.id.startup_wizard_login_information_ssl_only);
		
		nextButton = (Button) findViewById(R.id.startup_wizard_login_information_next_button);
		
		backButton = (Button) findViewById(R.id.startup_wizard_login_information_back_button);
		backButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
			
		});
	}
	
	protected String getNameInput() {
		return nameInput.getText().toString();
	}
	
	protected String getServerUrlInput() {
		return serverUrlInput.getText().toString();
	}
	
	protected String getSchoolInput() {
		return schoolInput.getText().toString();
	}

	protected String getUsernameInput() {
		return usernameInput.getText().toString();
	}

	protected String getPasswordInput() {
		return passwordInput.getText().toString();
	}
	
	protected boolean isSslOnly() {
		return sslOnlyInput.isChecked();
	}

	protected void initInputFields(String name, String serverUrl, String school, String username, String password, boolean sslOnly) {
		nameInput.setText(name);
		serverUrlInput.setText(serverUrl);
		schoolInput.setText(school);
		usernameInput.setText(username);
		passwordInput.setText(password);
		sslOnlyInput.setChecked(sslOnly);
	}
	
	public void setOnButtonClickListener(Button.OnClickListener buttonOnClickListener) {
		nextButton.setOnClickListener(buttonOnClickListener);
	}
	
	public boolean requiredDataEntered() {
		return nameInput.getText().toString().length() > 0 && serverUrlInput.getText().toString().length() > 0 && schoolInput.getText().toString().length() > 0 && usernameInput.getText().toString().length() > 0;
	}
	
	public void setBackButtonText(String text) {
		backButton.setText(text);
	}
	
	public void setNextButtonText(String text) {
		nextButton.setText(text);
	}
}
