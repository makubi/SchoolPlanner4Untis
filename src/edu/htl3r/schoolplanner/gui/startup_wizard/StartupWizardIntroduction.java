package edu.htl3r.schoolplanner.gui.startup_wizard;

import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import android.os.Bundle;

/**
 * Startup-Assistent Seite 1, welche dem Benutzer erklaert, welche Informationen angegeben werden muessen.
 */
public class StartupWizardIntroduction extends SchoolPlannerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_wizard);
	}
}
