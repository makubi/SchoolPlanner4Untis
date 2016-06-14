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
package edu.htl3r.schoolplanner.gui.selectScreen;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;

public class SpinnerMemory {

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	
	private final String spinnerMemoryClassList = getString(R.string.spinner_memory_class_list);
	private final String spinnerMemoryTeacherList = getString(R.string.spinner_memory_teacher_list);
	private final String spinnerMemoryRoomList = getString(R.string.spinner_memory_room_list);
	private final String spinnerMemorySubjectList = getString(R.string.spinner_memory_subject_list);
	
	private String classListLastElement;
	private String teacherListLastElement;
	private String roomListLastElement;
	private String subjectListLastElement;
	
	public SpinnerMemory() {
		preferences = PreferenceManager.getDefaultSharedPreferences(SchoolplannerContext.context);
		editor = preferences.edit();
		
		loadPreferences();
	}
	
	private void loadPreferences() {
		classListLastElement = preferences.getString(spinnerMemoryClassList, "");
		teacherListLastElement = preferences.getString(spinnerMemoryTeacherList, "");
		roomListLastElement = preferences.getString(spinnerMemoryRoomList, "");
		subjectListLastElement = preferences.getString(spinnerMemorySubjectList, "");
	}
	
	public String getClassListLastElement() {
		return classListLastElement;
	}

	public void setClassListLastElement(String classListLastElement) {
		this.classListLastElement = classListLastElement;
		saveStringToEditor(spinnerMemoryClassList, classListLastElement);
	}

	public String getTeacherListLastElement() {
		return teacherListLastElement;
	}

	public void setTeacherListLastElement(String teacherListLastElement) {
		this.teacherListLastElement = teacherListLastElement;
		saveStringToEditor(spinnerMemoryTeacherList, teacherListLastElement);
	}

	public String getRoomListLastElement() {
		return roomListLastElement;
	}

	public void setRoomListLastElement(String roomListLastElement) {
		this.roomListLastElement = roomListLastElement;
		saveStringToEditor(spinnerMemoryRoomList, roomListLastElement);
	}

	public String getSubjectListLastElement() {
		return subjectListLastElement;
	}

	public void setSubjectListLastElement(String subjectListLastElement) {
		this.subjectListLastElement = subjectListLastElement;
		saveStringToEditor(spinnerMemorySubjectList, subjectListLastElement);
	}

	private String getString(int resId) {
		return SchoolplannerContext.context.getString(resId);
	}

	private void saveStringToEditor(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * Verwende {@link ViewType#getName()} zum Speichern.
	 * @param selectedViewType
	 * @see SelectScreen#initViewTypeSpinner(Spinner, List<? extends ViewType>)
	 */
	public void setSelectedViewType(ViewType selectedViewType) {
		if(selectedViewType instanceof SchoolClass) {
			setClassListLastElement(selectedViewType.getName());
		}
		else if (selectedViewType instanceof SchoolTeacher) {
			setTeacherListLastElement(selectedViewType.getName());
		}
		else if (selectedViewType instanceof SchoolRoom) {
			setRoomListLastElement(selectedViewType.getName());
		}
		else if (selectedViewType instanceof SchoolSubject) {
			setSubjectListLastElement(selectedViewType.getName());
		}
	}
}
