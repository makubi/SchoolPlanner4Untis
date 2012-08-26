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

import org.codehaus.jackson.annotate.JsonProperty;

import edu.htl3r.schoolplanner.DateTime;

public class TimegridUnit implements Comparable<TimegridUnit> {

	private String name;
	
	private DateTime start = new DateTime();
	private DateTime end = new DateTime();

	public DateTime getStart() {
		return start;
	}

	public void setStart(DateTime start) {
		this.start = start;
	}

	public DateTime getEnd() {
		return end;
	}

	public void setEnd(DateTime end) {
		this.end = end;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonProperty(value="startTime")
	public void setStartTime(int startTimeInt) {		
		int startMinute = Integer.parseInt(getMinute(startTimeInt));
		int startHour = Integer.parseInt(getHour(startTimeInt));
		
		start.set(startMinute, startHour, start.getDay(), start.getMonth(), start.getYear());
	}
	
	@JsonProperty(value="endTime")
	public void endStartTime(int endTimeInt) {		
		int endMinute = Integer.parseInt(getMinute(endTimeInt));
		int endHour = Integer.parseInt(getHour(endTimeInt));
		
		end.set(endMinute, endHour, end.getDay(), end.getMonth(), end.getYear());
	}

	@Override
	public int compareTo(TimegridUnit another) {
		long start1 = getStart().getAndroidTime().toMillis(true);
		long start2 = another.getStart().getAndroidTime().toMillis(true);
		return (int) (start1 - start2);
	}
	
	private String getMinute(int timeString) {
		String time = ""+timeString;
		return time.length() <= 2 ? time : time.substring(time.length()-2);
	}
	
	private String getHour(int timeString) {
		String time = ""+timeString;
		return time.length() <= 2 ? "0" : time.substring(0, time.length()-2);
	}
}