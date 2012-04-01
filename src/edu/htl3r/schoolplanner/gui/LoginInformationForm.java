package edu.htl3r.schoolplanner.gui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;

/**
 * Startup-Assistent Seite 2, welche den Benutzer auffordert, Login-Informationen anzugeben.
 */
public abstract class LoginInformationForm extends SchoolPlannerActivity {

	private EditText nameInput;
	private MultiAutoCompleteTextView serverUrlInput;
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
		serverUrlInput = (MultiAutoCompleteTextView) findViewById(R.id.startup_wizard_login_information_server_url);
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
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.webuntis_server_urls));
		 
        serverUrlInput.setAdapter(adapter);
        serverUrlInput.setTokenizer(new WebUntisUrlTokenizer());   
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

	/**
	 * Initialisiert die Felder der GUI mit Werten.<br />
	 * Wird z.B. beim Editieren eines {@link LoginSet} verwendet.
	 * @param name Name, der gesetzt werden soll
	 * @param serverUrl Server-URL, die gesetzt werden soll
	 * @param school Schulname, der gesetzt werden soll
	 * @param username Benutzername, der gesetzt werden soll
	 * @param password Passwort, das gesetzt werden soll
	 * @param sslOnly Status des SSL-Only-Flags, der gesetzt werden soll
	 */
	protected void initInputFields(String name, String serverUrl, String school, String username, String password, boolean sslOnly) {
		nameInput.setText(name);
		serverUrlInput.setText(serverUrl);
		schoolInput.setText(school);
		usernameInput.setText(username);
		passwordInput.setText(password);
		sslOnlyInput.setChecked(sslOnly);
	}
	
	/**
	 * Setzt den {@link Button.OnClickListener}, der fuer den "Next"-Button verwendet werden soll.
	 * @param buttonOnClickListener {@link Button.OnClickListener} fuer den "Next"-Button
	 */
	public void setOnButtonClickListener(Button.OnClickListener buttonOnClickListener) {
		nextButton.setOnClickListener(buttonOnClickListener);
	}
	
	/**
	 * Ueberprueft, ob die benoetigten Daten fuer ein {@link LoginSet} eingegeben wurden.
	 * @return {@code true}, wenn Name, Server-URL, Schule und Benutzername eingegeben wurden, sonst {@code false}
	 */
	public boolean requiredDataEntered() {
		return nameInput.getText().toString().length() > 0 && serverUrlInput.getText().toString().length() > 0 && schoolInput.getText().toString().length() > 0 && usernameInput.getText().toString().length() > 0;
	}
	
	/**
	 * Setzt den Text des "Back"-Buttons.
	 * @param text Text, der auf dem "Back"-Button angezeigt werden soll
	 */
	public void setBackButtonText(String text) {
		backButton.setText(text);
	}
	
	/**
	 * Setzt den Text des "Next"-Buttons.
	 * @param text Text, der auf dem "Next"-Button angezeigt werden soll
	 */
	public void setNextButtonText(String text) {
		nextButton.setText(text);
	}
}
