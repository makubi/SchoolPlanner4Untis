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

package edu.htl3r.schoolplanner;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.MasterdataProvider;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetManager;

public class SchoolPlannerApp extends Application {
	
	private LoginSetManager loginManager;
	
	private Cache data;
	
	@Override
	public void onCreate() {
		super.onCreate();

		initBackend();
		
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
				setNetworkAvailable(info.isConnectedOrConnecting());
			}
		}, filter);
	}

	private void initBackend() {
		SchoolplannerContext.context = getApplicationContext();
		data = new Cache();
	}
	
	/**
	 * setzt den Netzwerkstatus neu und updatet die Information im {@link MasterdataProvider}
	 * @param isNetworkAvailable true wenn eine Datenverbindung vorhanden ist, wenn nicht false
	 */
	public void setNetworkAvailable(boolean isNetworkAvailable) {
		data.networkAvailabilityChanged(isNetworkAvailable);
	}
	
	public LoginSetManager getLoginManager() {
		return loginManager;
	}

	public void setLoginManager(LoginSetManager loginSetManager) {
		loginManager = loginSetManager;
	}
	
	public Cache getData() {
		return data;
	}
	
}
