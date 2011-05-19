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

import android.app.Application;
import android.content.Context;
import edu.htl3r.schoolplanner.backend.Cache;

/**
 * Stellt den {@link Context} sowie den {@link Cache} fuer andere Klassen zur Verfuegung.<br>
 * Der Cache muss mit {@link Cache#init()} initialisiert werden, bevor er verwendet werden kann.<br>
 * Der Context muss mit dem {@link Application#getApplicationContext()} initialisert werden. Zur Zeit geschieht dies in der Klasse {@link SchoolPlannerApp}.
 */
public class SchoolplannerContext {
	
	public static Context context;
	public final static Cache cache = new Cache();
	
	// No get/set
}
