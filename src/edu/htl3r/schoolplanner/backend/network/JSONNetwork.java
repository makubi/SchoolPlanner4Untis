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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;
import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.backend.Authentication;
import edu.htl3r.schoolplanner.backend.DataProvider;
import edu.htl3r.schoolplanner.backend.Preferences;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolObject;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTest;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTestType;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

/**
 * Netzwerkzugriff fuer die Datenabfrage ueber JSON.
 */
public class JSONNetwork implements DataProvider{

	/**
	 * JSON-RPC Version, die der Untis-Server verwendet.
	 */
	private final String jsonrpcVersion = "2.0";

	private final NetworkAccess network = new Network();
	private final JSONParser jsonParser = new JSONParser();
	
	private Authentication preferences;
	
	/**
	 * Zeitpunkt, wann das letzte mal der Stundenplan importiert wurde.
	 */
	private long latestTimetableImportTime = 0;
	
	/**
	 * Mapping der internen ViewType-Klassen auf die Nummern, die sie jeweils in WebUntis haben.
	 */
	private Map<Class<? extends ViewType>, Integer> viewTypeMapping = new HashMap<Class<? extends ViewType>, Integer>();
	
	public JSONNetwork() {
		viewTypeMapping.put(SchoolClass.class, WebUntis.SCHOOLCLASS);
		viewTypeMapping.put(SchoolTeacher.class, WebUntis.SCHOOLTEACHER);
		viewTypeMapping.put(SchoolSubject.class, WebUntis.SCHOOLSUBJECT);
		viewTypeMapping.put(SchoolRoom.class, WebUntis.SCHOOLROOM);
	}

	/**
	 * Setzt die Server-URL und den Schulnamen im Netzwerk sowie den Benutzernamen und das Passwort fuer die Authentifizierung.
	 * @param preferences {@link Preferences} die verwendet werden sollen
	 */
	public void setPreferences(Authentication preferences) {
		this.preferences = preferences;
		network.setPreferences(preferences);
	}

	/**
	 * Liefert die Daten zu einer JSON-Anfrage.
	 * 
	 * @param request
	 *            JSON-Anfrage
	 * @return Antwort auf die Anfrage
	 * @throws JSONException
	 * @throws IOException 
	 */
	private JSONObject getJSONData(final JSONObject request) throws IOException {
		JSONObject response = null;
		String responseString = network.getResponse(request.toString());
		
		try {
			response = parseData(responseString);
		

		if(response.has("error")) {
			JSONObject errorObject = response.getJSONObject("error");
			int errorCode = errorObject.getInt("code");
			String errorMessage = errorObject.getString("message");
			Log.d("JSON", "Received error code: "+errorCode+ ", message: "+errorMessage);
			
			if(errorCode == 0 && errorMessage.equals("not authenticated")) {
				Log.d("Network", "Reauthenticating");
				if(authenticate()) {
					responseString = network.getResponse(request.toString());
					response = parseData(responseString);
				}
			}
			else {
				throw new IOException("Received error code: +"+errorCode+ ", message: "+errorMessage);
			}
		
		}
		
		} catch (JSONException e) {
			Log.w("JSON", "Unable to parse String to JSONObject",e);
		}
		
		return response;
	}

	private JSONObject requestList(String id, String method)
			throws JSONException, IOException {
		Log.d("METHOD_CALL", "JSONNetwork.requestList(" + id + " : String, "
				+ method + " : String)");

		final JSONObject request = new JSONObject();

		try {
			request.put("jsonrpc", jsonrpcVersion);
			request.put("method", method);
			request.put("id", id);
			// Server benoetigt leere Params
			request.put("params", "");
		} catch (JSONException e) {
			Log.e("JSON", "Error on building request for list",e);
		}
		return getJSONData(request);
	}

	/**
	 * Wandelt den uebergebenen String, falls moeglich, in ein JSONObject um.
	 * Sollte dies Fehlschlagen (z.B. weil der String nicht JSON-Konform ist),
	 * wird eine JSONException geworfen
	 * 
	 * @param data
	 *            String, der geparst werden soll
	 * @return JSONObject, das die Daten des uebergebenen Strings repraesentiert
	 * @throws JSONException
	 *             Wird geworfen, falls es nicht moeglich ist, den String zu
	 *             parsen
	 */
	private JSONObject parseData(String data) throws JSONException {
		JSONTokener t = new JSONTokener(data);
		Object next = t.nextValue();

		Log.v("JSON", "Got class: " + next.getClass());
		Log.v("JSON", "Got value: " + next.toString());
		Log.v("JSON", "Another object available: " + t.more());

		if (next instanceof JSONObject) {
			return (JSONObject) next;
		}
		else {
			// Kann bei falscher URL- oder Schulangabe passieren.
			throw new JSONException("Unable to parse data");
		}
		
	}

