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

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetManager;
import edu.htl3r.schoolplanner.gui.settings.SettingsScreen;

/**
 * Diese Klasse erweitert die Standard Android-{@link Activity} um weitere
 * Funktionalitaeten. Dazu zaehlt unter Anderem das Hinzufuegen des
 * Standard-Menues oder der Zugriff auf die ProgressBar am unteren
 * Bildschirmrand.
 */
public abstract class SchoolPlannerActivity extends Activity {

	private ProgressBar progressWheel;
	private TextView loginProgressText;
	
	private Dialog infoDialog;

	/**
	 * Zeigt einen Text und den Lade-Kreis in der ProgressBar am unteren
	 * Bildschirmrand an oder versteckt den Lade-Kreis.
	 * 
	 * @param message
	 *            Text, der angezeigt werden soll
	 * @param active
	 *            Wenn 'true', wird der Lade-Kreis angezeigt, ansonsten wird er
	 *            versteckt
	 */
	public void setInProgress(String message, boolean active) {
		loginProgressText.setText(message);
		if (active) {
			progressWheel.setVisibility(View.VISIBLE);
		} else {
			progressWheel.setVisibility(View.INVISIBLE);
		}
	}
	
	
	public void showToastMessage(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		progressWheel = (ProgressBar) findViewById(R.id.loginProgress);
		loginProgressText = (TextView) findViewById(R.id.loginProgressText);
		
		buildInfoDialog();
	}

	private void buildInfoDialog() {
		infoDialog = new Dialog(this);
		infoDialog.setContentView(R.layout.info_dialog);
		infoDialog.setTitle(R.string.info_dialog_title);
		infoDialog.setCancelable(true);
		
		// Links clickable
		final TextView infoDialogLinks = (TextView) infoDialog.findViewById(R.id.info_dialog_links);
		final SpannableString clickableText = new SpannableString(infoDialogLinks.getText());
		Linkify.addLinks(clickableText, Linkify.ALL);
		infoDialogLinks.setText(clickableText);
		infoDialogLinks.setMovementMethod(LinkMovementMethod.getInstance());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.default_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(this, SettingsScreen.class));
			return true;
		case R.id.menu_info:
			infoDialog.show();
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// Speichere aktuelles LoginSet, damit es - sollte die Activity fuer die Stundenplananzeige pausiert werden - spaeter wieder ohne Relogin zur Verfuegung steht.
		LoginSetManager loginSetManager = ((SchoolPlannerApp) getApplication()).getLoginSetManager();
		LoginSet activeLoginSet;
		if((activeLoginSet = loginSetManager.getActiveLoginSet()) != null) {
			outState.putString(LoginSetConstants.nameKey, activeLoginSet.getName());
			outState.putString(LoginSetConstants.serverUrlKey, activeLoginSet.getServerUrl());
			outState.putString(LoginSetConstants.schoolKey, activeLoginSet.getSchool());
			outState.putString(LoginSetConstants.usernameKey, activeLoginSet.getUsername());
			outState.putString(LoginSetConstants.passwordKey, activeLoginSet.getPassword());
			outState.putBoolean(LoginSetConstants.sslOnlyKey, activeLoginSet.isSslOnly());
		}
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		// Stelle LoginSet wieder her.
		
		SchoolPlannerApp app = ((SchoolPlannerApp)getApplication());
		LoginSetManager loginSetManager = app.getLoginSetManager();
		
		if(loginSetManager.getActiveLoginSet() == null) {
			// LoginSet wird nur wiederhergestellt, wenn vorher schon eines gesetzt wurde.
			if(savedInstanceState.containsKey(LoginSetConstants.nameKey) && savedInstanceState.containsKey(LoginSetConstants.serverUrlKey) && savedInstanceState.containsKey(LoginSetConstants.schoolKey) && savedInstanceState.containsKey(LoginSetConstants.usernameKey)) {
				LoginSet loginSet = new LoginSet(savedInstanceState.getString(LoginSetConstants.nameKey),savedInstanceState.getString(LoginSetConstants.serverUrlKey),savedInstanceState.getString(LoginSetConstants.schoolKey),savedInstanceState.getString(LoginSetConstants.usernameKey),savedInstanceState.getString(LoginSetConstants.passwordKey),savedInstanceState.getBoolean(LoginSetConstants.sslOnlyKey));
				app.getData().setLoginCredentials(loginSet);
				loginSetManager.setActiveLoginSet(loginSet);
			}
		
		}
	}
	
	protected void initTitle(String title){
		((TextView)findViewById(R.id.header_text)).setText(title);
	}
}
