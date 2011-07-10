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

import java.util.Calendar;

/**
 * Stellt einige Hilfsmethoden zur Verfuegung, mit denen {@link Calendar}-Objekte verglichen, als String dargestellt oder spezifische Informationen aus diesem ausgelesen werden koennen.
 */
public class CalendarUtils {

	/**
	 * Gibt einen String des {@link Calendar} in Form YYYYMMDD zurueck. Ausserdem wird das Monat um 1 erhoeht.
	 * @param date Datum, angepasst und umgewandelt werden soll
	 * @return Einen String in Form YYYYMMDD
	 * @deprecated Eigentliche Form YYYY-MM-DD
	 */
	@Deprecated
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
	 * @return Angepasster String
	 */
	public static String normalizeDate(String datePart) {
		return datePart.length() < 2 ? "0" + datePart : datePart;
	}

}
