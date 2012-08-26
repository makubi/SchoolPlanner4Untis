/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
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
package edu.htl3r.schoolplanner.gui.timetable.GUIData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.ErrorMessage;
import edu.htl3r.schoolplanner.backend.cache.Cache;
import edu.htl3r.schoolplanner.backend.network.ErrorCodes;
import edu.htl3r.schoolplanner.backend.network.WebUntisErrorCodes;
import edu.htl3r.schoolplanner.backend.network.exceptions.WebUntisServiceException;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class GUIContentProvider implements GUIContentProviderSpez {

	private Cache cache;
	private Context context;
	private ArrayList<ErrorHandler> errorhandler = new ArrayList<GUIContentProvider.ErrorHandler>();

	public GUIContentProvider(Cache c, Context con) {
		cache = c;
		context = con;
	}

	@Override
	public List<SchoolRoom> getAllSchoolRooms() {
		DataFacade<List<SchoolRoom>> data = cache.getSchoolRoomList();
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			logToUser(getDisplayErrorMessage(data.getErrorMessage()));
			return new ArrayList<SchoolRoom>();
		}

	}

	@Override
	public List<SchoolClass> getAllSchoolClasses() {
		DataFacade<List<SchoolClass>> data = cache.getSchoolClassList();
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			logToUser(getDisplayErrorMessage(data.getErrorMessage()));
			return new ArrayList<SchoolClass>();
		}
	}

	@Override
	public List<SchoolSubject> getAllSchoolSubjects() {
		DataFacade<List<SchoolSubject>> data = cache.getSchoolSubjectList();
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			logToUser(getDisplayErrorMessage(data.getErrorMessage()));
			return new ArrayList<SchoolSubject>();
		}
	}

	@Override
	public List<SchoolTeacher> getAllSchoolTeachers() {
		DataFacade<List<SchoolTeacher>> data = cache.getSchoolTeacherList();
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			logToUser(getDisplayErrorMessage(data.getErrorMessage()));
			return new ArrayList<SchoolTeacher>();
		}
	}

	@Override
	public List<SchoolHoliday> getAllSchoolHolidays() {
		DataFacade<List<SchoolHoliday>> data = cache.getSchoolHolidayList();
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			logToUser(getDisplayErrorMessage(data.getErrorMessage()));
			return new ArrayList<SchoolHoliday>();
		}
	}

	@Override
	public Timegrid getTimeGrid() {
		DataFacade<Timegrid> data = cache.getTimegrid();
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			logToUser(getDisplayErrorMessage(data.getErrorMessage()));
			return new Timegrid();
		}
	}

//	@Override
//	public List<Lesson> getLessonsForDate(ViewType vt, DateTime start) {
//		DataFacade<List<Lesson>> data = cache.getLessons(vt, start);
//		if (data.isSuccessful()) {
//			return data.getData();
//		} else {
//			logToUser(getDisplayErrorMessage(data.getErrorMessage()));
//			return new ArrayList<Lesson>();
//		}
//	}

	@Override
	public DataFromNetwork getLessonsForSomeTime(ViewType vt, DateTime start, DateTime end) {
		DataFacade<Map<String, List<Lesson>>> data = cache.getLessons(vt, start, end);
		if (data.isSuccessful()) {
			return new DataFromNetwork(data.getData(), data.getLastRefreshTime());
		} else {
			logToUser(getDisplayErrorMessage(data.getErrorMessage()));
			return new DataFromNetwork(new HashMap<String, List<Lesson>>(), new DateTime());
		}
	}

	@Override
	public DataFromNetwork getLessonsForSomeTimeFromNetwork(ViewType vt, DateTime start, DateTime end) {
		DataFacade<Map<String, List<Lesson>>> data = cache.getLessonsFromNetwork(vt, start, end);
		if (data.isSuccessful()) {
			return new DataFromNetwork(data.getData(), data.getLastRefreshTime());
		} else {
			logToUser(getDisplayErrorMessage(data.getErrorMessage()));
			return new DataFromNetwork(new HashMap<String, List<Lesson>>(), new DateTime());
		}
	}

	private String getDisplayErrorMessage(ErrorMessage errorMessage) {
		String additionalInfo = errorMessage.getAdditionalInfo();
		int errorCode = errorMessage.getErrorCode();
		Throwable exception = errorMessage.getException();

		switch (errorCode) {

		case ErrorCodes.WEBUNTIS_SERVICE_EXCEPTION:
			final int webUntisErrorCode = ((WebUntisServiceException) exception).getWebUntisErrorCode();
			if (webUntisErrorCode == WebUntisErrorCodes.WEBUNTIS_NO_RIGHT_FOR_TIMETABLE) {
				return getString(R.string.error_webuntis_no_right_for_timetable);
			} else {
				return getString(R.string.webuntis_error_occurred) + " " + errorCode + ":" + additionalInfo;
			}

		case ErrorCodes.SOCKET_TIMEOUT_EXCEPTION:
			return getString(R.string.error_socket_timeout);
		default:
			return logDefaultErrorMessage(additionalInfo, errorCode, exception);
		}
	}

	private String logDefaultErrorMessage(String additionalInfo, int errorCode, Throwable exception) {
		Log.e("login", "========== ERROR");
		Log.e("login", "info: " + additionalInfo);
		Log.e("login", "code: " + errorCode);

		if (exception != null)
			Log.e("login", "e: " + exception.getMessage(), exception);
		return getString(R.string.error_occurred) + " " + (exception != null ? exception.getMessage() : errorCode) + ": " + additionalInfo;
	}

	private void logToUser(String error) {
		for(ErrorHandler h : errorhandler)
			h.logToUser(error);
	}

	public void addErrorHandler(ErrorHandler eh){
		errorhandler.add(eh);
	}
	
	public void removeErrorHandler(ErrorHandler eh){
		errorhandler.remove(eh);
	}
	
	private String getString(int resId) {
		return context.getString(resId);
	}
	

	public interface ErrorHandler{
		public void logToUser(String msg);
	}

}
