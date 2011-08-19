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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class SelectScreen extends SchoolPlannerActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_screen);
		
		//initSpinner();
		addOnClickListener();
	}
	
	private void initSpinner() {
		Spinner classSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerClass);
		// TODO: Konstanten fuer das Bundle
		DataFacade<List<SchoolClass>> classData = (DataFacade<List<SchoolClass>>) getIntent().getExtras().getSerializable("schoolClassList");
		if(classData.isSuccessful()) {
			initViewTypeSpinner(classSpinner, classData.getData());
		}
		else {
			ImageView imageClass = (ImageView) findViewById(R.id.selectScreen_imageClass);
			imageClass.setEnabled(false);
			classSpinner.setEnabled(false);
		}
		
		Spinner teacherSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerTeacher);
		DataFacade<List<SchoolTeacher>> teacherData = (DataFacade<List<SchoolTeacher>>) getIntent().getExtras().getSerializable("schoolTeacherList");
		if(teacherData.isSuccessful()) {
			initViewTypeSpinner(teacherSpinner, teacherData.getData());
		}
		else {
			teacherSpinner.setEnabled(false);
		}
		
		Spinner roomSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerRoom);
		DataFacade<List<SchoolRoom>> roomData = (DataFacade<List<SchoolRoom>>) getIntent().getExtras().getSerializable("schoolRoomList");
		if(roomData.isSuccessful()) {
			initViewTypeSpinner(roomSpinner, roomData.getData());
		}
		else {
			roomSpinner.setEnabled(false);
		}
		
		Spinner subjectSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerSubject);
		DataFacade<List<SchoolSubject>> subjectData = (DataFacade<List<SchoolSubject>>) getIntent().getExtras().getSerializable("schoolSubjectList");
		if(subjectData.isSuccessful()) {
			initViewTypeSpinner(subjectSpinner, subjectData.getData());
		}
		else {
			subjectSpinner.setEnabled(false);
		}
		
	}
	
	private void initViewTypeSpinner(Spinner spinner, List<? extends ViewType> list) {
		List<String> spinnerList = new ArrayList<String>();
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

		imageClass.setOnClickListener(new AnimatedOnClickListener(getApplicationContext()) {
			@Override
			public void onClick(View v) {
				super.onClick(v);
				Intent chris = new Intent(SelectScreen.this, ViewChris.class);
				startActivity(chris);
				Toast.makeText(SelectScreen.this, "Selected chris", Toast.LENGTH_SHORT).show();
				Log.d("Misc","selected chris' gui ");
			}
		});
		
		imageTeacher.setOnClickListener(new AnimatedOnClickListener(getApplicationContext()) {
			@Override
			public void onClick(View v) {
				super.onClick(v);
				Toast.makeText(SelectScreen.this, "Selected teacher", Toast.LENGTH_SHORT).show();
				Log.d("Misc","selected teacher");
			}
		});
		
		imageRoom.setOnClickListener(new AnimatedOnClickListener(getApplicationContext()) {
			@Override
			public void onClick(View v) {
				super.onClick(v);
				Toast.makeText(SelectScreen.this, "Selected rooms", Toast.LENGTH_SHORT).show();
				Log.d("Misc","selected rooms");
			}
		});
		
		imageSubject.setOnClickListener(new AnimatedOnClickListener(getApplicationContext()) {
			@Override
			public void onClick(View v) {
				super.onClick(v);
				Intent basti = new Intent(SelectScreen.this, ViewBasti.class);
				startActivity(basti);
				Toast.makeText(SelectScreen.this, "Selected basti", Toast.LENGTH_SHORT).show();
				Log.d("Misc","selected bastis gui ");
			}
		});
		
	}
}
