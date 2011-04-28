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

package edu.htl3r.schoolplanner.gui.elements;

import android.view.ContextMenu.ContextMenuInfo;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;


/**
 * @author Philip Woelfel <philip[at]woelfel[dot]at>
 *	wird benoetigt um im ContextMenu auf die {@link Lesson} des gewaehlten Views zugreifen zu koennen
 */
public class ViewLessonContextInfo implements ContextMenuInfo {
	public Lesson lesson;
	
	public ViewLessonContextInfo(Lesson les) {
		lesson = les;
	}
}
