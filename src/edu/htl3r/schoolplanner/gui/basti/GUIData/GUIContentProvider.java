package edu.htl3r.schoolplanner.gui.basti.GUIData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.ErrorMessage;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolHoliday;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.timegrid.Timegrid;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class GUIContentProvider implements GUIContentProviderSpez{
		
	private Cache cache;
	private Context context;
	
	public GUIContentProvider(Cache c,Context con){
		cache = c;
		context = con;
	}

	@Override
	public List<SchoolRoom> getAllSchoolRooms() {
		if(cache.getSchoolRoomList().isSuccessful()){
			return cache.getSchoolRoomList().getData();
		}else{
			toastError(cache.getSchoolRoomList().getErrorMessage().toString());
			return new ArrayList<SchoolRoom>();
		}
			
	}

	@Override
	public List<SchoolClass> getAllSchoolClasses() {
		if(cache.getSchoolClassList().isSuccessful()){
			return cache.getSchoolClassList().getData();
		}else{
			toastError(cache.getSchoolClassList().getErrorMessage().toString());
			return new ArrayList<SchoolClass>();
		}
	}

	@Override
	public List<SchoolSubject> getAllSchoolSubjects() {
		if(cache.getSchoolSubjectList().isSuccessful()){
			return cache.getSchoolSubjectList().getData();
		}else{
			toastError(cache.getSchoolSubjectList().getErrorMessage().toString());
			return new ArrayList<SchoolSubject>();
		}
	}

	@Override
	public List<SchoolTeacher> getAllSchoolTeachers() {
		if(cache.getSchoolTeacherList().isSuccessful()){
			return cache.getSchoolTeacherList().getData();
		}else{
			toastError(cache.getSchoolTeacherList().getErrorMessage().toString());
			return new ArrayList<SchoolTeacher>();
		}
	}

	@Override
	public List<SchoolHoliday> getAllSchoolHolidays() {
		if(cache.getSchoolHolidayList().isSuccessful()){
			return cache.getSchoolHolidayList().getData();
		}else{
			toastError(cache.getSchoolHolidayList().getErrorMessage().toString());
			return new ArrayList<SchoolHoliday>();			
		}
	}

	@Override
	public Timegrid getTimeGrid() {
		if(cache.getTimegrid().isSuccessful()){
			return cache.getTimegrid().getData();
		}else{
			toastError(cache.getSchoolHolidayList().getErrorMessage().toString());
			return new Timegrid();	
		}
	}

	@Override
	public List<Lesson> getLessonsForDate(ViewType vt, DateTime start) {
		if(cache.getLessons(vt, start).isSuccessful()){
			return cache.getLessons(vt, start).getData();
		}else{
			toastError(cache.getLessons(vt, start).getErrorMessage().toString());
			return new ArrayList<Lesson>();	
		}
	}

	@Override
	public Map<String, List<Lesson>> getLessonsForSomeTime(ViewType vt, DateTime start, DateTime end) {
		Log.d("basti", DateTimeUtils.toISO8601Date(start) + " " + DateTimeUtils.toISO8601Date(end));
		DataFacade<Map<String, List<Lesson>>> facade = cache.getLessons(vt, start,end);
		if(facade.isSuccessful()){
			return facade.getData();
		}else{
			ErrorMessage errorMessage = facade.getErrorMessage();
			Log.i("basti","error" + errorMessage.getAdditionalInfo(),errorMessage.getException());
			toastError(errorMessage.toString());
			return new HashMap<String, List<Lesson>>();	
		}
	}
	
	private void toastError(String errormsg){
		Toast.makeText(context, errormsg, Toast.LENGTH_LONG).show();
	}
	
}
