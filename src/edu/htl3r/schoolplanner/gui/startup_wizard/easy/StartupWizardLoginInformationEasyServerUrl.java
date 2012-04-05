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

	private final String SAVED_INSTANCE_KEY_NOT_IN_LIST_CHECKED = "notInListChecked";
	
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
				if (not_in_list.isChecked()) {
					url_spinner.setEnabled(false);
					url_spinner.setClickable(false);
					info2.setVisibility(View.VISIBLE);
					server_url.setVisibility(View.VISIBLE);

				} else {
					url_spinner.setEnabled(true);
					url_spinner.setClickable(true);
					info2.setVisibility(View.GONE);
					server_url.setVisibility(View.GONE);

				}
			}
		});

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
					showToastMessage("Error message missing!");
				}
			}
		});
	}
	
	private boolean requiredDataEntered() {
		return server_url.getText().length() > 0;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(SAVED_INSTANCE_KEY_NOT_IN_LIST_CHECKED, not_in_list.isChecked());
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		server_url.setVisibility(savedInstanceState.getBoolean(SAVED_INSTANCE_KEY_NOT_IN_LIST_CHECKED) ? View.VISIBLE : View.GONE);
	}
}
