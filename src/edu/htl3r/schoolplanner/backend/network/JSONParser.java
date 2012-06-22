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

package edu.htl3r.schoolplanner.backend.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.StatusData;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCode;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCodeFactory;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonTypeFactory;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;
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
	
	private String getValueFromJSON(JSONObject jsonObject, String key, String defaultValue) throws JSONException {
		return jsonObject.has(key) ? jsonObject.getString(key) : defaultValue;
	}
	
	@Deprecated
	public List<SchoolTeacher> jsonToTeacherList(JSONArray result)
			throws JSONException {
		List<SchoolTeacher> schoolTeacherList = new ArrayList<SchoolTeacher>();
		
		for (int i = 0; i < result.length(); i++) {
			SchoolTeacher schoolTeacherObject = new SchoolTeacher();

			JSONObject teacherObject = result.getJSONObject(i);

			int id = teacherObject.getInt("id");
			String name = teacherObject.getString("name");
			String foreName = teacherObject.getString("foreName");
			String longName = teacherObject.getString("longName");
			String foreColor = getValueFromJSON(teacherObject, "foreColor", "");
			String backColor = getValueFromJSON(teacherObject, "backColor", "");			
			schoolTeacherObject.setId(id);
			schoolTeacherObject.setName(name);
			schoolTeacherObject.setForeName(foreName);
			schoolTeacherObject.setLongName(longName);
			schoolTeacherObject.setForeColor(foreColor);
			schoolTeacherObject.setBackColor(backColor);
			
			schoolTeacherList.add(schoolTeacherObject);
		}
		return schoolTeacherList;
	}

	@Deprecated
	public List<SchoolClass> jsonToClassList(JSONArray result)
			throws JSONException {
		List<SchoolClass> schoolClassList = new ArrayList<SchoolClass>();

		for (int i = 0; i < result.length(); i++) {
			SchoolClass schoolClassObject = new SchoolClass();

			JSONObject classObject = result.getJSONObject(i);

			int id = classObject.getInt("id");
			String name = classObject.getString("name");
			String longName = classObject.getString("longName");
			String foreColor = getValueFromJSON(classObject, "foreColor", "");
			String backColor = getValueFromJSON(classObject, "backColor", "");

			schoolClassObject.setId(id);
			schoolClassObject.setName(name);
			schoolClassObject.setLongName(longName);
			schoolClassObject.setForeColor(foreColor);
			schoolClassObject.setBackColor(backColor);

			schoolClassList.add(schoolClassObject);
		}
		return schoolClassList;
	}

	@Deprecated
	public List<SchoolSubject> jsonToSubjectList(JSONArray result)
			throws JSONException {
		List<SchoolSubject> schoolSubjectList = new ArrayList<SchoolSubject>();

		for (int i = 0; i < result.length(); i++) {
			SchoolSubject schoolSubjectObject = new SchoolSubject();

			JSONObject subjectObject = result.getJSONObject(i);

			int id = subjectObject.getInt("id");
			String name = subjectObject.getString("name");
			String longName = subjectObject.getString("longName");
			String foreColor = getValueFromJSON(subjectObject, "foreColor", "");
			String backColor = getValueFromJSON(subjectObject, "backColor", "");
			schoolSubjectObject.setId(id);
			schoolSubjectObject.setName(name);
			schoolSubjectObject.setLongName(longName);
			schoolSubjectObject.setForeColor(foreColor);
			schoolSubjectObject.setBackColor(backColor);

			schoolSubjectList.add(schoolSubjectObject);
		}
		return schoolSubjectList;
	}

	@Deprecated
	public List<SchoolRoom> jsonToRoomList(JSONArray result)
			throws JSONException {
		List<SchoolRoom> schoolRoomsList = new ArrayList<SchoolRoom>();

		for (int i = 0; i < result.length(); i++) {
			SchoolRoom schoolRoomObject = new SchoolRoom();

			JSONObject roomObject = result.getJSONObject(i);

			int id = roomObject.getInt("id");
			String name = roomObject.getString("name");
			String longName = roomObject.getString("longName");
			String foreColor = getValueFromJSON(roomObject, "foreColor", "");
			String backColor = getValueFromJSON(roomObject, "backColor", "");
			schoolRoomObject.setId(id);
			schoolRoomObject.setName(name);
			schoolRoomObject.setLongName(longName);
			schoolRoomObject.setForeColor(foreColor);
			schoolRoomObject.setBackColor(backColor);
			
			schoolRoomsList.add(schoolRoomObject);
		}
		return schoolRoomsList;
	}

	/**
	 * Erzeugt aus dem uebergebenen JSONArray eine Liste mit freien Tagen.
	 * @param result JSONArray, in dem sich die Daten befinden
	 * @return Eine Liste mit freien Tagen
	 * @throws JSONException Wenn beim Abfragen der JSON-Daten ein Fehler auftritt
	 */
	public List<SchoolHoliday> jsonToHolidayList(JSONArray result) throws JSONException {
		List<SchoolHoliday> schoolHolidayList = new ArrayList<SchoolHoliday>();

		for (int i = 0; i < result.length(); i++) {
			SchoolHoliday schoolHolidayObject = new SchoolHoliday();

			JSONObject holidayObject = result.getJSONObject(i);

			int id = holidayObject.getInt("id");
			String name = holidayObject.getString("name");
			String longName = holidayObject.getString("longName");
			String startDateString = ""+holidayObject.getInt("startDate");
			String endDateString = ""+holidayObject.getInt("endDate");
			
			DateTime startDate = new DateTime();
			startDate.set(Integer.parseInt(startDateString.substring(6, 8)), Integer.parseInt(startDateString.substring(4, 6)), Integer.parseInt(startDateString.substring(0, 4)));
			DateTime endDate = new DateTime();
			endDate.set(Integer.parseInt(endDateString.substring(6, 8)), Integer.parseInt(endDateString.substring(4, 6)), Integer.parseInt(endDateString.substring(0, 4)));
			
			schoolHolidayObject.setId(id);
			schoolHolidayObject.setName(name);
			schoolHolidayObject.setLongName(longName);
			schoolHolidayObject.setStartDate(startDate);
			schoolHolidayObject.setEndDate(endDate);
			
			schoolHolidayList.add(schoolHolidayObject);
		}
		return schoolHolidayList;
	}

	/**
	 * Erzeugt aus dem uebergebenen JSONArray das Zeitraster.
	 * @param result JSONArray mit den benoetigten Daten
	 * @return Das initialisierte Zeitraster
	 * @throws JSONException Wenn ein Fehler beim Abfragen der JSON-Daten auftritt
	 */
	public Timegrid jsonToTimegrid(JSONArray result) throws JSONException {
		Timegrid timegrid = new Timegrid();
		for (int i = 0; i < result.length(); i++) {
			JSONObject timegridObject = result.getJSONObject(i);
			
			int day = timegridObject.getInt("day");
			
			JSONArray timegridUnitsArray = timegridObject.getJSONArray("timeUnits");
			List<TimegridUnit> timegridUnitList = new ArrayList<TimegridUnit>();
			for(int j = 0; j < timegridUnitsArray.length(); j++) {
				JSONObject timeUnit = timegridUnitsArray.getJSONObject(j);
				
				String name = timeUnit.getString("name");
						
				String starttime = ""+timeUnit.getInt("startTime");
				String endtime = ""+timeUnit.getInt("endTime");
				
				int startMinute = Integer.parseInt(getMinute(starttime));
				int startHour = Integer.parseInt(getHour(starttime));
				
				int endMinute = Integer.parseInt(getMinute(endtime));
				int endHour = Integer.parseInt(getHour(endtime));
				
				
				DateTime startTime = new DateTime();
				startTime.set(startMinute, startHour, startTime.getDay(), startTime.getMonth(), startTime.getYear());
				DateTime endTime = new DateTime();
				endTime.set(endMinute, endHour, endTime.getDay(), endTime.getMonth(), endTime.getYear());
				
				// Setze aktuelle Unit
				TimegridUnit timegridUnit = new TimegridUnit();
				timegridUnit.setName(name);
				timegridUnit.setStart(startTime);
				timegridUnit.setEnd(endTime);
								
				// Hinzufuegen zu Unit-Liste
				timegridUnitList.add(timegridUnit);
			}
			
			// Setze Unit-Liste fuer Tag
			timegrid.setTimegridForDay(day, timegridUnitList);
		}
		return timegrid;
	}

	public List<StatusData> jsonToStatusData(JSONObject result) throws JSONException {
		List<StatusData> statusDataList = new ArrayList<StatusData>();
		
		JSONArray lessonTypes = result.getJSONArray("lstypes");
		
		List<String> lessonTypeList = new LinkedList<String>();
		lessonTypeList.add(WebUntis.LESSON);
		lessonTypeList.add(WebUntis.BREAKSUPERVISION);
		lessonTypeList.add(WebUntis.EXAMINATION);
		lessonTypeList.add(WebUntis.OFFICEHOUR);
		lessonTypeList.add(WebUntis.STANDBY);
		
		
		for(int i = 0; i < lessonTypes.length(); i++) {
			JSONObject lessonType = lessonTypes.getJSONObject(i);
			
			for(String lsType : lessonTypeList) {
				if(lessonType.has(lsType)) {
					JSONObject concreteLessonType = lessonType.getJSONObject(lsType);
				
					LessonType relatedStatusData = lessonTypeCreator.createLessonType(lsType);
					
					if(relatedStatusData != null) {
						if(concreteLessonType != null) {
							String foreColor = getValueFromJSON(concreteLessonType, "foreColor", "");
							String backColor = getValueFromJSON(concreteLessonType, "backColor", "");
							
							StatusData statusData = new StatusData();
							statusData.setRelatedStatusDataClass(relatedStatusData.getClass());
							statusData.setCode(lsType);
							statusData.setForeColor(foreColor);
							statusData.setBackColor(backColor);
							
							statusDataList.add(statusData);
						}
					}
					else {
						Log.w("json","Unknown lesson type: "+lsType);
					}
				}
			}
		}
		
		JSONArray lessonCodes = result.getJSONArray("codes");
		
		List<String> lessonCodeList = new LinkedList<String>();
		lessonCodeList.add(WebUntis.CANCELLED);
		lessonCodeList.add(WebUntis.IRREGULAR);
		
		for(int i = 0; i < lessonCodes.length(); i++) {
			JSONObject lessonCode = lessonCodes.getJSONObject(i);
		
			for(String lsCode : lessonCodeList) {
				if(lessonCode.has(lsCode)) {
					JSONObject concreteLessonCode = lessonCode.getJSONObject(lsCode);
			
					if(concreteLessonCode != null) {
						String foreColor = getValueFromJSON(concreteLessonCode, "foreColor", "");
						String backColor = getValueFromJSON(concreteLessonCode, "backColor", "");
						
						StatusData statusData = new StatusData();
						statusData.setCode(lsCode);
						statusData.setRelatedStatusDataClass(lessonCodeCreator.createLessonCode(lsCode).getClass());
						statusData.setForeColor(foreColor);
						statusData.setBackColor(backColor);
						
						statusDataList.add(statusData);
					}
				}
			}
		}
		
		return statusDataList;
	}

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
