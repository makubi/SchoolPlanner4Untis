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

package edu.htl3r.schoolplanner.backend.network.json;

public enum JSONRequestMethod {	
	
	GET_TEACHERS("getTeachers"),
	GET_CLASSES("getKlassen"),
	GET_SUBJECTS("getSubjects"),
	GET_ROOMS("getRooms"),
	GET_HOLIDAYS("getHolidays"),
	GET_TIMEGRID_UNITS("getTimegridUnits"),
	GET_STATUS_DATA("getStatusData"),
	GET_TIMETABLE("getTimetable"),
	GET_LATEST_IMPORT_TIME("getLatestImportTime"),
	AUTHENTICATE("authenticate");
	
	private String requestMethod;
	
	private JSONRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	public String getRequestMethod() {
		return requestMethod;
	}
}
