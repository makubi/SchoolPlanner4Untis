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

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import android.widget.Toast;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.Preferences;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;

public class PrefScreen extends PreferenceActivity implements OnPreferenceChangeListener, OnPreferenceClickListener, OnAmbilWarnaListener, Runnable, OnClickListener {
	private static final int REQ_PRESETS = 135;
	public static final int RES_UPDATELOGIN = 2345;
	private SchoolPlannerApp app;
	private Preferences prefs;

	private ListPreference viewTypes;
	private ListPreference viewValues;
	private Preference bgColor;
	private Preference borderColor;
	private Preference headerColor;
	private Preference resync;
	private Preference touchColor;
	private Preference colorReset;
	private Preference showSaturday;
	private Preference showDate;
	private Preference showDaynames;
	private Preference showTimegrid;
	private Preference showZerohour;
	private Preference urlInput;
	private Preference userInput;
	private Preference schoolInput;
	private Preference passwordInput;
	private Preference presets;
	
	private AmbilWarnaDialog dial;
	private int colorPopup;

	private final int SYNC_FAILED = 0;
	private final int SYNC_OKAY = 1;
	private final int DIALOG_REFRESH=123;

	private final int POPUP_COLOR_BG = 1;
	private final int POPUP_COLOR_TOUCH = 2;
	private final int POPUP_COLOR_HEADER = 3;
	private final int POPUP_COLOR_BORDER = 4;

	protected Thread thisThread;
	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			dismissDialog(DIALOG_REFRESH);
			switch (msg.what) {
				case SYNC_FAILED:
					bitteToasten(getString(R.string.prefs_resync_popup_failed), 5);
					break;
				case SYNC_OKAY:
					bitteToasten(getString(R.string.prefs_resync_popup_okay), 5);
					setUpdateSelectScreen();
					break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (SchoolPlannerApp) getApplication();
		prefs = app.getPrefs();

		addPreferencesFromResource(R.xml.preferences);


		viewTypes = (ListPreference) findPreference(getString(R.string.pref_key_viewtype));
		viewTypes.setTitle(R.string.prefs_viewtypes);
		viewTypes.setSummary(R.string.prefs_viewtypes_msg);
		viewTypes.setEntries(R.array.viewTypes);
		viewTypes.setEntryValues(new CharSequence[] { "" + ViewType.SCHOOLCLASS, "" + ViewType.SCHOOLROOM, "" + ViewType.SCHOOLTEACHER, "" + ViewType.SCHOOLSUBJECT });
		viewTypes.setOnPreferenceChangeListener(this);

		viewValues = (ListPreference) findPreference(getString(R.string.pref_key_selection));
		viewValues.setTitle(R.string.prefs_viewvalues);
		viewValues.setSummary(R.string.prefs_viewvalues_msg);
		viewValues.setOnPreferenceChangeListener(this);
		viewValuesInitialization(prefs.getType());

		bgColor = findPreference(getString(R.string.pref_key_color_bg));
		bgColor.setOnPreferenceClickListener(this);

		touchColor = findPreference(getString(R.string.pref_key_color_touch));
		touchColor.setOnPreferenceClickListener(this);

		colorReset = findPreference(getString(R.string.pref_key_color_reset));
		colorReset.setOnPreferenceClickListener(this);

		borderColor = findPreference(getString(R.string.pref_key_color_border));
		borderColor.setOnPreferenceClickListener(this);

		headerColor = findPreference(getString(R.string.pref_key_color_header));
		headerColor.setOnPreferenceClickListener(this);

		resync = findPreference(getString(R.string.pref_key_resync));
		resync.setOnPreferenceClickListener(this);

		showSaturday = findPreference(getString(R.string.pref_key_show_saturday));
		showSaturday.setOnPreferenceClickListener(this);

		showDate = findPreference(getString(R.string.pref_key_show_header_dates));
		showDate.setOnPreferenceClickListener(this);

		showDaynames = findPreference(getString(R.string.pref_key_show_header_daynames));
		showDaynames.setOnPreferenceClickListener(this);

		showTimegrid = findPreference(getString(R.string.pref_key_show_timegrid));
		showTimegrid.setOnPreferenceClickListener(this);
		
		showZerohour = findPreference(getString(R.string.pref_key_show_zerohour));
		showZerohour.setOnPreferenceClickListener(this);
		
		urlInput = findPreference(getString(R.string.pref_key_serverurl));
		urlInput.setOnPreferenceChangeListener(this);
		
		userInput = findPreference(getString(R.string.pref_key_username));
		userInput.setOnPreferenceChangeListener(this);
		
		schoolInput = findPreference(getString(R.string.pref_key_school));
		schoolInput.setOnPreferenceChangeListener(this);
		
		passwordInput = findPreference(getString(R.string.pref_key_password));
		passwordInput.setOnPreferenceChangeListener(this);
		
		presets = findPreference(getString(R.string.pref_key_presets));
		presets.setOnPreferenceClickListener(this);
		
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == viewTypes) {
			int val = Integer.parseInt(newValue + "");
			Log.d("Philip", "newvalue: " + val);
			viewValuesInitialization(val);

		}
		else if(preference == urlInput){
			if(newValue.equals("")){
				bitteToasten(getString(R.string.settingsempty), Toast.LENGTH_SHORT);
				return false;
			}
			else {
				try {
					prefs.setServerUrl((String) newValue);
				} catch (URISyntaxException e) {
					bitteToasten(getString(R.string.wrongServerUrl), Toast.LENGTH_LONG);
				}
			}
		}
		else if(preference == userInput) {
			if(newValue.equals("")){
				bitteToasten(getString(R.string.settingsempty), Toast.LENGTH_SHORT);
				return false;
			}
			else {
				prefs.setUsername((String) newValue);
			}
		}
		else if(preference == schoolInput) {
			if(newValue.equals("")){
				bitteToasten(getString(R.string.settingsempty), Toast.LENGTH_SHORT);
				return false;
			}
			else {
				prefs.setSchool((String) newValue);
			}
		}
		else if(preference == passwordInput) {
				prefs.setPassword((String) newValue);
		}
		
