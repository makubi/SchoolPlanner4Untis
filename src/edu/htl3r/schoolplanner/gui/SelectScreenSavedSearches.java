package edu.htl3r.schoolplanner.gui;

import android.content.SearchRecentSuggestionsProvider;

public class SelectScreenSavedSearches extends SearchRecentSuggestionsProvider {
	public final static String AUTHORITY = "edu.htl3r.schoolplanner.gui.SelectScreenSavedSearches";
	public final static int MODE = DATABASE_MODE_QUERIES;

	public SelectScreenSavedSearches() {
		setupSuggestions(AUTHORITY, MODE);
	}
}
