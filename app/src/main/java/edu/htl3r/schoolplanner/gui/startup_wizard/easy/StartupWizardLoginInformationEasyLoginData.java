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
package edu.htl3r.schoolplanner.gui.startup_wizard.easy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;

public class StartupWizardLoginInformationEasyLoginData extends
		SchoolPlannerActivity {

	private Button back, next;
	private Activity thisActivity;
	private EditText school, user, pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.startup_wizard_login_information_easy_login_data);
		initTitle(getResources().getString(R.string.startup_wizard_header));

		school = (EditText) findViewById(R.id.startup_wizard_login_information_school);
		user = (EditText) findViewById(R.id.startup_wizard_login_information_username);
		pass = (EditText) findViewById(R.id.startup_wizard_login_information_password);

		thisActivity = this;
		initButtons();

		
	}

	private boolean requiredDataEntered() {
		if (school.getText().length() > 0 && user.getText().length() > 0 )
			return true;
		return false;
	}

	private void initButtons() {
		back = (Button) findViewById(R.id.startup_wizard_introduction_back_button);
		next = (Button) findViewById(R.id.startup_wizard_introduction_next_button);

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (requiredDataEntered()) {
					Intent intent = new Intent(thisActivity,StartupWizardLoginInformationNameSSL.class);
					intent.putExtra(LoginSetConstants.serverUrlKey,thisActivity.getIntent().getExtras().getString(LoginSetConstants.serverUrlKey));
					intent.putExtra(LoginSetConstants.schoolKey,school.getText().toString());
					intent.putExtra(LoginSetConstants.usernameKey,user.getText().toString());
					intent.putExtra(LoginSetConstants.passwordKey,pass.getText().toString());
					startActivity(intent);
				}
				else {
					showToastMessage(getString(R.string.startup_wizard_login_information_easy_error_school_user_missing));
				}
			}
		});
	}
}