		else if(preference == viewValues){
			Intent inth = new Intent();
			inth.putExtra(ExtrasStrings.UPDATESELSCRBTN, true);
			setResult(RESULT_OK, inth);
		}
		return true;
	}

	/**
	 * initialisiert den viewValues Menuepunkt mit den richtigen Daten
	 * @param val der ausgewaehlte viewType, je nachdem wird der Inhalt bestimmt bzw. der Menuepunkt deaktiviert
	 */
	private void viewValuesInitialization(int val) {
		try {
			viewValues.setEnabled(true);
			switch (val) {
				case ViewType.SCHOOLCLASS:
					viewValues.setEntries(SchoolplannerActivity.getElementNamesArray(app.getData().getSchoolClassList(), false));
					viewValues.setEntryValues(SchoolplannerActivity.getElementNamesArray(app.getData().getSchoolClassList(), false));
					break;
				case ViewType.SCHOOLROOM:
					viewValues.setEntries(SchoolplannerActivity.getElementNamesArray(app.getData().getSchoolRoomList(), false));
					viewValues.setEntryValues(SchoolplannerActivity.getElementNamesArray(app.getData().getSchoolRoomList(), false));
					break;
				case ViewType.SCHOOLTEACHER:
					viewValues.setEntries(SchoolplannerActivity.getElementNamesArray(app.getData().getSchoolTeacherList(), false));
					viewValues.setEntryValues(SchoolplannerActivity.getElementNamesArray(app.getData().getSchoolTeacherList(), false));
					break;
				case ViewType.SCHOOLSUBJECT:
					viewValues.setEntries(SchoolplannerActivity.getElementNamesArray(app.getData().getSchoolSubjectList(), false));
					viewValues.setEntryValues(SchoolplannerActivity.getElementNamesArray(app.getData().getSchoolSubjectList(), false));
					break;
				default:
					viewValues.setEnabled(false);
					// viewValues.setEntries(new CharSequence[]{});
					// viewValues.setEntryValues(new CharSequence[]{});
					break;
			}
		} catch (IOException e) {
			// TODO: Fehlermeldung anzeigen, wenn Netzwerkproblem auftritt
			e.printStackTrace();
		}
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference == bgColor) {
			colorPopup = POPUP_COLOR_BG;
			dial = new AmbilWarnaDialog(this, prefs.getBgColor(), this);
			dial.show();
			return true;
		}
		else if (preference == touchColor) {
			colorPopup = POPUP_COLOR_TOUCH;
			dial = new AmbilWarnaDialog(this, prefs.getTouchColor(), this);
			dial.show();
			return true;
		}
		else if (preference == borderColor) {
			colorPopup = POPUP_COLOR_BORDER;
			dial = new AmbilWarnaDialog(this, prefs.getBorderColor(), this);
			dial.show();
			return true;
		}
		else if (preference == headerColor) {
			colorPopup = POPUP_COLOR_HEADER;
			dial = new AmbilWarnaDialog(this, prefs.getHeaderColor(), this);
			dial.show();
			return true;
		}
		else if (preference == colorReset) {
			OnClickListener clickListener = new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
						case AlertDialog.BUTTON_POSITIVE: // ok
							prefs.setBgColor(Color.GRAY);
							prefs.setTouchColor(Color.YELLOW);
							prefs.setBorderColor(Color.WHITE);
							prefs.setHeaderColor(Color.GRAY);
							bitteToasten(getString(R.string.prefs_color_reset_msg), 5);
							setUpdateView();
							break;
						case AlertDialog.BUTTON_NEGATIVE: // cancel
							break;
					}
					dialog.dismiss();
				}
			};
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle(getString(R.string.prefs_reset_colors_title));
			dialog.setMessage(getString(R.string.prefs_reset_colors_message));
			dialog.setNegativeButton(getResources().getString(R.string.button_cancel), clickListener);
			dialog.setPositiveButton(getResources().getString(R.string.button_ok), clickListener);
			dialog.show();
		}
		else if (preference == resync) {
			if (app.isNetworkEnabled()) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(this);
				dialog.setTitle(getString(R.string.prefs_resync_question_title));
				dialog.setMessage(getString(R.string.prefs_resync_question));
				dialog.setNegativeButton(getResources().getString(R.string.button_cancel), this);
				dialog.setPositiveButton(getResources().getString(R.string.button_ok), this);
				dialog.show();
			}
			else {
				bitteToasten(getString(R.string.no_network_message), 10);
			}
		}
		else if (preference == showDate || preference == showDaynames || preference == showSaturday || preference == showTimegrid || preference == showZerohour) {
			setUpdateView();
		}
		else if(preference == presets){
			Intent in = new Intent(this, LoginPresetScreen.class);
			startActivityForResult(in, REQ_PRESETS);
		}

		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQ_PRESETS && resultCode == RES_UPDATELOGIN){
			((EditTextPreference)findPreference(getString(R.string.pref_key_school))).setText(prefs.getSchool());
			((EditTextPreference)findPreference(getString(R.string.pref_key_username))).setText(prefs.getUsername());
			((EditTextPreference)findPreference(getString(R.string.pref_key_password))).setText(prefs.getPassword());
			((EditTextPreference)findPreference(getString(R.string.pref_key_serverurl))).setText(prefs.getServerUrl());
		}
	}

	@Override
	public void onCancel(AmbilWarnaDialog dialog) {
		// do nothing methode
	}

	@Override
	public void onOk(AmbilWarnaDialog dialog, int color) {
		switch (colorPopup) {
			case POPUP_COLOR_BG:
				prefs.setBgColor(color);
				setUpdateView();
				break;
			case POPUP_COLOR_BORDER:
				prefs.setBorderColor(color);
				setUpdateView();
				break;

			case POPUP_COLOR_HEADER:
				prefs.setHeaderColor(color);
				setUpdateView();
				break;

			case POPUP_COLOR_TOUCH:
				prefs.setTouchColor(color);
				break;
			default:

				break;
		}

		Log.d("Philip", "color: " + color);
		colorPopup = 0;
		bitteToasten(getString(R.string.prefs_color_set), 5);

	}


	/**
	 * zeigt eine Benachrichtung (eine {@link android.widget.Toast}-Message)  an
	 * @param denToastBitte die Nachricht, die angezeigt werden soll
	 * @param wieGutDurch wie lang die Nachricht angezeigt werden soll
	 */
	private void bitteToasten(String denToastBitte, int wieGutDurch) {
		Toast toast = Toast.makeText(getApplicationContext(), denToastBitte, wieGutDurch);
		toast.show();
	}

	@Override
	public void run() {
		try {
			app.getData().resyncMasterData();
		} catch (IOException e) {
			handler.sendEmptyMessage(SYNC_FAILED);
			return;
		}
		handler.sendEmptyMessage(SYNC_OKAY);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
			case AlertDialog.BUTTON1: // ok
				thisThread = new Thread(this);
				thisThread.start();
				showDialog(DIALOG_REFRESH);
				break;
			case AlertDialog.BUTTON2: // cancel
				break;
		}
		dialog.dismiss();
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Log.d("Philip", getClass().getSimpleName() + ": oncreatedialog: " +id);
		if (id == DIALOG_REFRESH) {
			ProgressDialog loadingDialog = new ProgressDialog(this);
			loadingDialog.setMessage(getString(R.string.progress_resync_text));
			loadingDialog.setTitle(getString(R.string.progress_resync_title));
			loadingDialog.setIndeterminate(true);
			loadingDialog.setCancelable(true);
			return loadingDialog;
		}
		
		return super.onCreateDialog(id);
	}
	
	private void setUpdateView(){
		Intent inth = new Intent();
		inth.putExtra(ExtrasStrings.UPDATEVIEW, true);
		setResult(RESULT_OK, inth);
	}
	
	private void setUpdateSelectScreen(){
		Intent inth = new Intent();
		inth.putExtra(ExtrasStrings.UPDATESELSCR, true);
		setResult(RESULT_OK, inth);
	}
}