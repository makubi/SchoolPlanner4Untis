/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
			Sebastian Chlan <sebastian@schoolplanner.at>
			Christian Pascher <christian@schoolplanner.at>
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
public class DateTime implements Cloneable {

	private Time internalTime;
	
	public DateTime() {
		internalTime = new Time();
	}
	
	public DateTime(Time time) {
		internalTime = time;
		internalTime.monthDay += 1;
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
	
	public void set(int monthDay, int month, int year) {
		internalTime.set(monthDay, month-1, year);
	}
	
	public void set(int second, int minute, int hour, int monthDay, int month,
			int year) {
		internalTime.set(second, minute, hour, monthDay, month-1, year);
	}
	
	public void set(int minute, int hour, int monthDay, int month, int year) {
		set(00, minute, hour, monthDay, month, year);
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
	}
	
	public void setMonth(int month) {
		internalTime.month = month-1;
	}
	
	public void setDay(int day) {
		internalTime.monthDay = day;
	}
	
	public void setHour(int hour) {
		internalTime.hour = hour;
	}
	
	public void setMinute(int minute) {
		internalTime.minute = minute;
	}
	
	public void setSecond(int second) {
		internalTime.second = second;
	}
	
	public boolean before(DateTime other) {
		Time thisTime = DateTimeUtils.toTime(this);
		Time otherTime = DateTimeUtils.toTime(other);
		
		return thisTime.before(otherTime);
	}
	
	public boolean after(DateTime other) {
		Time thisTime = DateTimeUtils.toTime(this);
		Time otherTime = DateTimeUtils.toTime(other);
		
		return thisTime.after(otherTime);
	}
	
	public boolean equals(DateTime other) {
		Time thisTime = DateTimeUtils.toTime(this);
		Time otherTime = DateTimeUtils.toTime(other);
		
		return thisTime.equals(otherTime);
	}

	@Override
	public DateTime clone() {
		DateTime dateTimeClone = new DateTime();
		dateTimeClone.set(getSecond(), getMinute(), getHour(), getDay(), getMonth(), getYear());
		return dateTimeClone;
	}
	
}
