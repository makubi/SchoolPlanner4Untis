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
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.DataProvider;
import edu.htl3r.schoolplanner.backend.Preferences;
import edu.htl3r.schoolplanner.gui.timetableviews.ViewActivity;

public class SchoolPlannerApp extends Application {
		
	protected Preferences prefs;
	protected Cache data;

	protected Class<? extends ViewActivity> currentView;

	protected boolean hasNetwork;

	@Override
	public void onCreate() {
		super.onCreate();
		initBackend();
		prefs = new Preferences();
		data = SchoolplannerContext.cache;
		data.setPreferences(prefs);
		currentView = prefs.getView().getClass();

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

		Log.d("Philip", "endapp");
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
	 * gibt das aktuelle {@link Preferences} Objekt zurueck in dem die Einstellungen gespeichert sind
	 * @return prefs das {@link Preferences} Objekt
	 */
	public Preferences getPrefs() {
		return prefs;
	}

	/**
	 * setzt das {@link Preferences} Objekt neu und updatet auch das Objekt in dem {@link DataProvider}
	 * @param prefs das neue {@link Preferences} Objekt
	 */
	public void setPrefs(Preferences prefs) {
		this.prefs = prefs;
		data.setPreferences(prefs);
	}

	/**
	 * liefert das {@link DataProvider} Objekt zurueck aus dem Daten wie z.B.: die Klassenlisten, Stunden etc. ausgelesen werden koennen 
	 * @return das {@link DataProvider} Objekt mit den Daten
	 */
	public Cache getData() {
		return data;
	}

	/**
	 * setzt ein neue {@link DataProvider} Objekt
	 * @param data das neue {@link DataProvider} Objekt
	 */
	public void setData(Cache data) {
		this.data = data;
	}

	/**
	 * gibt den aktuellen Netzwerkstatus zurueck
	 * @return true wenn eine Datenverbindung vorhanden ist, wenn nicht false
	 */
	public boolean isNetworkEnabled() {
		return hasNetwork;
	}

	/**
	 * setzt den Netzwerkstatus neu und updatet die Information im {@link DataProvider}
	 * @param hasNetwork true wenn eine Datenverbindung vorhanden ist, wenn nicht false
	 */
	public void setNetworkEnabled(boolean hasNetwork) {
		this.hasNetwork = hasNetwork;
		data.networkAvailabilityChanged(hasNetwork);
		Log.d("Philip", "info.isConnected(): " + hasNetwork);
	}

	/**
	 * gibt die aktuelle Stundenplanansicht zurueck
	 * @return ein Class-Objekt einer von {@link ViewActivity} abgeleiteten Klasse
	 */
	public Class<? extends ViewActivity> getCurrentView() {
		return currentView;
	}

	/**
	 * setzt die aktuelle Stundenplanansicht neu
	 * @param currentView die neue Ansicht, ein Class-Objekt einer von {@link ViewActivity} abgeleiteten Klasse
	 */
	public void setCurrentView(Class<? extends ViewActivity> currentView) {
		this.currentView = currentView;
	}
}
