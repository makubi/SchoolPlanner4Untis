/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
						Gerald Schreiber <mail@gerald-schreiber.at>
						Philip Woelfel <philip@woelfel.at>
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.Authentication;

public class LoginScreen extends SchoolplannerActivity implements Runnable, OnCancelListener, OnItemSelectedListener, OnClickListener {
	
	private boolean isCanceled = false;
	private final int INITIALIZE_OKAY = 1;
	private final int INITIALIZE_FAIL = 0;
	private final int INITIALIZE_NETWORK_ERROR = -1;
	private final int RESYNC_DIALOG = 2;
	
	private boolean resync_data = false;

	protected EditText username;
	protected EditText password;
	protected EditText school;
	protected EditText url;
	protected Spinner presets;

	protected HashMap<String, Authentication> sets;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (!isCanceled) {
					
					// TODO: ugly hack, quickfix
					try {
						dismissDialog(SchoolplannerActivity.DIALOG_PROGRESS_CANCELABLE);
					}
					catch(IllegalArgumentException e) {
						Log.w("GUI", "Trying to dismiss dialog "+SchoolplannerActivity.DIALOG_PROGRESS_CANCELABLE+" that was never started.");
					}
					
					switch (msg.what) {
						case INITIALIZE_OKAY:
							app.setCurrentView(app.getPrefs().getView().getClass());
							Intent myIntent = new Intent(getApplicationContext(), SelectScreen.class);
							if(resync_data){
								myIntent.putExtra(ExtrasStrings.RESYNC_DATA, true);
							}
							startActivity(myIntent);
							break;

						case INITIALIZE_FAIL:
							bitteToasten(getString(R.string.login_failure), 10);
							break;
						case INITIALIZE_NETWORK_ERROR:
							bitteToasten(getString(R.string.login_network_error), Toast.LENGTH_LONG);
							break;
						case RESYNC_DIALOG:
							doYouWantToResync();
							break;
					}
				}
			}
		};

		setContentView(R.layout.login);
		username = (EditText) findViewById(R.id.loginUserInput);
		password = (EditText) findViewById(R.id.loginPasswordInput);
		school = (EditText) findViewById(R.id.loginSchoolInput);
		url = (EditText) findViewById(R.id.loginURLInput);
		presets = (Spinner) findViewById(R.id.loginPresetsDd);
		

		Log.d("Philip", "hasprefs: " + prefs.hasPreferences());
		if (prefs.hasPreferences()) {
			username.setText(prefs.getUsername());
			password.setText(prefs.getPassword());
			school.setText(prefs.getSchool());
			url.setText(prefs.getServerUrl());
			if (prefs.isAutologin()) {
				doLogin();
			}
		}

		Button login = (Button) findViewById(R.id.loginButton);
		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				doLogin();
			}

		});
		updateDropdown();
	}

	/**
	 * fuehrt login durch
	 */
	private void doLogin() {
		// TODO popup beim ersten mal willst daten speichern
		isCanceled = false;
		startDialogAction(getString(R.string.progress_login_title), getString(R.string.progress_login_text), this);
	}
	
	private void doYouWantToResync() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(getString(R.string.login_changed_title));
		dialog.setMessage(getString(R.string.login_changed));
		dialog.setNegativeButton(getResources().getString(R.string.button_no), this);
		dialog.setPositiveButton(getResources().getString(R.string.button_yes), this);
		dialog.show();
	}

	@Override
	public void run() {
		Log.d("Philip", getClass().getSimpleName() + ": running");
		String user = username.getText().toString();
		//String pwd = password.getText().toString();
		String schol = school.getText().toString();
		String urls = url.getText().toString();
		
		if(user!=null && !user.equals("") && urls!=null && !urls.equals("")  && schol!=null && !schol.equals("")){
				setPrefs();
				doTheRealLogin();
		}
		else{
			handler.sendEmptyMessage(INITIALIZE_FAIL);
		}
	}

	private void setPrefs(){
		String user = username.getText().toString();
		String pwd = password.getText().toString();
		String schol = school.getText().toString();
		String urls = url.getText().toString();
		prefs.setUsername(user);
		prefs.setPassword(pwd);
		prefs.setSchool(schol);
		try {
			prefs.setServerUrl(urls);
		} catch (URISyntaxException e1) {
			handler.sendEmptyMessage(INITIALIZE_FAIL);
			e1.printStackTrace();
			return;
		}
		Log.d("Philip", "" +prefs.toString());
		app.getData().setPreferences(prefs);
	}
	
	
	private void doTheRealLogin() {
		String user = new String(prefs.getUsername());
		//String pwd = password.getText().toString();
		String schol = new String(prefs.getSchool());
		String urls = new String(prefs.getServerUrl());
		
		if (app.isNetworkEnabled()) {
			try {
				if (app.getData().authenticate()) {
					if(!user.equals(prefs.getUsername()) || !schol.equals(prefs.getSchool()) || !urls.equals(prefs.getServerUrl())){
						if(urls != null && urls.length() > 0 && schol != null && schol.length() > 0 && user != null && user.length() > 0) {
							handler.sendEmptyMessage(RESYNC_DIALOG);
						}
						else {
							resync_data = true;
							handler.sendEmptyMessage(INITIALIZE_OKAY);
						}
					}
					else {
						handler.sendEmptyMessage(INITIALIZE_OKAY);
					}
				}
				else {
					handler.sendEmptyMessage(INITIALIZE_FAIL);
				}
			} catch (IOException e) {
				Log.e("Network", "IOException on login occured", e);
				handler.sendEmptyMessage(INITIALIZE_NETWORK_ERROR);
			}
		}
		else {
			handler.sendEmptyMessage(INITIALIZE_OKAY);
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		isCanceled = true;
		Log.d("Philip", "canceled");
	}
	
	private void setData(Authentication auth) {
		setData(auth.getUsername(), auth.getPassword(), auth.getSchool(), auth.getServerUrl());
	}
	
	private void setData(String usr, String pwd, String schol, String urls){
		username.setText(usr);
		password.setText(pwd);
		school.setText(schol);
		url.setText(urls);
	}

	private void updateDropdown() {
		sets = app.getData().getAllPresets();
		
		if (sets != null && sets.size() > 0) {
			ArrayList<String> list = new ArrayList<String>(sets.keySet());
			list.add(0, "");
			initializeAdapter(presets, list, this);
			presets.invalidate();
		}
		else {
			ArrayList<String> list = new ArrayList<String>();
			list.add("No presets. Disabled.");
			initializeAdapter(presets, list, null);
			presets.setEnabled(false);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String titl = (String) parent.getSelectedItem();
		if (!titl.equals("")) {
			setData(sets.get(titl));
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
			case AlertDialog.BUTTON_POSITIVE: // okay
				resync_data = true;
				break;
				
			case AlertDialog.BUTTON_NEGATIVE: // cancel
				resync_data = false;
				break;
		}
		handler.sendEmptyMessage(INITIALIZE_OKAY);
		doLogin();
	}
}
