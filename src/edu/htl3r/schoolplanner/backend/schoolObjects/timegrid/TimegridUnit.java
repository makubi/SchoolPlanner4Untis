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

import java.util.Calendar;

import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolObject;

public class TimegridUnit implements SchoolObject, Comparable<TimegridUnit> {

	private Calendar begin;
	private Calendar end;

	public Calendar getBegin() {
		return begin;
	}
	public void setBegin(Calendar begin) {
		this.begin = begin;
	}
	public Calendar getEnd() {
		return end;
	}
	public void setEnd(Calendar end) {
		this.end = end;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() +": begin: " +CalendarUtils.getDateString(begin, true) +", " +CalendarUtils.getTimeStr(begin, false) +", end: " +CalendarUtils.getDateString(end, true) +", " +CalendarUtils.getTimeStr(end, false);
	}
	@Override
	public int compareTo(TimegridUnit another) {
		int start1 = (int) getBegin().getTimeInMillis();
		int start2 = (int) another.getBegin().getTimeInMillis();
		return start1 - start2;
	}
}