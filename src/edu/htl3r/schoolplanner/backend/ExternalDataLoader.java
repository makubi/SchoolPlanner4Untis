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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import android.util.Log;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.localdata.LocalData;
import edu.htl3r.schoolplanner.backend.network.JSONNetwork;
import edu.htl3r.schoolplanner.backend.preferences.Authentication;
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
 * @see LocalData
 * @see JSONNetwork
 */
public class ExternalDataLoader implements UnsaveDataSourceMasterdataProvider, UnsaveDataSourceTimetableDataProvider {

	private LocalData database = new LocalData();
	private JSONNetwork network = new JSONNetwork();

	private boolean networkAvailable = true;

	@Override
	public DataFacade<List<SchoolClass>> getSchoolClassList() {
		DataFacade<List<SchoolClass>> schoolClassList;

		// Check database
		/*if ((schoolClassList = database.getSchoolClassList()) != null) {
			Log.v("DataSource", "schoolClassList: Database");
			return schoolClassList;
		}*/
		// Check network
		if (networkAvailable) {
			if ((schoolClassList = network.getSchoolClassList()) != null) {
				database.setSchoolClassList(schoolClassList.getData());
				Log.v("DataSource", "schoolClassList: Network");
				return schoolClassList;
			}
		}

		return null;
	}

	@Override
	public DataFacade<List<SchoolTeacher>> getSchoolTeacherList() {
		DataFacade<List<SchoolTeacher>> schoolTeacherList;

		// Check database
		/*if ((schoolTeacherList = database.getSchoolTeacherList()) != null) {
			Log.v("DataSource", "schoolTeacherList: Database");
			return schoolTeacherList;
		}*/
		// Check network
		if (networkAvailable) {
			if ((schoolTeacherList = network.getSchoolTeacherList()) != null) {
				database.setSchoolTeacherList(schoolTeacherList.getData());
				Log.v("DataSource", "schoolTeacherList: Network");
				return schoolTeacherList;
			}
		}

		return null;
	}

	@Override
	public DataFacade<List<SchoolRoom>> getSchoolRoomList() {
		DataFacade<List<SchoolRoom>> schoolRoomList;

		// Check database
		/*if ((schoolRoomList = database.getSchoolRoomList()) != null) {
			Log.v("DataSource", "schoolRoomList: Database");
			return schoolRoomList;
		}*/
		// Check network
		if (networkAvailable) {
			if ((schoolRoomList = network.getSchoolRoomList()) != null) {
				database.setSchoolRoomList(schoolRoomList.getData());
				Log.v("DataSource", "schoolRoomList: Network");
				return schoolRoomList;
			}
		}

		return null;
	}

	@Override
	public DataFacade<List<SchoolSubject>> getSchoolSubjectList() {
		DataFacade<List<SchoolSubject>> schoolSubjectList;

		// Check database
		/*if ((schoolSubjectList = database.getSchoolSubjectList()) != null) {
			Log.v("DataSource", "schoolSubjectList: Database");
			return schoolSubjectList;
		}*/
		// Check network
		if (networkAvailable) {
			if ((schoolSubjectList = network.getSchoolSubjectList()) != null) {
				database.setSchoolSubjectList(schoolSubjectList.getData());
				Log.v("DataSource", "schoolSubjectList: Network");
				return schoolSubjectList;
			}
		}

		return null;
	}

	@Override
	public DataFacade<List<SchoolHoliday>> getSchoolHolidayList() {
		DataFacade<List<SchoolHoliday>> schoolHolidayList;

		// Check database
		/*if ((schoolHolidayList = database.getSchoolHolidayList()) != null) {
			Log.v("DataSource", "schoolHolidayList: Database");
			return schoolHolidayList;
		}*/
		// Check network
		if (networkAvailable) {
			if ((schoolHolidayList = network.getSchoolHolidayList()) != null) {
				database.setSchoolHolidayList(schoolHolidayList.getData());
				Log.v("DataSource", "schoolHolidayList: Network");
				return schoolHolidayList;
			}
		}

		return null;
	}
	
	@Override
	public DataFacade<Timegrid> getTimegrid() {
		DataFacade<Timegrid> timegrid;

		// Check database
		/*if ((timegrid = database.getTimegrid()) != null) {
			Log.v("DataSource", "timegrid: Database");
			return timegrid;
		}*/
		// Check network
		if (networkAvailable) {
			if ((timegrid = network.getTimegrid()) != null) {
				database.setTimegrid(timegrid.getData());
				Log.v("DataSource", "timegrid: Network");
				return timegrid;
			}
		}

		return null;
	}

