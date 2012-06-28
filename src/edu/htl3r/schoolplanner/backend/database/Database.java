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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.backend.AutoSelectHandler;
import edu.htl3r.schoolplanner.backend.LoginSetHandler;
import edu.htl3r.schoolplanner.backend.MasterdataProvider;
import edu.htl3r.schoolplanner.backend.MasterdataStore;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseCreateConstants;
import edu.htl3r.schoolplanner.backend.preferences.AutoSelectSet;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetDatabase;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class Database implements MasterdataStore, MasterdataProvider, LoginSetHandler, AutoSelectHandler {
	
	private DatabaseHelper databaseHelper = new DatabaseHelper(SchoolplannerContext.context);

	private MasterDataDatabase masterDataDatabase = new MasterDataDatabase(this);
	
	private LoginSetDatabase loginSetDatabase = new LoginSetDatabase(this);
	
	private AutoSelectDatabase autoselectDatabase = new AutoSelectDatabase(this);
	
	private String loginSetKey;
	
	public SQLiteDatabase openDatabase(boolean writeable) {
		return writeable ? databaseHelper.getWritableDatabase() : databaseHelper.getReadableDatabase();
	}
	
	public void closeDatabase(SQLiteDatabase database) {
		database.close();
	}
	
	public synchronized long insert(SQLiteDatabase database, String table, ContentValues values) {
		return database.insert(table, null, values);
	}
	
	/**
	 * Liefert den {@link Cursor} zum Query zum uebergebenen Table-Namen.
	 * @param table
	 * @return
	 */
	public synchronized Cursor query(SQLiteDatabase database, String table) {
		return database.query(table, null, null, null, null, null, null);
	}
	
	public synchronized Cursor queryWithLoginSetKey(SQLiteDatabase database, String table) {
		return database.query(table, null, DatabaseCreateConstants.TABLE_LOGINSET_KEY+"=?", new String[]{loginSetKey}, null, null, null);
	}
	
	public synchronized void deleteAllRows(SQLiteDatabase database, String table) {
		database.delete(table, null, null);
	}
	
	public synchronized void deleteAllRowsWithLoginSetKey(SQLiteDatabase database, String table) {
		database.delete(table, DatabaseCreateConstants.TABLE_LOGINSET_KEY+"=?", new String[]{loginSetKey});
	}
	
	public synchronized void deleteAllRowsWithLoginSetKey(SQLiteDatabase database,
			String table, String loginSetKey) {
		database.delete(table, DatabaseCreateConstants.TABLE_LOGINSET_KEY+"=?", new String[]{loginSetKey});
	}

	@Override
	public synchronized List<SchoolClass> getSchoolClassList() {
		return masterDataDatabase.getSchoolClassList();
	}

	@Override
	public synchronized List<SchoolTeacher> getSchoolTeacherList() {
		return masterDataDatabase.getSchoolTeacherList();
	}

	@Override
	public synchronized List<SchoolRoom> getSchoolRoomList() {
		return masterDataDatabase.getSchoolRoomList();
	}

	@Override
	public synchronized List<SchoolSubject> getSchoolSubjectList() {
		return masterDataDatabase.getSchoolSubjectList();
	}

	@Override
	public synchronized List<SchoolHoliday> getSchoolHolidayList() {
		return masterDataDatabase.getSchoolHolidayList();
	}

	@Override
	public synchronized Timegrid getTimegrid() {
		return masterDataDatabase.getTimegrid();
	}

	@Override
	public synchronized void setSchoolClassList(List<SchoolClass> schoolClasses) {
		masterDataDatabase.setSchoolClassList(schoolClasses);
	}

	@Override
	public synchronized void setSchoolTeacherList(List<SchoolTeacher> schoolTeachers) {
		masterDataDatabase.setSchoolTeacherList(schoolTeachers);
	}

	@Override
	public synchronized void setSchoolSubjectList(List<SchoolSubject> schoolSubjects) {
		masterDataDatabase.setSchoolSubjectList(schoolSubjects);
	}

	@Override
	public synchronized void setSchoolRoomList(List<SchoolRoom> schoolRooms) {
		masterDataDatabase.setSchoolRoomList(schoolRooms);
	}

	@Override
	public synchronized void setSchoolHolidayList(List<SchoolHoliday> holidayList) {
		masterDataDatabase.setSchoolHolidayList(holidayList);
	}

	@Override
	public synchronized void setTimegrid(Timegrid timegrid) {
		masterDataDatabase.setTimegrid(timegrid);
	}

	@Override
	public synchronized void saveLoginSet(LoginSet loginSet) {
		loginSetDatabase.saveLoginSet(loginSet);
	}
	
	@Override
	public synchronized void removeLoginSet(LoginSet loginSet) {
		String loginSetKey = md5(loginSet.getServerUrl()+loginSet.getSchool());
		deleteMasterdataForLoginSetKey(loginSetKey);
		autoselectDatabase.deleteAllRowsFromAutoSelectWithLoginSetKey(loginSetKey);
		loginSetDatabase.removeLoginSet(loginSet);
	}

	@Override
	public synchronized void editLoginSet(String name, String serverUrl, String school, String username, String password, boolean checked, String oldName, String oldServerUrl, String oldSchool) {
		if(!serverUrl.equals(oldServerUrl) || !school.equals(oldSchool)) {
			String loginSetKey = md5(serverUrl+school);
			deleteMasterdataForLoginSetKey(loginSetKey);
			autoselectDatabase.deleteAllRowsFromAutoSelectWithLoginSetKey(loginSetKey);
		}
		loginSetDatabase.editLoginSet(name, serverUrl, school, username, password, checked);
	}

	@Override
	public synchronized List<LoginSet> getAllLoginSets() {
		return loginSetDatabase.getAllLoginSets();
	}
	
	public synchronized void deleteMasterdataForLoginSetKey(String loginSetKey) {
		masterDataDatabase.deleteAllRowsFromSchoolClassListWithLoginSetKey(loginSetKey);
		masterDataDatabase.deleteAllRowsFromSchoolTeacherListWithLoginSetKey(loginSetKey);
		masterDataDatabase.deleteAllRowsFromSchoolRoomListWithLoginSetKey(loginSetKey);
		masterDataDatabase.deleteAllRowsFromSchoolSubjectListWithLoginSetKey(loginSetKey);
		
		masterDataDatabase.deleteAllRowsFromSchoolHolidayListWithLoginSetKey(loginSetKey);
		masterDataDatabase.deleteAllRowsFromTimegridWithLoginSetKey(loginSetKey);
		masterDataDatabase.deleteAllRowsFromStatusDataListWithLoginSetKey(loginSetKey);
	}

	public long dateTimeToMillis(DateTime dateTime) {
		return dateTime.getAndroidTime().toMillis(true);
	}

	public DateTime millisToDateTime(long millis) {
		DateTime dateTime = new DateTime();
		dateTime.getAndroidTime().set(millis);
		return dateTime;
	}

	public void setActiveLoginSet(LoginSet activeLoginSet) {
		loginSetKey = md5(activeLoginSet.getServerUrl()+activeLoginSet.getSchool());
	}

	/**
	 * Liefert den LoginSet-Key, der sich aus Server-URL + Schulname zusammensetzt.
	 * Dieser wird beim Setzen des zu verwendenden LoginSets generiert.
	 * @return Den LoginSet-Key zum aktuell gesetzten LoginSet
	 */
	public String getLoginSetKeyForTable() {
		return loginSetKey;
	}

	/**
	 * Generiert einen MD5 Hash
	 * @param s der zu verhasende String
	 * @return Hash
	 */
	private String md5(final String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
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
			Log.w("database",e);
		}
		return "nomd5"+s;
	}

	@Override
	public AutoSelectSet getAutoSelect() {
		return autoselectDatabase.getAutoSelect();
	}

	@Override
	public void setAutoSelect(AutoSelectSet autoSelectSet) {
		autoselectDatabase.setAutoSelect(autoSelectSet);
	}
	
}
