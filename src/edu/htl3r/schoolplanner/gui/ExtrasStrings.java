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

package edu.htl3r.schoolplanner.gui;

/**
 * Ein Interface fuer die Strings die bei einer Intentweiterleitung fuer die Extras verwendet werden
 * ( z.B.: Intent myIntent = new Intent(this, app.getCurrentView());
 * myIntent.putExtra(ExtrasStrings.VIEWTYPE, schoolClassList.get(position - 1)); )
 */
public interface ExtrasStrings {

	public static String VIEWTYPE = "viewtype";
	public static String DATE = "date";
	public static String PREVCLASS = "prevClass";
	public static String UPDATEVIEW = "updateView";
	public static String LESSON = "lesson";
	public static String DATELIST = "datelist";
	public static String HELPCLASS = "helpclass";
	public static String UPDATESELSCR = "updateSelectScreen";
	public static String UPDATESELSCRBTN = "updateSelectScreenButton";
	public static String RESYNC_DATA = "resyncData";
}