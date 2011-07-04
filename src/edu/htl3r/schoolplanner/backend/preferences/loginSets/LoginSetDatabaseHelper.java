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

import edu.htl3r.schoolplanner.gui.Constants;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginSetDatabaseHelper extends SQLiteOpenHelper {

	    private static final int DATABASE_VERSION = 3;
	    private static final String LOGINSET_DATABASE_NAME = "db_loginSets";
	    private static final String LOGINSET_TABLE_NAME = "loginSets";

	    public LoginSetDatabaseHelper(Context context) {
	        super(context, LOGINSET_DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        db.execSQL("CREATE TABLE " + LOGINSET_TABLE_NAME + " ("
	        		+ Constants.nameKey + " TEXT PRIMARY KEY, "
	        		+ Constants.serverUrlKey + " TEXT, "
	        		+ Constants.schoolKey + " TEXT, "
	        		+ Constants.usernameKey + " TEXT, "
	        		+ Constants.passwordKey + " TEXT, "
	        		+ Constants.sslOnlyKey + " BOOLEAN"
	        		+ ");"
	        		);
	    }

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion <= 2) {
				db.execSQL("ALTER TABLE " + LOGINSET_TABLE_NAME + " ADD COLUMN " + Constants.sslOnlyKey + " BOOLEAN" + ";");
			}
		}

		public String getLoginsetTableName() {
			return LOGINSET_TABLE_NAME;
		}
}
