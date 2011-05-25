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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.Authentication;

/**
 * @author Philip Woelfel <philip[at]woelfel[dot]at>
 *
 */
public class LoginPresetScreen extends SchoolplannerActivity implements OnItemSelectedListener {

	protected EditText username;
	protected EditText password;
	protected EditText school;
	protected EditText url;
	protected EditText title;
	protected Spinner presets;

	protected HashMap<String, Authentication> sets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presetscreen);

		username = (EditText) findViewById(R.id.presetUserInput);
		password = (EditText) findViewById(R.id.presetPasswordInput);
		school = (EditText) findViewById(R.id.presetSchoolInput);
		url = (EditText) findViewById(R.id.presetURLInput);
		title = (EditText) findViewById(R.id.presetTitleInput);
		presets = (Spinner) findViewById(R.id.presetsDd);
		presets.setOnItemSelectedListener(this);

		Button save = (Button) findViewById(R.id.presetButton);
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				savePreset();
			}

		});
		
		Button use = (Button) findViewById(R.id.presetSetDataButton);
		use.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				updatePrefs();
			}

		});
		
		Button del = (Button) findViewById(R.id.presetDeleteButton);
		del.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				deletePreset();
			}

			

		});

		updateDropdown();
	}
	
	private void deletePreset() {
		String titl = title.getText().toString();
		Log.d("Philip", getClass().getSimpleName() + ": titl: " + titl);
		app.getData().deletePreset(titl);
		updateDropdown();
		setData("","","","","");
		bitteToasten(getString(R.string.preset_deleted), Toast.LENGTH_SHORT);
	}

	private void savePreset() {
		String user = username.getText().toString();
		String pwd = password.getText().toString();
		String schol = school.getText().toString();
		String urls = url.getText().toString();
		String titl = title.getText().toString();

		if (user != null && !user.equals("") && urls != null && !urls.equals("") && schol != null && !schol.equals("") && titl != null && !titl.equals("")) {
			Authentication auth = new Authentication();
			auth.setUsername(user);
			auth.setPassword(pwd);
			auth.setSchool(schol);
			try {
				auth.setServerUrl(urls);
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
			app.getData().savePreset(titl, auth);
			updateDropdown();
			bitteToasten(getString(R.string.preset_added), 3);
		}
		else {
			bitteToasten(getString(R.string.preset_add_failed), 3);
		}

	}

	private void setData(Authentication auth, String titl) {
		setData(auth.getUsername(), auth.getPassword(), auth.getSchool(), auth.getServerUrl(), titl);
	}

	private void setData(String usr, String pwd, String schol, String urls, String titl) {
		username.setText(usr);
		password.setText(pwd);
		school.setText(schol);
		url.setText(urls);
		title.setText(titl);
	}

	private void updateDropdown() {
		sets = app.getData().getAllPresets();
		
		if (sets != null && sets.size() > 0) {
			ArrayList<String> list = new ArrayList<String>(sets.keySet());
			list.add(0, "");
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			presets.setAdapter(adapter);
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
			setData(sets.get(titl), titl);
		}
		else {
			setData("", "", "", "", "");
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	private void updatePrefs() {
		String usr = username.getText().toString();
		String pwd = password.getText().toString();
		String schol = school.getText().toString();
		String urls = url.getText().toString();
		String titl = title.getText().toString();

		if (usr != null && !usr.equals("") && urls != null && !urls.equals("") && schol != null && !schol.equals("") && titl != null && !titl.equals("")) {
			prefs.setUsername(usr);
			prefs.setPassword(pwd);
			prefs.setSchool(schol);
			try {
				prefs.setServerUrl(urls);
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
				return;
			}
			app.getData().setPreferences(prefs);
			bitteToasten(getString(R.string.preset_prefs_updated), Toast.LENGTH_SHORT);
			setResult(PrefScreen.RES_UPDATELOGIN);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
