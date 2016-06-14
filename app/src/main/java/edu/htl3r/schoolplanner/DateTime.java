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

import android.text.format.Time;

/**
 * Diese Klasse repraesentiert ein {@link Time}-Objekt mit erweiterter Funktionalitaet.<br>
 * Es ist moeglich, den Tag dieses Objekts problemlos zu erhoehen oder verringern.
 */
public class DateTime implements Cloneable, Comparable<DateTime> {

	private Time internalTime;
	
	public DateTime() {
		internalTime = new Time();
	}
	
	public DateTime(Time time) {
		internalTime = time;
	}
	
	/**
	 * Erhoeht den Tag um 1 und normalisiert danach das Datum.
	 * {@link #normalize(boolean)}
	 */
	public void increaseDay() {
		internalTime.monthDay+=1;
		internalTime.normalize(true);
	}
	
	/**
	 * Verringert den Tag um 1 und normalisiert danach das Datum.
	 * @see {@link #normalize(boolean)}
	 */
	public void decreaseDay() {
		internalTime.monthDay-=1;
		internalTime.normalize(true);
	}
	
	public void set(int day, int month, int year) {
		internalTime.set(day, month-1, year);
		internalTime.normalize(true);
	}
	
	public void set(int second, int minute, int hour, int day, int month,
			int year) {
		internalTime.set(second, minute, hour, day, month-1, year);
		internalTime.normalize(true);
	}
	
	/**
	 * Setzt die uebergebenen Werte und die Sekunde auf 0.
	 * @param minute
	 * @param hour
	 * @param monthDay
	 * @param month
	 * @param year
	 */
	public void set(int minute, int hour, int day, int month, int year) {
		set(00, minute, hour, day, month, year);
	}
	
	public int getYear() {
		return internalTime.year;
	}
	
	public int getMonth() {
		return internalTime.month+1;
	}
	
	public int getDay() {
		return internalTime.monthDay;
	}
	
	public int getWeekDay() {
		return internalTime.weekDay;
	}
	
	public int getHour() {
		return internalTime.hour;
	}
	
	public int getMinute() {
		return internalTime.minute;
	}
	
	public int getSecond() {
		return internalTime.second;
	}
	
	public void setYear(int year) {
		internalTime.year = year;
		internalTime.normalize(true);
	}
	
	public void setMonth(int month) {
		internalTime.month = month-1;
		internalTime.normalize(true);
	}
	
	public void setDay(int day) {
		internalTime.monthDay = day;
		internalTime.normalize(true);
	}
	
	public void setHour(int hour) {
		internalTime.hour = hour;
		internalTime.normalize(true);
	}
	
	public void setMinute(int minute) {
		internalTime.minute = minute;
		internalTime.normalize(true);
	}
	
	public void setSecond(int second) {
		internalTime.second = second;
		internalTime.normalize(true);
	}
	
	public Time getAndroidTime() {
		return internalTime;
	}
	
	public boolean beforeOrEquals(DateTime o) {
		return compareTo(o) <= 0;
	}
	
	@Override
	public DateTime clone() {
		DateTime dateTimeClone = new DateTime();
		dateTimeClone.set(getSecond(), getMinute(), getHour(), getDay(), getMonth(), getYear());
		
		return dateTimeClone;
	}

	@Override
	public int compareTo(DateTime o) {
		return Time.compare(getAndroidTime(), o.getAndroidTime());
	}
	
	@Override
	public String toString() {
		return getDay() +"."+getMonth()+"."+getYear() + " " + getHour()+":" +
				(((""+getMinute()).length()==2)? getMinute()+"" : "0"+getMinute());
	}
	
}
