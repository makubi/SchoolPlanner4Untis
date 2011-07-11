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
public class DateTime extends Time {

	public DateTime(DateTime other) {
		super(other);
	}

	/**
	 * Erhoeht den Tag um 1 und normalisiert danach das Datum.
	 * {@link #normalize(boolean)}
	 */
	public void increaseDay() {
		monthDay+=1;
		normalize(true);
	}
	
	/**
	 * Verringert den Tag um 1 und normalisiert danach das Datum.
	 * @see {@link #normalize(boolean)}
	 */
	public void decreaseDay() {
		monthDay-=1;
		normalize(true);
	}
}
