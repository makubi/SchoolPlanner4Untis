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
package edu.htl3r.schoolplanner.gui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;

public class LoginSetDialog extends Dialog{
	
	private EditText nameInput;
	private EditText serverUrlInput;
	private EditText schoolInput;
	private EditText usernameInput;
	private EditText passwordInput;
	private CheckBox sslOnly;
	
	private Button saveButton;
	
	private WelcomeScreen parent;
	
	private Context context;
	
	private String oldName;
	private String oldServerUrl;
	private String oldSchool;
	
	/**
	 * Benutzen, um ein neues LoginSet hinzuzufuegen.
	 * @param context
	 */
	public LoginSetDialog(Context context) {
		super(context);
		this.context = context;
		init();

    	saveButton.setOnClickListener(new OnClickAddLoginSetListener());
	}
	
	/**
	 * Benutzen, um das aktuelle LoginSet zu editieren.
	 * @param context
	 * @param loginSet LoginSet mit neuen Daten
	 */
	public LoginSetDialog(Context context, final LoginSet loginSet) {
		super(context);
		this.context = context;
		init();
		
		oldName = new String(loginSet.getName());
		oldServerUrl = new String(loginSet.getServerUrl());
		oldSchool = new String(loginSet.getSchool());
		
		nameInput.setText(oldName);
		serverUrlInput.setText(oldServerUrl);
		schoolInput.setText(oldSchool);
		usernameInput.setText(loginSet.getUsername());
		passwordInput.setText(loginSet.getPassword());
		sslOnly.setChecked(loginSet.isSslOnly());
		
		saveButton.setOnClickListener(new OnClickEditLoginSetListener());
	}
	
	private void init() {
		setContentView(R.layout.login_set_add_dialog);
	    setTitle(R.string.login_set_add_title);
	    setCancelable(true);
	    
	    nameInput = (EditText) findViewById(R.id.login_set_add_name_input);
		serverUrlInput = (EditText) findViewById(R.id.login_set_add_server_url_input);
		schoolInput = (EditText) findViewById(R.id.login_set_add_school_input);
		usernameInput = (EditText) findViewById(R.id.login_set_add_username_input);
		passwordInput = (EditText) findViewById(R.id.login_set_add_password_input);
		sslOnly = (CheckBox) findViewById(R.id.login_set_add_ssl_only);
		
	    saveButton = (Button) findViewById(R.id.login_set_add_save);
	}

	private boolean requiredDataEntered() {
		return nameInput.getText().toString().length() > 0 && serverUrlInput.getText().toString().length() > 0 && schoolInput.getText().toString().length() > 0 && usernameInput.getText().toString().length() > 0;
	}
	
	public void setParent(WelcomeScreen parent) {
		this.parent = parent;
	}
	
	protected class OnClickEditLoginSetListener implements android.widget.Button.OnClickListener {
		@Override
		public void onClick(View v) {    	    	    	    	
	    	if(requiredDataEntered()) {
	    		if(parent.editLoginSet(nameInput.getText().toString(), serverUrlInput.getText().toString(), schoolInput.getText().toString(), usernameInput.getText().toString(), passwordInput.getText().toString(), sslOnly.isChecked(), oldName, oldServerUrl, oldSchool)) {
	    			dismiss();
	    		}
	    		else {
	    			showErrorMessage("A LoginSet with that name already exists");
	    		}
	    	}
	    	else {
	    		showErrorMessage("Please enter at least name, server url, school and username");
	    	}
	    }
	}
	
	protected class OnClickAddLoginSetListener implements android.widget.Button.OnClickListener {
		@Override
		public void onClick(View v) {    	    	    	    	
	    	if(requiredDataEntered()) {
	    		if(parent.addLoginSet(nameInput.getText().toString(), serverUrlInput.getText().toString(), schoolInput.getText().toString(), usernameInput.getText().toString(), passwordInput.getText().toString(), sslOnly.isChecked())) {
	    			dismiss();
	    		}
	    		else {
	    			showErrorMessage("A LoginSet with that name already exists");
	    		}
	    	}
	    	else {
	    		showErrorMessage("Please enter at least name, server url, school and username");
	    	}
	    }
	}
	
	private void showErrorMessage(String message) {
		Toast errorMessage = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		errorMessage.show();
	}
	
}
