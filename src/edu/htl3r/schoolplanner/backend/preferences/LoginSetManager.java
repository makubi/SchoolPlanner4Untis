/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
			Sebastian Chlan <sebastian@schoolplanner.at>
			Christian Pascher <christian@schoolplanner.at>
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.gui.Constants;

public class LoginSetManager {

	private final String nameKey = Constants.nameKey;
	private final String urlKey = Constants.serverUrlKey;
	private final String schoolKey = Constants.schoolKey;
	private final String userKey = Constants.usernameKey;
	private final String loginsharedpreferences = Constants.loginsharedpreferences;

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	public final static String fail = "error";
	
	public LoginSetManager() {
		preferences = SchoolplannerContext.context.getSharedPreferences(loginsharedpreferences, Context.MODE_PRIVATE);
		editor = preferences.edit();
	}

	/**
	 * Füge ein LoginSet zu den SharedPreferences
	 * 
	 * @param le
	 * @return
	 */
	public boolean addLoginEntry(LoginSet le) {
		String key = generateMagicKey(le.getServerUrl(), le.getSchool());
		String value = le.toString();
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * Füge ein LoginSet zu den SharedPreferences
	 * @param name
	 * @param url
	 * @param school
	 * @param user
	 * @return
	 */
	public boolean addLoginEntry(String name, String url, String school, String user) {
		return addLoginEntry(new LoginSet(getDataEntry(name, url, school, user)));
	}

	/**
	 * Auslesen aller gespeicherten Sets für den List Adapter im WelcomeScreen
	 * 
	 * @return
	 */
	public List<Map<String, String>> getAllLoginSetsForListAdapter() {
		HashMap<String, String> all = (HashMap<String, String>) preferences.getAll();
		List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
		Set<String> keys = all.keySet();

		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			ret.add(getDataEntry(preferences.getString(key, fail)));
		}
		return ret;
	}
	
	/**
	 * Auslesen allger gespeicherten Sets
	 * @return
	 */
	public List<LoginSet> getAllLoginSets(){
		HashMap<String, String> all = (HashMap<String, String>) preferences.getAll();
		List<LoginSet> ret = new ArrayList<LoginSet>();
		Set<String> keys = all.keySet();

		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			ret.add(new LoginSet(getDataEntry(preferences.getString(key, fail))));
		}
		return ret;
	}

	/**
	 * Lösche ein LoginSet
	 * 
	 * @param le
	 * @return
	 */
	public boolean deleteLoginEntry(LoginSet le) {
		editor.remove(generateMagicKey(le.getServerUrl(), le.getSchool()));
		return editor.commit();
	}

	
	private String generateMagicKey(String serverurl, String school) {
		return md5(serverurl + school);
	}
	

	private Map<String, String> getDataEntry(String data) {
		String tmp[] = data.split("§");
		Map<String, String> dataEntry = new HashMap<String, String>();
		dataEntry.put(nameKey, tmp[0]);
		dataEntry.put(urlKey, tmp[1]);
		dataEntry.put(schoolKey, tmp[2]);
		dataEntry.put(userKey, tmp[3]);
		return dataEntry;
	}
	
	private Map<String, String> getDataEntry(String name, String url, String school, String user) {
		Map<String, String> dataEntry = new HashMap<String, String>();
		dataEntry.put(nameKey, name);
		dataEntry.put(urlKey, url);
		dataEntry.put(schoolKey, school);
		dataEntry.put(userKey, user);
		return dataEntry;
	}

	private String md5(final String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
}
