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

package edu.htl3r.schoolplanner.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.cache.Cache;
import edu.htl3r.schoolplanner.backend.database.Database;
import edu.htl3r.schoolplanner.backend.network.json.JSONNetwork;
import edu.htl3r.schoolplanner.backend.preferences.AutoSelectSet;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

/**
 * Laedt Daten aus externen Datenquellen, wie der lokalen Datenbank oder dem Netzwerk.
 * @see Database
 * @see JSONNetwork
 */
public class ExternalDataLoader implements UnsaveDataSourceMasterdataProvider, NetworkTimetableDataProvider, LoginSetHandler, AutoSelectHandler {

	private Database database = new Database();
	private JSONNetwork network = new JSONNetwork();

	private boolean networkAvailable = true;

	@Override
	public DataFacade<List<SchoolClass>> getSchoolClassList() {
		DataFacade<List<SchoolClass>> schoolClassList = new DataFacade<List<SchoolClass>>();

		// Check database
		List<SchoolClass> data = database.getSchoolClassList();
		if (data != null && data.size() > 0) {
			Log.v("data_source","class list: database");
			schoolClassList.setDataSource(DataSource.DATABASE);
			schoolClassList.setData(data);
		}
		// Check network
		else if (networkAvailable) {
			if ((schoolClassList = network.getSchoolClassList()).isSuccessful()) {
				Log.v("data_source","class list: network");
				schoolClassList.setDataSource(DataSource.NETWORK);
				database.setSchoolClassList(schoolClassList.getData());
			}
		}
		else {
			schoolClassList.setErrorMessage(getUnableToLoadDataErrorMessage());
		}

		return schoolClassList;
	}

	@Override
	public DataFacade<List<SchoolTeacher>> getSchoolTeacherList() {
		DataFacade<List<SchoolTeacher>> schoolTeacherList = new DataFacade<List<SchoolTeacher>>();

		// Check database
		List<SchoolTeacher> data = database.getSchoolTeacherList();
		if (data != null && data.size() > 0) {
			Log.v("data_source","teacher list: database");
			schoolTeacherList.setDataSource(DataSource.DATABASE);
			schoolTeacherList.setData(data);
		}
		// Check network
		else if (networkAvailable) {
			if ((schoolTeacherList = network.getSchoolTeacherList()).isSuccessful()) {
				Log.v("data_source","teacher list: network");
				schoolTeacherList.setDataSource(DataSource.NETWORK);
				database.setSchoolTeacherList(schoolTeacherList.getData());
			}
		}
		else {
			schoolTeacherList.setErrorMessage(getUnableToLoadDataErrorMessage());
		}

		return schoolTeacherList;
	}

	@Override
	public DataFacade<List<SchoolRoom>> getSchoolRoomList() {
		DataFacade<List<SchoolRoom>> schoolRoomList = new DataFacade<List<SchoolRoom>>();

		// Check database
		List<SchoolRoom> data = database.getSchoolRoomList();
		if (data != null && data.size() > 0) {
			Log.v("data_source","room list: database");
			schoolRoomList.setDataSource(DataSource.DATABASE);
			schoolRoomList.setData(data);
		}
		// Check network
		else if (networkAvailable) {
			if ((schoolRoomList = network.getSchoolRoomList()).isSuccessful()) {
				Log.v("data_source","room list: network");
				schoolRoomList.setDataSource(DataSource.NETWORK);
				database.setSchoolRoomList(schoolRoomList.getData());
			}
		}
		else {
			schoolRoomList.setErrorMessage(getUnableToLoadDataErrorMessage());
		}

		return schoolRoomList;
	}

	@Override
	public DataFacade<List<SchoolSubject>> getSchoolSubjectList() {
		DataFacade<List<SchoolSubject>> schoolSubjectList = new DataFacade<List<SchoolSubject>>();

		// Check database
		List<SchoolSubject> data = database.getSchoolSubjectList();
		if (data != null && data.size() > 0) {
			Log.v("data_source","subject list: database");
			schoolSubjectList.setDataSource(DataSource.DATABASE);
			schoolSubjectList.setData(data);
		}
		// Check network
		else if (networkAvailable) {
			if ((schoolSubjectList = network.getSchoolSubjectList()).isSuccessful()) {
				Log.v("data_source","subject list: network");
				schoolSubjectList.setDataSource(DataSource.NETWORK);
				database.setSchoolSubjectList(schoolSubjectList.getData());
			}
		}
		else {
			schoolSubjectList.setErrorMessage(getUnableToLoadDataErrorMessage());
		}

		return schoolSubjectList;
	}

