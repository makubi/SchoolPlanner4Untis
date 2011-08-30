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
package edu.htl3r.schoolplanner.backend.preferences.loginSets;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.constants.LoginSetConstants;

public class LoginSetDatabaseHelper extends SQLiteOpenHelper {

	    private static final int DATABASE_VERSION = 4;
	    private static final String LOGINSET_DATABASE_NAME = "db_loginSets";
	    private static final String LOGINSET_TABLE_NAME = "loginSets";
		private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE " + LOGINSET_TABLE_NAME + " ("
	        		+ LoginSetConstants.nameKey + " TEXT UNIQUE, "
	        		+ LoginSetConstants.serverUrlKey + " TEXT, "
	        		+ LoginSetConstants.schoolKey + " TEXT, "
	        		+ LoginSetConstants.usernameKey + " TEXT, "
	        		+ LoginSetConstants.passwordKey + " TEXT, "
	        		+ LoginSetConstants.sslOnlyKey + " BOOLEAN"
	        		+ ");";

	    public LoginSetDatabaseHelper(Context context) {
	        super(context, LOGINSET_DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL(CREATE_TABLE_STATEMENT);
	        transferLoginDataToLoginSet(db);
	    }
	    
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion <= 3) {				
				List<LoginSet> allLoginSets = new ArrayList<LoginSet>();
				
				Cursor query = db.query(LOGINSET_TABLE_NAME, null, null, null, null, null, null);
				
				while(query.moveToNext()) {
					String name = query.getString(0);
					String serverUrl = query.getString(1);
					String school = query.getString(2);
					String username = query.getString(3);
					String password = query.getString(4);
					boolean sslOnly = query.getInt(5)>0;
					
					LoginSet loginSet = new LoginSet(name, serverUrl, school, username, password, sslOnly);
					allLoginSets.add(loginSet);
				}
				
				query.close();
				
				db.beginTransaction();
				
				db.execSQL("DROP TABLE "+LOGINSET_TABLE_NAME);
				db.execSQL(CREATE_TABLE_STATEMENT);
				
				for(LoginSet loginSet : allLoginSets) {
					ContentValues values = new ContentValues();
					values.put(LoginSetConstants.nameKey, loginSet.getName());
					values.put(LoginSetConstants.serverUrlKey, loginSet.getServerUrl());
					values.put(LoginSetConstants.schoolKey, loginSet.getSchool());
					values.put(LoginSetConstants.usernameKey, loginSet.getUsername());
					values.put(LoginSetConstants.passwordKey, loginSet.getPassword());
					values.put(LoginSetConstants.sslOnlyKey, loginSet.isSslOnly());
					
					db.insert(LOGINSET_TABLE_NAME, null, values);
				}
				db.setTransactionSuccessful();
				db.endTransaction();
				
			}
			if(oldVersion <= 2) {
				db.beginTransaction();
				
				db.execSQL("ALTER TABLE " + LOGINSET_TABLE_NAME + " ADD COLUMN " + LoginSetConstants.sslOnlyKey + " BOOLEAN" + ";");
				
				db.setTransactionSuccessful();
				db.endTransaction();
			}
		}
		
		private void transferLoginDataToLoginSet(SQLiteDatabase db) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SchoolplannerContext.context);
			final String v1_pref_key_serverurl = "serverURL";
			final String v1_pref_key_school = "school";
			final String v1_pref_key_username = "username";
			final String v1_pref_key_password = "password";
			
			String url = preferences.getString(v1_pref_key_serverurl, "");
			String school = preferences.getString(v1_pref_key_school, "");
			String username = preferences.getString(v1_pref_key_username, "");
			String password = preferences.getString(v1_pref_key_password, "");
			
			if(url.length() > 0 && school.length() > 0 && username.length() > 0) {
				ContentValues values = new ContentValues();
				values.put(LoginSetConstants.nameKey, school);
				values.put(LoginSetConstants.serverUrlKey, url);
				values.put(LoginSetConstants.schoolKey, school);
				values.put(LoginSetConstants.usernameKey, username);
				values.put(LoginSetConstants.passwordKey, password);
				values.put(LoginSetConstants.sslOnlyKey, false);
				
				db.beginTransaction();
				db.insert(LOGINSET_TABLE_NAME, null, values);
				db.setTransactionSuccessful();
				db.endTransaction();
			}
			SharedPreferences.Editor editor = preferences.edit();
			editor.clear();
			editor.commit();
		}


		public String getLoginsetTableName() {
			return LOGINSET_TABLE_NAME;
		}
}
