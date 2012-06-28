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

package edu.htl3r.schoolplanner.backend.network.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.backend.cache.Cache;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCode;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCodeFactory;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonTypeFactory;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

/**
 * Diese Klasse kann JSONObjekte in Programminterne Objekte umwandeln.
 */
public class JSONParser {

	private Cache cache;
	private final LessonCodeFactory lessonCodeCreator = new LessonCodeFactory();
	private final LessonTypeFactory lessonTypeCreator = new LessonTypeFactory();
	
	private Map<Integer, ? extends ViewType> schoolClassIdObjectMap;
	private Map<Integer, ? extends ViewType> schoolTeacherIdObjectMap;
	private Map<Integer, ? extends ViewType> schoolSubjectIdObjectMap;
	private Map<Integer, ? extends ViewType> schoolRoomIdObjectMap;
	
	public Map<String,List<Lesson>> jsonLessonsToTimetable(List<JSONLesson> jsonLessonList) {
		initViewTypeIdObjectMappings();
		
		Map<String,List<Lesson>> data = new HashMap<String, List<Lesson>>();
		
		for(JSONLesson jsonLesson : jsonLessonList) {
			Lesson lesson = jsonLessonToLesson(jsonLesson);
			data = putLessonToMap(data,lesson);
		}
		
		return data;
	}
	
	private void initViewTypeIdObjectMappings() {
		List<SchoolClass> schoolClassList = cache.getSchoolClassList().getData();
		List<SchoolTeacher> schoolTeacherList = cache.getSchoolTeacherList().getData();
		List<SchoolRoom> schoolRoomList = cache.getSchoolRoomList().getData();
		List<SchoolSubject> schoolSubjectList = cache.getSchoolSubjectList().getData();
		
		schoolClassIdObjectMap = initIdToSchoolObjectListMapping(schoolClassList);
		schoolTeacherIdObjectMap = initIdToSchoolObjectListMapping(schoolTeacherList);
		schoolSubjectIdObjectMap = initIdToSchoolObjectListMapping(schoolSubjectList);
		schoolRoomIdObjectMap = initIdToSchoolObjectListMapping(schoolRoomList);
	}

	private Lesson jsonLessonToLesson(JSONLesson jsonLesson) {
		Lesson lesson = new Lesson();
		
		lesson = lessonSetId(jsonLesson, lesson);
		lesson = lessonSetDateAndTimes(jsonLesson, lesson);
		lesson = lessonSetViewTypeLists(jsonLesson, lesson);
		lesson = lessonSetLessonCodeAndType(jsonLesson, lesson);
		
		return lesson;
	}

	private Lesson lessonSetId(JSONLesson jsonLesson, Lesson lesson) {
		Lesson updatedLesson = new Lesson(lesson);
		
		updatedLesson.setId(jsonLesson.getId());
		
		return updatedLesson;
	}

	private Lesson lessonSetDateAndTimes(JSONLesson jsonLesson, Lesson lesson) {
		Lesson updatedLesson = new Lesson(lesson);
		
		// Verarbeite Datumsstring
		String rawDateString = ""+jsonLesson.getDate();
		
		String rawYear = rawDateString.substring(0,4);
		String rawMonth = rawDateString.substring(4,6);
		String rawDay = rawDateString.substring(6,8);
		
		String startTime = ""+jsonLesson.getStartTime();
		String endTime = ""+jsonLesson.getEndTime();
		
		int year = Integer.parseInt(rawYear);
		int month = Integer.parseInt(rawMonth);
		int day = Integer.parseInt(rawDay);
		
		int startMinute = Integer.parseInt(getMinute(startTime));
		int startHour = Integer.parseInt(getHour(startTime));
		
		int endMinute = Integer.parseInt(getMinute(endTime));
		int endHour = Integer.parseInt(getHour(endTime));
		
		updatedLesson.setDate(year, month, day);
		updatedLesson.setStartTime(startHour, startMinute);
		updatedLesson.setEndTime(endHour, endMinute);
		
		return updatedLesson;
	}

