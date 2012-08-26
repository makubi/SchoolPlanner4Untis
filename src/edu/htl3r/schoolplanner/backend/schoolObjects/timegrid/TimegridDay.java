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

package edu.htl3r.schoolplanner.backend.schoolObjects.timegrid;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;


public class TimegridDay {

	private int day;
	private List<TimegridUnit> timegridUnitList = new ArrayList<TimegridUnit>();

	public void addTimegridUnit(TimegridUnit timegridUnit) {
		timegridUnitList.add(timegridUnit);
	}
	
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public List<TimegridUnit> getTimegridUnitList() {
		return timegridUnitList;
	}
	
	@JsonProperty(value="timeUnits")
	public void setTimegridUnitList(List<TimegridUnit> timegridUnitList) {
		this.timegridUnitList = timegridUnitList;
	}
}
