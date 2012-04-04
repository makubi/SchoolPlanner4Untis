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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;

public class LoginSetEditor extends LoginInformationForm {
		
	private String oldName;
	private String oldServerUrl;
	private String oldSchool;
	
	public final static String LOGIN_SET_EDIT_NAME_KEY = "nameKey";
	public final static String LOGIN_SET_EDIT_SERVER_URL_KEY = "serverUrlKey";
	public final static String LOGIN_SET_EDIT_SCHOOL_KEY = "schoolKey";
	public final static String LOGIN_SET_EDIT_USERNAME_KEY = "usernameKey";
	public final static String LOGIN_SET_EDIT_PASSWORD_KEY = "passwordKey";
	public final static String LOGIN_SET_EDIT_SSL_ONLY_KEY = "sslOnlyKey";
	
	public final static String LOGIN_SET_EDIT_OLD_NAME_KEY = "oldNameKey";
	public final static String LOGIN_SET_EDIT_OLD_SERVER_URL_KEY = "oldServerUrlKey";
	public final static String LOGIN_SET_EDIT_OLD_SCHOOL_KEY = "oldSchoolKey";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		LoginSet loginSet = new LoginSet(extras.getString(LoginSetConstants.nameKey), extras.getString(LoginSetConstants.serverUrlKey), extras.getString(LoginSetConstants.schoolKey), extras.getString(LoginSetConstants.usernameKey), extras.getString(LoginSetConstants.passwordKey), extras.getBoolean(LoginSetConstants.sslOnlyKey));
		
		oldName = new String(loginSet.getName());
		oldServerUrl = new String(loginSet.getServerUrl());
		oldSchool = new String(loginSet.getSchool());
		
		initInputFields(loginSet.getName(), loginSet.getServerUrl(), loginSet.getSchool(), loginSet.getUsername(), loginSet.getPassword(), loginSet.isSslOnly());
		
		setOnButtonClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(requiredDataEntered()) {
					Intent intent = new Intent();
					intent.putExtra(LOGIN_SET_EDIT_NAME_KEY, getNameInput());
					intent.putExtra(LOGIN_SET_EDIT_SERVER_URL_KEY, getServerUrlInput());
					intent.putExtra(LOGIN_SET_EDIT_SCHOOL_KEY, getSchoolInput());
					intent.putExtra(LOGIN_SET_EDIT_USERNAME_KEY, getUsernameInput());
					intent.putExtra(LOGIN_SET_EDIT_PASSWORD_KEY, getPasswordInput());
					intent.putExtra(LOGIN_SET_EDIT_SSL_ONLY_KEY, isSslOnly());
					
					intent.putExtra(LOGIN_SET_EDIT_OLD_NAME_KEY, getOldName());
					intent.putExtra(LOGIN_SET_EDIT_OLD_SERVER_URL_KEY, getOldServerUrl());
					intent.putExtra(LOGIN_SET_EDIT_OLD_SCHOOL_KEY, getOldSchool());
					
					setResult(RESULT_OK, intent);
					finish();
		
		    	}
		    	else {
		    		showToastMessage(getString(R.string.error_login_set_information_missing));
		    	}
			}
			
		});
		
		setBackButtonText(getString(R.string.login_set_edit_exit));
		setNextButtonText(getString(R.string.login_set_edit_save));
	}

	private String getOldName() {
		return oldName;
	}

	private String getOldServerUrl() {
		return oldServerUrl;
	}

	private String getOldSchool() {
		return oldSchool;
	}
}