	private List<? extends ViewType> getViewTypeList(String id, String method) throws IOException {
		List<? extends ViewType> responseList = null;

		try {
			JSONObject responseObject = requestList(id, method);

			JSONArray result = responseObject.getJSONArray("result");
			Log.v("JSON", "Got object type [response/result]: "
					+ responseObject.get("result").getClass().toString());
			Log.v("JSON",
					"Got object data [response/result]: " + result.toString());

			if (method.equals(JSONGetMethods.getTeachers)) {
				responseList = jsonParser.jsonToTeacherList(result);
			} else if (method.equals(JSONGetMethods.getClasses)) {
				responseList = jsonParser.jsonToClassList(result);
			} else if (method.equals(JSONGetMethods.getSubjects)) {
				responseList = jsonParser.jsonToSubjectList(result);
			} else if (method.equals(JSONGetMethods.getRooms)) {
				responseList = jsonParser.jsonToRoomList(result);
			} else {
				Log.e("JSON", "Unknown request method: " + method);
			}

		} catch (JSONException e) {
			Log.e("JSON", "Unable to parse JSON-String", e);
		}
		if(responseList == null) {
			throw new IOException("Empty list returned, id:"+id+", method:"+method);
		}
		return responseList;
	}

	/**
	 * Liefert eine Liste anhand der uebergebenen Parameter, die passende Objekten beinhaltet, z.B. ein Liste mit freien Tagen.
	 * @param id ID, die fuer die Anfrage verwendet werden soll
	 * @param method Methode, die fuer die Anfrage verwendet werden soll (siehe dazu {@link JSONGetMethods}).
	 * @return Eine Liste mit den Objekten fuer die uebergeben Methode
	 * @throws IOException Wird geworfen, falls waehrnd der Abfrage im Netzwerk ein Fehler auftritt
	 */
	private List<SchoolObject> getList(String id, String method) throws IOException {
		List<SchoolObject> responseList = null;

		try {
			JSONObject responseObject = requestList(id, method);

			JSONArray result = responseObject.getJSONArray("result");
			Log.v("JSON", "Got object type [response/result]: "
					+ responseObject.get("result").getClass().toString());
			Log.v("JSON",
					"Got object data [response/result]: " + result.toString());

			if (method.equals(JSONGetMethods.getHolidays)) {
				responseList = jsonParser.jsonToHolidayList(result);
			} else {
				Log.e("JSON", "Unknown request method: " + method);
			}

		} catch (JSONException e) {
			Log.e("JSON", "Unable to parse JSON-String", e);
		}
		return responseList;
	}
	
	/**
	 * Liefert das passende Objekt zur angefragten Methode, z.B. das Stundenraster.
	 * @param id ID, die fuer die Anfrage verwendet werden soll
	 * @param method Methode, die fuer die Anfrage verwendet werden soll (siehe dazu {@link JSONGetMethods}).
	 * @return Das passende Objekt zur Anfrage
	 * @throws IOException Wird geworfen, falls waehrnd der Abfrage im Netzwerk ein Fehler auftritt
	 */
	private SchoolObject getSchoolObject(String id, String method) throws IOException {
		SchoolObject response = null;

		try {
			JSONObject responseObject = requestList(id, method);

			JSONArray result = responseObject.getJSONArray("result");
			Log.v("JSON", "Got object type [response/result]: "
					+ responseObject.get("result").getClass().toString());
			Log.v("JSON",
					"Got object data [response/result]: " + result.toString());

			if (method.equals(JSONGetMethods.getTimegridUnits)) {
				response = jsonParser.jsonToTimegrid(result);
			} else {
				Log.e("JSON", "Unknown request method: " + method);
			}

		} catch (JSONException e) {
			Log.e("JSON", "Unable to parse JSON-String", e);
		}
		return response;
	}

	@Override
	public List<SchoolTeacher> getSchoolTeacherList() throws IOException {
		final String id = "ID";
		final String method = JSONGetMethods.getTeachers;

		List<SchoolTeacher> list = new ArrayList<SchoolTeacher>();
		for (ViewType obj : getViewTypeList(id, method)) {
			if (obj instanceof SchoolTeacher) {
				list.add((SchoolTeacher) obj);
			}
		}
		return list;
	}

