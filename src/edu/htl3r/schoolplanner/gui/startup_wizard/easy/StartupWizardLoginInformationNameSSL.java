package edu.htl3r.schoolplanner.gui.startup_wizard.easy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import edu.htl3r.schoolplanner.gui.startup_wizard.StartupWizardLoginInformationCheck;

public class StartupWizardLoginInformationNameSSL extends SchoolPlannerActivity {
	
	private Button back, next;
	private Activity thisActivity;
	private EditText name;
	private CheckBox ssl;
	private Bundle information;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.startup_wizard_login_information_easy_ssl_name);
		initTitle(getResources().getString(R.string.startup_wizard_header));

		name = (EditText)findViewById(R.id.swi_name);
		ssl = (CheckBox)findViewById(R.id.swi_force_ssl);
		thisActivity = this;
		information = getIntent().getExtras();
		initButtons();
		
	}
	
	private boolean requiredDataEntered() {
		if (name.getText().length() > 0)
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
					Intent intent = new Intent(thisActivity,StartupWizardLoginInformationCheck.class);
					
					Bundle intentStuff = thisActivity.getIntent().getExtras();
					intent.putExtra(LoginSetConstants.serverUrlKey,intentStuff.getString(LoginSetConstants.serverUrlKey));
					
					intent.putExtra(LoginSetConstants.schoolKey,intentStuff.getString(LoginSetConstants.schoolKey));
					intent.putExtra(LoginSetConstants.usernameKey,intentStuff.getString(LoginSetConstants.usernameKey));
					intent.putExtra(LoginSetConstants.passwordKey,intentStuff.getString(LoginSetConstants.passwordKey));
	
					intent.putExtra(LoginSetConstants.nameKey,name.getText().toString());
					intent.putExtra(LoginSetConstants.sslOnlyKey,ssl.isChecked());
					startActivity(intent);
				}
				else {
					showToastMessage("Error message missing!");
				}
			}
		});
	}
}
