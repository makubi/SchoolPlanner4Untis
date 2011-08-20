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

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import edu.htl3r.schoolplanner.gui.basti.ViewBasti;
import edu.htl3r.schoolplanner.gui.chris.ViewChris;
import edu.htl3r.schoolplanner.gui.selectScreen.AnimatedOnClickListener;
import edu.htl3r.schoolplanner.gui.selectScreen.ViewTypeOnClickListener;
import edu.htl3r.schoolplanner.gui.selectScreen.ViewTypeSpinnerOnItemSelectedListener;

public class SelectScreen extends SchoolPlannerActivity{
	
	private List<SchoolClass> classList;
	private List<SchoolTeacher> teacherList;
	private List<SchoolRoom> roomList;
	private List<SchoolSubject> subjectList;
	
	/** Wird benoetigt, damit die Listener der Spinner nicht auf die voreingestellte Auswahl reagiern und die Aktionen der Methode {@link OnItemSelectedListener#onItemSelected(android.widget.AdapterView, View, int, long)} nur ausgefuehrt werden, wenn der Benutzer eine aktion setzt. */
	private List<ViewTypeSpinnerOnItemSelectedListener> viewTypeSpinnerOnItemSelectedListeners = new ArrayList<ViewTypeSpinnerOnItemSelectedListener>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_screen);
		
		initSpinner();
		addOnClickListener();
	}
	
	private void initSpinner() {
		Spinner classSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerClass);
		// TODO: Konstanten fuer das Bundle
		DataFacade<List<SchoolClass>> classData = (DataFacade<List<SchoolClass>>) getIntent().getExtras().getSerializable("schoolClassList");
		if(classData.isSuccessful()) {
			classList = classData.getData();
			initViewTypeSpinner(classSpinner, classList);
		}
		else {
			ImageView imageClass = (ImageView) findViewById(R.id.selectScreen_imageClass);
			imageClass.setEnabled(false);
			classSpinner.setEnabled(false);
		}
		
		Spinner teacherSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerTeacher);
		DataFacade<List<SchoolTeacher>> teacherData = (DataFacade<List<SchoolTeacher>>) getIntent().getExtras().getSerializable("schoolTeacherList");
		if(teacherData.isSuccessful()) {
			teacherList = teacherData.getData();
			initViewTypeSpinner(teacherSpinner, teacherList);
		}
		else {
			ImageView imageClass = (ImageView) findViewById(R.id.selectScreen_imageTeacher);
			imageClass.setEnabled(false);
			teacherSpinner.setEnabled(false);
		}
		
		Spinner roomSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerRoom);
		DataFacade<List<SchoolRoom>> roomData = (DataFacade<List<SchoolRoom>>) getIntent().getExtras().getSerializable("schoolRoomList");
		if(roomData.isSuccessful()) {
			roomList = roomData.getData();
			initViewTypeSpinner(roomSpinner, roomList);
		}
		else {
			ImageView imageClass = (ImageView) findViewById(R.id.selectScreen_imageRoom);
			imageClass.setEnabled(false);
			roomSpinner.setEnabled(false);
		}
		
		Spinner subjectSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerSubject);
		DataFacade<List<SchoolSubject>> subjectData = (DataFacade<List<SchoolSubject>>) getIntent().getExtras().getSerializable("schoolSubjectList");
		if(subjectData.isSuccessful()) {
			subjectList = subjectData.getData();
			initViewTypeSpinner(subjectSpinner, subjectList);
		}
		else {
			ImageView imageClass = (ImageView) findViewById(R.id.selectScreen_imageSubject);
			imageClass.setEnabled(false);
			subjectSpinner.setEnabled(false);
		}
		
		addViewTypeSpinnerOnItemSelectedListener(classSpinner, new Intent(), classList);
		addViewTypeSpinnerOnItemSelectedListener(teacherSpinner, new Intent(), teacherList);
		addViewTypeSpinnerOnItemSelectedListener(roomSpinner, new Intent(), roomList);
		addViewTypeSpinnerOnItemSelectedListener(subjectSpinner, new Intent(), subjectList);
	}
	
	private void addViewTypeSpinnerOnItemSelectedListener(Spinner spinner, Intent intent, List<? extends ViewType> list) {
		ViewTypeSpinnerOnItemSelectedListener listener = new ViewTypeSpinnerOnItemSelectedListener(this, intent, list);
		viewTypeSpinnerOnItemSelectedListeners.add(listener);
		spinner.setOnItemSelectedListener(listener);		
	}
	
	private void initViewTypeSpinner(Spinner spinner, List<? extends ViewType> list) {
		List<String> spinnerList = new ArrayList<String>();
		// Leeritem hinzufuegen, damit bei onCreate nicht direkt onItemSelected von Spinner ausgeloest wird
		spinnerList.add("");
		for(ViewType schoolRoom : list) {
			spinnerList.add(schoolRoom.getName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	private void addOnClickListener() {
		ImageView imageClass = (ImageView) findViewById(R.id.selectScreen_imageClass);
		ImageView imageTeacher = (ImageView) findViewById(R.id.selectScreen_imageTeacher);
		ImageView imageRoom = (ImageView) findViewById(R.id.selectScreen_imageRoom);
		ImageView imageSubject = (ImageView) findViewById(R.id.selectScreen_imageSubject);

		
		Intent chris = new Intent(SelectScreen.this, ViewChris.class);
		imageClass.setOnClickListener(new ViewTypeOnClickListener(this,chris));
		
		// TODO: umstellung auf ViewTypeOnClickListener
		imageTeacher.setOnClickListener(new AnimatedOnClickListener(getApplicationContext()) {
			@Override
			public void onClick(View v) {
				super.onClick(v);
				Toast.makeText(SelectScreen.this, "Selected teacher", Toast.LENGTH_SHORT).show();
				Log.d("Misc","selected teacher");
			}
		});
		
		// TODO: umstellung auf ViewTypeOnClickListener
		imageRoom.setOnClickListener(new AnimatedOnClickListener(getApplicationContext()) {
			@Override
			public void onClick(View v) {
				super.onClick(v);
				Toast.makeText(SelectScreen.this, "Selected rooms", Toast.LENGTH_SHORT).show();
				Log.d("Misc","selected rooms");
			}
		});
		
		Intent basti = new Intent(SelectScreen.this, ViewBasti.class);
		imageSubject.setOnClickListener(new ViewTypeOnClickListener(this, basti));
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		//ignoreFirstActionOnListeners();
		Log.d("Misc","called");
	}
	
	/**
	 * Hack to stop application from selecting automatically item by non-user action (e.g. on create or on rotate).
	 */
	private void ignoreFirstActionOnListeners() {
		for(ViewTypeSpinnerOnItemSelectedListener listener : viewTypeSpinnerOnItemSelectedListeners) {
			listener.setSkipNextAction(true);
		}
	}
	
}
