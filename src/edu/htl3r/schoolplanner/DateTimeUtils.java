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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.ParseException;

import android.content.Context;
import android.text.format.Time;

public class DateTimeUtils {

	private static Context context = SchoolplannerContext.context;
	
	public static String[] SHORT_WEEKDAY_NAMES = new String[] {
		getString(R.string.timetable_week_header_sunday_short),
		getString(R.string.timetable_week_header_monday_short),
		getString(R.string.timetable_week_header_tuesday_short),
		getString(R.string.timetable_week_header_wednesday_short),
		getString(R.string.timetable_week_header_thursday_short),
		getString(R.string.timetable_week_header_friday_short),
		getString(R.string.timetable_week_header_saturday_short)};
	
	public static String[] MONTH_NAMES = new String[] {
		getString(R.string.month_name_january),
		getString(R.string.month_name_february),
		getString(R.string.month_name_march),
		getString(R.string.month_name_april),
		getString(R.string.month_name_may),
		getString(R.string.month_name_june),
		getString(R.string.month_name_july),
		getString(R.string.month_name_august),
		getString(R.string.month_name_september),
		getString(R.string.month_name_october),
		getString(R.string.month_name_november),
		getString(R.string.month_name_december)
	};
	
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
	
	public static String toISO8601Time(DateTime time) {
		return normalizeTimePart(""+time.getHour()) + ":" + normalizeTimePart(""+time.getMinute());
	}
	
	/**
	 * Wandelt einen {@link #toISO8601Date(DateTime)}-String in ein DateTime-Objekt um.
	 * @param dateString String, der umgewandelt werden soll
	 * @return Das initialisierte {@link DateTime}-Objekt
	 * @throws ParseException Falls der String nicht geparst werden kann
	 */
	public static DateTime iso8601StringToDateTime(String dateString) throws ParseException {
		Pattern p = Pattern.compile("^([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})$");
		Matcher m = p.matcher(dateString);
		if(m.matches()) {
			DateTime dateTime = new DateTime();
			dateTime.set(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
			return dateTime;
		}
		throw new ParseException("Unable to parse String "+dateString+" to date.");
	}
	
	public static DateTime iso8601StringToTime(String timeString) throws ParseException {
		Pattern p = Pattern.compile("^([0-9]{2}):([0-9]{2})$");
		Matcher m = p.matcher(timeString);
		if(m.matches()) {
			DateTime dateTime = new DateTime();
			dateTime.setHour(Integer.parseInt(m.group(1)));
			dateTime.setMinute(Integer.parseInt(m.group(2)));
			return dateTime;
		}
		throw new ParseException("Unable to parse String "+timeString+" to time.");
	}
	
	private static String normalizeDate(String datePart) {
		return datePart.length() < 2 ? "0" + datePart : datePart;
	}
	
	private static String normalizeTimePart(String timePart) {
		return timePart.length() < 2 ? "0" + timePart : timePart;
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
	
	public static String getMonthName(DateTime dateTime) {
		return MONTH_NAMES[dateTime.getMonth()-1];
	}
	
	public static String getShortWeekDayName(DateTime dateTime) {
		return SHORT_WEEKDAY_NAMES[dateTime.getWeekDay()];
	}
	
	private static String getString(int resId) {
		return context.getString(resId);
	}

	public static DateTime getNow() {
		DateTime now = new DateTime();
		now.getAndroidTime().setToNow();
		return now;
	}
}
