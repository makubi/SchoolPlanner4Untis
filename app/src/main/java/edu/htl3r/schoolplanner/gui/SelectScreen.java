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
package edu.htl3r.schoolplanner.gui;

import java.util.ArrayList;
import java.util.List;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.AutoSelectHandler;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.preferences.AutoSelectSet;
import edu.htl3r.schoolplanner.backend.preferences.SettingsConstants;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import edu.htl3r.schoolplanner.gui.selectScreen.AutoselectDialog;
import edu.htl3r.schoolplanner.gui.selectScreen.SelectScreenInstanceBundle;
import edu.htl3r.schoolplanner.gui.selectScreen.SpinnerMemory;
import edu.htl3r.schoolplanner.gui.selectScreen.ViewTypeOnClickListener;
import edu.htl3r.schoolplanner.gui.selectScreen.ViewTypeSpinnerOnItemSelectedListener;
import edu.htl3r.schoolplanner.gui.timetable.WeekView;

public class SelectScreen extends SchoolPlannerActivity {

	private List<SchoolClass> classList;
	private List<SchoolTeacher> teacherList;
	private List<SchoolRoom> roomList;
	private List<SchoolSubject> subjectList;

	private Spinner classSpinner;
	private Spinner teacherSpinner;
	private Spinner roomSpinner;
	private Spinner subjectSpinner;

	private SpinnerMemory spinnerMemory = new SpinnerMemory();
	
	private DataFacade<List<SchoolClass>> classData;
	private DataFacade<List<SchoolTeacher>> teacherData;
	private DataFacade<List<SchoolRoom>> roomData;
	private DataFacade<List<SchoolSubject>> subjectData;
	
	private boolean autoSelectDone = false;
	
	private boolean loadingTimetable = false;
	
	private boolean listsAvailable = false;
	
	private AutoSelectHandler cache;
	
	private AutoselectDialog autoselectDialog;
	private AutoSelectSet autoSelect;
	
