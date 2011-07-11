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

package edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode;

import java.io.Serializable;

import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCode;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class LessonCodeSubstitute extends LessonCode implements Serializable {

	private static final long serialVersionUID = 2973013043522940591L;
	
	@SuppressWarnings("unused")
	private SchoolClass originSchoolClass;
	private SchoolTeacher originSchoolTeacher;
	private SchoolRoom originSchoolRoom;
	@SuppressWarnings("unused")
	private SchoolSubject originSchoolSubject;
	
	public SchoolTeacher getOriginSchoolTeacher() {
		return originSchoolTeacher;
	}
	
	public void setOriginSchoolTeacher(SchoolTeacher originSchoolTeacher) {
		this.originSchoolTeacher = originSchoolTeacher;
	}
	
	public SchoolRoom getOriginSchoolRoom() {
		return originSchoolRoom;
	}
	
	public void setOriginSchoolRoom(SchoolRoom originSchoolRoom) {
		this.originSchoolRoom = originSchoolRoom;
	}
}
