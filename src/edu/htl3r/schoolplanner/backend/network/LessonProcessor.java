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
package edu.htl3r.schoolplanner.backend.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;

public class LessonProcessor {

	/**
	 * Fuegt fuer jeden Tag, der zwischen Start- und Enddatum liegen und an dem keine Stunden stattfinden, eine leere ArrayList hinzu.
	 * @param lessonMap Map, zu der die leeren ArrayListen hinzugefuegt werden sollen
	 * @param startDate Startdatum
	 * @param endDate Enddatum
	 * @return Map mit leeren Listen an den Tagen, an denen kein Unterricht stattfindet
	 */
	public Map<String, List<Lesson>> addEmptyDaysToLessonMap(Map<String, List<Lesson>> lessonMap, DateTime startDate, DateTime endDate) {
		Map<String, List<Lesson>> updatedLessonMap = lessonMap;
		DateTime tmpDate = startDate.clone();

		while(tmpDate.beforeOrEquals(endDate)) {
			String date = DateTimeUtils.toISO8601Date(tmpDate);
			if(!updatedLessonMap.containsKey(date)) {
				updatedLessonMap.put(date, new ArrayList<Lesson>());
			}
			tmpDate.increaseDay();
		}
		
		return updatedLessonMap;
	}
}
