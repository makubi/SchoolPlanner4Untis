/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
						Gerald Schreiber <mail@gerald-schreiber.at>
						Philip Woelfel <philip@woelfel.at>
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

package edu.htl3r.schoolplanner.backend.localdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Oeffnet und verwaltet die Datenbank
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private final String LOGTAG = "Database";
	private static final String DATABASE_NAME = "HTL3R_SchoolPlanner";
	private static final int DATABASE_VERSION = 25;
	private final String[] TABLE_CREATES_VERSION_24 = {
			"create table if not exists SchoolClass (" +
				"id integer, " +
				"name text, " +
				"longname text, " +
				"forecolor integer, " +
				"backcolor integer" +
			");",
			"create table if not exists SchoolTeacher (" +
				"id integer, " +
				"name text, " +
				"longname text, " +
				"forecolor integer, " +
				"backcolor integer" +
			");",
			"create table if not exists SchoolSubject (" +
				"id integer, " +
				"name text, " +
				"longname text, " +
				"forecolor integer, " +
				"backcolor integer" +
			");",
			"create table if not exists SchoolRoom (" +
				"id integer, " +
				"name text, " +
				"longname text, " +
				"forecolor integer, " +
				"backcolor integer" +
			");",
			"create table if not exists SchoolHoliday (" +
				"id integer, " +
				"name text, " +
				"longname text, " +
				"startdate integer, " +
				"enddate integer" +
			");",
			"create table if not exists SchoolTestType (" +
				"id integer, " +
				"title text, " +
				"description text, " +
				"color integer" +
			");",
			"create table if not exists TimegridUnit (" +
				"id integer primary key autoincrement, " +
				"begin integer, " +
				"end integer," +
				"day integer" +
			");",
			"create table if not exists SchoolTest (" +
				"id text, " +
				"title text, " +
				"description text, " +
				"date integer," +
				"start integer, " +
				"end integer, " +
				"type integer references SchoolTestType(id), " +
				"last_update integer," +
				"local text" +
			");",
			"create table if not exists SchoolTest_SchoolClass (" +
				"schoolClass_id integer references SchoolClass(id), " +
				"test_id text references SchoolTest(id) on delete cascade" +
			");",
			"create table if not exists SchoolTest_SchoolTeacher (" +
				"schoolTeacher_id integer references SchoolTeacher(id), " +
				"test_id text references SchoolTest(id) on delete cascade" +
			");",
			"create table if not exists SchoolTest_SchoolSubject (" +
				"schoolSubject_id integer references SchoolSubject(id), " +
				"test_id text references SchoolTest(id) on delete cascade" +
			");",
			"create table if not exists SchoolTest_SchoolRoom (" +
				"schoolRoom_id integer references SchoolRoom(id), " +
				"test_id text references SchoolTest(id) on delete cascade" +
			");",
			"create table if not exists Lesson (" +
				"id integer, " +
				"viewtype text, " +
				"schoolobject text, " +
				"date integer, " +
				"starttime integer, " +
				"endtime integer, " +
				"lessonType text, " +
				"lessonCode text," +
				"last_update integer" +
			");",
			"create table if not exists Lesson_Substitution (" +
				"lesson_id integer references Lesson(id) on delete cascade, " +
				"schoolClass_id integer references SchoolClass(id), " +
				"schoolTeacher_id integer references SchoolTeacher(id), " +
				"schoolRoom_id integer references SchoolRoom(id), " +
				"schoolSubject_id integer references SchoolSubject(id) " +
			");",
			"create table if not exists Lesson_SchoolClass (" +
				"schoolClass_id integer references SchoolClass(id), " +
				"lesson_id integer references Lesson(id) on delete cascade" +
			");",
			"create table if not exists Lesson_SchoolTeacher (" +
				"schoolTeacher_id integer references SchoolTeacher(id), " +
				"lesson_id integer references Lesson(id) on delete cascade" +
			");",
			"create table if not exists Lesson_SchoolSubject (" +
				"schoolSubject_id integer references SchoolSubject(id), " +
				"lesson_id integer references Lesson(id) on delete cascade" +
			");",
			"create table if not exists Lesson_SchoolRoom (" +
				"schoolRoom_id integer references SchoolRoom(id), " +
				"lesson_id integer references Lesson(id) on delete cascade" +
			");"
	};
	private final String[] TABLE_DROPS_VERSION_24 = {
			"drop table if exists SchoolClass;",
			"drop table if exists SchoolTeacher;",
			"drop table if exists SchoolSubject;",
			"drop table if exists SchoolRoom;",
			"drop table if exists SchoolHoliday;",
			"drop table if exists Holiday;",
			"drop table if exists SchoolTestType;",
			"drop table if exists TestType;",
			"drop table if exists SchoolTimePeriod;",
			"drop table if exists TimePeriod;",
			"drop table if exists SchoolTest;",
			"drop table if exists Test;",
			"drop table if exists SchoolTest_SchoolClass;",
			"drop table if exists SchoolTest_SchoolTeacher;",
			"drop table if exists SchoolTest_SchoolSubject;",
			"drop table if exists SchoolTest_SchoolRoom;",
			"drop table if exists Lesson;",
			"drop table if exists Lesson_Substitution;",
			"drop table if exists Lesson_SchoolClass;",
			"drop table if exists Lesson_SchoolTeacher;",
			"drop table if exists Lesson_SchoolSubject;",
			"drop table if exists Lesson_SchoolRoom;",
			"drop table if exists TimegridUnit;",
			"drop table if exists TimegridDay;",
			"drop table if exists TimegridDay_TimegridUnit;",
			"drop table if exists Timegrid;",
			"drop table if exists Timegrid_TimegridDay;"
	};

	/**
	 * Default-Konstruktor des DatabaseHelpers
	 * @param context Kontext für die Datenbank
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTablesVersion24(db);	// Tabellen werden zum ersten Mal angelegt (in Version 24)
		updateTables(db, 24);		// Tabellen werden aktualisiert
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// ab Version 24 koennen Tests gespeichert werden --> duerfen nicht geloescht werden
		if(oldVersion < 24) {
			dropTablesVersion24(db);		// alle Tabellen loeschen
			createTablesVersion24(db);		// Tabellen in Version 24 anlegen
			updateTables(db, 24);			// Tabellen aktualisieren
		}
		else {
			updateTables(db, oldVersion);	// Tabellen aktualisieren
		}
	}

	/**
	 * Legt alle Tabellen in der Datenbank an
	 * @param db Datenbank
	 */
	private void createTablesVersion24(SQLiteDatabase db) {
		Log.d(LOGTAG, "Create Tables");
		for (int i = 0; i < TABLE_CREATES_VERSION_24.length; i++) {
			db.execSQL(TABLE_CREATES_VERSION_24[i]);
			Log.d(LOGTAG, "\t" + TABLE_CREATES_VERSION_24[i]);
		}
		Log.d(LOGTAG, "Tables created");
	}

	/**
	 * Loescht alle Tabellen in der Datenbank
	 * @param db Datenbank
	 */
	private void dropTablesVersion24(SQLiteDatabase db) {
		Log.d(LOGTAG, "Drop Tables");
		for (int i = 0; i < TABLE_DROPS_VERSION_24.length; i++) {
			db.execSQL(TABLE_DROPS_VERSION_24[i]);
			Log.d(LOGTAG, "\t" + TABLE_DROPS_VERSION_24[i]);
		}
		Log.d(LOGTAG, "Tables dropped");
	}

	private void updateTables(SQLiteDatabase db, int oldVersion) {
		// Aenderungen an der DB nur mehr einzeln durchfuehren
		Log.d(LOGTAG, "Update Tables");
		if(oldVersion == 24) { // auf Version 25 aktualisieren
			String command =
			"create table if not exists Presets (" +
				"title text, " +
				"serverUrl text, " +
				"school text, " +
				"username text, " +
				"password text" +
			");";
			db.execSQL(command);
			Log.d(LOGTAG, "\t" + command);
		}
		Log.d(LOGTAG, "Tables updated");
	}
}