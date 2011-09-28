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
package edu.htl3r.schoolplanner.backend.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseCreateConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseLoginSetConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabasePermanentLessonConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabasePermanentLessonViewTypeConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseSchoolHolidayConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseStatusDataConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseTimegridConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseViewTypeConstants;
import edu.htl3r.schoolplanner.constants.LoginSetConstants;

public class DatabaseHelper extends SQLiteOpenHelper{

	private final static int DATABASE_VERSION = 7;
	private final static String DATABASE_NAME = "db_schoolplanner_data";
	
	private final List<String> CREATE_TABLE_STATEMENTS = new ArrayList<String>();
	private final String CREATE_TABLE_SQL = "CREATE TABLE";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		CREATE_TABLE_STATEMENTS.addAll(getCreateViewTypeTables());
	}
	
	private List<String> getCreateViewTypeTables() {
		final List<String> createStatements = new ArrayList<String>();
		
		final Map<String, String> coloumnDefinitions = new HashMap<String, String>();
		coloumnDefinitions.put(DatabaseViewTypeConstants.TABLE_SCHOOL_CLASSES_NAME, "("+DatabaseCreateConstants.TABLE_VIEW_TYPE_COLUMN_DEFINITIONS+");");
		coloumnDefinitions.put(DatabaseViewTypeConstants.TABLE_SCHOOL_TEACHER_NAME, "("+DatabaseCreateConstants.TABLE_VIEW_TYPE_COLUMN_DEFINITIONS+");");
		coloumnDefinitions.put(DatabaseViewTypeConstants.TABLE_SCHOOL_ROOMS_NAME, "("+DatabaseCreateConstants.TABLE_VIEW_TYPE_COLUMN_DEFINITIONS+");");
		coloumnDefinitions.put(DatabaseViewTypeConstants.TABLE_SCHOOL_SUBJECTS_NAME, "("+DatabaseCreateConstants.TABLE_VIEW_TYPE_COLUMN_DEFINITIONS+");");
		coloumnDefinitions.put(DatabaseSchoolHolidayConstants.TABLE_SCHOOL_HOLIDAYS_NAME, "("+DatabaseCreateConstants.TABLE_SCHOOL_HOLIDAY_COLUMN_DEFINITIONS+");");
		coloumnDefinitions.put(DatabaseTimegridConstants.TABLE_TIMEGRID_NAME, "("+DatabaseCreateConstants.TABLE_TIMEGRID_COLUMN_DEFINITIONS+");");
		coloumnDefinitions.put(DatabaseStatusDataConstants.TABLE_STATUS_DATA_NAME, "("+DatabaseCreateConstants.TABLE_STATUS_DATA_COLUMN_DEFINITIONS+");");
		
		coloumnDefinitions.put(DatabasePermanentLessonConstants.TABLE_PERMANENT_LESSONS_NAME, "("+DatabaseCreateConstants.TABLE_PERMANENT_LESSONS_DEFINITIONS+");");
		coloumnDefinitions.put(DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_CLASSES_NAME, "("+DatabaseCreateConstants.TABLE_PERMANENT_LESSONS_VIEW_TYPE_DEFINITIONS+");");
		coloumnDefinitions.put(DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_TEACHER_NAME, "("+DatabaseCreateConstants.TABLE_PERMANENT_LESSONS_VIEW_TYPE_DEFINITIONS+");");
		coloumnDefinitions.put(DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_ROOMS_NAME, "("+DatabaseCreateConstants.TABLE_PERMANENT_LESSONS_VIEW_TYPE_DEFINITIONS+");");
		coloumnDefinitions.put(DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_SUBJECTS_NAME, "("+DatabaseCreateConstants.TABLE_PERMANENT_LESSONS_VIEW_TYPE_DEFINITIONS+");");
		
		coloumnDefinitions.put(DatabaseLoginSetConstants.TABLE_LOGIN_SETS_NAME, "("+DatabaseCreateConstants.TABLE_LOGIN_SETS_DEFINITIONS+");");
		
		for(String tableName : coloumnDefinitions.keySet()) {
			createStatements.add(CREATE_TABLE_SQL+" "+tableName+" "+coloumnDefinitions.get(tableName));
		}
		
		return createStatements;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for(String sqlStatement : CREATE_TABLE_STATEMENTS) {
			db.execSQL(sqlStatement);
		}
		
		final String v1_DatabaseName = "HTL3R_SchoolPlanner";
		ContextWrapper c = new ContextWrapper(SchoolplannerContext.context);
		
		transferLoginDataToLoginSet(db, c, v1_DatabaseName);
		removeV1Database(c, v1_DatabaseName);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion < 6) {
			recreateTable(db, DatabaseStatusDataConstants.TABLE_STATUS_DATA_NAME, DatabaseCreateConstants.TABLE_STATUS_DATA_COLUMN_DEFINITIONS);
		}
		if(oldVersion < 7) {
			recreateTable(db, DatabaseTimegridConstants.TABLE_TIMEGRID_NAME, DatabaseCreateConstants.TABLE_TIMEGRID_COLUMN_DEFINITIONS);
		}
	}
	
	private void recreateTable(SQLiteDatabase db, String table, String coloumnDefinitions) {
		db.beginTransaction();
		db.execSQL("DROP TABLE "+ table);
		db.execSQL("CREATE TABLE " + table + "(" + coloumnDefinitions + ");");
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	private void removeV1Database(ContextWrapper c, String v1_DatabaseName) {
		for(String name : c.databaseList()) {
			if(v1_DatabaseName.equals(name)) {
				Log.i("database","Deleted old database: "+c.deleteDatabase(name));
			}
		}
	}
	
	private void transferLoginDataToLoginSet(SQLiteDatabase db, ContextWrapper c, String v1_DatabaseName) {		
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
			db.insert(DatabaseLoginSetConstants.TABLE_LOGIN_SETS_NAME, null, values);
			db.setTransactionSuccessful();
			db.endTransaction();
			
			Log.i("database","Transferred old login data: "+school);
		}
		SharedPreferences.Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
		
		SQLiteDatabase v1_db = c.openOrCreateDatabase(v1_DatabaseName, ContextWrapper.MODE_PRIVATE, null);
		v1_db.execSQL("CREATE TABLE IF NOT EXISTS Presets (title TEXT, serverUrl TEXT, school TEXT, username TEXT, password TEXT);");
		Cursor query = v1_db.query("Presets", null, null, null, null, null, null);
		
		final String v1_presets_title = "title";
		final String v1_presets_serverUrl = "serverUrl";
		final String v1_presets_school = "school";
		final String v1_presets_username = "username";
		final String v1_presets_password = "password";
		
		int titleIndex = query.getColumnIndex(v1_presets_title);
		int serverUrlIndex = query.getColumnIndex(v1_presets_serverUrl);
		int schoolIndex = query.getColumnIndex(v1_presets_school);
		int usernameIndex = query.getColumnIndex(v1_presets_username);
		int passwordIndex = query.getColumnIndex(v1_presets_password);
		
		while(query.moveToNext()) {
			String presetTitle = query.getString(titleIndex);
			String presetServerUrl = query.getString(serverUrlIndex);
			String presetSchool = query.getString(schoolIndex);
			String presetUsername = query.getString(usernameIndex);
			String presetPassword = query.getString(passwordIndex);
			
			if(db.query(DatabaseLoginSetConstants.TABLE_LOGIN_SETS_NAME, new String[]{LoginSetConstants.nameKey}, LoginSetConstants.nameKey+"=?", new String[]{presetTitle}, null, null, null).getCount() < 1) {
				ContentValues values = new ContentValues();
				values.put(LoginSetConstants.nameKey, presetTitle);
				values.put(LoginSetConstants.serverUrlKey, presetServerUrl);
				values.put(LoginSetConstants.schoolKey, presetSchool);
				values.put(LoginSetConstants.usernameKey, presetUsername);
				values.put(LoginSetConstants.passwordKey, presetPassword);
				values.put(LoginSetConstants.sslOnlyKey, false);
			
				db.beginTransaction();
				db.insert(DatabaseLoginSetConstants.TABLE_LOGIN_SETS_NAME, null, values);
				db.setTransactionSuccessful();
				db.endTransaction();
				
				Log.i("database","Transferred login set: "+presetTitle);
			}
		}
	
	}

}
