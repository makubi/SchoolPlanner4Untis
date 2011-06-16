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

package edu.htl3r.schoolplanner;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import edu.htl3r.schoolplanner.backend.Authentication;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.DataProvider;
import edu.htl3r.schoolplanner.backend.Preferences;

public class SchoolPlannerApp extends Application {
		
	protected Authentication prefs;
	protected Cache data;

	protected boolean hasNetwork;

	@Override
	public void onCreate() {
		super.onCreate();
		initBackend();
		prefs = new Authentication();
		data = SchoolplannerContext.cache;
		data.setPreferences(prefs);

		ConnectivityManager conmgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conmgr.getActiveNetworkInfo();
		updateNetstat(info);

		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
				updateNetstat(info);
			}
		}, filter);
	}
	
	private void initBackend() {
		SchoolplannerContext.context = getApplicationContext();
		SchoolplannerContext.cache.init();
	}

	/**
	 * updatet den Netzwerkstatus in der DataSelection 
	 * 
	 * @param info
	 *            das NetworkInfo objekt das die noetigen Info enthaelt
	 */
	private void updateNetstat(NetworkInfo info) {
		if (info != null) {
			setNetworkEnabled(info.isConnected());
		}
	}

	/**
	 * setzt den Netzwerkstatus neu und updatet die Information im {@link DataProvider}
	 * @param hasNetwork true wenn eine Datenverbindung vorhanden ist, wenn nicht false
	 */
	public void setNetworkEnabled(boolean hasNetwork) {
		data.networkAvailabilityChanged(hasNetwork);
	}
}
