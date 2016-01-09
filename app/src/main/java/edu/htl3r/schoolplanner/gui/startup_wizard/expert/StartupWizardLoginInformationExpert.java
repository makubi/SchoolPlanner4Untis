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
package edu.htl3r.schoolplanner.gui.startup_wizard.expert;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;
import edu.htl3r.schoolplanner.gui.LoginInformationForm;
import edu.htl3r.schoolplanner.gui.startup_wizard.StartupWizardLoginInformationCheck;

/**
 * Startup-Assistent Seite 2, welche den Benutzer auffordert,
 * Login-Informationen anzugeben.
 */
public class StartupWizardLoginInformationExpert extends LoginInformationForm {

	private Activity thisActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initTitle(getResources().getString(R.string.startup_wizard_header));

		setOnButtonClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (requiredDataEntered()) {
					Intent nextScreen = new Intent(thisActivity, StartupWizardLoginInformationCheck.class);
					putEnteredLoginInformation(nextScreen);

					startActivity(nextScreen);
				} else {
					showToastMessage(getString(R.string.error_login_set_information_missing));
				}
			}

		});
	}

	/**
	 * Speichert die eingegebenen Login-Daten im uebergebenen Intent.<br />
	 * Verwendet die Keys aus der Klasse {@link LoginSetConstants}.
	 * 
	 * @param intent
	 *            {@link Intent}, in das die Daten gespeichert werden sollen
	 * @see LoginSetConstants
	 */
	private void putEnteredLoginInformation(Intent intent) {
		intent.putExtra(LoginSetConstants.nameKey, getNameInput());
		intent.putExtra(LoginSetConstants.serverUrlKey, getServerUrlInput());
		intent.putExtra(LoginSetConstants.schoolKey, getSchoolInput());
		intent.putExtra(LoginSetConstants.usernameKey, getUsernameInput());
		intent.putExtra(LoginSetConstants.passwordKey, getPasswordInput());
		intent.putExtra(LoginSetConstants.sslOnlyKey, isSslOnly());
	}

	private void insertDataIntoFieldsIfIntent() {
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			initInputFields(extra.getString(LoginSetConstants.nameKey), extra.getString(LoginSetConstants.serverUrlKey),
					extra.getString(LoginSetConstants.schoolKey), extra.getString(LoginSetConstants.usernameKey),
					extra.getString(LoginSetConstants.passwordKey), extra.getBoolean(LoginSetConstants.sslOnlyKey));
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		thisActivity = this;
		insertDataIntoFieldsIfIntent();
	}
}
