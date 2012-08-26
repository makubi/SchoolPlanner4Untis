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

package edu.htl3r.schoolplanner.backend.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.AutoSelectHandler;
import edu.htl3r.schoolplanner.backend.DataConnection;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.DataSource;
import edu.htl3r.schoolplanner.backend.ErrorMessage;
import edu.htl3r.schoolplanner.backend.ExternalDataLoader;
import edu.htl3r.schoolplanner.backend.LoginSetHandler;
import edu.htl3r.schoolplanner.backend.MasterData;
import edu.htl3r.schoolplanner.backend.NetworkTimetableDataProvider;
import edu.htl3r.schoolplanner.backend.TimetableDay;
import edu.htl3r.schoolplanner.backend.UnsaveDataSourceMasterdataProvider;
import edu.htl3r.schoolplanner.backend.cache.timetable.TimetableCache;
import edu.htl3r.schoolplanner.backend.network.ErrorCodes;
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
 * Liefert Daten aus dem Backend. Diese werden aus dem internen Speicher und wenn nicht vorhanden, von einer externen Datenquelle geholt.
 * @see InternalMemory
 * @see ExternalDataLoader
 */
public class Cache implements DataConnection, UnsaveDataSourceMasterdataProvider, NetworkTimetableDataProvider, LoginSetHandler, AutoSelectHandler {
	
	private InternalMemory internalMemory = new InternalMemory();
	private TimetableCache timetableCache = new TimetableCache();
	
	private ExternalDataLoader externalDataLoader = new ExternalDataLoader();
	private boolean networkAvailable;
	
	public Cache() {
		externalDataLoader.setCache(this);
	}

	@Override
	public DataFacade<List<SchoolClass>> getSchoolClassList() {
		DataFacade<List<SchoolClass>> data;
		List<SchoolClass> internalSchoolClassList = internalMemory.getSchoolClassList();
		
		if(internalSchoolClassList != null) {
			data = new DataFacade<List<SchoolClass>>();
			data.setDataSource(DataSource.INTERNAL_RAM);
			data.setData(internalSchoolClassList);
		}
		else {
			data = externalDataLoader.getSchoolClassList();
			if(data.isSuccessful()) {
				internalMemory.setSchoolClassList(data.getData());
			}
		}
		
		return data;
		
	}

	@Override
	public DataFacade<List<SchoolTeacher>> getSchoolTeacherList() {
		DataFacade<List<SchoolTeacher>> data;
		List<SchoolTeacher> internalSchoolTeacherList = internalMemory.getSchoolTeacherList();
		
		if(internalSchoolTeacherList != null) {
			data = new DataFacade<List<SchoolTeacher>>();
			data.setDataSource(DataSource.INTERNAL_RAM);
			data.setData(internalSchoolTeacherList);
		}
		else {
			data = externalDataLoader.getSchoolTeacherList();
			if(data.isSuccessful()) {
				internalMemory.setSchoolTeacherList(data.getData());
			}
		}
		
		return data;
	}

	@Override
	public DataFacade<List<SchoolRoom>> getSchoolRoomList() {
		DataFacade<List<SchoolRoom>> data;
		List<SchoolRoom> internalSchoolRoomList = internalMemory.getSchoolRoomList();
		
		if(internalSchoolRoomList != null) {
			data = new DataFacade<List<SchoolRoom>>();
			data.setDataSource(DataSource.INTERNAL_RAM);
			data.setData(internalSchoolRoomList);
		}
		else {
			data = externalDataLoader.getSchoolRoomList();
			if(data.isSuccessful()) {
				internalMemory.setSchoolRoomList(data.getData());
			}
		}
		
		return data;
	}

	@Override
	public DataFacade<List<SchoolSubject>> getSchoolSubjectList() {
		DataFacade<List<SchoolSubject>> data;
		List<SchoolSubject> internalSchoolSubjectList = internalMemory.getSchoolSubjectList();
		
		if(internalSchoolSubjectList != null) {
			data = new DataFacade<List<SchoolSubject>>();
			data.setDataSource(DataSource.INTERNAL_RAM);
			data.setData(internalSchoolSubjectList);
		}
		else {
			data = externalDataLoader.getSchoolSubjectList();
			if(data.isSuccessful()) {
				internalMemory.setSchoolSubjectList(data.getData());
			}
		}
		
		return data;
	}

