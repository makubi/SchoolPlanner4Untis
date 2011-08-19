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

import edu.htl3r.schoolplanner.backend.database.constants.DatabaseCreateConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseSchoolHolidayConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseStatusDataConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseTimegridConstants;
import edu.htl3r.schoolplanner.backend.database.constants.DatabaseViewTypeConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	private final static int DATABASE_VERSION = 2;
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
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
