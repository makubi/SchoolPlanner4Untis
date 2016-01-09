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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseAutoSelectConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseCreateConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseLoginSetConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabasePermanentLessonConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabasePermanentLessonViewTypeConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseSchoolHolidayConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseStatusDataConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseTimegridConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseViewTypeConstants;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;

public class DatabaseHelper extends SQLiteOpenHelper{

	private final static int DATABASE_VERSION = 9;
	private final static String DATABASE_NAME = "db_schoolplanner_data";
	
	private final List<String> CREATE_TABLE_STATEMENTS = new ArrayList<String>();
	private final String CREATE_TABLE_SQL = "CREATE TABLE";
	
	private Context context;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		CREATE_TABLE_STATEMENTS.addAll(getCreateMasterDataTables());
		CREATE_TABLE_STATEMENTS.addAll(getCreatePermanentLessonTables());
		CREATE_TABLE_STATEMENTS.addAll(getCreateSettingsTables());
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		for(String sqlStatement : CREATE_TABLE_STATEMENTS) {
			db.execSQL(sqlStatement);
		}
		
		final String v1_DatabaseName = "HTL3R_SchoolPlanner";
		Context c = SchoolplannerContext.context;
		
		try {
			SQLiteDatabase v1_db = SQLiteDatabase.openDatabase("/data/data/edu.htl3r.schoolplanner/databases/HTL3R_SchoolPlanner", null, SQLiteDatabase.OPEN_READONLY);
			transferLoginDataToLoginSet(db, v1_db);
			v1_db.close();
			
			removeV1Database(c, v1_DatabaseName);
		}
		catch(SQLiteException e) {
			// Alte Datenbank existiert nicht
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion < 6) {
			recreateTable(db, DatabaseStatusDataConstants.TABLE_STATUS_DATA_NAME, DatabaseCreateConstants.TABLE_STATUS_DATA_COLUMN_DEFINITIONS);
		}
		if(oldVersion < 7) {
			recreateTable(db, DatabaseTimegridConstants.TABLE_TIMEGRID_NAME, DatabaseCreateConstants.TABLE_TIMEGRID_COLUMN_DEFINITIONS);
		}
		if(oldVersion < 8) {
			db.beginTransaction();
			db.execSQL("CREATE TABLE " + DatabaseAutoSelectConstants.TABLE_AUTO_SELECT_NAME + "(" + DatabaseCreateConstants.TABLE_AUTO_SELECT_DEFINITIONS + ");");
			db.setTransactionSuccessful();
			db.endTransaction();
			
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SchoolplannerContext.context);
			SharedPreferences.Editor editor = preferences.edit();
			editor.remove("autoselect");
			editor.remove("autoselect_type");
			editor.commit();
		}
		if(oldVersion < 9){
			db.delete(DatabaseViewTypeConstants.TABLE_SCHOOL_CLASSES_NAME, null, null);
			db.delete(DatabaseViewTypeConstants.TABLE_SCHOOL_ROOMS_NAME, null, null);
			db.delete(DatabaseViewTypeConstants.TABLE_SCHOOL_SUBJECTS_NAME, null, null);
			db.delete(DatabaseViewTypeConstants.TABLE_SCHOOL_TEACHER_NAME, null, null);
			deleteFilesInFolder(context.getCacheDir().getAbsoluteFile());
		}
	}
	
	private void deleteFilesInFolder(File dir){
		for (File file : dir.listFiles()) {
			if(file.isDirectory()){
				deleteFilesInFolder(file);
			}else{
				file.delete();
			}
		}
	}

	
	private List<String> getCreateMasterDataTables() {		
		final Map<String, String> coloumnDefinitions = new HashMap<String, String>();
		
		addColoumnDefinition(coloumnDefinitions, DatabaseViewTypeConstants.TABLE_SCHOOL_CLASSES_NAME, "("+DatabaseCreateConstants.TABLE_VIEW_TYPE_COLUMN_DEFINITIONS+");");
		addColoumnDefinition(coloumnDefinitions, DatabaseViewTypeConstants.TABLE_SCHOOL_TEACHER_NAME, "("+DatabaseCreateConstants.TABLE_VIEW_TYPE_COLUMN_DEFINITIONS+");");
		addColoumnDefinition(coloumnDefinitions, DatabaseViewTypeConstants.TABLE_SCHOOL_ROOMS_NAME, "("+DatabaseCreateConstants.TABLE_VIEW_TYPE_COLUMN_DEFINITIONS+");");
		addColoumnDefinition(coloumnDefinitions, DatabaseViewTypeConstants.TABLE_SCHOOL_SUBJECTS_NAME, "("+DatabaseCreateConstants.TABLE_VIEW_TYPE_COLUMN_DEFINITIONS+");");
		
		addColoumnDefinition(coloumnDefinitions, DatabaseSchoolHolidayConstants.TABLE_SCHOOL_HOLIDAYS_NAME, "("+DatabaseCreateConstants.TABLE_SCHOOL_HOLIDAY_COLUMN_DEFINITIONS+");");
		addColoumnDefinition(coloumnDefinitions, DatabaseTimegridConstants.TABLE_TIMEGRID_NAME, "("+DatabaseCreateConstants.TABLE_TIMEGRID_COLUMN_DEFINITIONS+");");
		addColoumnDefinition(coloumnDefinitions, DatabaseStatusDataConstants.TABLE_STATUS_DATA_NAME, "("+DatabaseCreateConstants.TABLE_STATUS_DATA_COLUMN_DEFINITIONS+");");
		
		return getSQLCreateStatement(coloumnDefinitions);
	}

	private List<String> getCreatePermanentLessonTables() {		
		final Map<String, String> coloumnDefinitions = new HashMap<String, String>();
		
		addColoumnDefinition(coloumnDefinitions, DatabasePermanentLessonConstants.TABLE_PERMANENT_LESSONS_NAME, "("+DatabaseCreateConstants.TABLE_PERMANENT_LESSONS_DEFINITIONS+");");
		addColoumnDefinition(coloumnDefinitions, DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_CLASSES_NAME, "("+DatabaseCreateConstants.TABLE_PERMANENT_LESSONS_VIEW_TYPE_DEFINITIONS+");");
		addColoumnDefinition(coloumnDefinitions, DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_TEACHER_NAME, "("+DatabaseCreateConstants.TABLE_PERMANENT_LESSONS_VIEW_TYPE_DEFINITIONS+");");
		addColoumnDefinition(coloumnDefinitions, DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_ROOMS_NAME, "("+DatabaseCreateConstants.TABLE_PERMANENT_LESSONS_VIEW_TYPE_DEFINITIONS+");");
		addColoumnDefinition(coloumnDefinitions, DatabasePermanentLessonViewTypeConstants.TABLE_PERMANENT_LESSONS_SCHOOL_SUBJECTS_NAME, "("+DatabaseCreateConstants.TABLE_PERMANENT_LESSONS_VIEW_TYPE_DEFINITIONS+");");
		
		return getSQLCreateStatement(coloumnDefinitions);
	}

	private List<String> getCreateSettingsTables() {
		final Map<String, String> coloumnDefinitions = new HashMap<String, String>();
		
		addColoumnDefinition(coloumnDefinitions, DatabaseLoginSetConstants.TABLE_LOGIN_SETS_NAME, "("+DatabaseCreateConstants.TABLE_LOGIN_SETS_DEFINITIONS+");");
		
		addColoumnDefinition(coloumnDefinitions, DatabaseAutoSelectConstants.TABLE_AUTO_SELECT_NAME, "("+DatabaseCreateConstants.TABLE_AUTO_SELECT_DEFINITIONS+");");
		
		return getSQLCreateStatement(coloumnDefinitions);
	}

	private void addColoumnDefinition(Map<String, String> coloumnDefinitions, String tableName, String coloumDefinition) {
		coloumnDefinitions.put(tableName, coloumDefinition);
	}

	private List<String> getSQLCreateStatement(Map<String, String> coloumnDefinitions) {
		final List<String> createStatements = new ArrayList<String>();
		
		for(String tableName : coloumnDefinitions.keySet()) {
			createStatements.add(CREATE_TABLE_SQL+" "+tableName+" "+coloumnDefinitions.get(tableName));
		}
		
		return createStatements;
	}

	private void recreateTable(SQLiteDatabase db, String table, String coloumnDefinitions) {
		db.beginTransaction();
		db.execSQL("DROP TABLE "+ table);
		db.execSQL("CREATE TABLE " + table + "(" + coloumnDefinitions + ");");
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	private void removeV1Database(Context c, String v1_DatabaseName) {
		Log.i("database","Deleted old database: "+c.deleteDatabase(v1_DatabaseName));
	}
	
	private void transferLoginDataToLoginSet(SQLiteDatabase db, SQLiteDatabase v1_db) {		
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
