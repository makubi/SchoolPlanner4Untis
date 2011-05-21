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

package edu.htl3r.schoolplanner;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

/**
 * Stellt einige Hilfsmethoden zur Verfuegung, mit denen {@link Calendar}-Objekte verglichen, als String dargestellt oder spezifische Informationen aus diesem ausgelesen werden koennen.
 */
public class CalendarUtils {

	/**
	 * gibt einen String mit der Zeit in der Form HH:MM:SS.MS zurueck
	 * @param cal das Calendar Objekt von dem die Zeit ausgelesen werden soll
	 * @param seconds Sekunden anzeigen oder nicht
	 * @return ein String mit der formatierten Zeit
	 */
	public static String getTimeStr(Calendar cal, boolean seconds) {
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int m = cal.get(Calendar.MINUTE);
		int s = cal.get(Calendar.SECOND);
		int ms = cal.get(Calendar.MILLISECOND);

		String hS = (h < 10 ? "0" + h : "" + h);
		String mS = (m < 10 ? "0" + m : "" + m);
		String sS = (s < 10 ? "0" + s + "." + ms : "" + s + "." + ms);
		return hS + ":" + mS + (seconds ? ":" + sS : "");
	}

	/**
	 * gibt einen formatierten String (in der Form DD.MM.YYYY) des mitgebeben Datums zurueck
	 * @param date das Datum das formatiert werden soll
	 * @param dayname TODO
	 * @return ein String mit dem formatierten Datum
	 */
	public static String getDateString(Calendar date, boolean dayname) {
		int day = date.get(Calendar.DAY_OF_MONTH);
		int mon = (date.get(Calendar.MONTH) + 1);
		int year = date.get(Calendar.YEAR);
		String days = "";
		String mons = "";
		String years = "" + year;
		if (day < 10) {
			days = "0" + day;
		}
		else {
			days = "" + day;
		}
		if (mon < 10) {
			mons = "0" + mon;
		}
		else {
			mons = "" + mon;
		}

		String out = (dayname ? getWeekdayString(date.get(Calendar.DAY_OF_WEEK)) + ", " : "") + days + "." + mons + "." + years;

		return out;
	}

	/**
	 * gibt die Anzahl der Minuten zwischen den zwei {@link Calendar}-Objekten zurueck
	 * @param start das Datum von dem aus gezaehlt werden soll
	 * @param end das Datum bis zu dem gezaehlt werden soll, muss nach {@code start} sein
	 * @return die Minuten zwischen den zwei Daten
	 */
	public static long minBetween(Calendar start, Calendar end) {
		if (start.after(end)) {
			return 0;
		}

		long out = 0;
		Calendar date = (Calendar) start.clone();
		while (date.before(end)) {
			out++;
			date.add(Calendar.MINUTE, 1);
		}
		return out;
	}

	private static String[] weekdays_de = { "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag" };
	private static String[] weekdays_en = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	/**
	 * gibt den Namen des Tages zurueck
	 * @param day der Tag von dem der Name gebraucht wird (ist z.B.: {@link Calendar.MONDAY} etc.)
	 * @return Name des Tages als String 
	 */
	public static String getWeekdayString(int day) {
		DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
		String wday = dfs.getWeekdays()[day];
		try {
			int w = Integer.parseInt(wday);
			if (Locale.getDefault() == Locale.GERMAN || Locale.getDefault() == Locale.GERMANY) {
				return weekdays_de[w-1];
			}
			else {
				return weekdays_en[w-1];
			}
		} catch (NumberFormatException e) {
			return wday;
		}
	}

	
	private static String[] months_de = { "Jänner", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};
	private static String[] months_en = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

	public static String getMonthString(int month) {
		DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
		String mon = dfs.getMonths()[month];
		try {
			int w = Integer.parseInt(mon);
			if (Locale.getDefault() == Locale.GERMAN || Locale.getDefault() == Locale.GERMANY) {
				return months_de[w-1];
			}
			else {
				return months_en[w-1];
			}
		} catch (NumberFormatException e) {
			return mon;
		}
		
	}

	public static String dateToStr(Calendar date) {
		return getDateString(date, true) + ", " + getTimeStr(date, true);
	}

	/**
	 * Gibt einen String des {@link Calendar} in Form YYYYMMDD zurueck. Ausserdem wird das Monat um 1 erhoeht.
	 * @param date Datum, angepasst und umgewandelt werden soll
	 * @return Einen String in Form YYYYMMDD
	 */
	public static String getCalendarAs8601String(Calendar date) {
		String startYear = "" + date.get(Calendar.YEAR);
		String startMonth = "" + (date.get(Calendar.MONTH) + 1);
		String startDay = "" + date.get(Calendar.DAY_OF_MONTH);

		startMonth = normalizeDate(startMonth);
		startDay = normalizeDate(startDay);

		return startYear + startMonth + startDay;
	}

	/**
	 * Fuege eine 0 hinzu, falls Stringlaenge < 2 ist.
	 * @return
	 */
	public static String normalizeDate(String datePart) {
		return datePart.length() < 2 ? "0" + datePart : datePart;
	}

	public static String getShortWeekday(int day) {
		return new DateFormatSymbols(Locale.getDefault()).getShortWeekdays()[day];
	}

	public static String getDateStringNoYears(Calendar date, boolean dayname) {
		int day = date.get(Calendar.DAY_OF_MONTH);
		int mon = (date.get(Calendar.MONTH) + 1);

		String days = "";
		String mons = "";
		if (day < 10) {
			days = "0" + day;
		}
		else {
			days = "" + day;
		}
		if (mon < 10) {
			mons = "0" + mon;
		}
		else {
			mons = "" + mon;
		}

		String out = (dayname ? getWeekdayString(date.get(Calendar.DAY_OF_WEEK)) + ", " : "") + days + "." + mons + ".";

		return out;
	}

	public static boolean compareCalendarDates(Calendar d1, Calendar d2) {
		if (d1.get(Calendar.DAY_OF_MONTH) == d2.get(Calendar.DAY_OF_MONTH) && d1.get(Calendar.MONTH) == d2.get(Calendar.MONTH) && d1.get(Calendar.YEAR) == d2.get(Calendar.YEAR)) {
			return true;
		}
		return false;
	}
	
	public static boolean betweenCalendars(Calendar start, Calendar end, Calendar search) {
		Calendar lstart = (Calendar) start.clone();
		lstart.set(Calendar.DAY_OF_MONTH, lstart.get(Calendar.DAY_OF_MONTH) - 1);
		Calendar lend = (Calendar) end.clone();
		lend.set(Calendar.DAY_OF_MONTH, lend.get(Calendar.DAY_OF_MONTH) + 1);		
		
		return search.after(lstart) && search.before(lend) ? true : false;
	}

	public static Calendar getEmptyInstance() {
		Calendar cal = Calendar.getInstance();
		cal.set(0, 0, 0, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

}
