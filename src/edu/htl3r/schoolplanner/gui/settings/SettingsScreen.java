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
package edu.htl3r.schoolplanner.gui.settings;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;

public class SettingsScreen extends PreferenceActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.settings);
        
        String[] loginSetNames = getLoginSetNames();
        ListPreference autologinSetSetting = (ListPreference) getPreferenceManager().findPreference(getString(R.string.settings_key_autologin_set));
        
        autologinSetSetting.setEntryValues(loginSetNames);
        autologinSetSetting.setEntries(loginSetNames);
    }
	
    private String[] getLoginSetNames() {
        List<String> loginSetNames = new ArrayList<String>();
    	for(LoginSet loginSet : ((SchoolPlannerApp)getApplication()).getLoginSetManager().getAllLoginSets()) {
    		loginSetNames.add(loginSet.getName());
    	}
    	return loginSetNames.toArray(new String[loginSetNames.size()]);
    }
    
}
