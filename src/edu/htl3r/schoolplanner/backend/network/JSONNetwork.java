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
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.ExtendedStatusDataProvider;
import edu.htl3r.schoolplanner.backend.preferences.Authentication;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolObject;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

/**
 * Netzwerkzugriff fuer die Datenabfrage ueber JSON.
 */
public class JSONNetwork implements ExtendedStatusDataProvider {

	/**
	 * JSON-RPC Version, die der Untis-Server verwendet.
	 */
	private final String jsonrpcVersion = "2.0";

	private final NetworkAccess network = new Network();
	private final JSONParser jsonParser = new JSONParser();

	private Authentication authentication;

	private int id = 0;

	public String getNextID() {
		return "" + id++;
	}

	/**
	 * Zeitpunkt, wann das letzte mal der Stundenplan importiert wurde.
	 */
	private long latestTimetableImportTime = 0;

	/**
	 * Mapping der internen ViewType-Klassen auf die Nummern, die sie jeweils in
	 * WebUntis haben.
	 */
	private Map<Class<? extends ViewType>, Integer> viewTypeMapping = new HashMap<Class<? extends ViewType>, Integer>();

	public JSONNetwork() {
		viewTypeMapping.put(SchoolClass.class, WebUntis.SCHOOLCLASS);
		viewTypeMapping.put(SchoolTeacher.class, WebUntis.SCHOOLTEACHER);
		viewTypeMapping.put(SchoolSubject.class, WebUntis.SCHOOLSUBJECT);
		viewTypeMapping.put(SchoolRoom.class, WebUntis.SCHOOLROOM);
	}

