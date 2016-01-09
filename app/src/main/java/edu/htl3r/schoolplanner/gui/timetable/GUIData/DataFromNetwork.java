package edu.htl3r.schoolplanner.gui.timetable.GUIData;

import java.util.List;
import java.util.Map;

import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;

public class DataFromNetwork {

	private Map<String, List<Lesson>> lessons;
	private DateTime lastRefresh;

	public DataFromNetwork(Map<String, List<Lesson>> lessons, DateTime lastRefresh) {
		this.lessons = lessons;
		this.lastRefresh = lastRefresh;
	}

	public Map<String, List<Lesson>> getLessons() {
		return lessons;
	}

	public DateTime getLastRefresh() {
		return lastRefresh;
	}

}
