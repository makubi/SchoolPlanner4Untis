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

package edu.htl3r.schoolplanner.backend.schoolObjects;

import org.codehaus.jackson.annotate.JsonProperty;

import edu.htl3r.schoolplanner.DateTime;


public class SchoolHoliday {

	private int id;
	private String name;
	private String longName;
	private DateTime startDate;
	private DateTime endDate;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	
	public DateTime getStartDate() {
		return startDate;
	}
	
	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}
	
	public DateTime getEndDate() {
		return endDate;
	}
	
	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}
	
	@JsonProperty(value="startDate")
	public void setStartDate(String startDateString) {
		DateTime startDate = new DateTime();
		startDate.set(Integer.parseInt(startDateString.substring(6, 8)), Integer.parseInt(startDateString.substring(4, 6)), Integer.parseInt(startDateString.substring(0, 4)));
		
		this.startDate = startDate;
	}
	
	@JsonProperty(value="endDate")
	public void setEndDate(String endDateString) {
		DateTime endDate = new DateTime();
		endDate.set(Integer.parseInt(endDateString.substring(6, 8)), Integer.parseInt(endDateString.substring(4, 6)), Integer.parseInt(endDateString.substring(0, 4)));
		
		this.endDate = endDate;
	}
}