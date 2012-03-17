package edu.htl3r.schoolplanner.gui.startup_wizard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;

/**
 * Startup-Assistent Seite 2, welche den Benutzer auffordert, Login-Informationen anzugeben.
 */
public class StartupWizardLoginInformation extends SchoolPlannerActivity {

	private Button nextButton;
	
	private Activity thisActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_wizard_login_information);
		
//		nextButton = (Button) findViewById(R.id.startup_wizard_login_information_next_button);
//		nextButton.setOnClickListener(new Button.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent nextScreen = new Intent(thisActivity, StartupWizardLoginInformationCheck.class);
//				startActivity(nextScreen);
//			}
//		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		thisActivity = this;
	}
}