	@Override
	public DataFacade<List<SchoolHoliday>> getSchoolHolidayList() {
		DataFacade<List<SchoolHoliday>> schoolHolidayList = new DataFacade<List<SchoolHoliday>>();

		// Check database
		List<SchoolHoliday> data = database.getSchoolHolidayList();
		if (data != null && data.size() > 0) {
			Log.v("data_source","holiday list: database");
			schoolHolidayList.setDataSource(DataSource.DATABASE);
			schoolHolidayList.setData(data);
		}
		// Check network
		else if (networkAvailable) {
			if ((schoolHolidayList = network.getSchoolHolidayList()).isSuccessful()) {
				Log.v("data_source","holiday list: network");
				schoolHolidayList.setDataSource(DataSource.NETWORK);
				database.setSchoolHolidayList(schoolHolidayList.getData());
			}
		}
		else {
			schoolHolidayList.setErrorMessage(getUnableToLoadDataErrorMessage());
		}

		return schoolHolidayList;
	}
	
	@Override
	public DataFacade<Timegrid> getTimegrid() {
		DataFacade<Timegrid> timegrid = new DataFacade<Timegrid>();

		// Check database
		Timegrid data = database.getTimegrid();
		if (data != null) {
			Log.v("data_source","timegrid: database");
			timegrid.setDataSource(DataSource.DATABASE);
			timegrid.setData(data);
		}
		// Check network
		else if (networkAvailable) {
			if ((timegrid = network.getTimegrid()).isSuccessful()) {
				Log.v("data_source","timegrid: network");
				timegrid.setDataSource(DataSource.NETWORK);
				database.setTimegrid(timegrid.getData());
			}
		}
		else {
			timegrid.setErrorMessage(getUnableToLoadDataErrorMessage());
		}

		return timegrid;
	}

	@Override
	public DataFacade<List<Lesson>> getLessons(ViewType viewType, DateTime date) {
		DataFacade<List<Lesson>> lessonList = new DataFacade<List<Lesson>>();
		
		if (networkAvailable) {
			if ((lessonList = network.getLessons(viewType, date)).isSuccessful()) {
				lessonList.setDataSource(DataSource.NETWORK);
			}
		}
		else {
			lessonList.setErrorMessage(getUnableToLoadDataErrorMessage());
		}

		return lessonList;
	}

	@Override
	public DataFacade<Map<String, List<Lesson>>> getLessons(ViewType viewType,
			DateTime startDate, DateTime endDate) {
		DataFacade<Map<String, List<Lesson>>> lessonMap = new DataFacade<Map<String, List<Lesson>>>();
		
		if (networkAvailable) {
			if ((lessonMap = network.getLessons(viewType, startDate, endDate)).isSuccessful()) {
				lessonMap.setDataSource(DataSource.NETWORK);
			}
		}
		else {
			lessonMap.setErrorMessage(getUnableToLoadDataErrorMessage());
		}

		return lessonMap;
	}

	@Override
	public DataFacade<Map<String, List<Lesson>>> getLessonsFromNetwork(ViewType viewType,
			DateTime startDate, DateTime endDate) {		
		DataFacade<Map<String, List<Lesson>>> lessons = network.getLessons(viewType, startDate, endDate);
		
		if(lessons.isSuccessful()) lessons.setDataSource(DataSource.NETWORK);
		
		return lessons;
	}

	/**
	 * Authentifiziert sich mit dem Untis-Server ueber das Netzwerk.
	 * @return true, wenn die Authentifizierung erfolgreich war, sonst false
	 */
	public DataFacade<Boolean> authenticate() {
		DataFacade<Boolean> authenticate = network.authenticate();
		
		if(authenticate.isSuccessful()) authenticate.setDataSource(DataSource.NETWORK);
		
		return authenticate;
	}

	/**
	 * Setzt den Status des Netzwerks bei Aenderung dessen.
	 * @param networkAvailable true, wenn das Netzwerk verfuegbar ist, sonst false
	 */
	public void networkAvailabilityChanged(boolean networkAvailable) {
		this.networkAvailable = networkAvailable;
	}