	private ViewTypeSpinnerOnItemSelectedListener classSpinnerOnItemSelectedListener;
	private ViewTypeSpinnerOnItemSelectedListener teacherSpinnerOnItemSelectedListener;
	private ViewTypeSpinnerOnItemSelectedListener roomSpinnerOnItemSelectedListener;
	private ViewTypeSpinnerOnItemSelectedListener subjectSpinnerOnItemSelectedListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_screen);
		
		cache = ((SchoolPlannerApp) getApplication()).getData();
		
		initSpinner();
		checkSearch(getIntent());		
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		handleAutoSelect();
	}
	
	private void handleAutoSelect() {
		autoSelect = cache.getAutoSelect();
		initAutoSelect();
		
		// Waehle automatisch Stundenplan
		if(!autoSelectDone) {
			autoSelectDone = true;
			String autoSelectType = autoSelect.getAutoSelectType();
			int autoSelectValue = autoSelect.getAutoSelectValue();
			
			if(autoSelect.isEnabled() && autoSelectType.length() > 0 && autoSelectValue >= 0) {
				if(autoSelectType.equals(SettingsConstants.AUTOSELECT_TYPE_CLASS)) {
					if(viewTypeExistsInList(autoSelectValue, classList)) classSpinnerOnItemSelectedListener.fireEventByIdAndDontRemember(autoSelectValue);
				}
				else if(autoSelectType.equals(SettingsConstants.AUTOSELECT_TYPE_TEACHER)) {
					if(viewTypeExistsInList(autoSelectValue, teacherList)) teacherSpinnerOnItemSelectedListener.fireEventByIdAndDontRemember(autoSelectValue);
				}
				else if(autoSelectType.equals(SettingsConstants.AUTOSELECT_TYPE_ROOM)) {
					if(viewTypeExistsInList(autoSelectValue, roomList)) roomSpinnerOnItemSelectedListener.fireEventByIdAndDontRemember(autoSelectValue);
				}
				else if(autoSelectType.equals(SettingsConstants.AUTOSELECT_TYPE_SUBJECT)) {
					if(viewTypeExistsInList(autoSelectValue, subjectList)) subjectSpinnerOnItemSelectedListener.fireEventByIdAndDontRemember(autoSelectValue);
				}
			}
		}
	}

	private void initAutoSelect() {
		autoselectDialog = new AutoselectDialog(this, classList, teacherList, roomList, subjectList);
		autoselectDialog.setAutoSelectSet(autoSelect);
	}
	
	public void setAutoSelect(AutoSelectSet autoSelectSet) {
		cache.setAutoSelect(autoSelectSet);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		checkSearch(intent);
	}
	
	private void checkSearch(Intent intent){
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      Log.d("basti", "Search String: " + query);
	      
	      SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SelectScreenSavedSearches.AUTHORITY, SelectScreenSavedSearches.MODE);	 
	      suggestions.saveRecentQuery(query, null);
	      
	      if(searchForSearchResults(classSpinner, query))return;
	      if(searchForSearchResults(teacherSpinner, query))return;
	      if(searchForSearchResults(roomSpinner, query))return;
	      if(searchForSearchResults(subjectSpinner, query))return;
	      
	      Toast.makeText(this, getResources().getString(R.string.selectscreen_search_fail), Toast.LENGTH_SHORT).show();
	    }
	}
	
	private boolean searchForSearchResults(Spinner spinner, String query){
	      ArrayAdapter<String> spinneradapter = (ArrayAdapter<String>) spinner.getAdapter();
		  ViewTypeSpinnerOnItemSelectedListener onItemClickListener = (ViewTypeSpinnerOnItemSelectedListener) spinner.getOnItemSelectedListener();
		  
	      for(int i=0; i<spinneradapter.getCount(); i++){
	    	  String sname = onItemClickListener.getViewType(i).getName();
	    	  String lname = onItemClickListener.getViewType(i).getLongName();
	    	  if(sname.equalsIgnoreCase(query)){
	    		  Log.d("basti", "result: " + sname);
	    		  onItemClickListener.fireEvent(i);
	    		  return true;
	    	  }
	    	  if(query.length() > 4 && lname.toLowerCase().contains(query.toLowerCase())){
	    		  Log.d("basti", "result: " + lname);
	    		  onItemClickListener.fireEvent(i);
	    		  return true;
	    	  }
	      }
	      return false;
	}

	private void retrieveSpinnerData() {
		retrieveSavedBundle();
		Bundle extras = getIntent().getExtras();
		if (classData == null) classData = (DataFacade<List<SchoolClass>>) extras.getSerializable(BundleConstants.SCHOOL_CLASS_LIST);
		if (teacherData == null) teacherData = (DataFacade<List<SchoolTeacher>>) extras.getSerializable(BundleConstants.SCHOOL_TEACHER_LIST);
		if(roomData == null) roomData = (DataFacade<List<SchoolRoom>>) extras.getSerializable(BundleConstants.SCHOOL_ROOM_LIST);
		if(subjectData == null) subjectData = (DataFacade<List<SchoolSubject>>) extras.getSerializable(BundleConstants.SCHOOL_SUBJECT_LIST);
	}

	private void retrieveSavedBundle() {
		final SelectScreenInstanceBundle bundle = (SelectScreenInstanceBundle) getLastNonConfigurationInstance();
		if(bundle != null) {
			classData = bundle.getClassData();
			teacherData = bundle.getTeacherData();
			roomData = bundle.getRoomData();
			subjectData = bundle.getSubjectData();
			autoSelectDone = bundle.isAutoSelectDone();
		}
	}

	private void initSpinner() {
		retrieveSpinnerData();
		classSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerClass);
		
		// Initialisiere Spinner und Images
		if (classData.isSuccessful()) {
			classList = classData.getData();
			initViewTypeSpinner(classSpinner, classList);
		} else {
			ImageView imageClass = (ImageView) findViewById(R.id.selectScreen_imageClass);
			setListImageNotAvailable(imageClass);
			classSpinner.setEnabled(false);
		}

		teacherSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerTeacher);
		
		if (teacherData.isSuccessful()) {
			teacherList = teacherData.getData();
			initViewTypeSpinner(teacherSpinner, teacherList);
		} else {
			ImageView imageTeacher = (ImageView) findViewById(R.id.selectScreen_imageTeacher);
			setListImageNotAvailable(imageTeacher);
			teacherSpinner.setEnabled(false);
		}

		roomSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerRoom);
		
		if (roomData.isSuccessful()) {
			roomList = roomData.getData();
			initViewTypeSpinner(roomSpinner, roomList);
		} else {
			ImageView imageRoom = (ImageView) findViewById(R.id.selectScreen_imageRoom);
			setListImageNotAvailable(imageRoom);
			roomSpinner.setEnabled(false);
		}

		subjectSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerSubject);
		
		if (subjectData.isSuccessful()) {
			subjectList = subjectData.getData();
			initViewTypeSpinner(subjectSpinner, subjectList);
		} else {
			ImageView imageSubject = (ImageView) findViewById(R.id.selectScreen_imageSubject);
			setListImageNotAvailable(imageSubject);
			subjectSpinner.setEnabled(false);
		}

		// Ueberprufe, ob alle Listen verfuegbar sind
		if (classData.isSuccessful() && teacherData.isSuccessful() && roomData.isSuccessful() && subjectData.isSuccessful()) {
			listsAvailable = true;
			
			// Initialisiere OnItemSelectListener der Spinner
			Intent classIntent = new Intent(SelectScreen.this, WeekView.class);
			classSpinnerOnItemSelectedListener = new ViewTypeSpinnerOnItemSelectedListener(this, classIntent, classList, spinnerMemory);
			classSpinner.setOnItemSelectedListener(classSpinnerOnItemSelectedListener);

			Intent teacherIntent = new Intent(SelectScreen.this, WeekView.class);
			teacherSpinnerOnItemSelectedListener = new ViewTypeSpinnerOnItemSelectedListener(this, teacherIntent, teacherList, spinnerMemory);
			teacherSpinner.setOnItemSelectedListener(teacherSpinnerOnItemSelectedListener);

			Intent roomIntent = new Intent(SelectScreen.this, WeekView.class);
			roomSpinnerOnItemSelectedListener = new ViewTypeSpinnerOnItemSelectedListener(this, roomIntent, roomList, spinnerMemory);
			roomSpinner.setOnItemSelectedListener(roomSpinnerOnItemSelectedListener);

			Intent subjectIntent = new Intent(SelectScreen.this, WeekView.class);
			subjectSpinnerOnItemSelectedListener = new ViewTypeSpinnerOnItemSelectedListener(this, subjectIntent, subjectList, spinnerMemory);
			subjectSpinner.setOnItemSelectedListener(subjectSpinnerOnItemSelectedListener);

			// Setze die zuletzt ausgewaehlten Positionen der Spinner
			int classSpinnerLastPos = getPositionForItem(classSpinner, spinnerMemory.getClassListLastElement());
			if (classSpinnerLastPos > -1) {
				classSpinner.setSelection(classSpinnerLastPos);
			}

			int teacherSpinnerLastPos = getPositionForItem(teacherSpinner, spinnerMemory.getTeacherListLastElement());
			if (teacherSpinnerLastPos > -1) {
				teacherSpinner.setSelection(teacherSpinnerLastPos);
			}

			int roomSpinnerLastPos = getPositionForItem(roomSpinner, spinnerMemory.getRoomListLastElement());
			if (roomSpinnerLastPos > -1) {
				roomSpinner.setSelection(roomSpinnerLastPos);
			}

			int subjectSpinnerLastPos = getPositionForItem(subjectSpinner, spinnerMemory.getSubjectListLastElement());
			if (subjectSpinnerLastPos > -1) {
				subjectSpinner.setSelection(subjectSpinnerLastPos);
			}
			addImageOnClickListener(true);
		} else {
			listsAvailable = false;
			
			classSpinner.setEnabled(false);
			teacherSpinner.setEnabled(false);
			roomSpinner.setEnabled(false);
			subjectSpinner.setEnabled(false);

			addImageOnClickListener(false);
		}
	}
	
	
	private boolean viewTypeExistsInList(int viewTypeId, List<? extends ViewType> list) {
		for(ViewType viewType : list) {
			if(viewType.getId() == viewTypeId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		final SelectScreenInstanceBundle bundle = new SelectScreenInstanceBundle();
		bundle.setClassData(classData);
		bundle.setTeacherData(teacherData);
		bundle.setRoomData(roomData);
		bundle.setSubjectData(subjectData);
		bundle.setAutoSelectDone(autoSelectDone);
		
		return bundle;
	}

	private int getPositionForItem(Spinner spinner, String item) {
		return getPositionForItem(spinner.getAdapter(), item);
	}

	private int getPositionForItem(Adapter adapter, String item) {
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).equals(item)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Verwende {@link ViewType#getName()} fuer Adapter.
	 * 
	 * @param spinner
	 * @param list
	 * @see SpinnerMemory#setSelectedViewType(ViewType)
	 */
	private void initViewTypeSpinner(Spinner spinner, List<? extends ViewType> list) {
		List<String> spinnerList = new ArrayList<String>();
		for (ViewType schoolRoom : list) {
			spinnerList.add(schoolRoom.getName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	private void setListImageNotAvailable(ImageView imageView) {
		Drawable[] layers = new Drawable[] { toGrayscale(imageView.getDrawable()), getResources().getDrawable(R.drawable.ic_not) };
		imageView.setImageDrawable(new LayerDrawable(layers));
		imageView.setEnabled(false);
	}

	private Drawable toGrayscale(Drawable original) {
		return new BitmapDrawable(toGrayscale(((BitmapDrawable) original).getBitmap()));
	}

	private Bitmap toGrayscale(Bitmap bmpOriginal) {
		Bitmap bmpGrayscale = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	private void addImageOnClickListener(boolean enabled) {

		ImageView imageClass = (ImageView) findViewById(R.id.selectScreen_imageClass);
		ImageView imageTeacher = (ImageView) findViewById(R.id.selectScreen_imageTeacher);
		ImageView imageRoom = (ImageView) findViewById(R.id.selectScreen_imageRoom);
		ImageView imageSubject = (ImageView) findViewById(R.id.selectScreen_imageSubject);

		if (enabled) {
			Intent basti4 = new Intent(SelectScreen.this, WeekView.class);
			imageClass.setOnClickListener(new ViewTypeOnClickListener(this, basti4, classList, classSpinner));

			Intent basti = new Intent(SelectScreen.this, WeekView.class);
			imageSubject.setOnClickListener(new ViewTypeOnClickListener(this, basti, subjectList, subjectSpinner));

			Intent basti2 = new Intent(SelectScreen.this, WeekView.class);
			imageRoom.setOnClickListener(new ViewTypeOnClickListener(this, basti2, roomList, roomSpinner));

			Intent basti3 = new Intent(SelectScreen.this, WeekView.class);
			imageTeacher.setOnClickListener(new ViewTypeOnClickListener(this, basti3, teacherList, teacherSpinner));
		} else {
			OnClickListener unableToDisplayTimetableOnClickListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					showToastMessage(getString(R.string.view_type_list_missing));
				}
			};

			imageClass.setOnClickListener(unableToDisplayTimetableOnClickListener);
			imageTeacher.setOnClickListener(unableToDisplayTimetableOnClickListener);
			imageRoom.setOnClickListener(unableToDisplayTimetableOnClickListener);
			imageSubject.setOnClickListener(unableToDisplayTimetableOnClickListener);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setInProgress("", false);
		loadingTimetable = false;
	}
	
	@Override
	public void setInProgress(String message, boolean active) {
		super.setInProgress(message, active);
		active = true;
	}

	/**
	 * Gibt 'true' zurueck, wenn das Laden des Stundenplans aktiv ist, ansonsten 'false'.
	 * @return 'true', wenn das Laden des Stundeplans aktiv ist
	 */
	public boolean isLoadingTimetable() {
		return loadingTimetable;
	}

	@Override
	public boolean onSearchRequested() {
		
		return super.onSearchRequested();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.selectscreen_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.selectscreen_menu_search:
			onSearchRequested();
			return true;
		case R.id.selectscreen_menu_autoselect:
			autoselectDialog.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem autoselectMenuItem = menu.findItem(R.id.selectscreen_menu_autoselect);
		autoselectMenuItem.setEnabled(listsAvailable);
		return super.onPrepareOptionsMenu(menu);
	}
	
}