	@Override
	public DataFacade<List<StatusData>> getStatusData() {
		DataFacade<List<StatusData>> statusData;
	
		// Check database
		/*if ((schoolSubjectList = database.getSchoolSubjectList()) != null) {
			Log.v("DataSource", "schoolSubjectList: Database");
			return schoolSubjectList;
		}*/
		// Check network
		if (networkAvailable) {
			if ((statusData = network.getStatusData()) != null) {
				//database.setStatusData(statusData.getData());
				return statusData;
			}
		}
	
		return null;
	
	}

	@Override
	public DataFacade<List<Lesson>> getLessons(ViewType viewType, DateTime date) {
		DataFacade<List<Lesson>> data;
		
		// Check network
		if (networkAvailable) {
			if ((data = network.getLessons(viewType, date)) != null) {
				// TODO: Set in database
				return data;
			}
		}

		// TODO: Check database
		
		return null;
	}

	@Override
	public DataFacade<Map<String, List<Lesson>>> getLessons(ViewType viewType,
			DateTime startDate, DateTime endDate) {
		DataFacade<Map<String, List<Lesson>>> data;
		
		// Check network
		if (networkAvailable) {
			if ((data = network.getLessons(viewType, startDate, endDate)) != null) {
				// TODO: Set in database
				return data;
			}
		}

		// TODO: Check database
		
		return null;
	}

	/**
	 * Authentifiziert sich mit dem Untis-Server ueber das Netzwerk.
	 * @return true, wenn die Authentifizierung erfolgreich war, sonst false
	 * @throws IOException Wenn ein Problem waehrend der Netzwerkanfrage auftritt
	 */
	public DataFacade<Boolean> authenticate() {
		return network.authenticate();
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
	 * @throws IOException Wenn waehrend dem Abruf der Daten ein Fehler auftritt
	 */
	public DataFacade<MasterData> resyncMasterData() {
		DataFacade<List<SchoolClass>> schoolClassList = network.getSchoolClassList();
		DataFacade<List<SchoolTeacher>> schoolTeacherList = network.getSchoolTeacherList();
		DataFacade<List<SchoolRoom>> schoolRoomList = network.getSchoolRoomList();
		DataFacade<List<SchoolSubject>> schoolSubjectList = network.getSchoolSubjectList();
		
		DataFacade<List<SchoolHoliday>> schoolHolidayList = network.getSchoolHolidayList();
		DataFacade<Timegrid> timegrid = network.getTimegrid();
		DataFacade<List<StatusData>> statusData = network.getStatusData();
		
		DataFacade<MasterData> data = new DataFacade<MasterData>();
		
		if(schoolClassList.isSuccessful() && schoolHolidayList.isSuccessful() && schoolRoomList.isSuccessful() && schoolSubjectList.isSuccessful() && schoolTeacherList.isSuccessful() && timegrid.isSuccessful() && statusData.isSuccessful()) {
			MasterData masterData = new MasterData();
			masterData.setSchoolClassList(schoolClassList.getData());
			masterData.setSchoolRoomList(schoolRoomList.getData());
			masterData.setSchoolSubjectList(schoolSubjectList.getData());
			masterData.setSchoolTeacherList(schoolTeacherList.getData());
			masterData.setSchoolHolidayList(schoolHolidayList.getData());
			masterData.setTimegrid(timegrid.getData());
			masterData.setStatusData(statusData.getData());
		
			data.setData(masterData);
		
			database.setSchoolClassList(schoolClassList.getData());
			database.setSchoolTeacherList(schoolTeacherList.getData());
			database.setSchoolRoomList(schoolRoomList.getData());
			database.setSchoolSubjectList(schoolSubjectList.getData());
			database.setSchoolHolidayList(schoolHolidayList.getData());
			database.setTimegrid(timegrid.getData());
			database.setStatusData(statusData.getData());
		
		}
		else {
			data.setErrorCode(254);
		}
		
		return data;
	}

	public void setCache(Cache cache) {
		network.setCache(cache);
	}

	/**
	 * Setzt die Preferences fuer das Netzwerk neu.
	 * @param authentications Preferences, die gesetzt werden sollen
	 */
	public void setLoginCredentials(Authentication authentications) {
		network.setLoginCredentials(authentications);
	}
	
}
