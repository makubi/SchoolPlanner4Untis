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

package edu.htl3r.schoolplanner.backend.cache.timetable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import android.content.Context;
import android.util.Log;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.DateTimeUtils;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.ErrorMessage;
import edu.htl3r.schoolplanner.backend.TimetableDataStore;
import edu.htl3r.schoolplanner.backend.UnsaveDataSourceTimetableDataProvider;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;

public class TimetableCache implements UnsaveDataSourceTimetableDataProvider, TimetableDataStore {

	private static final Context CONTEXT = SchoolplannerContext.context;
	private static final String ABSOLUTE_CACHE_PATH = CONTEXT.getCacheDir().getAbsolutePath();
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	private String currentLoginSetCacheFolder;
	
	public TimetableCache() {
		  objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
	}
	
	@Override
	public DataFacade<List<Lesson>> getLessons(ViewType viewType, DateTime date) {
		DataFacade<List<Lesson>> data = new DataFacade<List<Lesson>>();
		final String viewTypeFolder = viewType.getClass().getCanonicalName();
		
		final String fileName = DateTimeUtils.toISO8601Date(date)+".json";
		File file = new File(currentLoginSetCacheFolder + File.separator + viewTypeFolder + File.separator + viewType.getId(), fileName);
		
		if(file.exists()) {
			try {
				List<Lesson> readValue = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, new Lesson().getClass()));
				data.setData(readValue);
				
				// Last refresh setzen
				DateTime dateTime = new DateTime();
				dateTime.getAndroidTime().set(file.lastModified());
				data.setLastRefresh(dateTime);
			} catch (FileNotFoundException e) {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setException(e);
				errorMessage.setAdditionalInfo("Cache file "+fileName+" not found");
				data.setErrorMessage(errorMessage);
			} catch (JsonGenerationException e) {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setException(e);
				errorMessage.setAdditionalInfo("Unable to generate json for lesson, file: "+fileName);
				data.setErrorMessage(errorMessage);
			} catch (JsonMappingException e) {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setException(e);
				errorMessage.setAdditionalInfo("Unable to map json to lesson, file: "+fileName);
				data.setErrorMessage(errorMessage);
			} catch (IOException e) {
				ErrorMessage errorMessage = new ErrorMessage();
				errorMessage.setException(e);
				errorMessage.setAdditionalInfo("IOException, file: "+fileName);
				data.setErrorMessage(errorMessage);
			}
		}
		else {
			ErrorMessage errorMessage = new ErrorMessage();
			errorMessage.setAdditionalInfo("Cache file "+fileName+" does not exists");
			data.setErrorMessage(errorMessage);
		}
		
		Log.v("cache","Loaded lessons for "+fileName+": "+data.isSuccessful());
		return data;
	}

	@Override
	public DataFacade<Map<String, List<Lesson>>> getLessons(ViewType viewType,
			DateTime startDate, DateTime endDate) {
		DataFacade<Map<String, List<Lesson>>> data = new DataFacade<Map<String,List<Lesson>>>();
		Map<String, List<Lesson>> lessonMap = new HashMap<String, List<Lesson>>();
		
		DateTime tmpDateTime = startDate.clone();
		while(tmpDateTime.beforeOrEquals(endDate)) {
			DataFacade<List<Lesson>> lessons = getLessons(viewType, tmpDateTime);
			if(lessons.isSuccessful()) {
				lessonMap.put(DateTimeUtils.toISO8601Date(tmpDateTime), lessons.getData());
				// Last refresh setzen
				// Datum vom letzten Tag der Woche wird verwendet
				data.setLastRefresh(lessons.getLastRefreshTime());
			}
			else {
				data.setErrorMessage(lessons.getErrorMessage());
				return data;
			}
			tmpDateTime.increaseDay();
		}
		data.setData(lessonMap);
		
		return data;
	}

	@Override
	public void setLessons(ViewType view, DateTime date, List<Lesson> lessons) {
		final String fileName = DateTimeUtils.toISO8601Date(date)+".json";
		final String viewTypeFolder = view.getClass().getCanonicalName();
		
		File folder = new File(currentLoginSetCacheFolder + File.separator + viewTypeFolder + File.separator + view.getId());
		if(!folder.exists()) folder.mkdirs();
		
		try {
			FileOutputStream fos = new FileOutputStream(new File(folder, fileName));
			
			fos.write(objectMapper.writeValueAsString(lessons).getBytes());
			
			fos.close();
			Log.v("cache","Saved lessons for "+fileName);
		} catch (FileNotFoundException e) {
			Log.w("cache", "Unable to open file for caching: "+fileName, e);
		} catch (JsonGenerationException e) {
			Log.w("cache", "Unable to generate json for lesson, file: "+fileName, e);
		} catch (JsonMappingException e) {
			Log.w("cache", "Unable to map json for lesson, file: "+fileName, e);
		} catch (IOException e) {
			Log.w("cache", "IOException, file: "+fileName, e);
		}
	}

	@Override
	public void setLessons(ViewType view, DateTime startDate, DateTime endDate,
			Map<String, List<Lesson>> lessonMap) {
		DateTime tmpDateTime = startDate.clone();
		while(tmpDateTime.beforeOrEquals(endDate)) {
			setLessons(view, tmpDateTime, lessonMap.get(DateTimeUtils.toISO8601Date(tmpDateTime)));
			tmpDateTime.increaseDay();
		}
	}

	public void setCurrentLoginSetName(String currentLoginSetName) {
		String alphaOnly = getAlphaOnlyString(currentLoginSetName);
		this.currentLoginSetCacheFolder = ABSOLUTE_CACHE_PATH+File.separator+alphaOnly;
	}

	public void loginSetRemoved(String name) {
		String alphaOnly = getAlphaOnlyString(name);
		File loginSetCacheRoot = new File(ABSOLUTE_CACHE_PATH+File.separator+alphaOnly);
		
		if(loginSetCacheRoot.exists()) {
			for(File file : loginSetCacheRoot.listFiles()) {
				file.delete();
			}
		loginSetCacheRoot.delete();
		}
	}
	
	private String getAlphaOnlyString(String string) {
		return string.replaceAll("[^\\p{Alpha}]+","_");
	}

}
