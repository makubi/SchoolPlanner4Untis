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
import android.preference.PreferenceManager;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;

public class Settings {
	
	private SharedPreferences preferences;
	private SharedPreferences.OnSharedPreferenceChangeListener listener;
	
	private boolean autoLogin;
	private String autoLoginSet;
	
	private boolean displaySaturday;
	private boolean displayZerothLesson;
	private boolean highlightCurrentLesson;
	
	private boolean cachingEnabled;
	private int cacheLifeTimeInHours;
	
	public static final int MIN_CACHE_LIFE_TIME = 0;
	public static final int MAX_CACHE_LIFE_TIME = 8784;
	
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
		// Nicht-gesetzte 'defaultValue'-boolean-Werte sind standardgemaess 'false'
		
		// login options
		autoLogin = preferences.getBoolean(getString(R.string.settings_key_autologin), false);
		autoLoginSet = preferences.getString(getString(R.string.settings_key_autologin_set), "");
		
		// display options
		displaySaturday = preferences.getBoolean(getString(R.string.settings_key_show_saturday), false);
		displayZerothLesson = preferences.getBoolean(getString(R.string.settings_key_show_zeroth_lesson), false);
		highlightCurrentLesson = preferences.getBoolean(getString(R.string.settings_key_highlight_current_lesson), false);
		
		cachingEnabled = preferences.getBoolean(getString(R.string.settings_key_caching_enabled), true);
		cacheLifeTimeInHours = preferences.getInt(getString(R.string.settings_key_caching_life_time_in_hours), 1);
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

	public boolean isDisplaySaturday() {
		return displaySaturday;
	}

	public boolean isDisplayZerothLesson() {
		return displayZerothLesson;
	}

	public boolean isHighlightCurrentLesson() {
		return highlightCurrentLesson;
	}

	private String getString(int resId) {
		return SchoolplannerContext.context.getString(resId);
	}
	
	public boolean isCachingEnabled() {
		return cachingEnabled;
	}
	
	public int getCacheLifeTimeInHours() {
		return cacheLifeTimeInHours;
	}
	
}
