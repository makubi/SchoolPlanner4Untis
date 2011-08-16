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
import java.util.Calendar;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTest;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTestType;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import edu.htl3r.schoolplanner.gui.elements.SpinnerMultiAdapter;

/**
 * anzeige eines termins
 * 
 * @author Philip Woelfel <philip@woelfel.at>
 * 
 */
public class TestView extends SchoolplannerActivity implements OnItemSelectedListener, OnClickListener {
	private Spinner classesDd;
	private Spinner teachersDd;
	private Spinner roomsDd;
	private Spinner subjectsDd;
	private Spinner typesDd;
	private Button saveButton;
	private DatePicker datePicker;
	private TimePicker startTime;
	private TimePicker endTime;

	private Calendar date;
	private Lesson lesson;

	private ViewType selectedViewType;

	private List<SchoolClass> schoolClassList;
	private List<SchoolTeacher> schoolTeacherList;
	private List<SchoolSubject> schoolSubjectList;
	private List<SchoolRoom> schoolRoomList;
	private List<SchoolTestType> testTypeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (getIntent().hasExtra(ExtrasStrings.VIEWTYPE)) {
			selectedViewType = (ViewType) extras.get(ExtrasStrings.VIEWTYPE);
		}
		if (getIntent().hasExtra(ExtrasStrings.DATE)) {
			date = (Calendar) extras.get(ExtrasStrings.DATE);
		}
		if (getIntent().hasExtra(ExtrasStrings.LESSON)) {
			lesson = (Lesson) extras.get(ExtrasStrings.LESSON);
		}