	private Lesson lessonSetViewTypeLists(JSONLesson jsonLesson, Lesson lesson) {
		Lesson updatedLesson = new Lesson(lesson);
		
		List<SchoolClass> schoolClassList = getSchoolClassList(jsonLesson.getSchoolClasses(), schoolClassIdObjectMap);		
		List<SchoolRoom> schoolRoomList = getSchoolRoomList(jsonLesson.getSchoolRooms(), schoolRoomIdObjectMap);
		List<SchoolSubject> schoolSubjectsList = getSchoolSubjectList(jsonLesson.getSchoolSubjects(), schoolSubjectIdObjectMap);
		List<SchoolTeacher> schoolTeacherList = getSchoolTeacherList(jsonLesson.getSchoolTeachers(), schoolTeacherIdObjectMap);
		
		updatedLesson.setSchoolClasses(schoolClassList);
		updatedLesson.setSchoolRooms(schoolRoomList);
		updatedLesson.setSchoolSubjects(schoolSubjectsList);
		updatedLesson.setSchoolTeachers(schoolTeacherList);
		
		return updatedLesson;
	}

	private List<SchoolClass> getSchoolClassList(List<Integer> viewTypeIds, Map<Integer, ? extends ViewType> idObjectMap) {
		List<SchoolClass> viewTypeList = new ArrayList<SchoolClass>();
		
		for(int id : viewTypeIds) {
			viewTypeList.add(getSchoolClassListObjectFromMap(idObjectMap, id));
		}
		
		return viewTypeList;
	}
	
	private List<SchoolRoom> getSchoolRoomList(List<Integer> viewTypeIds, Map<Integer, ? extends ViewType> idObjectMap) {
		List<SchoolRoom> viewTypeList = new ArrayList<SchoolRoom>();
		
		for(int id : viewTypeIds) {
			viewTypeList.add(getSchoolRoomListObjectFromMap(idObjectMap, id));
		}
		
		return viewTypeList;
	}
	
	private List<SchoolSubject> getSchoolSubjectList(List<Integer> viewTypeIds, Map<Integer, ? extends ViewType> idObjectMap) {
		List<SchoolSubject> viewTypeList = new ArrayList<SchoolSubject>();
		
		for(int id : viewTypeIds) {
			viewTypeList.add(getSchoolSubjectListObjectFromMap(idObjectMap, id));
		}
		
		return viewTypeList;
	}
	
	private List<SchoolTeacher> getSchoolTeacherList(List<Integer> viewTypeIds, Map<Integer, ? extends ViewType> idObjectMap) {
		List<SchoolTeacher> viewTypeList = new ArrayList<SchoolTeacher>();
		
		for(int id : viewTypeIds) {
			viewTypeList.add(getSchoolTeacherListObjectFromMap(idObjectMap, id));
		}
		
		return viewTypeList;
	}
	
	private Lesson lessonSetLessonCodeAndType(JSONLesson jsonLesson,
			Lesson lesson) {
		Lesson updatedLesson = new Lesson(lesson);
		
		LessonCode lessonCode = null;
		LessonType lessonType = null;
		
		// Ermittle LessonType
		if(jsonLesson.getLessonType() != null) {				
			lessonType = lessonTypeCreator.createLessonType(jsonLesson.getLessonType());
		}
					
		// Ermittle LessonCode
		if(jsonLesson.getLessonCode() != null) {				
			lessonCode = lessonCodeCreator.createLessonCode(jsonLesson.getLessonCode());
			// Wenn Supplierung und Entfall, ueberschreibt Entfall hier.
		}
		
		updatedLesson.setLessonCode(lessonCode);
		updatedLesson.setLessonType(lessonType);
		
		return updatedLesson;
	}

	private Map<String, List<Lesson>> putLessonToMap(Map<String, List<Lesson>> data, Lesson lesson) {
		Map<String, List<Lesson>> updatedMap = new HashMap<String, List<Lesson>>(data);
		String date = DateTimeUtils.toISO8601Date(lesson.getDate());
		
		if(!updatedMap.containsKey(date)) {
			updatedMap.put(date, new ArrayList<Lesson>());
		}
		
		updatedMap.get(date).add(lesson);
		
		return updatedMap;
	}

	private String getMinute(String time) {
		return time.length() <= 2 ? time : time.substring(time.length()-2);
	}
	
	private String getHour(String time) {
		return time.length() <= 2 ? "0" : time.substring(0, time.length()-2);
	}

	/**
	 * Liefert eine Map mit ID --> ViewType anhand der uebergebenen Liste.
	 * @param schoolObjectList Liste mit ViewTypes
	 * @return Eine Map mit ID --> ViewType
	 */
	private Map<Integer, ? extends ViewType> initIdToSchoolObjectListMapping(
			List<? extends ViewType> schoolObjectList) {
		Map<Integer, ViewType> schoolObjectMap = new HashMap<Integer, ViewType>();
		for(ViewType view : schoolObjectList) {
			schoolObjectMap.put(view.getId(), view);
		}
		
		return schoolObjectMap;
	}

