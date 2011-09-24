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

import edu.htl3r.schoolplanner.backend.network.WebUntis;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeCancelled;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeIrregular;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;

public class LessonCodeFactory {

	private Map<String, LessonCode> lessonCodes = new HashMap<String, LessonCode>();
	private LessonCodeSubstitute lessonCodeSubstitude = new LessonCodeSubstitute();
	
	public LessonCodeFactory() {
		lessonCodes.put(WebUntis.CANCELLED, new LessonCodeCancelled());
		lessonCodes.put(WebUntis.IRREGULAR, new LessonCodeIrregular());
	}
	
	public LessonCode createLessonCode(String lessonCode) { 
		try {
			return lessonCodes.get(lessonCode) != null ? (LessonCode) lessonCodes.get(lessonCode).clone() : null;
		} catch (CloneNotSupportedException e) {
			if(WebUntis.CANCELLED.equals(lessonCode)) {
				return new LessonCodeCancelled();
			}
			else if(WebUntis.IRREGULAR.equals(lessonCode)) {
				return new LessonCodeIrregular();
			}
			throw new UnsupportedOperationException("Unable to clone object for LessonCode: "+lessonCode);
		}
	}
	
	/**
	 * Benoetigt eigene Methode, von Untis kein Code fuer Supplierung vorgegeben.
	 * @return Den LessonCode Supplierung
	 */
	public LessonCodeSubstitute createLessonCodeSubstitude() {
		try {
			return (LessonCodeSubstitute) lessonCodeSubstitude.clone();
		} catch (CloneNotSupportedException e) {
			return new LessonCodeSubstitute();
		}
	}
}
