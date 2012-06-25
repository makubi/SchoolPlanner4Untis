/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mathias@schoolplanner.at>
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

package edu.htl3r.schoolplanner.backend.network.json;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.conn.HttpHostConnectException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.ErrorMessage;
import edu.htl3r.schoolplanner.backend.UnsaveDataSourceMasterdataProvider;
import edu.htl3r.schoolplanner.backend.UnsaveDataSourceTimetableDataProvider;
import edu.htl3r.schoolplanner.backend.network.ErrorCodes;
import edu.htl3r.schoolplanner.backend.network.LessonProcessor;
import edu.htl3r.schoolplanner.backend.network.Network;
import edu.htl3r.schoolplanner.backend.network.WebUntis;
import edu.htl3r.schoolplanner.backend.network.WebUntisErrorCodes;
import edu.htl3r.schoolplanner.backend.network.exceptions.SSLForcedButUnavailableException;
import edu.htl3r.schoolplanner.backend.network.exceptions.WebUntisServiceException;
import edu.htl3r.schoolplanner.backend.network.exceptions.WrongPortNumberException;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridDay;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.TimegridUnit;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

/**
 * Netzwerkzugriff fuer die Datenabfrage ueber JSON.
 */
public class JSONNetwork implements UnsaveDataSourceMasterdataProvider,
		UnsaveDataSourceTimetableDataProvider {

	/**
	 * JSON-RPC Version, die der Untis-Server verwendet.
	 */
	public static final String JSON_RPC_VERSION = "2.0";

	private final Network network = new Network();
	private final JSONParser jsonParser = new JSONParser();

	private LoginSet loginCredentials;

	private int id = 0;

	private String getNextID() {
		return "" + id++;
	}
	
	/**
	 * Mapping der internen ViewType-Klassen auf die Nummern, die sie jeweils in
	 * WebUntis haben.
	 */
	private Map<Class<? extends ViewType>, Integer> viewTypeMapping = new HashMap<Class<? extends ViewType>, Integer>();

	private LessonProcessor lessonProcessor = new LessonProcessor();
	
	public JSONNetwork() {
		viewTypeMapping.put(SchoolClass.class, WebUntis.SCHOOLCLASS);
		viewTypeMapping.put(SchoolTeacher.class, WebUntis.SCHOOLTEACHER);
		viewTypeMapping.put(SchoolSubject.class, WebUntis.SCHOOLSUBJECT);
		viewTypeMapping.put(SchoolRoom.class, WebUntis.SCHOOLROOM);
	}

	/**
	 * Liefert die Daten zu einer JSON-Anfrage oder die ErrorMessage, falls ein Error aufgetreten ist.
	 * 
	 * @param request
	 *            JSON-Anfrage
	 * @return Antwort auf die Anfrage
	 */
	private DataFacade<JSONObject> getJSONData(final JSONObject request) {
		DataFacade<JSONObject> data = new DataFacade<JSONObject>();
		try {
			JSONObject response = parseData(network.getResponse(request.toString()));
			
			if (response.has(JSONResponseObjectKeys.ERROR)) {
				if(unauthenticated(response)) {
					Log.i("Network", "Reauthenticating");
					authenticate();
					response = parseData(network.getResponse(request.toString()));
				}
				else {
					data.setErrorMessage(getWebUntisErrorMessage(response));
				}
			}
			
			data.setData(response);
		} catch (Exception e) {
			data.setErrorMessage(getErrorMessage(e));
		}
		
		return data;
	}

	private boolean unauthenticated(JSONObject response) throws JSONException {
		JSONObject errorObject = response.getJSONObject(JSONResponseObjectKeys.ERROR);
		
		return errorObject.getInt(JSONResponseObjectKeys.ERROR_CODE) == WebUntisErrorCodes.WEBUNTIS_NOT_AUTHENTICATED;
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
		JSONTokener tokener = new JSONTokener(data);
		Object next = tokener.nextValue();

		if (next instanceof JSONObject) {
			return (JSONObject) next;
		} else {
			throw new JSONException("Unable to parse JSON data");
		}

	}
	
	private DataFacade<JSONObject> requestList(JSONRequestMethod method) {
		DataFacade<JSONObject> data = new DataFacade<JSONObject>();
		final JSONObject request = new JSONObject();
		
		final String id = getNextID();

		try {
			request.put(JSONRequestObjectKeys.JSON_RPC_VERSION, JSON_RPC_VERSION);

			request.put(JSONRequestObjectKeys.METHOD, method.getRequestMethod());
			request.put(JSONRequestObjectKeys.ID, id);
			// Server benoetigt leere Params
			request.put(JSONRequestObjectKeys.PARAMS, "");
			
			data = getJSONData(request);
			
		} catch (JSONException e) {
			data.setErrorMessage(getErrorMessage(e));
		}
		
		return data;
	}
	
	private <E> DataFacade<List<E>> getList(E typeDef, JSONRequestMethod method) {
		DataFacade<List<E>> data = new DataFacade<List<E>>();
		
		try {		
			DataFacade<JSONObject> jsonData = requestList(method);
			
			if(jsonData.isSuccessful()) {
				ObjectMapper objectMapper = new ObjectMapper();
				List <E> list = objectMapper.readValue(jsonData.getData().getJSONArray(JSONResponseObjectKeys.RESULT).toString(), objectMapper.getTypeFactory().constructCollectionType(List.class,typeDef.getClass()));
				data.setData(list);
			}
			else {
				data.setErrorMessage(jsonData.getErrorMessage());
			}
		} catch (JSONException e) {
			data.setErrorMessage(getErrorMessage(e));
		} catch (JsonParseException e) {
			data.setErrorMessage(getErrorMessage(e));
		} catch (JsonMappingException e) {
			data.setErrorMessage(getErrorMessage(e));
		} catch (IOException e) {
			data.setErrorMessage(getErrorMessage(e));
		}
			
		return data;
	}
	
	@Override
	public DataFacade<List<SchoolTeacher>> getSchoolTeacherList() {
		// TODO logging mit commons-logging
		// TODO command object
		return getList(new SchoolTeacher(), JSONRequestMethod.GET_TEACHERS);
	}

	@Override
	public DataFacade<List<SchoolClass>> getSchoolClassList() {
		return getList(new SchoolClass(), JSONRequestMethod.GET_CLASSES);
	}

	@Override
	public DataFacade<List<SchoolSubject>> getSchoolSubjectList() {
		return getList(new SchoolSubject(), JSONRequestMethod.GET_SUBJECTS);
	}

	@Override
	public DataFacade<List<SchoolRoom>> getSchoolRoomList() {		
		return getList(new SchoolRoom(), JSONRequestMethod.GET_ROOMS);
	}

	@Override
	public DataFacade<List<SchoolHoliday>> getSchoolHolidayList() {
		return getList(new SchoolHoliday(), JSONRequestMethod.GET_HOLIDAYS);
	}

	@Override
	public DataFacade<Timegrid> getTimegrid() {		
		DataFacade<Timegrid> data = new DataFacade<Timegrid>();
		DataFacade<List<TimegridDay>> list = getList(new TimegridDay(), JSONRequestMethod.GET_TIMEGRID_UNITS);
		
		if(list.isSuccessful()) {
			Timegrid timegrid = new Timegrid();
			
			for(TimegridDay timegridDay : list.getData()) {
				timegrid.setTimegridForDay(timegridDay.getDay(), timegridDay);
			}
			
			data.setData(timegrid);
		}
		else data.setErrorMessage(list.getErrorMessage());
		
		return data;
	}

	@Override
	public DataFacade<List<Lesson>> getLessons(ViewType viewType, DateTime date) {
		DataFacade<List<Lesson>> data = new DataFacade<List<Lesson>>();
	
		DataFacade<Map<String, List<Lesson>>> tmpData = getLessons(viewType,
				date, date);
		if (tmpData.isSuccessful()) {
			data.setData(tmpData.getData().get(
					DateTimeUtils.toISO8601Date(date)));
		} else {
			data.setErrorMessage(tmpData.getErrorMessage());
		}
	
		return data;
	}

	@Override
	public DataFacade<Map<String, List<Lesson>>> getLessons(ViewType view,
			DateTime startDate, DateTime endDate) {
		final String id = getNextID();
		final JSONRequestMethod method = JSONRequestMethod.GET_TIMETABLE;
	
		final JSONObject request = new JSONObject();
		final JSONObject params = new JSONObject();
	
		DataFacade<Map<String, List<Lesson>>> data = new DataFacade<Map<String, List<Lesson>>>();
	
		Map<String, List<Lesson>> lessonMap = new HashMap<String, List<Lesson>>();
	
		try {
			// Setze die ID des ViewTypes (z.B. 5AN)
			params.put("id", view.getId());
	
			// Setze die Art des ViewTypes (z.B. Klasse)
			params.put("type", viewTypeMapping.get(view.getClass()));
	
			// Parsen der Daten
			String startYear = "" + startDate.getYear();
			String startMonth = "" + startDate.getMonth();
			String startDay = "" + startDate.getDay();
	
			if (startMonth.length() < 2) {
				startMonth = "0" + startMonth;
			}
	
			if (startDay.length() < 2) {
				startDay = "0" + startDay;
			}
	
			String endYear = "" + endDate.getYear();
			String endMonth = "" + endDate.getMonth();
			String endDay = "" + endDate.getDay();
	
			if (endMonth.length() < 2) {
				endMonth = "0" + endMonth;
			}
	
			if (endDay.length() < 2) {
				endDay = "0" + endDay;
			}
	
			// Setze die Daten der Abfrage
			params.put("startDate", startYear + startMonth + startDay);
			params.put("endDate", endYear + endMonth + endDay);
	
			request.put(JSONRequestObjectKeys.JSON_RPC_VERSION, JSON_RPC_VERSION);
			request.put(JSONRequestObjectKeys.METHOD, method.getRequestMethod());
			request.put(JSONRequestObjectKeys.ID, id);
			request.put(JSONRequestObjectKeys.PARAMS, params);
	
			// Netzwerkanfrage
			DataFacade<JSONObject> response = getJSONData(request);
			
			
			if(response.isSuccessful()) {
				// Extrahiere Nutzdaten
				JSONArray result = response.getData().getJSONArray(JSONResponseObjectKeys.RESULT);
			
				// Parse die JSON-Response zu passender Map
				lessonMap = jsonParser.jsonToLessonMap(result);
				
				// Fuege leere Listen fuer Daten ohne Stunden hinzu
				lessonProcessor.addEmptyDaysToLessonMap(lessonMap, startDate, endDate);
	
				data.setData(lessonMap);
			}
	
		} catch (JSONException e) {
			data.setErrorMessage(getErrorMessage(e));
		}
	
		return data;
	}

	@Override
	public DataFacade<Map<String, List<Lesson>>> getLessons(ViewType viewType,
			DateTime startDate, DateTime endDate, boolean forceNetwork) {
		return getLessons(viewType, startDate, endDate);
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
	public DataFacade<Boolean> authenticate() {
		final String id = getNextID();
		final JSONRequestMethod method = JSONRequestMethod.AUTHENTICATE;
		final JSONObject params = new JSONObject();
		final JSONObject request = new JSONObject();

		JSONObject response = null;

		DataFacade<Boolean> data = new DataFacade<Boolean>();
		data.setData(false);

		try {
			params.put("user", loginCredentials.getUsername());
			params.put("password", loginCredentials.getPassword());

			request.put(JSONRequestObjectKeys.JSON_RPC_VERSION, JSON_RPC_VERSION);
			request.put(JSONRequestObjectKeys.METHOD, method.getRequestMethod());
			request.put(JSONRequestObjectKeys.PARAMS, params);
			request.put(JSONRequestObjectKeys.ID, id);
		}
		catch(JSONException e) {
			
		}
			DataFacade<JSONObject> jsonData = getJSONData(request);
			
			if(jsonData.isSuccessful()) {
				response = jsonData.getData();
			
				try {
				
				if (response.has(JSONResponseObjectKeys.ERROR)) {
					JSONObject errorObject = response.getJSONObject(JSONResponseObjectKeys.ERROR);
					int errorCode = errorObject.getInt(JSONResponseObjectKeys.ERROR_CODE);
					
					if(!(errorCode == WebUntisErrorCodes.WEBUNTIS_BAD_CREDENTIALS)) {
						data.setErrorMessage(getWebUntisErrorMessage(response));
					}
				}
				
				else {
					JSONObject result = response.getJSONObject(JSONResponseObjectKeys.RESULT);
					String sessionId = result.getString("sessionId");
					if (!result.equals("null")) {
						network.setJsessionid(sessionId);
						data.setData(true);
					} else {
						network.setJsessionid(null);
					}
				}
			}
				catch (JSONException e) {
					data.setErrorMessage(getErrorMessage(e));
				}
			}
			else if(jsonData.getErrorMessage().getErrorCode() == WebUntisErrorCodes.WEBUNTIS_BAD_CREDENTIALS) {
					data.setData(false);
			}
			else {
				data.setErrorMessage(jsonData.getErrorMessage());
			}
		
		return data;
	}

	public void setCache(Cache cache) {
		jsonParser.setCache(cache);
	}

	public void setLoginCredentials(LoginSet loginSet) {
		this.loginCredentials = loginSet;
		network.setLoginCredentials(loginSet);
	}
	
	private ErrorMessage getErrorMessage(Exception e) {
		ErrorMessage errorMessage = new ErrorMessage();
		int errorCode = ErrorCodes.IO_EXCEPTION;
		
		if(e instanceof JSONException) {
			errorCode = ErrorCodes.JSON_EXCEPTION;
		}
		else if (e instanceof HttpHostConnectException) errorCode = ErrorCodes.HTTP_HOST_CONNECTION_EXCEPTION;
		else if (e instanceof UnknownHostException) errorCode = ErrorCodes.UNKNOWN_HOST_EXCEPTION;
		else if (e instanceof SSLForcedButUnavailableException) errorCode = ErrorCodes.SSL_FORCED_BUT_UNAVAILABLE;
		else if (e instanceof SocketTimeoutException) errorCode = ErrorCodes.SOCKET_TIMEOUT_EXCEPTION;
		else if (e instanceof WrongPortNumberException) errorCode = ErrorCodes.WRONG_PORT_NUMBER;
			
		errorMessage.setErrorCode(errorCode);
		errorMessage.setException(e);
		
		return errorMessage;
	}
	
	private ErrorMessage getWebUntisErrorMessage(JSONObject jsonObject) throws JSONException {
		if(jsonObject.has(JSONResponseObjectKeys.ERROR)) {
			try {
				ErrorMessage errorMessage = new ErrorMessage();
				JSONObject errorObject = jsonObject.getJSONObject(JSONResponseObjectKeys.ERROR);
				int errorCode = errorObject.getInt(JSONResponseObjectKeys.ERROR_CODE);
				String errorString = errorObject.optString(JSONResponseObjectKeys.ERROR_MESSAGE);
				
				errorMessage.setErrorCode(ErrorCodes.WEBUNTIS_SERVICE_EXCEPTION);
				errorMessage.setException(new WebUntisServiceException(errorCode));
				errorMessage.setAdditionalInfo(errorString);
				
				return errorMessage;
			} catch (JSONException e) {
				Log.e("Network","Unable to parse JSON error",e);
				throw new JSONException("Unable to parse JSON error: "+jsonObject);
			}
		}
		else return null;
	}

}
