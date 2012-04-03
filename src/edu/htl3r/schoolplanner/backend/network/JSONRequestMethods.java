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

package edu.htl3r.schoolplanner.backend.network;

public interface JSONRequestMethods {
	public static final String getTeachers = "getTeachers";
	public static final String getClasses = "getKlassen";
	public static final String getSubjects = "getSubjects";
	public static final String getRooms = "getRooms";
	
	public static final String getHolidays = "getHolidays";
	public static final String getTimegridUnits = "getTimegridUnits";
	
	public static final String getTimetable = "getTimetable";
	public static final String getStatusData = "getStatusData";
	public static final String getLatestImportTime = "getLatestImportTime";
	
	public static final String authenticate = "authenticate";
}
