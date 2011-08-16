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

package edu.htl3r.schoolplanner.gui.timetableviews;

import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.Lesson;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonCode;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.LessonType;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeCancelled;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonCode.LessonCodeSubstitute;
import edu.htl3r.schoolplanner.backend.schoolObjects.lesson.lessonType.LessonTypeStandby;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import edu.htl3r.schoolplanner.gui.ExtrasStrings;
import edu.htl3r.schoolplanner.gui.SchoolplannerActivity;
import edu.htl3r.schoolplanner.gui.TestView;

public class LessonView extends SchoolplannerActivity implements OnClickListener {
	private Lesson lesson;
	private final int NEW_TEST=1356; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lesson);
		
		if (getIntent().hasExtra("lesson")) {
			lesson = (Lesson) getIntent().getExtras().get("lesson");
			// TextView alles = (TextView) findViewById(R.id.lesson_ganz);
			// alles.setText(lesson.toString());
			// Log.d("Philip", getClass().getSimpleName() + ": "+lesson);
			Calendar date = lesson.getDate();
			Calendar startDate = lesson.getStartTime();
			Calendar endDate = lesson.getEndTime();

			TextView headertv = (TextView) findViewById(R.id.lessonTitleLabel); 
			headertv.setText(CalendarUtils.getDateString(date, true) + "\n" + CalendarUtils.getTimeStr(startDate, false) + "-" + CalendarUtils.getTimeStr(endDate, false));
		
			
			TableRow classesRow = (TableRow) findViewById(R.id.lesson_classes_tr);
			TextView classes = (TextView) findViewById(R.id.lesson_classes_msg);
			initializeRow(lesson.getSchoolClasses(), classes, classesRow);

			TableRow teachersRow = (TableRow) findViewById(R.id.lesson_teachers_tr);
			TextView teachers = (TextView) findViewById(R.id.lesson_teachers_msg);
			initializeRow(lesson.getSchoolTeachers(), teachers, teachersRow);
			
			TableRow subjectsRow = (TableRow) findViewById(R.id.lesson_subjects_tr);
			TextView subjects = (TextView) findViewById(R.id.lesson_subjects_msg);
			initializeRow(lesson.getSchoolSubjects(), subjects, subjectsRow);
			
			TableRow roomsRow = (TableRow) findViewById(R.id.lesson_rooms_tr);
			TextView rooms = (TextView) findViewById(R.id.lesson_rooms_msg);
			initializeRow(lesson.getSchoolRooms(), rooms, roomsRow);
			

			TextView typetv = (TextView) findViewById(R.id.lesson_type_msg);
			LessonType lsType = lesson.getLessonType();
			
			if (lsType != null){
				typetv.setText(lsType.getText());
				// Setze Farben
				headertv.setBackgroundColor(lsType.getBgColor());
				headertv.setTextColor(lsType.getFgColor());
			}
			else {
				typetv.setText(R.string.lessonview_default);
			}
			
			
			TextView codetv = (TextView) findViewById(R.id.lesson_code_msg);
			LessonCode lsCode = lesson.getLessonCode();

			if (lsCode instanceof LessonCodeCancelled) {
				codetv.setText(R.string.lessonview_cancelled);
			}
			else if (lsCode instanceof LessonCodeSubstitute) {
				codetv.setText(R.string.lessonview_substitute);
				SchoolTeacher originTeacher = ((LessonCodeSubstitute)(lesson.getLessonCode())).getOriginSchoolTeacher();
				SchoolRoom originRoom = ((LessonCodeSubstitute)(lesson.getLessonCode())).getOriginSchoolRoom();
				if(originTeacher != null) {
					teachers.setText(teachers.getText() + ", (" + originTeacher.getName() + ")");
				}
				if(originRoom != null) {
					rooms.setText(rooms.getText() + ", (" + originRoom.getName() + ")");
				}
			}
			else {
				codetv.setText(R.string.lessonview_default);
			}

			if(lsCode != null) {
				// Setze Farben
				headertv.setBackgroundColor(lsCode.getBgColor());
				headertv.setTextColor(lsCode.getFgColor());
			}
			
			Button addtestbt = (Button) findViewById(R.id.lessonButton);
			if(lsCode instanceof LessonCodeCancelled || lsType instanceof LessonTypeStandby){
				addtestbt.setVisibility(View.GONE);
			}
			else{
				addtestbt.setOnClickListener(this);
			}
		}
		else {
			sendMessageToHandler(getString(R.string.lessonview_error));
		}
	}
	
	private void initializeRow(List<? extends ViewType> list, TextView tv, TableRow tr){
		String schoolList = "";
		for (ViewType tmp : list) {

			if ("".equals(schoolList)) {
				schoolList += tmp.getName();
			}
			else {
				schoolList += ", " + tmp.getName();
			}
		}
		tv.setText(schoolList);
		if("".equals(schoolList)) {
			tr.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		Intent myIntent = new Intent(this, TestView.class);
		myIntent.putExtra(ExtrasStrings.LESSON, lesson);
		myIntent.putExtra(ExtrasStrings.DATE, lesson.getDate());
		startActivityForResult(myIntent, NEW_TEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case NEW_TEST:
				if (resultCode == RESULT_OK) {
					sendMessageToHandler(getString(R.string.addtest_success));

				}
				else {
					sendMessageToHandler(getString(R.string.addtest_failed));
				}
				break;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}