	@Override
	public List<SchoolClass> getSchoolClassList() throws IOException {
		final String id = "ID";
		final String method = JSONGetMethods.getClasses;

		List<SchoolClass> list = new ArrayList<SchoolClass>();
		for (ViewType obj : getViewTypeList(id, method)) {
			if (obj instanceof SchoolClass) {
				list.add((SchoolClass) obj);
			}
		}
		return list;
	}

	@Override
	public List<SchoolSubject> getSchoolSubjectList() throws IOException {
		final String id = "ID";
		final String method = JSONGetMethods.getSubjects;

		List<SchoolSubject> list = new ArrayList<SchoolSubject>();
		for (ViewType obj : getViewTypeList(id, method)) {
			if (obj instanceof SchoolSubject) {
				list.add((SchoolSubject) obj);
			}
		}
		return list;
	}

	@Override
	public List<SchoolRoom> getSchoolRoomList() throws IOException {
		final String id = "ID";
		final String method = JSONGetMethods.getRooms;

		List<SchoolRoom> list = new ArrayList<SchoolRoom>();
		for (ViewType obj : getViewTypeList(id, method)) {
			if (obj instanceof SchoolRoom) {
				list.add((SchoolRoom) obj);
			}
		}
		return list;
	}

	@Override
	public List<SchoolHoliday> getSchoolHolidayList() throws IOException {
		final String id = "ID";
		final String method = JSONGetMethods.getHolidays;

		List<SchoolHoliday> list = new ArrayList<SchoolHoliday>();
		for (SchoolObject obj : getList(id, method)) {
			if (obj instanceof SchoolHoliday) {
				list.add((SchoolHoliday) obj);
			}
		}
		return list;
	}

	@Override
	public Timegrid getTimegrid() throws IOException {
		final String id = "ID";
		final String method = JSONGetMethods.getTimegridUnits;
		
		Timegrid timegrid = new Timegrid();
		SchoolObject obj = getSchoolObject(id, method);
		if (obj instanceof Timegrid) {
			timegrid = (Timegrid) obj;
		}
		return timegrid;
	}

	@Override
	public Map<String, List<Lesson>> getLessons(ViewType view, Calendar startDate,
			Calendar endDate) throws IOException {
		final String id = "ID";
		final String method = JSONGetMethods.getTimetable;
		
		final JSONObject request = new JSONObject();
		final JSONObject params = new JSONObject();
		
		Map<String, List<Lesson>> responseList = new HashMap<String, List<Lesson>>();
		
		try {
			// Setze die ID des ViewTypes (z.B. 5AN)
			params.put("id", view.getId());
			
			// Setze die Art des ViewTypes (z.B. Klasse)
			params.put("type", viewTypeMapping.get(view.getClass()));
			
			// Parsen der Daten
			String startYear = ""+startDate.get(Calendar.YEAR);
			// Intern 0 - 11
			String startMonth = ""+(startDate.get(Calendar.MONTH)+1);
			String startDay = ""+startDate.get(Calendar.DAY_OF_MONTH);
			
			if(startMonth.length() < 2) {
				startMonth = "0"+startMonth;
			}
			
			if(startDay.length() < 2) {
				startDay = "0"+startDay;
			}
			
			String endYear = ""+endDate.get(Calendar.YEAR);
			// Intern 0 - 11
			String endMonth = ""+(endDate.get(Calendar.MONTH)+1);
			String endDay = ""+endDate.get(Calendar.DAY_OF_MONTH);
			
			if(endMonth.length() < 2) {
				endMonth = "0"+endMonth;
			}
			
			if(endDay.length() < 2) {
				endDay = "0"+endDay;
			}
			
			// Setze die Daten der Abfrage
			params.put("startDate",startYear+startMonth+startDay);
			params.put("endDate",endYear+endMonth+endDay);
			
			request.put("jsonrpc", jsonrpcVersion);
			request.put("method", method);
			request.put("id", id);
			request.put("params", params);
			
			// Netzwerkanfrage
			JSONObject response = getJSONData(request);
			
			// Extrahiere Nutzdaten
			JSONArray result = response.getJSONArray("result");
			
			// Parse die JSON-Response zu passender Map
			responseList = jsonParser.jsonToLessonMap(result);			
		} catch (JSONException e) {
			Log.e("JSON", "Error on requesting lessons",e);
		}
		
		return responseList;
	}

	@Override
	public List<Lesson> getLessons(ViewType type,
			Calendar date) throws IOException {
		List<Lesson> result = getLessons(type, date, date).get(CalendarUtils.getCalendarAs8601String(date));
		return result != null ? result : new ArrayList<Lesson>();
	}
	
