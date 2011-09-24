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
import android.content.Intent;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.ErrorMessage;
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
import edu.htl3r.schoolplanner.gui.timetable.GUIErrorHandler;


public class GUIContentProvider implements GUIContentProviderSpez {

	private Cache cache;
	private Context context;
	private Intent errorIntent = new Intent(GUIErrorHandler.ERRORINTENT);

	public GUIContentProvider(Cache c, Context con) {
		cache = c;
		context = con;
		errorIntent.putExtra(GUIErrorHandler.ERROR_TITLE_FIELD, GUIErrorHandler.ERROR_MESSAGE_TYPE_TOAST);
	}

	@Override
	public List<SchoolRoom> getAllSchoolRooms() {
		DataFacade<List<SchoolRoom>> data = cache.getSchoolRoomList();
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			errorIntent.putExtra(GUIErrorHandler.ERROR_TITLE_FIELD, data.getErrorMessage().getAdditionalInfo());
			context.sendBroadcast(errorIntent);
			return new ArrayList<SchoolRoom>();
		}

	}

	@Override
	public List<SchoolClass> getAllSchoolClasses() {
		DataFacade<List<SchoolClass>> data = cache.getSchoolClassList();
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			errorIntent.putExtra(GUIErrorHandler.ERROR_TITLE_FIELD, data.getErrorMessage().getAdditionalInfo());
			context.sendBroadcast(errorIntent);

			return new ArrayList<SchoolClass>();
		}
	}

	@Override
	public List<SchoolSubject> getAllSchoolSubjects() {
		DataFacade<List<SchoolSubject>> data = cache.getSchoolSubjectList();
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			errorIntent.putExtra(GUIErrorHandler.ERROR_TITLE_FIELD, data.getErrorMessage().getAdditionalInfo());
			context.sendBroadcast(errorIntent);
			return new ArrayList<SchoolSubject>();
		}
	}

	@Override
	public List<SchoolTeacher> getAllSchoolTeachers() {
		DataFacade<List<SchoolTeacher>> data = cache.getSchoolTeacherList();
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			errorIntent.putExtra(GUIErrorHandler.ERROR_TITLE_FIELD, data.getErrorMessage().getAdditionalInfo());
			context.sendBroadcast(errorIntent);

			return new ArrayList<SchoolTeacher>();
		}
	}

	@Override
	public List<SchoolHoliday> getAllSchoolHolidays() {
		DataFacade<List<SchoolHoliday>> data = cache.getSchoolHolidayList();
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			errorIntent.putExtra(GUIErrorHandler.ERROR_TITLE_FIELD, data.getErrorMessage().getAdditionalInfo());
			context.sendBroadcast(errorIntent);

			return new ArrayList<SchoolHoliday>();
		}
	}

	@Override
	public Timegrid getTimeGrid() {
		DataFacade<Timegrid> data = cache.getTimegrid();
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			errorIntent.putExtra(GUIErrorHandler.ERROR_TITLE_FIELD, data.getErrorMessage().getAdditionalInfo());
			context.sendBroadcast(errorIntent);

			return new Timegrid();
		}
	}

	@Override
	public List<Lesson> getLessonsForDate(ViewType vt, DateTime start) {
		DataFacade<List<Lesson>> data = cache.getLessons(vt, start);
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			errorIntent.putExtra(GUIErrorHandler.ERROR_TITLE_FIELD, data.getErrorMessage().getAdditionalInfo());
			context.sendBroadcast(errorIntent);
			return new ArrayList<Lesson>();
		}
	}

	@Override
	public Map<String, List<Lesson>> getLessonsForSomeTime(ViewType vt, DateTime start, DateTime end) {
		DataFacade<Map<String, List<Lesson>>> data = cache.getLessons(vt, start, end);
		if (data.isSuccessful()) {
			return data.getData();
		} else {
			errorIntent.putExtra(GUIErrorHandler.ERROR_TITLE_FIELD, getDisplayErrorMessage(data.getErrorMessage()));
			context.sendBroadcast(errorIntent);
			return new HashMap<String, List<Lesson>>();
		}
	}
	
	private String getDisplayErrorMessage(ErrorMessage errorMessage) {
		Throwable exception = errorMessage.getException();
		if(exception instanceof WebUntisServiceException) {
			final int webUntisErrorCode = ((WebUntisServiceException) exception).getWebUntisErrorCode();
			if(webUntisErrorCode == WebUntisErrorCodes.WEBUNTIS_NO_RIGHT_FOR_TIMETABLE) {
				return getString(R.string.error_webuntis_no_right_for_timetable);
			}
		}
		return exception.getMessage()+": "+errorMessage.getAdditionalInfo();
	}

	private String getString(int resId) {
		return context.getString(resId);
	}
}
