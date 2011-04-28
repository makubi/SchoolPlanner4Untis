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

package edu.htl3r.schoolplanner.backend.schoolObjects.lesson;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import edu.htl3r.schoolplanner.backend.network.WebUntis;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeBreakSupervision;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeExamination;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeOfficeHour;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeStandby;

public class LessonTypeCreator {

	private Map<String, LessonType> lessonTypes = new HashMap<String, LessonType>();
	
	public LessonTypeCreator() {
		lessonTypes.put(WebUntis.OFFICEHOUR, new LessonTypeOfficeHour());
		lessonTypes.put(WebUntis.STANDBY, new LessonTypeStandby());
		lessonTypes.put(WebUntis.BREAKSUPERVISION, new LessonTypeBreakSupervision());
		lessonTypes.put(WebUntis.EXAMINATION, new LessonTypeExamination());
	}
	
	public LessonType createLessonType(String lessonTypeString) {
		try {
			return lessonTypes.get(lessonTypeString) != null ? (LessonType) lessonTypes.get(lessonTypeString).clone() : null;
		} catch (CloneNotSupportedException e) {
			Log.w("Misc", "Unable to clone lessonType",e);
		}
		return lessonTypes.get(lessonTypeString);
	}
	
	public void setLessonTypeColor(String lsType, int fgColor, int bgColor) {
		LessonType lt = lessonTypes.get(lsType);
		if(lt != null) {
			lt.setFgColor(fgColor);
			lt.setBgColor(bgColor);
		}
	}
}