	/**
	 * Setzt die Server-URL und den Schulnamen im Netzwerk sowie den
	 * Benutzernamen und das Passwort fuer die Authentifizierung.
	 * 
	 * @param authentication
	 *            {@link Preferences} die verwendet werden sollen
	 */
	public void setLoginCredentials(Authentication authentication) {
		this.authentication = authentication;
		network.setLoginCredentials(authentication);
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
	private JSONObject getJSONData(final JSONObject request)
			throws IOException, JSONException {
		return parseData(network.getResponse(request.toString()));
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

		if (next instanceof JSONObject) {
			return (JSONObject) next;
		} else {
			throw new JSONException("Unable to parse data");
		}

	}

	private JSONObject requestList(String id, String method)
			throws JSONException, IOException {
		final JSONObject request = new JSONObject();

		request.put("jsonrpc", jsonrpcVersion);
		request.put("method", method);
		request.put("id", id);
		// Server benoetigt leere Params
		request.put("params", "");

		return getJSONData(request);
	}

	private DataFacade<List<? extends ViewType>> getViewTypeList(String id,
			String method) throws IOException {
		DataFacade<List<? extends ViewType>> data = new DataFacade<List<? extends ViewType>>();

		try {
			JSONObject responseObject = requestList(id, method);

			if (responseObject.has("error")) {
				JSONObject errorObject = responseObject.getJSONObject("error");
				int errorCode = errorObject.getInt("code");
				String errorMessage = errorObject.getString("message");
				Log.d("JSON", "Received error code: " + errorCode
						+ ", message: " + errorMessage);

				data.setErrorCode(errorCode);
			}

			else {
				JSONArray result = responseObject.getJSONArray("result");

				if (method.equals(JSONGetMethods.getTeachers)) {
					data.setData(jsonParser.jsonToTeacherList(result));
				} else if (method.equals(JSONGetMethods.getClasses)) {
					data.setData(jsonParser.jsonToClassList(result));
				} else if (method.equals(JSONGetMethods.getSubjects)) {
					data.setData(jsonParser.jsonToSubjectList(result));
				} else if (method.equals(JSONGetMethods.getRooms)) {
					data.setData(jsonParser.jsonToRoomList(result));
				} else {
					Log.e("JSON", "Unknown request method: " + method);
				}

			}
		} catch (JSONException e) {
			data.setErrorCode(255);
		}

		return data;
	}

	/**
	 * Liefert eine Liste anhand der uebergebenen Parameter, die passende
	 * Objekten beinhaltet, z.B. ein Liste mit freien Tagen.
	 * 
	 * @param id
	 *            ID, die fuer die Anfrage verwendet werden soll
	 * @param method
	 *            Methode, die fuer die Anfrage verwendet werden soll (siehe
	 *            dazu {@link JSONGetMethods}).
	 * @return Eine Liste mit den Objekten fuer die uebergeben Methode
	 * @throws IOException
	 *             Wird geworfen, falls waehrnd der Abfrage im Netzwerk ein
	 *             Fehler auftritt
	 */
	private DataFacade<List<SchoolObject>> getList(String id, String method)
			throws IOException {
		DataFacade<List<SchoolObject>> data = new DataFacade<List<SchoolObject>>();

		try {
			JSONObject responseObject = requestList(id, method);

			if (responseObject.has("error")) {
				JSONObject errorObject = responseObject.getJSONObject("error");
				int errorCode = errorObject.getInt("code");
				String errorMessage = errorObject.getString("message");
				Log.d("JSON", "Received error code: " + errorCode
						+ ", message: " + errorMessage);

				data.setErrorCode(errorCode);
			}

			JSONArray result = responseObject.getJSONArray("result");

			if (method.equals(JSONGetMethods.getHolidays)) {
				data.setData(jsonParser.jsonToHolidayList(result));
			} else {
				Log.e("JSON", "Unknown request method: " + method);
			}

		} catch (JSONException e) {
			data.setErrorCode(255);
		}
		return data;
	}

	/**
	 * Liefert das passende Objekt zur angefragten Methode, z.B. das
	 * Stundenraster.
	 * 
	 * @param id
	 *            ID, die fuer die Anfrage verwendet werden soll
	 * @param method
	 *            Methode, die fuer die Anfrage verwendet werden soll (siehe
	 *            dazu {@link JSONGetMethods}).
	 * @return Das passende Objekt zur Anfrage
	 * @throws IOException
	 *             Wird geworfen, falls waehrnd der Abfrage im Netzwerk ein
	 *             Fehler auftritt
	 */
	private DataFacade<SchoolObject> getSchoolObject(String id, String method)
			throws IOException {
		DataFacade<SchoolObject> data = new DataFacade<SchoolObject>();

		try {
			JSONObject responseObject = requestList(id, method);
			JSONArray result = responseObject.getJSONArray("result");

			if (method.equals(JSONGetMethods.getTimegridUnits)) {
				data.setData(jsonParser.jsonToTimegrid(result));
			} else {
				Log.e("JSON", "Unknown request method: " + method);
			}

		} catch (JSONException e) {
			data.setErrorCode(255);
		}
		return data;
	}

	@Override
	public DataFacade<List<SchoolTeacher>> getSchoolTeacherList()
			throws IOException {
		final String id = getNextID();
		final String method = JSONGetMethods.getTeachers;

		DataFacade<List<? extends ViewType>> viewTypeList = getViewTypeList(id,
				method);
		DataFacade<List<SchoolTeacher>> data = new DataFacade<List<SchoolTeacher>>();

		if (viewTypeList.isSuccessful()) {
			
			List<SchoolTeacher> list = new ArrayList<SchoolTeacher>();
			for (ViewType obj : viewTypeList.getData()) {
				if (obj instanceof SchoolTeacher) {
					list.add((SchoolTeacher) obj);
				}
			}
			data.setData(list);
		} else {
			data.setErrorCode(viewTypeList.getErrorCode());
		}

		return data;
	}

	@Override
	public DataFacade<List<SchoolClass>> getSchoolClassList()
			throws IOException {
		final String id = getNextID();
		final String method = JSONGetMethods.getClasses;

		DataFacade<List<? extends ViewType>> viewTypeList = getViewTypeList(id,
				method);
		DataFacade<List<SchoolClass>> data = new DataFacade<List<SchoolClass>>();

		if (viewTypeList.isSuccessful()) {

			List<SchoolClass> list = new ArrayList<SchoolClass>();
			for (ViewType obj : viewTypeList.getData()) {
				if (obj instanceof SchoolClass) {
					list.add((SchoolClass) obj);
				}
			}
			data.setData(list);
		} else {
			data.setErrorCode(viewTypeList.getErrorCode());
		}

		return data;
	}

	@Override
	public DataFacade<List<SchoolSubject>> getSchoolSubjectList()
			throws IOException {
		final String id = getNextID();
		final String method = JSONGetMethods.getSubjects;

		DataFacade<List<? extends ViewType>> viewTypeList = getViewTypeList(id,
				method);
		DataFacade<List<SchoolSubject>> data = new DataFacade<List<SchoolSubject>>();

		if (viewTypeList.isSuccessful()) {

			List<SchoolSubject> list = new ArrayList<SchoolSubject>();
			for (ViewType obj : viewTypeList.getData()) {
				if (obj instanceof SchoolSubject) {
					list.add((SchoolSubject) obj);
				}
			}
			data.setData(list);
		} else {
			data.setErrorCode(viewTypeList.getErrorCode());
		}
		return data;
	}

	@Override
	public DataFacade<List<SchoolRoom>> getSchoolRoomList() throws IOException {
		final String id = getNextID();
		final String method = JSONGetMethods.getRooms;

		DataFacade<List<? extends ViewType>> viewTypeList = getViewTypeList(id,
				method);
		DataFacade<List<SchoolRoom>> data = new DataFacade<List<SchoolRoom>>();

		if (viewTypeList.isSuccessful()) {

			List<SchoolRoom> list = new ArrayList<SchoolRoom>();
			for (ViewType obj : viewTypeList.getData()) {
				if (obj instanceof SchoolRoom) {
					list.add((SchoolRoom) obj);
				}
			}
			data.setData(list);
		} else {
			data.setErrorCode(viewTypeList.getErrorCode());
		}

		return data;
	}

	@Override
	public DataFacade<List<SchoolHoliday>> getSchoolHolidayList()
			throws IOException {
		final String id = getNextID();
		final String method = JSONGetMethods.getHolidays;

		DataFacade<List<SchoolObject>> schoolObjectList = getList(id, method);
		DataFacade<List<SchoolHoliday>> data = new DataFacade<List<SchoolHoliday>>();

		if (schoolObjectList.isSuccessful()) {

			List<SchoolHoliday> list = new ArrayList<SchoolHoliday>();
			for (SchoolObject obj : schoolObjectList.getData()) {
				if (obj instanceof SchoolHoliday) {
					list.add((SchoolHoliday) obj);
				}
			}
			data.setData(list);
		} else {
			data.setErrorCode(schoolObjectList.getErrorCode());
		}

		return data;
	}

	@Override
	public DataFacade<Timegrid> getTimegrid() throws IOException {
		final String id = getNextID();
		final String method = JSONGetMethods.getTimegridUnits;

		DataFacade<SchoolObject> schoolObjectList = getSchoolObject(id, method);
		DataFacade<Timegrid> data = new DataFacade<Timegrid>();

		if (schoolObjectList.isSuccessful()) {

			Timegrid timegrid = new Timegrid();
			SchoolObject obj = schoolObjectList.getData();
			if (obj instanceof Timegrid) {
				timegrid = (Timegrid) obj;
			}
			data.setData(timegrid);
		} else {
			data.setErrorCode(schoolObjectList.getErrorCode());
		}

		return data;
	}

	/**
	 * Versucht, eine Verbindung zum Server herzustellen und sich mit diesem zu
	 * authentifizieren. Ist dies erfolgreich, wird die SessionID gesetzt, mit
	 * der jede weitere Abfrage durchgefuehrt wird.
	 * 
	 * @return true, wenn die Authentifizierung erfolgreich war
	 * @throws IOException
	 *             Wird geworfen, falls waehrnd der Abfrage im Netzwerk ein
	 *             Fehler auftritt
	 */
	public DataFacade<Boolean> authenticate() throws IOException {
		final String id = getNextID();
		final String method = "authenticate";
		final JSONObject params = new JSONObject();
		final JSONObject request = new JSONObject();

		JSONObject response = null;

		DataFacade<Boolean> data = new DataFacade<Boolean>();
		data.setData(false);

		try {
			params.put("user", authentication.getUsername());
			params.put("password", authentication.getPassword());

			request.put("jsonrpc", jsonrpcVersion);
			request.put("method", method);
			request.put("params", params);
			request.put("id", id);

			response = getJSONData(request);

			if (response.has("error")) {
				JSONObject errorObject = response.getJSONObject("error");
				int errorCode = errorObject.getInt("code");
				String errorMessage = errorObject.getString("message");
				Log.d("JSON", "Received error code: " + errorCode
						+ ", message: " + errorMessage);

				data.setErrorCode(errorCode);
			}

			else {

				// TODO: Pruefung auf ID und jsonrpc-version?
				if (response != null) {
					JSONObject result = response.getJSONObject("result");
					if (result != null) {
						String sessionId = result.getString("sessionId");
						if (!result.equals("null")) {
							network.setJsessionid(sessionId);
							data.setData(true);
						} else {
							network.setJsessionid(null);
						}
					}
				}
			}
		} catch (JSONException e) {
			data.setErrorCode(255);
		}
		return data;
	}

	/**
	 * Initialisiert die Farben der LessonTypes und -Codes.
	 * 
	 * @throws IOException
	 *             Wenn ein Fehler bei der Uebertragung auftritt
	 */
	public void initStatusData() throws IOException {
		final String id = getNextID();
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
	 * 
	 * @return Den Zeitpunkt des letzten Imports des Stundenplans
	 * @throws IOException
	 *             Wird geworfen, falls waehrnd der Abfrage im Netzwerk ein
	 *             Fehler auftritt
	 */
	private long getLatestTimetableImportTime() throws IOException {
		final String id = getNextID();
		final String method = JSONGetMethods.getLatestImportTime;

		long latestImport = -1;

		try {
			JSONObject response = requestList(id, method);
			latestImport = response.getLong("result");
			Log.v("Misc", "Last import time: " + response.get("result"));
		} catch (JSONException e) {
			Log.e("JSON", "Unable to parse JSON-String", e);
		}

		return latestImport >= 0 ? latestImport : latestTimetableImportTime;
	}

	/**
	 * Eruiert, ob der Stundenplan seit dem letzten Methodenaufruf geaendert
	 * wurde.
	 * 
	 * @return true, wenn der Stundenplan seit dem letzten Methodenaufruf
	 *         geaendert wurde
	 * @throws IOException
	 *             Wird geworfen, falls waehrnd der Abfrage im Netzwerk ein
	 *             Fehler auftritt
	 */
	public boolean timetableUpdated() throws IOException {
		long newLatestTimetableImportTime = getLatestTimetableImportTime();

		if (newLatestTimetableImportTime > latestTimetableImportTime) {
			latestTimetableImportTime = newLatestTimetableImportTime;
			return true;
		}

		return false;
	}

	public void setCache(Cache cache) {
		jsonParser.setCache(cache);
	}

}
