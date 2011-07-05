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
package edu.htl3r.schoolplanner.backend.preferences.loginSets;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import edu.htl3r.schoolplanner.gui.Constants;

public class LoginSetDatabaseHelper extends SQLiteOpenHelper {

	    private static final int DATABASE_VERSION = 4;
	    private static final String LOGINSET_DATABASE_NAME = "db_loginSets";
	    private static final String LOGINSET_TABLE_NAME = "loginSets";
		private static final String CREATE_TABLE_STATEMENT = "CREATE TABLE " + LOGINSET_TABLE_NAME + " ("
	        		+ Constants.nameKey + " TEXT UNIQUE, "
	        		+ Constants.serverUrlKey + " TEXT, "
	        		+ Constants.schoolKey + " TEXT, "
	        		+ Constants.usernameKey + " TEXT, "
	        		+ Constants.passwordKey + " TEXT, "
	        		+ Constants.sslOnlyKey + " BOOLEAN"
	        		+ ");";

	    public LoginSetDatabaseHelper(Context context) {
	        super(context, LOGINSET_DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL(CREATE_TABLE_STATEMENT
	        		);
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
					values.put(Constants.nameKey, loginSet.getName());
					values.put(Constants.serverUrlKey, loginSet.getServerUrl());
					values.put(Constants.schoolKey, loginSet.getSchool());
					values.put(Constants.usernameKey, loginSet.getUsername());
					values.put(Constants.passwordKey, loginSet.getPassword());
					values.put(Constants.sslOnlyKey, loginSet.isSslOnly());
					
					db.insert(LOGINSET_TABLE_NAME, null, values);
				}
				db.setTransactionSuccessful();
				db.endTransaction();
				
			}
			if(oldVersion <= 2) {
				db.beginTransaction();
				
				db.execSQL("ALTER TABLE " + LOGINSET_TABLE_NAME + " ADD COLUMN " + Constants.sslOnlyKey + " BOOLEAN" + ";");
				
				db.setTransactionSuccessful();
				db.endTransaction();
			}
		}

		public String getLoginsetTableName() {
			return LOGINSET_TABLE_NAME;
		}
}
