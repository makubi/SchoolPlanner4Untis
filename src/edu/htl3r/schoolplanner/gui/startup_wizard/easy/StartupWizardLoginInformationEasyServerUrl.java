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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;

public class StartupWizardLoginInformationEasyServerUrl extends
		SchoolPlannerActivity {
	
	private Spinner url_spinner;
	private CheckBox not_in_list;
	private EditText server_url;
	private Button back, next;
	private TextView info2;
	private Activity thisActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_wizard_login_information_easy_url);
		initTitle(getResources().getString(R.string.startup_wizard_header));

		url_spinner = (Spinner) findViewById(R.id.swi_url_spinner);
		info2 = (TextView) findViewById(R.id.swi_url_info2);
		server_url = (EditText) findViewById(R.id.swi_url_alternative);
		
		info2.setVisibility(View.GONE);
		server_url.setVisibility(View.GONE);
		
		thisActivity = this;
		
		initCheckbox();
		initButtons();
		
	}

	private void initCheckbox() {
		not_in_list = (CheckBox) findViewById(R.id.swi_url_not_in_list);
		not_in_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				refreshManuelServerURLDependingViews();
			}
		});

	}
	
	private void refreshManuelServerURLDependingViews() {
		boolean manuelServerURLEnabled = not_in_list.isChecked();
		
		url_spinner.setEnabled(!manuelServerURLEnabled);
		url_spinner.setClickable(!manuelServerURLEnabled);
		
		info2.setVisibility(manuelServerURLEnabled ? View.VISIBLE : View.GONE);
		server_url.setVisibility(manuelServerURLEnabled ? View.VISIBLE : View.GONE);
	}
	
	private void initButtons(){
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
				Intent intent = new Intent(thisActivity, StartupWizardLoginInformationEasyLoginData.class);
				if(not_in_list.isChecked() && requiredDataEntered()){
					intent.putExtra(LoginSetConstants.serverUrlKey, server_url.getText().toString());
					startActivity(intent);
				}	
				
				else if(!not_in_list.isChecked()){
					intent.putExtra(LoginSetConstants.serverUrlKey, (String)url_spinner.getSelectedItem());
					startActivity(intent);
				}
				
				else {
					showToastMessage(getString(R.string.startup_wizard_login_information_easy_error_server_url_missing));
				}
			}
		});
	}
	
	private boolean requiredDataEntered() {
		return server_url.getText().length() > 0;
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		refreshManuelServerURLDependingViews();
	}
}
