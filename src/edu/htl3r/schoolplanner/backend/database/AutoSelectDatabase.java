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

import org.springframework.util.Assert;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import edu.htl3r.schoolplanner.backend.AutoSelectHandler;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseAutoSelectConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseCreateConstants;
import edu.htl3r.schoolplanner.backend.preferences.AutoSelectSet;

public class AutoSelectDatabase implements AutoSelectHandler{

	private Database database;
	
	public AutoSelectDatabase(Database database) {
		this.database = database;
	}

	@Override
	public AutoSelectSet getAutoSelect() {
		AutoSelectSet autoSelectSet = new AutoSelectSet();
		
		SQLiteDatabase database = this.database.openDatabase(false);
		
		Cursor query = this.database.queryWithLoginSetKey(database, DatabaseAutoSelectConstants.TABLE_AUTO_SELECT_NAME);
		
		Assert.isTrue(query.getCount() <= 1, "More than one auto-select entry for "+this.database.getLoginSetKeyForTable()+".");
		
		int indexEnabled = query.getColumnIndex(DatabaseAutoSelectConstants.ENABLED);
		int indexType = query.getColumnIndex(DatabaseAutoSelectConstants.TYPE);
		int indexValue = query.getColumnIndex(DatabaseAutoSelectConstants.VALUE);
		
		while(query.moveToNext()) {
			boolean enabled = query.getInt(indexEnabled)>0;
			String type = query.getString(indexType);
			int value = query.getInt(indexValue);
			
			autoSelectSet.setEnabled(enabled);
			autoSelectSet.setAutoSelectType(type);
			autoSelectSet.setAutoSelectValue(value);
		}
		query.close();
		this.database.closeDatabase(database);
		
		return autoSelectSet;
	}

	@Override
	public void setAutoSelect(AutoSelectSet autoSelectSet) {
		final String TABLE = DatabaseAutoSelectConstants.TABLE_AUTO_SELECT_NAME;
		
		SQLiteDatabase database = this.database.openDatabase(true);
		
		this.database.deleteAllRowsWithLoginSetKey(database, TABLE);
		
		ContentValues values = new ContentValues();
		values.put(DatabaseCreateConstants.TABLE_LOGINSET_KEY, this.database.getLoginSetKeyForTable());
		values.put(DatabaseAutoSelectConstants.ENABLED, autoSelectSet.isEnabled());
		values.put(DatabaseAutoSelectConstants.TYPE, autoSelectSet.getAutoSelectType());
		values.put(DatabaseAutoSelectConstants.VALUE, autoSelectSet.getAutoSelectValue());
		
		database.beginTransaction();
		
		this.database.insert(database, TABLE, values);
		
		database.setTransactionSuccessful();
		database.endTransaction();
		this.database.closeDatabase(database);
	}
	
	private void deleteAllRowsFromDatabaseTransaction(String table, String loginSetKey) {
		SQLiteDatabase database = this.database.openDatabase(true);
		database.beginTransaction();
		
		this.database.deleteAllRowsWithLoginSetKey(database, table, loginSetKey);
		
		database.setTransactionSuccessful();
		database.endTransaction();
		this.database.closeDatabase(database);
	}
	
	public void deleteAllRowsFromAutoSelectWithLoginSetKey(String loginSetKey) {
		deleteAllRowsFromDatabaseTransaction(DatabaseAutoSelectConstants.TABLE_AUTO_SELECT_NAME, loginSetKey);
	}
	
}