	@Override
	public DataFacade<List<SchoolHoliday>> getSchoolHolidayList() {
		DataFacade<List<SchoolHoliday>> data;
		List<SchoolHoliday> internalSchoolHolidayList = internalMemory.getSchoolHolidayList();
		
		if(internalSchoolHolidayList != null) {
			data = new DataFacade<List<SchoolHoliday>>();
			data.setDataSource(DataSource.INTERNAL_RAM);
			data.setData(internalSchoolHolidayList);
		}
		else {
			data = externalDataLoader.getSchoolHolidayList();
			if(data.isSuccessful()) {
				internalMemory.setSchoolHolidayList(data.getData());
			}
		}
		
		return data;
	}

	@Override
	public DataFacade<Timegrid> getTimegrid() {
		DataFacade<Timegrid> data;
		Timegrid internalTimegrid = internalMemory.getTimegrid();
		
		if(internalTimegrid != null) {
			data = new DataFacade<Timegrid>();
			data.setDataSource(DataSource.INTERNAL_RAM);
			data.setData(internalTimegrid);
		}
		else {
			data = externalDataLoader.getTimegrid();
			if(data.isSuccessful()) {
				internalMemory.setTimegrid(data.getData());
			}
		}
		
		return data;
	}

	@Override
	public DataFacade<List<Lesson>> getLessons(ViewType viewType, DateTime date) {
		DataFacade<List<Lesson>> data;
		List<Lesson> lessons;
		TimetableDay timetableDay;
	
		if((timetableDay = internalMemory.getTimetableForDay(viewType, date)) != null) {
			lessons = timetableDay.getLessons();
			data = new DataFacade<List<Lesson>>();
			data.setDataSource(DataSource.INTERNAL_RAM);
			data.setLastRefresh(timetableDay.getLastRefreshTime());
			data.setData(lessons);
		}
		else if ((data = timetableCache.getLessons(viewType, date)).isSuccessful()) {
			internalMemory.setLessonsForDay(viewType, date, data.getData(), data.getLastRefreshTime());
			data.setDataSource(DataSource.CACHE);
		}
		else {
			data = externalDataLoader.getLessons(viewType, date);
			if(data.isSuccessful()) {
				lessons = data.getData();
				internalMemory.setLessonsForDay(viewType, date, lessons, data.getLastRefreshTime());
				timetableCache.setLessons(viewType, date, lessons);
			}
		}
		
		return data;
	}

	@Override
	public DataFacade<Map<String, List<Lesson>>> getLessons(ViewType viewType,
			DateTime startDate, DateTime endDate) {
		
		DataFacade<Map<String, List<Lesson>>> data;
		Map<String, List<Lesson>> lessonMap = new HashMap<String, List<Lesson>>();
		Map<String, TimetableDay> timetableDayMap;
		
		if((timetableDayMap = internalMemory.getTimetableForTimePeriod(viewType, startDate, endDate)) != null) {
			data = new DataFacade<Map<String, List<Lesson>>>();
			data.setDataSource(DataSource.INTERNAL_RAM);
			
			for(String date : timetableDayMap.keySet()) {
				TimetableDay timetableDay = timetableDayMap.get(date);
				lessonMap.put(date, timetableDay.getLessons());
				data.setLastRefresh(timetableDay.getLastRefreshTime());
			}
			
			data.setData(lessonMap);
		}
		else if ((data = timetableCache.getLessons(viewType, startDate, endDate)).isSuccessful()) {
			data.setDataSource(DataSource.CACHE);
			internalMemory.setLessonsForDay(viewType, startDate, endDate, data.getData(), data.getLastRefreshTime());
		}
		else {
			data = externalDataLoader.getLessons(viewType, startDate, endDate);
			if(data.isSuccessful()) {
				lessonMap = data.getData();
				internalMemory.setLessonsForDay(viewType, startDate, endDate, lessonMap, data.getLastRefreshTime());
				timetableCache.setLessons(viewType, startDate, endDate, lessonMap);
			}
		}
		
		return data;
	}

