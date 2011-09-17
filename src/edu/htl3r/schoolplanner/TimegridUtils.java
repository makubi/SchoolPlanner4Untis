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
package edu.htl3r.schoolplanner;

import java.util.Collections;
import java.util.List;

import android.text.format.Time;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;

public class TimegridUtils {

	public static final int[] ANDROID_TIME_WEEK_DAYS = new int[] {Time.SUNDAY, Time.MONDAY, Time.TUESDAY, Time.WEDNESDAY, Time.THURSDAY, Time.FRIDAY, Time.SATURDAY};
	
	/**
	 * Sortiert die {@link TimegridUnit}-Listen fuer jeden Tag im Timegrid.
	 * @param timegrid Timegrid, dessen Listen sortiert werden sollen
	 */
	public static void sortTimegridUnits(Timegrid timegrid) {
		for(int day : ANDROID_TIME_WEEK_DAYS) {
			List<TimegridUnit> timegridForDateTimeDay = timegrid.getTimegridForDateTimeDay(day);
			if(timegridForDateTimeDay != null) Collections.sort(timegridForDateTimeDay);
		}
	}
}
