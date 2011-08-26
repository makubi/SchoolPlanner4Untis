package edu.htl3r.schoolplanner.gui.basti.GUIData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.Toast;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.DataFacade;
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
		DataFacade<List<SchoolRoom>> data = cache.getSchoolRoomList();
		if(data.isSuccessful()){
			return data.getData();
		}else{
			toastError(data.getErrorMessage().getAdditionalInfo());
		return new ArrayList<SchoolRoom>();
		}
			
	}

	@Override
	public List<SchoolClass> getAllSchoolClasses() {
		DataFacade<List<SchoolClass>> data = cache.getSchoolClassList();
		if(data.isSuccessful()){
			return data.getData();
		}else{
			toastError(data.getErrorMessage().getAdditionalInfo());
			return new ArrayList<SchoolClass>();
		}
	}

	@Override
	public List<SchoolSubject> getAllSchoolSubjects() {
		DataFacade<List<SchoolSubject>> data = cache.getSchoolSubjectList();
		if(data.isSuccessful()){
			return data.getData();
		}else{
			toastError(data.getErrorMessage().getAdditionalInfo());
			return new ArrayList<SchoolSubject>();
		}
	}

	@Override
	public List<SchoolTeacher> getAllSchoolTeachers() {
		DataFacade<List<SchoolTeacher>> data = cache.getSchoolTeacherList();
		if(data.isSuccessful()){
			return data.getData();
		}else{
			toastError(data.getErrorMessage().getAdditionalInfo());
			return new ArrayList<SchoolTeacher>();
		}
	}

	@Override
	public List<SchoolHoliday> getAllSchoolHolidays() {
		DataFacade<List<SchoolHoliday>> data = cache.getSchoolHolidayList();
		if(data.isSuccessful()){
			return data.getData();
		}else{
			toastError(data.getErrorMessage().getAdditionalInfo());
			return new ArrayList<SchoolHoliday>();
		}
	}

	@Override
	public Timegrid getTimeGrid() {
		DataFacade<Timegrid> data = cache.getTimegrid();
		if(data.isSuccessful()){
			return data.getData();
		}else{
			toastError(data.getErrorMessage().getAdditionalInfo());
			return new Timegrid();
		}
	}

	@Override
	public List<Lesson> getLessonsForDate(ViewType vt, DateTime start) {
		DataFacade<List<Lesson>> data = cache.getLessons(vt, start);
		if(data.isSuccessful()){
			return data.getData();
		}else{
			toastError(data.getErrorMessage().getAdditionalInfo());
			return new ArrayList<Lesson>();
		}
	}

	@Override
	public Map<String, List<Lesson>> getLessonsForSomeTime(ViewType vt, DateTime start, DateTime end) {
		DataFacade<Map<String, List<Lesson>>> data = cache.getLessons(vt, start,end);
		if(data.isSuccessful()){
			return data.getData();
		}else{
			toastError(data.getErrorMessage().getAdditionalInfo());
			return new HashMap<String, List<Lesson>>();	
		}
	}
	
	private void toastError(String errormsg){
		Toast.makeText(context, errormsg, Toast.LENGTH_LONG).show();
	}
	
}
