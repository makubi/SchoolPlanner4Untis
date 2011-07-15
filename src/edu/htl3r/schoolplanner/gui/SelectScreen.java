/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
						Gerald Schreiber <mail@gerald-schreiber.at>
						Philip Woelfel <philip@woelfel.at>
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class SelectScreen extends SchoolplannerActivity implements OnItemSelectedListener, Runnable, OnClickListener, OnCancelListener {
	private Spinner classesDd;
	
	private Spinner teachersDd;
	
	private Spinner roomsDd;
	
	private Spinner subjectsDd;
	
	
	private Button show;

	private List<SchoolClass> schoolClassList;
	private List<SchoolTeacher> schoolTeacherList;
	private List<SchoolSubject> schoolSubjectList;
	private List<SchoolRoom> schoolRoomList;
	private boolean canceled = false;
	private boolean resync_data = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (!canceled) {
					dismissDialog(SchoolplannerActivity.DIALOG_PROGRESS_CANCELABLE);
				}
				switch (msg.what) {
					case 0:
						createLayout();
						break;
				}
			};
		};
		if (getIntent().hasExtra(ExtrasStrings.RESYNC_DATA) && getIntent().getExtras().getBoolean(ExtrasStrings.RESYNC_DATA)) {
			resync_data = true;
		}

		startFillThread();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (classesDd != null) {
			classesDd.setSelection(0);
		}
		if (teachersDd != null) {
			teachersDd.setSelection(0);
		}
		if (roomsDd != null) {
			roomsDd.setSelection(0);
		}
		if (subjectsDd != null) {
			subjectsDd.setSelection(0);
		}
		if (prefs.getType()!=0 && !prefs.getSelection().equals("") && show!=null) {
			show.setEnabled(true);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MENU_SETTINGS && resultCode == RESULT_OK && data != null) {
			if (data.hasExtra(ExtrasStrings.UPDATESELSCR)) {
				startFillThread();
			}
			if (data.hasExtra(ExtrasStrings.UPDATESELSCRBTN)) {
				show.setEnabled(true);
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		String clas = (String) parent.getSelectedItem();
		if (!clas.equals("")) {
			Intent myIntent = new Intent(this, app.getCurrentView());
			if (parent == classesDd) {
				myIntent.putExtra(ExtrasStrings.VIEWTYPE, schoolClassList.get(position - 1));
			}
			else if (parent == teachersDd) {
				myIntent.putExtra(ExtrasStrings.VIEWTYPE, schoolTeacherList.get(position - 1));
			}
			else if (parent == subjectsDd) {
				myIntent.putExtra(ExtrasStrings.VIEWTYPE, schoolSubjectList.get(position - 1));
			}
			else if (parent == roomsDd) {
				myIntent.putExtra(ExtrasStrings.VIEWTYPE, schoolRoomList.get(position - 1));
			}
			startActivity(myIntent);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// Do nothing methode

	}

	@Override
	public void run() {
		if (resync_data) {
			try {
				app.getData().resyncMasterData();
			} catch (IOException e) {
				bitteToasten("Exception ("+getClass().getSimpleName()+") at resync: "+e.getMessage(), Toast.LENGTH_LONG);
				Log.w("Network", e.getMessage(),e);
			}
		}
		try {
			schoolClassList = app.getData().getSchoolClassList();
			schoolTeacherList = app.getData().getSchoolTeacherList();
			schoolSubjectList = app.getData().getSchoolSubjectList();
			schoolRoomList = app.getData().getSchoolRoomList();
		} catch (IOException e) {
			bitteToasten("Exception ("+getClass().getSimpleName()+") at calling new lists: "+e.getMessage(), Toast.LENGTH_LONG);
			Log.w("Network", e.getMessage(),e);
		}

		Log.d("Philip", "nachlisten");
		handler.sendEmptyMessage(0);
	}

	/**
	 * zeigt das Layout an
	 */
	private void createLayout() {
		if (schoolClassList == null && schoolTeacherList == null && schoolSubjectList == null && schoolRoomList == null) {
			// iwas is null --> fehler
			TextView tv = new TextView(getApplicationContext());
			tv.setText(getString(R.string.error_laoddata));
			tv.setTextColor(Color.RED);
			setContentView(tv);
			Log.w("Philip", getClass().getSimpleName() + ": alle Listen sind null");
		}
		else {
			setContentView(R.layout.selection);

			classesDd = (Spinner) findViewById(R.id.classesDd);
			if (schoolClassList != null && schoolClassList.size() > 0) {
				initializeAdapter(classesDd, schoolClassList, this, true);
			}
			else {
				classesDd.setEnabled(false);
				
				List<String> emptyMessage = new ArrayList<String>();
				emptyMessage.add("No list elements available.");
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, emptyMessage);
				classesDd.setAdapter(adapter);
			}

			teachersDd = (Spinner) findViewById(R.id.teachersDd);
			if (schoolTeacherList != null && schoolTeacherList.size() > 0) {
				initializeAdapter(teachersDd, schoolTeacherList, this, true);
			}
			else {
				teachersDd.setEnabled(false);
			
				List<String> emptyMessage = new ArrayList<String>();
				emptyMessage.add("No list elements available.");
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, emptyMessage);
				teachersDd.setAdapter(adapter);
			}

			roomsDd = (Spinner) findViewById(R.id.roomsDd);
			if (schoolRoomList != null && schoolRoomList.size() > 0) {
				initializeAdapter(roomsDd, schoolRoomList, this, true);
			}
			else {
				roomsDd.setEnabled(false);
				
				List<String> emptyMessage = new ArrayList<String>();
				emptyMessage.add("No list elements available.");
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, emptyMessage);
				roomsDd.setAdapter(adapter);
			}

			subjectsDd = (Spinner) findViewById(R.id.subjectsDd);
			if (schoolSubjectList != null && schoolSubjectList.size() > 0) {
				initializeAdapter(subjectsDd, schoolSubjectList, this, true);
			}
			else {
				subjectsDd.setEnabled(false);
				
				List<String> emptyMessage = new ArrayList<String>();
				emptyMessage.add("No list elements available.");
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, emptyMessage);
				subjectsDd.setAdapter(adapter);
			}

			show = (Button) findViewById(R.id.showButton);
			show.setOnClickListener(this);
			show.setText(R.string.show_choosen_timetable);
			if (prefs.getType() == 0 || prefs.getSelection().equals("")) {
				show.setEnabled(false);
			}

			if (prefs.isAutochoose()) {
				autoChoose();
			}
		}

	}

	/**
	 * startet das Laden und Anzeigen der Listen
	 */
	public void startFillThread() {
		canceled = false;
		startDialogAction(getString(R.string.progress_schoolObjectList_title), getString(R.string.progress_schoolObjectList_text), this);
	}

	private void autoChoose() {
		switch (prefs.getType()) {
			case ViewType.SCHOOLCLASS:
				selectSpinnerByValue(classesDd, prefs.getSelection());
				break;
			case ViewType.SCHOOLROOM:
				selectSpinnerByValue(roomsDd, prefs.getSelection());
				break;
			case ViewType.SCHOOLTEACHER:
				selectSpinnerByValue(teachersDd, prefs.getSelection());
				break;
			case ViewType.SCHOOLSUBJECT:
				selectSpinnerByValue(subjectsDd, prefs.getSelection());
				break;
			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == show) {
			autoChoose();
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		canceled = true;
		finish();

	}

}