	@Override
	public List<SchoolTestType> getSchoolTestTypeList() {
		// TODO Not implemented in v1.0
		return null;
	}

	@Override
	public List<SchoolTest> getSchoolTestList() {
		// TODO Not implemented in v1.0
		return null;
	}

	/**
	 * Versucht, eine Verbindung zum Server herzustellen und sich mit diesem zu
	 * authentifizieren. Ist dies erfolgreich, wird die SessionID gesetzt, mit
	 * der jede weitere Abfrage durchgefuehrt wird.
	 * 
	 * @return true, wenn die Authentifizierung erfolgreich war
	 * @throws IOException Wird geworfen, falls waehrnd der Abfrage im Netzwerk ein Fehler auftritt
	 */
	public boolean authenticate() throws IOException {
		Log.d("METHOD_CALL", "JSONNetwork.authenticate()");

		final String id = "ID";
		final String method = "authenticate";
		final JSONObject params = new JSONObject();
		final JSONObject request = new JSONObject();

		JSONObject response = null;

		try {
			params.put("user", preferences.getUsername());
			params.put("password", preferences.getPassword());

			request.put("jsonrpc", jsonrpcVersion);
			request.put("method", method);
			request.put("params", params);
			request.put("id", id);

			response = getJSONData(request);
		
			// TODO: Pruefung auf ID und jsonrpc-version?
			if(response != null) {
				JSONObject result = response.getJSONObject("result");
				if(result != null) {
					String sessionId = result.getString("sessionId");
					if(!result.equals("null")) {
						network.setJsessionid(sessionId);
						return true;
					}
					else {
						network.setJsessionid(null);
					}
				}
			}
		} catch (JSONException e) {
			Log.e("JSON", "Error on authentication", e);
		}
		return false;
	}

	@Override
	public List<Lesson> getMergedLessons(ViewType view, Calendar date)
			throws IOException {
		List<Lesson> lessonList = getLessons(view, date);
		return lessonList;
	}

	@Override
	public Map<String, List<Lesson>> getMergedLessons(ViewType view,
			Calendar startDate, Calendar endDate) throws IOException {
		Map<String, List<Lesson>> lessonMap = getLessons(view, startDate, endDate);
		return lessonMap;
	}
	
	@Override
	public List<SchoolTest> getSchoolTestList(ViewType view, Calendar startDate,
			Calendar endDate) {
		// TODO Not implemented in v1.0
		return null;
	}

	@Override
	public void saveSchoolTest(SchoolTest schoolTest) {
		// TODO Not implemented in v1.0
	}
	
	
	/**
	 * Initialisiert die Farben der LessonTypes und -Codes.
	 * @throws IOException Wenn ein Fehler bei der Uebertragung auftritt
	 */
	public void initStatusData() throws IOException {		
		final String id = "ID";
		final String method = JSONGetMethods.getStatusData;
		
		JSONObject response;
		try {
			response = requestList(id, method);
			JSONObject result = response.getJSONObject("result");
			jsonParser.resyncStatusData(result);
		} catch (JSONException e) {
			Log.e("JSON", "Unable to parse JSON-String", e);
		}
	}
	
	/**
	 * Liefert den Zeitpunkt des letzten Imports des Stundenplans.
	 * @return Den Zeitpunkt des letzten Imports des Stundenplans
	 * @throws IOException Wird geworfen, falls waehrnd der Abfrage im Netzwerk ein Fehler auftritt
	 */
	private long getLatestTimetableImportTime() throws IOException {
		final String id = "ID";
		final String method = JSONGetMethods.getLatestImportTime;
		
		long latestImport = -1;
		
		try {
			JSONObject response = requestList(id, method);
			latestImport = response.getLong("result");
			Log.v("Misc","Last import time: "+response.get("result"));	
		} catch (JSONException e) {
			Log.e("JSON", "Unable to parse JSON-String", e);
		}
		
		return latestImport >= 0 ? latestImport : latestTimetableImportTime;
	}
	
	/**
	 * Eruiert, ob der Stundenplan seit dem letzten Methodenaufruf geaendert wurde.
	 * @return true, wenn der Stundenplan seit dem letzten Methodenaufruf geaendert wurde
	 * @throws IOException Wird geworfen, falls waehrnd der Abfrage im Netzwerk ein Fehler auftritt
	 */
	public boolean timetableUpdated() throws IOException {
		long newLatestTimetableImportTime = getLatestTimetableImportTime();
		
		if(newLatestTimetableImportTime > latestTimetableImportTime) {
			latestTimetableImportTime = newLatestTimetableImportTime;
			return true;
		}
		
		return false;
	}

}
