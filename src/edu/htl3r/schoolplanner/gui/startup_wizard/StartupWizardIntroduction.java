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
package edu.htl3r.schoolplanner.gui.startup_wizard;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import edu.htl3r.schoolplanner.gui.startup_wizard.easy.StartupWizardLoginInformationEasyServerUrl;
import edu.htl3r.schoolplanner.gui.startup_wizard.expert.StartupWizardLoginInformationExpert;
import edu.htl3r.schoolplanner.gui.startup_wizard.qrcode.IntentIntegrator;
import edu.htl3r.schoolplanner.gui.startup_wizard.qrcode.IntentResult;
import edu.htl3r.schoolplanner.gui.startup_wizard.qrcode.QRCodeUrlAnalyser;


/**
 * Startup-Assistent Seite 1, welche dem Benutzer erklaert, welche Informationen
 * angegeben werden muessen.
 */
public class StartupWizardIntroduction extends SchoolPlannerActivity {

	private Button nextButton;
	private Button backButton;

	private RadioButton expert;
	private RadioButton easy;
	private RadioButton qrcode;
	
	private TextView helpQrCode;

	private Activity thisActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_wizard_introduction);
		initTitle(getResources().getString(R.string.startup_wizard_header));

		expert = (RadioButton) findViewById(R.id.swi_radio_expert);
		easy = (RadioButton) findViewById(R.id.swi_radio_easy);
		qrcode = (RadioButton) findViewById(R.id.swi_radio_qrcode);
		helpQrCode = (TextView) findViewById(R.id.swi_qrcode_help);
		
		helpQrCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( "http://www.schoolplanner.at/qr-code" ) );
				 startActivity( browse );
			}
		});

		nextButton = (Button) findViewById(R.id.startup_wizard_introduction_next_button);
		nextButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (expert.isChecked())
					startActivity(new Intent(thisActivity, StartupWizardLoginInformationExpert.class));
				if (easy.isChecked())
					startActivity(new Intent(thisActivity, StartupWizardLoginInformationEasyServerUrl.class));
				if (qrcode.isChecked())
					startQRCodeReader();
			}
		});

		backButton = (Button) findViewById(R.id.startup_wizard_introduction_back_button);
		backButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}

		});
	}

	private void startQRCodeReader() {
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
			String data = scanResult.getContents();
			if (scanResult != null && data.startsWith("http://loginset.schoolplanner.at/")) {
				QRCodeUrlAnalyser qrCodeUrlAnalyser = new QRCodeUrlAnalyser();
				qrCodeUrlAnalyser.startWizardCauseOfUriInput(scanResult.getContents(), this);
			} else {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.startup_wizard_introduction_qrcode_error), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		thisActivity = this;
	}
}
