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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.htl3r.schoolplanner.constants.LoginSetConstants;

public class LoginSetDatabase {
	
	private LoginSetDatabaseHelper databaseHelper;
	private String loginSetTableName;
	
	public void saveLoginSet(LoginSet loginSet) {		
		SQLiteDatabase writableDatabase = openDatabase(true);
		
		ContentValues values = new ContentValues();
		values.put(LoginSetConstants.nameKey, loginSet.getName());
		values.put(LoginSetConstants.serverUrlKey, loginSet.getServerUrl());
		values.put(LoginSetConstants.schoolKey, loginSet.getSchool());
		values.put(LoginSetConstants.usernameKey, loginSet.getUsername());
		values.put(LoginSetConstants.passwordKey, loginSet.getPassword());
		values.put(LoginSetConstants.sslOnlyKey, loginSet.isSslOnly());
		
		writableDatabase.beginTransaction();
		writableDatabase.insert(loginSetTableName, null, values);
		writableDatabase.setTransactionSuccessful();
		writableDatabase.endTransaction();
		
		closeDatabase(writableDatabase);
	}
	
	private SQLiteDatabase openDatabase(boolean writeable) {
		return writeable ? databaseHelper.getWritableDatabase() : databaseHelper.getReadableDatabase();
	}
	
	private void closeDatabase(SQLiteDatabase database) {
		database.close();
	}
	
	public List<LoginSet> getAllLoginSets() {
		List<LoginSet> allLoginSets = new ArrayList<LoginSet>();
		
		SQLiteDatabase readableDatabase = openDatabase(false);
				
		Cursor query = readableDatabase.query(loginSetTableName, null, null, null, null, null, null);
		
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
		closeDatabase(readableDatabase);
		
		return allLoginSets;
	}
	
	public void removeLoginSet(LoginSet loginSet) {
		SQLiteDatabase writableDatabase = openDatabase(true);
		
		writableDatabase.beginTransaction();
		writableDatabase.delete(loginSetTableName, LoginSetConstants.nameKey+"=?", new String[] {loginSet.getName()});
		writableDatabase.setTransactionSuccessful();
		writableDatabase.endTransaction();
		
		closeDatabase(writableDatabase);
	}
	
	public void setContext(Context context) {
		databaseHelper = new LoginSetDatabaseHelper(context);
		loginSetTableName = databaseHelper.getLoginsetTableName();
	}

	public void editLoginSet(String name, String serverUrl, String school,
			String username, String password, boolean checked) {
		SQLiteDatabase writableDatabase = openDatabase(true);
		
		ContentValues values = new ContentValues();
		values.put(LoginSetConstants.serverUrlKey, serverUrl);
		values.put(LoginSetConstants.schoolKey, school);
		values.put(LoginSetConstants.usernameKey, username);
		values.put(LoginSetConstants.passwordKey, password);
		values.put(LoginSetConstants.sslOnlyKey, checked);
		
		writableDatabase.beginTransaction();
		writableDatabase.update(loginSetTableName, values, LoginSetConstants.nameKey+"=?", new String[]{name});
		writableDatabase.setTransactionSuccessful();
		writableDatabase.endTransaction();
		
		closeDatabase(writableDatabase);
	}
	
}
