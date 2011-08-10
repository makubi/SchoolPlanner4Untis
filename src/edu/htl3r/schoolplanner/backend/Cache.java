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

import java.util.List;
import java.util.Map;

import edu.htl3r.schoolplanner.DateTime;
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
 * Liefert Daten. Diese werden aus dem internen Speicher und wenn nicht vorhanden, von einer externen Datenquelle geholt.
 * @see InternalMemory
 * @see ExternalDataLoader
 */
public class Cache implements DataConnection, UnsaveDataSourceMasterdataProvider, UnsaveDataSourceTimetableDataProvider {
	
	private InternalMemory internalMemory = new InternalMemory();
	private ExternalDataLoader externalDataLoader = new ExternalDataLoader();
	
	
	public Cache() {
		externalDataLoader.setCache(this);
	}

	@Override
	public DataFacade<List<SchoolClass>> getSchoolClassList() {
		DataFacade<List<SchoolClass>> data;
		List<SchoolClass> internalSchoolClassList = internalMemory.getSchoolClassList();
		
		if(internalSchoolClassList != null) {
			data = new DataFacade<List<SchoolClass>>();
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
	public void setLoginCredentials(Authentication authentication) {
		externalDataLoader.setLoginCredentials(authentication);
	}

	@Override
	public void networkAvailabilityChanged(boolean networkAvailable) {
		externalDataLoader.networkAvailabilityChanged(networkAvailable);
	}

	@Override
	public DataFacade<Boolean> authenticate() {
		return externalDataLoader.authenticate();
	}

	@Override
	public DataFacade<Boolean> resyncMasterData() {		
		DataFacade<MasterData> masterData = externalDataLoader.resyncMasterData();
		MasterData masterDataContent = masterData.getData();
		
		DataFacade<Boolean> data = new DataFacade<Boolean>();
		
		if(masterData.isSuccessful()) {
			internalMemory.setSchoolClassList(masterDataContent.getSchoolClassList());
			internalMemory.setSchoolTeacherList(masterDataContent.getSchoolTeacherList());
			internalMemory.setSchoolRoomList(masterDataContent.getSchoolRoomList());
			internalMemory.setSchoolSubjectList(masterDataContent.getSchoolSubjectList());
			internalMemory.setSchoolHolidayList(masterDataContent.getSchoolHolidayList());
			internalMemory.setTimegrid(masterDataContent.getTimegrid());
			internalMemory.setStatusData(masterDataContent.getStatusData());
		
			data.setData(true);
		}
		else {
			data.setErrorMessage(masterData.getErrorMessage());
		}
		
		return data;
	}

	@Override
	public DataFacade<List<Lesson>> getLessons(ViewType viewType, DateTime date) {
		DataFacade<List<Lesson>> data;
		List<Lesson> internalTimegrid = internalMemory.getLessons(viewType, date);
		
		if(internalTimegrid != null) {
			data = new DataFacade<List<Lesson>>();
			data.setData(internalTimegrid);
		}
		else {
			data = externalDataLoader.getLessons(viewType, date);
			if(data.isSuccessful()) {
				internalMemory.setLessons(viewType, date, data.getData());
			}
		}
		
		return data;
	}

	@Override
	public DataFacade<Map<String, List<Lesson>>> getLessons(ViewType viewType,
			DateTime startDate, DateTime endDate) {
		DataFacade<Map<String, List<Lesson>>> data;
		Map<String, List<Lesson>> internalTimegrid = internalMemory.getLessons(viewType, startDate, endDate);
		
		if(internalTimegrid != null) {
			data = new DataFacade<Map<String, List<Lesson>>>();
			data.setData(internalTimegrid);
		}
		else {
			data = externalDataLoader.getLessons(viewType, startDate, endDate);
			if(data.isSuccessful()) {
				internalMemory.setLessons(viewType, startDate, endDate, data.getData());
			}
		}
		
		return data;
	}

	@Override
	public DataFacade<List<StatusData>> getStatusData() {
		DataFacade<List<StatusData>> data;
		List<StatusData> internalSchoolSubjectList = internalMemory.getStatusData();
		
		if(internalSchoolSubjectList != null) {
			data = new DataFacade<List<StatusData>>();
			data.setData(internalSchoolSubjectList);
		}
		else {
			data = externalDataLoader.getStatusData();
			if(data.isSuccessful()) {
				internalMemory.setStatusData(data.getData());
			}
		}
		
		return data;
	}
}
