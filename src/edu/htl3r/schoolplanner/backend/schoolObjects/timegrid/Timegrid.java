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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.format.Time;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.network.WebUntis;

/**
 * Diese Klasse repraesentiert einen Zeitraster.
 * Die Zeitrastereinheiten koennen fuer alle Tag, die sich im Zeitraster befinden, abgerufen werden.
 * Zu unterscheiden ist die Abfrage nach den Zeitrastern nach Calendar.DAY oder WebUntis.DAY. 
 */
public class Timegrid {

	private Map<Integer, Integer> staticCalendarDayMapping = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> staticTimeDayMapping = new HashMap<Integer, Integer>();
	
	private Map<Integer, TimegridDay> days = new HashMap<Integer, TimegridDay>();
	
	public Timegrid() {
		staticCalendarDayMapping.put(Calendar.SUNDAY, WebUntis.SUNDAY);
		staticCalendarDayMapping.put(Calendar.MONDAY, WebUntis.MONDAY);
		staticCalendarDayMapping.put(Calendar.TUESDAY, WebUntis.TUESDAY);
		staticCalendarDayMapping.put(Calendar.WEDNESDAY, WebUntis.WEDNESDAY);
		staticCalendarDayMapping.put(Calendar.THURSDAY, WebUntis.THURSDAY);
		staticCalendarDayMapping.put(Calendar.FRIDAY, WebUntis.FRIDAY);
		staticCalendarDayMapping.put(Calendar.SATURDAY, WebUntis.SATURDAY);
		
		staticTimeDayMapping.put(Time.SUNDAY, WebUntis.SUNDAY);
		staticTimeDayMapping.put(Time.MONDAY, WebUntis.MONDAY);
		staticTimeDayMapping.put(Time.TUESDAY, WebUntis.TUESDAY);
		staticTimeDayMapping.put(Time.WEDNESDAY, WebUntis.WEDNESDAY);
		staticTimeDayMapping.put(Time.THURSDAY, WebUntis.THURSDAY);
		staticTimeDayMapping.put(Time.FRIDAY, WebUntis.FRIDAY);
		staticTimeDayMapping.put(Time.SATURDAY, WebUntis.SATURDAY);
	} 
	
	/**
	 * Liefert eine Liste von TimegridUnits fuer den angebenenen Tag.
	 * @param day Webuntis-Tag, fuer den die Liste geliefert werden soll (z.B. {@link WebUntis#MONDAY})
	 * @return Liste zu angegebenem Tag
	 */
	public List<TimegridUnit> getTimegridForDay(int day) {
		TimegridDay timegridDay = days.get(day);
			return timegridDay != null ? timegridDay.getTimegridUnitList() : null;
	}
	
	/**
	 * Liefert eine Liste von TimegridUnits fuer den angebenenen Tag.
	 * @param staticCalendarDay Calendar-Tag, fuer den die Liste geliefert werden soll (z.B. {@link Calendar#MONDAY})
	 * @return Liste zu angegebenem Tag
	 */
	public List<TimegridUnit> getTimegridForCalendarDay(int staticCalendarDay) {		
		return getTimegridForDay(staticCalendarDayMapping.get(staticCalendarDay));
	}
	
	/**
	 * Liefert eine Liste von TimegridUnits fuer den angebenenen Tag.
	 * @param dateTimeDay Time-Tag, fuer den die Liste geliefert werden soll (z.B. {@link Time#MONDAY})
	 * @return Liste zu angegebenem Tag
	 */
	public List<TimegridUnit> getTimegridForDateTimeDay(int dateTimeDay) {
		return getTimegridForDay(staticTimeDayMapping.get(dateTimeDay));
	}
	
	/**
	 * Setzt die Liste fuer einen bestimmten WebUntis-Tag.
	 * @param day WebUntis-Tag, fuer den die Liste gesetzt werden soll (z.B. {@link WebUntis#MONDAY})
	 * @param timegridUnitList Liste mit TimegridUnits
	 */
	public void setTimegridForDay(int day, List<TimegridUnit> timegridUnitList) {
		TimegridDay gridDay = new TimegridDay();
		gridDay.setTimegridUnitList(timegridUnitList);
		days.put(day,gridDay);
	}
	
	public void setTimegridForDay(int day, TimegridDay timegridDay) {
		days.put(day, timegridDay);
	}
	
	/**
	 * Fuegt eine Zeitraster-Einheit zu einem {@link Time}-Tag hinzu.
	 * @param day Time-Tag, zu dem die Zeitraster-Einheit hinzugefuegt werden soll
	 * @param timegridUnit Zeitraster-Einheit, die hinzugefuegt werden soll
	 */
	public void putTimegridUnitForDateTimeDay(int day, TimegridUnit timegridUnit) {
		int webuntisDay = staticTimeDayMapping.get(day);
		
		if(!days.containsKey(webuntisDay)) days.put(webuntisDay, new TimegridDay());
		
		days.get(webuntisDay).addTimegridUnit(timegridUnit);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int day : days.keySet()) {
			sb.append("======= "+day+"\n");
			for(TimegridUnit timegridUnit : days.get(day).getTimegridUnitList()) {
				DateTime start = timegridUnit.getStart();
				DateTime end = timegridUnit.getEnd();
				sb.append(start.getHour()+":"+start.getMinute()+", ");
				sb.append(end.getHour()+":"+end.getMinute()+"\n");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	
}