		startDialogAction(getString(R.string.progress_schoolObjectList_title), getString(R.string.progress_schoolObjectList_text));
	}

	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// do nothing methode! :D

	}

	@Override
	public void onClick(View v) {
		if (v == saveButton) {
			SchoolTest test = new SchoolTest();
			
			Calendar testdate = Calendar.getInstance();
			testdate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
			test.setDate(testdate);
			
			Calendar teststart = Calendar.getInstance();
			teststart.set(Calendar.HOUR_OF_DAY, startTime.getCurrentHour());
			teststart.set(Calendar.MINUTE, startTime.getCurrentMinute());
			test.setStart(teststart);
			Calendar testend = Calendar.getInstance();
			testend.set(Calendar.HOUR_OF_DAY, startTime.getCurrentHour());
			testend.set(Calendar.MINUTE, startTime.getCurrentMinute());
			test.setEnd(testend);
			
			TextView descTv = (TextView) findViewById(R.id.add_date_text);
			test.setDescription(descTv.getText()+"");
			TextView titleTv = (TextView) findViewById(R.id.add_date_title);	
			test.setTitle(titleTv.getText()+"");
			
			ArrayList<SchoolClass> cl = new ArrayList<SchoolClass>();
			ArrayList<Integer> clI = ((SpinnerMultiAdapter)classesDd.getAdapter()).getCheckedIndexes();
			for (Integer i : clI) {
				cl.add(schoolClassList.get(i));
			}
			test.setSchoolClasses(cl);
			
			ArrayList<SchoolTeacher> te = new ArrayList<SchoolTeacher>();
			ArrayList<Integer> clT = ((SpinnerMultiAdapter)teachersDd.getAdapter()).getCheckedIndexes();
			for (Integer i : clT) {
				te.add(schoolTeacherList.get(i));
			}
			test.setSchoolTeachers(te);
			
			ArrayList<SchoolSubject> sub = new ArrayList<SchoolSubject>();
			ArrayList<Integer> clS = ((SpinnerMultiAdapter)subjectsDd.getAdapter()).getCheckedIndexes();
			for (Integer i : clS) {
				sub.add(schoolSubjectList.get(i));
			}
			test.setSchoolSubjects(sub);
			
			ArrayList<SchoolRoom> ro = new ArrayList<SchoolRoom>();
			ArrayList<Integer> clR = ((SpinnerMultiAdapter)roomsDd.getAdapter()).getCheckedIndexes();
			for (Integer i : clR) {
				ro.add(schoolRoomList.get(i));
			}
			test.setSchoolRooms(ro);
			
			ArrayList<SchoolTestType> tt = new ArrayList<SchoolTestType>();
			tt.add(testTypeList.get(typesDd.getSelectedItemPosition()));
			test.setSchoolRooms(ro);
			
			test.setType(testTypeList.get((int) typesDd.getSelectedItemId()));
			
			Log.d("Philip", getClass().getSimpleName() + ": test: " + test);
			
			app.getData().saveSchoolTest(test);
			
			setResult(RESULT_OK);
			finish();
		}

	}

	@Override
	public void run() {
		try {
			Log.d("Philip", "schooclass");
			schoolClassList = app.getData().getSchoolClassList();
			Log.d("Philip", "teacher");
			schoolTeacherList = app.getData().getSchoolTeacherList();
			Log.d("Philip", "subject");
			schoolSubjectList = app.getData().getSchoolSubjectList();
			Log.d("Philip", "room");
			schoolRoomList = app.getData().getSchoolRoomList();
			Log.d("Philip", "testype");
			testTypeList = app.getData().getSchoolTestTypeList();
			Log.d("Philip", "done");
		} catch (IOException e) {
			sendMessageToHandler(getString(R.string.error_laoddata));
			e.printStackTrace();
		}
		handler.sendEmptyMessage(0);
	}
	
	/**
	 * zeigt das Layout an
	 */
	private void createLayout() {
		//Log.d("Philip", "listen: " + schoolClassList + " " + schoolTeacherList + " " + schoolSubjectList + " " + schoolRoomList + " " + testTypeList);
		if (schoolClassList == null || schoolTeacherList == null || schoolSubjectList == null || schoolRoomList == null || testTypeList == null) {
			// iwas is null --> fehler
			TextView tv = new TextView(this);
			tv.setText(getString(R.string.error_laoddata));
			tv.setTextColor(Color.RED);
			setContentView(tv);
			Log.d("Philip", "fehler initalisierung");
		}
		else {
			setContentView(R.layout.add_test);

			
			// FIXME: leerelement weil wenn zb ueber menubutton neuer test werden falsche daten (erstes element in der liste) angezeigt
			
			
			typesDd = (Spinner) findViewById(R.id.add_date_type);
			initializeTestTypeAdapter(typesDd, testTypeList, this, false);

			classesDd = (Spinner) findViewById(R.id.add_date_class);
			initializeMultiAdapter(classesDd, schoolClassList, this, false);

			teachersDd = (Spinner) findViewById(R.id.add_date_teacher);
			initializeMultiAdapter(teachersDd, schoolTeacherList, this, false);

			roomsDd = (Spinner) findViewById(R.id.add_date_room);
			initializeMultiAdapter(roomsDd, schoolRoomList, this, false);

			subjectsDd = (Spinner) findViewById(R.id.add_date_subject);
			initializeMultiAdapter(subjectsDd, schoolSubjectList, this, false);

			saveButton = (Button) findViewById(R.id.add_date_save);
			saveButton.setOnClickListener(this);

			datePicker = (DatePicker) findViewById(R.id.add_date_date);
			startTime = (TimePicker) findViewById(R.id.add_date_starttime);
			startTime.setIs24HourView(true);
			endTime = (TimePicker) findViewById(R.id.add_date_endtime);
			endTime.setIs24HourView(true);

			if (lesson == null) {
				if (selectedViewType != null) {
					if (selectedViewType instanceof SchoolClass) {
						selectSpinnerByValue(classesDd, selectedViewType.getName());
					}
					else if (selectedViewType instanceof SchoolRoom) {
						selectSpinnerByValue(roomsDd, selectedViewType.getName());
					}
					else if (selectedViewType instanceof SchoolClass) {
						selectSpinnerByValue(subjectsDd, selectedViewType.getName());
					}
					else if (selectedViewType instanceof SchoolClass) {
						selectSpinnerByValue(teachersDd, selectedViewType.getName());
					}
				}
				if (date != null) {
					datePicker.init(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), null);
				}
			}
			else {
				if (date != null) {
					selectData();
				}
			}
		}

	}
	
	private void checkMultiSpinner(Spinner spin, List<? extends ViewType> list){
		SpinnerMultiAdapter madap = (SpinnerMultiAdapter) spin.getAdapter();
		for (ViewType viewType : list) {
			madap.setChecked(viewType.getName(), true);
		}
	}

	/**
	 * waehlt die, der Activity mitgegebenen, Werte bei den einzelnen Elementen aus
	 */
	private void selectData() {
		Log.d("Philip", "selecting data");
		// selectSpinnerByValue(classesDd, lesson.getSchoolClasses().get(0).getName());
		// selectSpinnerByValue(subjectsDd, lesson.getSchoolSubjects().get(0).getName());
		// selectSpinnerByValue(roomsDd, lesson.getSchoolRooms().get(0).getName());
		// selectSpinnerByValue(teachersDd, lesson.getSchoolTeachers().get(0).getName());
		if(lesson.getSchoolClasses().size()>0){
			selectSpinnerByValue(classesDd, lesson.getSchoolClasses().get(0).getName());
			checkMultiSpinner(classesDd, lesson.getSchoolClasses());
		}
		if(lesson.getSchoolSubjects().size()>0){
			selectSpinnerByValue(subjectsDd, lesson.getSchoolSubjects().get(0).getName());
			checkMultiSpinner(subjectsDd, lesson.getSchoolSubjects());
		}
		if(lesson.getSchoolRooms().size()>0){
			selectSpinnerByValue(roomsDd, lesson.getSchoolRooms().get(0).getName());
			checkMultiSpinner(roomsDd, lesson.getSchoolRooms());
		}
		if(lesson.getSchoolTeachers().size()>0){
			selectSpinnerByValue(teachersDd, lesson.getSchoolTeachers().get(0).getName());
			checkMultiSpinner(teachersDd, lesson.getSchoolTeachers());
		}
		if (lesson.getDate() != null) {
			datePicker.init(lesson.getDate().get(Calendar.YEAR), lesson.getDate().get(Calendar.MONTH), lesson.getDate().get(Calendar.DAY_OF_MONTH), null);
		}
		if (lesson.getStartTime() != null) {
			Log.d("Philip", "startime: " + lesson.getStartTime().get(Calendar.HOUR_OF_DAY) + ":" + lesson.getStartTime().get(Calendar.MINUTE));
			startTime.setCurrentHour(lesson.getStartTime().get(Calendar.HOUR_OF_DAY));
			startTime.setCurrentMinute(lesson.getStartTime().get(Calendar.MINUTE));
		}
		if (lesson.getEndTime() != null) {
			Log.d("Philip", "endtime: " + lesson.getEndTime().get(Calendar.HOUR_OF_DAY) + ":" + lesson.getEndTime().get(Calendar.MINUTE));
			endTime.setCurrentHour(lesson.getEndTime().get(Calendar.HOUR_OF_DAY));
			endTime.setCurrentMinute(lesson.getEndTime().get(Calendar.MINUTE));
		}
	}


}
