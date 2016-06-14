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
package edu.htl3r.schoolplanner.backend.database;

import java.util.HashMap;
import java.util.Map;

import edu.htl3r.schoolplanner.backend.database.constants.DatabaseViewTypeConstants;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class ViewTypeFactory {

	private Map<String, ViewType> viewTypeTableObjectMapping = new HashMap<String, ViewType>();
	
	public ViewTypeFactory() {
		viewTypeTableObjectMapping.put(DatabaseViewTypeConstants.TABLE_SCHOOL_CLASSES_NAME, new SchoolClass());
		viewTypeTableObjectMapping.put(DatabaseViewTypeConstants.TABLE_SCHOOL_TEACHER_NAME, new SchoolTeacher());
		viewTypeTableObjectMapping.put(DatabaseViewTypeConstants.TABLE_SCHOOL_ROOMS_NAME, new SchoolRoom());
		viewTypeTableObjectMapping.put(DatabaseViewTypeConstants.TABLE_SCHOOL_SUBJECTS_NAME, new SchoolSubject());
	}
	
	public ViewType get(String table) {
		try {
			return (ViewType) (viewTypeTableObjectMapping.get(table) != null ? viewTypeTableObjectMapping.get(table).clone() : null);
		} catch (CloneNotSupportedException e) {
			if(DatabaseViewTypeConstants.TABLE_SCHOOL_CLASSES_NAME.equals(table)) {
				return new SchoolClass();
			}
			else if(DatabaseViewTypeConstants.TABLE_SCHOOL_TEACHER_NAME.equals(table)) {
				return new SchoolTeacher();
			}
			else if(DatabaseViewTypeConstants.TABLE_SCHOOL_ROOMS_NAME.equals(table)) {
				return new SchoolRoom();
			}
			else if(DatabaseViewTypeConstants.TABLE_SCHOOL_SUBJECTS_NAME.equals(table)) {
				return new SchoolSubject();
			}
		}
		throw new UnsupportedOperationException("Unable to clone object for table: "+table);
	}
}
