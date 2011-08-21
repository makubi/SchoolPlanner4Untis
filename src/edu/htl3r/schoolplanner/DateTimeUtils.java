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

import java.util.Calendar;

import android.text.format.Time;

public class DateTimeUtils {

	/**
	 * Liefert ein {@link Time}-Objekt als ISO-8601-String (Form: YYYY-MM-DD).
	 * @param date {@link Time}-Objekt, von dem der String erzeugt werden soll
	 * @return Uebergebenes {@link Time}-Objekt als ISO-8601-String
	 */
	public static String toISO8601Date(Time date) {
		String month = normalizeDate(""+date.month);
		String day = normalizeDate(""+(date.monthDay+1));
		
		return date.year + "-" + month + "-" + day;
	}
	
	/**
	 * Liefert ein {@link DateTime}-Objekt als ISO-8601-String (Form: YYYY-MM-DD).
	 * @param date {@link DateTime}-Objekt, von dem der String erzeugt werden soll
	 * @return Uebergebenes {@link DateTime}-Objekt als ISO-8601-String
	 */
	public static String toISO8601Date(DateTime date) {
		String month = normalizeDate(""+date.getMonth());
		String day = normalizeDate(""+(date.getDay()));
		
		return date.getYear() + "-" + month + "-" + day;
	}
	
	/**
	 * Wandelt einen {@link #toISO8601Date(DateTime)}-String in ein DateTime-Objekt um.
	 * @param dateString String, der umgewandelt werden soll
	 * @return Das initialisierte {@link DateTime}-Objekt
	 */
	public static DateTime iso8601StringToDateTime(String dateString) {
		DateTime dateTime = new DateTime();
		final String dateSeparator = "-";
		String[] date = dateString.split(dateSeparator);
		dateTime.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
		return dateTime;
	}
	
	private static String normalizeDate(String datePart) {
		return datePart.length() < 2 ? "0" + datePart : datePart;
	}
	
	public static Time toTime(DateTime dateTime) {
		Time time = new Time();
		time.set(dateTime.getSecond(), dateTime.getMinute(), dateTime.getHour(), dateTime.getDay(), dateTime.getMonth()-1, dateTime.getYear());
		return time;
	}
	
	public static DateTime toDateTime(Calendar calendar) {
		DateTime dateTime = new DateTime();
		dateTime.set(calendar.get(Calendar.MINUTE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
		return dateTime;
	}
}
