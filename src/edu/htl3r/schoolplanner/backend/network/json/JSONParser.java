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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCode;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCodeFactory;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonTypeFactory;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;
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
	
	/**
	 * Diese Methode erzeugt aus dem uebergeben JSONArray eine Map mit den darin enthaltenen Stunden.
	 * Lehrer-, Klassen-, etc.-Listen vom Cache werden verwendet, um direkt die Objekte, statt den IDs aus dem Netzwerk zuzuweisen.
	 * @param result JSONArray, das die Daten im JSON-Format enthaelt
	 * @return Eine Map mit Datums-String (siehe {@link DateTimeUtils#toISO8601Date(android.text.format.Time)}) zu Liste mit Stunden an diesem Tag
	 * @throws JSONException Wird geworfen, wenn ein Fehler beim Abfragen der Daten aus den JSON-Objekten auftritt. Z.B. wenn ein benoetigter Parameter fehlt oder die Struktur des JSON-Objekts nicht passend ist
	 * @throws IOException Wird geworfen, wenn beim Abfragen der Lehrer-, Klassen-, etc.-Listen ein Fehler auftritt
	 */
	public Map<String, List<Lesson>> jsonToLessonMap(JSONArray result) throws JSONException {
		// TODO Reagieren auf nicht-verfuegbare Listen
		List<SchoolClass> schoolClassList = cache.getSchoolClassList().getData();
		List<SchoolTeacher> schoolTeacherList = cache.getSchoolTeacherList().getData();
		List<SchoolRoom> schoolRoomList = cache.getSchoolRoomList().getData();
		List<SchoolSubject> schoolSubjectList = cache.getSchoolSubjectList().getData();
		
		Map<Integer, ? extends ViewType> schoolClassIdObjectMap = initIdToSchoolObjectListMapping(schoolClassList);
		Map<Integer, ? extends ViewType> schoolTeacherIdObjectMap = initIdToSchoolObjectListMapping(schoolTeacherList);
		Map<Integer, ? extends ViewType> schoolSubjectIdObjectMap = initIdToSchoolObjectListMapping(schoolSubjectList);
		Map<Integer, ? extends ViewType> schoolRoomIdObjectMap = initIdToSchoolObjectListMapping(schoolRoomList);
		
		Map<String,List<Lesson>> lessonList = new HashMap<String, List<Lesson>>();
		
		for(int i = 0; i < result.length(); i++){
			JSONObject lessonObject = result.getJSONObject(i);
			
			int id = lessonObject.getInt("id");
			
			// Verarbeite Datumsstring
			String rawDateString = ""+lessonObject.getInt("date");
			
			String rawYear = rawDateString.substring(0,4);
			String rawMonth = rawDateString.substring(4,6);
			String rawDay = rawDateString.substring(6,8);
			
			String startTime = ""+lessonObject.getInt("startTime");
			String endTime = ""+lessonObject.getInt("endTime");
			
			int year = Integer.parseInt(rawYear);
			int month = Integer.parseInt(rawMonth);
			int day = Integer.parseInt(rawDay);
			
			// WebUntis date string --> ISO8601 date string
			String dateString = rawYear+"-"+rawMonth+"-"+rawDay;
			
			int startMinute = Integer.parseInt(getMinute(startTime));
			int startHour = Integer.parseInt(getHour(startTime));
			
			int endMinute = Integer.parseInt(getMinute(endTime));
			int endHour = Integer.parseInt(getHour(endTime));
			
			
			LessonCode lessonCode = null;
			LessonType lessonType = null;
			
			// Ermittle Schulklassenliste
			List<SchoolClass> klList = new ArrayList<SchoolClass>();
			JSONArray klArray = lessonObject.getJSONArray("kl");
			for(int j = 0; j < klArray.length(); j++) {
				JSONObject kl = klArray.getJSONObject(j);
				int klId = kl.getInt("id");
				
				klList.add(getSchoolClassListObjectFromMap(schoolClassIdObjectMap, klId));
			}
			
			// Ermittle Lehrerliste
			List<SchoolTeacher> teList = new ArrayList<SchoolTeacher>();
			JSONArray teArray = lessonObject.getJSONArray("te");
			for(int j = 0; j < teArray.length(); j++) {
				JSONObject te = teArray.getJSONObject(j);
				int teId = te.getInt("id");
				
				if(te.has("orgid")) {
					int orgId = te.getInt("orgid");
					lessonCode = lessonCode == null ? new LessonCodeSubstitute() : lessonCode;
					((LessonCodeSubstitute)lessonCode).setOriginSchoolTeacher(getSchoolTeacherListObjectFromMap(schoolTeacherIdObjectMap, orgId));
				}
				
				teList.add(getSchoolTeacherListObjectFromMap(schoolTeacherIdObjectMap, teId));
			}
			
			// Ermittle Fachliste
			List<SchoolSubject> suList = new ArrayList<SchoolSubject>();
			JSONArray suArray = lessonObject.getJSONArray("su");
			for(int j = 0; j < suArray.length(); j++) {
				JSONObject su = suArray.getJSONObject(j);
				int suId = su.getInt("id");
				
				suList.add(getSchoolSubjectListObjectFromMap(schoolSubjectIdObjectMap, suId));
			}
			
			// Ermittle Raumliste
			List<SchoolRoom> roList = new ArrayList<SchoolRoom>();
			JSONArray roArray = lessonObject.getJSONArray("ro");
			for(int j = 0; j < roArray.length(); j++) {
				JSONObject ro = roArray.getJSONObject(j);
				int roId = ro.getInt("id");
				
				if(ro.has("orgid")) {
					int orgId = ro.getInt("orgid");
					lessonCode = lessonCode == null ? new LessonCodeSubstitute() : lessonCode;
					((LessonCodeSubstitute)lessonCode).setOriginSchoolRoom(getSchoolRoomListObjectFromMap(schoolRoomIdObjectMap, orgId));
				}
				
				roList.add(getSchoolRoomListObjectFromMap(schoolRoomIdObjectMap, roId));
			}
			
			// Ermittle LessonType
			if(lessonObject.has("lstype")) {				
				lessonType = lessonTypeCreator.createLessonType(lessonObject.getString("lstype"));
			}
			
			// Ermittle LessonCode
			if(lessonObject.has("code")) {				
				lessonCode = lessonCodeCreator.createLessonCode(lessonObject.getString("code"));
				// Wenn Supplierung und Entfall, ueberschreibt Entfall hier.
			}
			
			// Baue Lesson zusammen
			Lesson lesson = new Lesson();
			lesson.setId(id);
			lesson.setDate(year, month, day);
			lesson.setStartTime(startHour, startMinute);
			lesson.setEndTime(endHour, endMinute);
			lesson.setSchoolClasses(klList);
			lesson.setSchoolTeachers(teList);
			lesson.setSchoolSubjects(suList);
			lesson.setSchoolRooms(roList);
			
			lesson.setLessonType(lessonType);
			lesson.setLessonCode(lessonCode);
			
			// Fuege neue Leere ArrayList hinzu, wenn fuer Datum noch kein Eintrag in Map vorhanden
			if(!lessonList.containsKey(dateString)) {
				lessonList.put(dateString, new ArrayList<Lesson>());
			}
			
			// Fuege Lesson zu Datum in Map hinzu
			lessonList.get(dateString).add(lesson);
		}
		return lessonList;
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