	@Override
	public DataFacade<Map<String, List<Lesson>>> getLessonsFromNetwork(ViewType viewType,
			DateTime startDate, DateTime endDate) {
		
			DataFacade<Map<String, List<Lesson>>> data = externalDataLoader.getLessonsFromNetwork(viewType, startDate, endDate);
			if(data.isSuccessful()) {
				Map<String, List<Lesson>> lessonMap = data.getData();
				internalMemory.setLessonsForDay(viewType, startDate, endDate, lessonMap, data.getLastRefreshTime());
				timetableCache.setLessons(viewType, startDate, endDate, lessonMap);
			}
			return data;
	}

	@Override
	public void networkAvailabilityChanged(boolean networkAvailable) {
		this.networkAvailable  = networkAvailable;
		externalDataLoader.networkAvailabilityChanged(networkAvailable);
	}

	@Override
	public DataFacade<Boolean> authenticate() {
		DataFacade<Boolean> authenticate = new DataFacade<Boolean>();
		
		if(networkAvailable) authenticate = externalDataLoader.authenticate();
		else {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setErrorCode(ErrorCodes.NETWORK_NEEDED_BUT_UNAVAILABLE);
			
			authenticate.setErrorMessage(errorMessage);
		}
		
		return authenticate;
	}

	@Override
	public DataFacade<Boolean> resyncMasterData() {		
		DataFacade<MasterData> masterData = externalDataLoader.resyncMasterData();
		
		DataFacade<Boolean> data = new DataFacade<Boolean>();
		
		if(masterData.isSuccessful()) {
			MasterData masterDataContent = masterData.getData();
			
			internalMemory.setSchoolClassList(masterDataContent.getSchoolClassList());
			internalMemory.setSchoolTeacherList(masterDataContent.getSchoolTeacherList());
			internalMemory.setSchoolRoomList(masterDataContent.getSchoolRoomList());
			internalMemory.setSchoolSubjectList(masterDataContent.getSchoolSubjectList());
			internalMemory.setSchoolHolidayList(masterDataContent.getSchoolHolidayList());
			internalMemory.setTimegrid(masterDataContent.getTimegrid());
		
			data.setData(true);
		}
		else {
			data.setErrorMessage(masterData.getErrorMessage());
		}
		
		return data;
	}

	@Override
	public void setLoginCredentials(LoginSet loginSet) {
		timetableCache.setCurrentLoginSetName(loginSet.getName());
		externalDataLoader.setLoginCredentials(loginSet);
	}
	
	@Override
	public void clearInternalCache() {
		internalMemory = new InternalMemory();
	}

	@Override
	public void saveLoginSet(LoginSet loginSet) {
		externalDataLoader.saveLoginSet(loginSet);
	}

	@Override
	public void removeLoginSet(LoginSet loginSet) {
		timetableCache.loginSetRemoved(loginSet.getName());
		externalDataLoader.removeLoginSet(loginSet);
	}

	@Override
	public void editLoginSet(String name, String serverUrl, String school,
			String username, String password, boolean checked, String oldName, String oldServerUrl, String oldSchool) {
		timetableCache.loginSetRemoved(oldName);
		externalDataLoader.editLoginSet(name, serverUrl, school, username, password, checked, oldName, oldServerUrl, oldSchool);
	}

	@Override
	public List<LoginSet> getAllLoginSets() {
		return externalDataLoader.getAllLoginSets();
	}

	@Override
	public AutoSelectSet getAutoSelect() {
		return externalDataLoader.getAutoSelect();
	}

	@Override
	public void setAutoSelect(AutoSelectSet autoSelectSet) {
		externalDataLoader.setAutoSelect(autoSelectSet);
	}
}