	/**
	 * Laedt die neusten Stammdaten herunter und aktualisiert danach die Datenbank.<br>
	 * Stammdaten, die zur Zeit aktualisiert werden: Liste der Schuklasse, Liste der Lehrer, Liste der Raeume, Liste der Faecher, Stundenraster. 
	 * @return Ein Objekt, das die Stammdaten, die aktualisiert wurden, enthaelt.
	 */
	public DataFacade<MasterData> resyncMasterData() {
		List<DataFacade<?>> requests = new ArrayList<DataFacade<?>>();
		
		// Load data from network
		DataFacade<List<SchoolClass>> schoolClassList = network.getSchoolClassList();
		DataFacade<List<SchoolTeacher>> schoolTeacherList = network.getSchoolTeacherList();
		DataFacade<List<SchoolRoom>> schoolRoomList = network.getSchoolRoomList();
		DataFacade<List<SchoolSubject>> schoolSubjectList = network.getSchoolSubjectList();
		
		DataFacade<List<SchoolHoliday>> schoolHolidayList = network.getSchoolHolidayList();
		DataFacade<Timegrid> timegrid = network.getTimegrid();
		
		// Collect data
		requests.add(schoolClassList);
		requests.add(schoolTeacherList);
		requests.add(schoolRoomList);
		requests.add(schoolSubjectList);
		
		requests.add(schoolHolidayList);
		requests.add(timegrid);
		
		// check for errors
		DataFacade<MasterData> data = new DataFacade<MasterData>();
		for(DataFacade<?> dataFacade : requests) {
			if(!dataFacade.isSuccessful()) {
				data.setErrorMessage(dataFacade.getErrorMessage());
				return data;
			}
		}
		
		// build object
		MasterData masterData = new MasterData();
		masterData.setSchoolClassList(schoolClassList.getData());
		masterData.setSchoolRoomList(schoolRoomList.getData());
		masterData.setSchoolSubjectList(schoolSubjectList.getData());
		masterData.setSchoolTeacherList(schoolTeacherList.getData());
		masterData.setSchoolHolidayList(schoolHolidayList.getData());
		masterData.setTimegrid(timegrid.getData());
		
		data.setData(masterData);
		
		
		// update database
		database.setSchoolClassList(schoolClassList.getData());
		database.setSchoolTeacherList(schoolTeacherList.getData());
		database.setSchoolRoomList(schoolRoomList.getData());
		database.setSchoolSubjectList(schoolSubjectList.getData());
		database.setSchoolHolidayList(schoolHolidayList.getData());
		database.setTimegrid(timegrid.getData());
		
		if(data.isSuccessful()) data.setDataSource(DataSource.NETWORK);
		
		return data;
	}

	public void setCache(Cache cache) {
		network.setCache(cache);
	}

	/**
	 * Setzt das LoginSet mit den Login-Daten, die im Netzwerk verwendet werden sollen.
	 * @param loginSet
	 * @see JSONNetwork#authenticate()
	 */
	public void setLoginCredentials(LoginSet loginSet) {
		database.setActiveLoginSet(loginSet);
		network.setLoginCredentials(loginSet);
	}

	/**
	 * Liefert eine {@link ErrorMessage} mit der Nachricht, dass keine Daten aus externen Quellen geladen werden konnten.
	 * @return
	 */
	private ErrorMessage getUnableToLoadDataErrorMessage() {
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setErrorCode(-1);
		errorMessage.setAdditionalInfo("Unable to load data from external data sources");
		return errorMessage;
	}

	@Override
	public void saveLoginSet(LoginSet loginSet) {
		database.saveLoginSet(loginSet);
	}

	@Override
	public void removeLoginSet(LoginSet loginSet) {
		database.removeLoginSet(loginSet);
	}

	@Override
	public void editLoginSet(String name, String serverUrl, String school,
			String username, String password, boolean checked, String oldName, String oldServerUrl, String oldSchool) {
		database.editLoginSet(name, serverUrl, school, username, password, checked, oldName, oldServerUrl, oldSchool);
	}

	@Override
	public List<LoginSet> getAllLoginSets() {
		return database.getAllLoginSets();
	}

	@Override
	public AutoSelectSet getAutoSelect() {
		return database.getAutoSelect();
	}

	@Override
	public void setAutoSelect(AutoSelectSet autoSelectSet) {
		database.setAutoSelect(autoSelectSet);
	}
	
}
