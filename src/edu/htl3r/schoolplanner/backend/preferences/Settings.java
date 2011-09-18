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
package edu.htl3r.schoolplanner.backend.preferences;

import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;

public class Settings {
	
	private SharedPreferences preferences;
	private SharedPreferences.OnSharedPreferenceChangeListener listener;
	
	private boolean autoLogin;
	private String autoLoginSet;

//	private boolean autoSelect;
//	private String autoSelectType;
//	private String autoSelectValue;
	
	private String defaultView;
	private boolean displaySaturday;
	private boolean displayDate;
	private boolean displayWeekdayNames;
	private boolean displayTimegrid;
	private boolean displayZerothLesson;
	
	public Settings() {
		 preferences = PreferenceManager.getDefaultSharedPreferences(SchoolplannerContext.context);
		 listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				loadPreferences();
			}
		};
		preferences.registerOnSharedPreferenceChangeListener(listener);
		
		loadPreferences();
	}

	private void loadPreferences() {
		// login options
		autoLogin = preferences.getBoolean(getString(R.string.settings_key_autologin), false);
		autoLoginSet = preferences.getString(getString(R.string.settings_key_autologin_set), "");
		
		// autoselect options
//		autoSelect = preferences.getBoolean(getString(R.string.settings_key_autoselect), false);
//		autoSelectType = preferences.getString(getString(R.string.settings_key_autoselect_type), "");
//		autoSelectValue = preferences.getString(getString(R.string.settings_key_autoselect_value), "");
		
		// display options
		defaultView = preferences.getString(getString(R.string.settings_key_default_view), SettingsConstants.DEFAULT_VIEW_WEEK);
		displaySaturday = preferences.getBoolean(getString(R.string.settings_key_show_saturday), false);
		displayDate = preferences.getBoolean(getString(R.string.settings_key_show_date), false);
		displayWeekdayNames = preferences.getBoolean(getString(R.string.settings_key_show_weekday_names), false);
		displayTimegrid = preferences.getBoolean(getString(R.string.settings_key_show_timegrid), false);
		displayZerothLesson = preferences.getBoolean(getString(R.string.settings_key_show_zeroth_lesson), false);
	}
	
	public boolean isAutoLogin() {
		return autoLogin;
	}

	/**
	 * Liefert den Namen das {@link LoginSet}s, das fuer den Autologin verwendet werden soll.
	 * @return Den Namen des gewaehlten {@link LoginSet}s oder einen Leerstring, wenn es nicht definiert ist
	 */
	public String getAutoLoginSet() {
		return autoLoginSet;
	}

	/**
	 * Liefert die gesetzte Standardansicht. Der ermittelte Wert ist einer aus der Liste {{@link SettingsConstants#DEFAULT_VIEW_DAY}, {@link SettingsConstants#DEFAULT_VIEW_WEEK}, {@link SettingsConstants#DEFAULT_VIEW_MONTH}}.
	 * @return
	 */
	public String getDefaultView() {
		return defaultView;
	}

	public boolean isDisplaySaturday() {
		return displaySaturday;
	}

	public boolean isDisplayDate() {
		return displayDate;
	}

	public boolean isDisplayWeekdayNames() {
		return displayWeekdayNames;
	}

	public boolean isDisplayTimegrid() {
		return displayTimegrid;
	}

	public boolean isDisplayZerothLesson() {
		return displayZerothLesson;
	}

	private String getString(int resId) {
		return SchoolplannerContext.context.getString(resId);
	}
}
