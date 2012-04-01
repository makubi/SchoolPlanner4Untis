package edu.htl3r.schoolplanner.gui.startup_wizard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import edu.htl3r.schoolplanner.gui.startup_wizard.expert.StartupWizardLoginInformationExpert;

/**
 * Startup-Assistent Seite 1, welche dem Benutzer erklaert, welche Informationen angegeben werden muessen.
 */
public class StartupWizardIntroduction extends SchoolPlannerActivity {

	private Button nextButton;
	private Button backButton;
	
	private RadioButton expert;
	private RadioButton easy;
	
	private Activity thisActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_wizard_introduction);
		
		expert = (RadioButton)findViewById(R.id.swi_radio_expert);
		easy = (RadioButton)findViewById(R.id.swi_radio_easy);
		
		nextButton = (Button) findViewById(R.id.startup_wizard_introduction_next_button);
		nextButton.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(expert.isChecked())
					startActivity(new Intent(thisActivity, StartupWizardLoginInformationExpert.class));
				if(easy.isChecked())
					//TODO Easy Stuff
					Log.d("basti","easy");
			}
		});
		
		backButton = (Button) findViewById(R.id.startup_wizard_introduction_back_button);
		backButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				//onBackPressed();
				finish();
			}
			
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		thisActivity = this;
	}
}