	/**
	 * Liefert das passende Schulklassenobjekt zu der uebergebenen ID. Sollte die ID nicht gefunden werden, wird ein 'Unknown'-Objekt zurueckgegeben.
	 * @param schoolClassIdObjectMap Map, in der sich ID --> SchoolClassObject befinden
	 * @param id ID, die abgefragt werden soll
	 * @return Das Schulklassenobjekt zur ID oder ein 'Unknown'-Objekt, wenn keines gefunden wurde
	 */
	private SchoolClass getSchoolClassListObjectFromMap(Map<Integer, ? extends ViewType> schoolClassIdObjectMap, int id) {
		return (SchoolClass) getSchoolListObject(SchoolClass.class, schoolClassIdObjectMap, id);
	}

	/**
	 * Liefert das passende Lehrerobjekt zu der uebergebenen ID. Sollte die ID nicht gefunden werden, wird ein 'Unknown'-Objekt zurueckgegeben.
	 * @param schoolTeacherIdObjectMap Map, in der sich ID --> SchoolTeacherObject befinden
	 * @param id ID, die abgefragt werden soll
	 * @return Das Lehrerobjekt zur ID oder ein 'Unknown'-Objekt, wenn keines gefunden wurde
	 */
	private SchoolTeacher getSchoolTeacherListObjectFromMap(Map<Integer, ? extends ViewType> schoolTeacherIdObjectMap, int id) {
		return (SchoolTeacher) getSchoolListObject(SchoolTeacher.class, schoolTeacherIdObjectMap, id);
	}

	/**
	 * Liefert das passende Fachobjekt zu der uebergebenen ID. Sollte die ID nicht gefunden werden, wird ein 'Unknown'-Objekt zurueckgegeben.
	 * @param schoolSubjectIdObjectMap Map, in der sich ID --> SchoolSubjectObject befinden
	 * @param id ID, die abgefragt werden soll
	 * @return Das Fachobjekt zur ID oder ein 'Unknown'-Objekt, wenn keines gefunden wurde
	 */
	private SchoolSubject getSchoolSubjectListObjectFromMap(Map<Integer, ? extends ViewType> schoolSubjectIdObjectMap, int id) {
		return (SchoolSubject) getSchoolListObject(SchoolSubject.class, schoolSubjectIdObjectMap, id);
	}

	/**
	 * Liefert das passende Raumobjekt zu der uebergebenen ID. Sollte die ID nicht gefunden werden, wird ein 'Unknown'-Objekt zurueckgegeben.
	 * @param schoolRoomIdObjectMap Map, in der sich ID --> SchoolRoomObject befinden
	 * @param id ID, die abgefragt werden soll
	 * @return Das Raumobjekt zur ID oder ein 'Unknown'-Objekt, wenn keines gefunden wurde
	 */
	private SchoolRoom getSchoolRoomListObjectFromMap(Map<Integer, ? extends ViewType> schoolRoomIdObjectMap, int id) {
		return (SchoolRoom) getSchoolListObject(SchoolRoom.class, schoolRoomIdObjectMap, id);
	}

	/**
	 * Liefert ein ViewType-Objekt anhand der uebergeben Map und ID. Sollte die ID nicht gefunden werden koennen, wird anhand der uebergeben Class ein 'Unknown'-Objekt erzeugt und zurueckgegeben.
	 * @param type Class, die das zurueckgegeben Objekt hat
	 * @param schoolObjectMap Map mit ID --> ViewType
	 * @param id ID, fuer die der passende ViewType gesucht werden soll
	 * @return Wenn ID vorhanden, passender ViewType, sonst ein 'Unknown'-Objekt
	 */
	private ViewType getSchoolListObject(Class<? extends ViewType> type, Map<Integer, ? extends ViewType> schoolObjectMap, int id) {
		if(schoolObjectMap.containsKey(id)) {
			return schoolObjectMap.get(id);
		}
		else {
			ViewType unknownObject = null;
			if(type == SchoolClass.class) {
				unknownObject = new SchoolClass();
			}
			else if (type == SchoolTeacher.class) {
				unknownObject = new SchoolTeacher();
				((SchoolTeacher)unknownObject).setForeName("");
			}
			else if (type == SchoolSubject.class) {
				unknownObject = new SchoolSubject();
			}
			else if (type == SchoolRoom.class) {
				unknownObject = new SchoolRoom();
			}
			unknownObject.setId(-1);
			unknownObject.setName("?");
			unknownObject.setLongName("Unknown object");
			unknownObject.setBackColor("");
			unknownObject.setForeColor("");
			
			return unknownObject;
		}		
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}
}
