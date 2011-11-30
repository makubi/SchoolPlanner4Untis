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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ToggleButton;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.AutoSelectSet;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import edu.htl3r.schoolplanner.gui.SelectScreen;

public class AutoselectDialog extends Dialog {
	
	private SelectScreen context;
	private String[] typeEntries;
	
	private Spinner typeSpinner;
	private Spinner valueSpinner;
	private ToggleButton enabledButton;
	
	private Map<String, List<? extends ViewType>> spinnerWire = new HashMap<String, List<? extends ViewType>>();
	
	private AutoSelectSet autoSelectSet;
	
	private int lastSelectedPosition = -1;
	
	public AutoselectDialog(SelectScreen context, List<SchoolClass> classList, List<SchoolTeacher> teacherList, List<SchoolRoom> roomList, List<SchoolSubject> subjectList) {
		super(context);
		this.context = context;
		setViewTypeLists(classList, teacherList, roomList, subjectList);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCanceledOnTouchOutside(true);
		setContentView(R.layout.autoselect_dialog);
		setTitle(R.string.autoselect_dialog_title);
		setCancelable(true);
		
		typeEntries = context.getResources().getStringArray(R.array.settings_autoselect_type_entryvalues);
		
		initEnabledButton();
		initSpinner();
		initAutoSelectSet();
		
		setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				saveAutoSelectSet();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		saveAutoSelectSet();
		super.onBackPressed();
	}
	
	private void saveAutoSelectSet() {
		context.setAutoSelect(autoSelectSet);
	}

	private void initAutoSelectSet() {		
		int typeSpinnerPos = -1;
		int valueSpinnerPos = -1;
		
		// init default
		if(!isAutoselectSetInitialized(autoSelectSet)) initDefaultAutoSelectSet(autoSelectSet);
		
		String type = autoSelectSet.getAutoSelectType();
		
		typeSpinnerPos = getTypeSpinnerPos(type);
		
		List<? extends ViewType> viewTypeList = spinnerWire.get(type);
		initSpinner(valueSpinner, viewTypeList);
		
		final int valueSpinnerId = autoSelectSet.getAutoSelectValue();
		
		// wenn ein listenelement vorhanden
		if(valueSpinnerId > 0 && viewTypeList.size() > 0) valueSpinnerPos = getValueSpinnerPos(viewTypeList, valueSpinnerId);
		
		
		if(typeSpinnerPos >= 0) typeSpinner.setSelection(typeSpinnerPos);
		if(valueSpinnerPos >= 0) valueSpinner.setSelection(valueSpinnerPos);
	}
	
	private void initDefaultAutoSelectSet(AutoSelectSet autoSelectSet) {
		autoSelectSet.setAutoSelectType(getString(R.string.settings_autoselect_type_class));
		autoSelectSet.setAutoSelectValue(0);
	}

	private boolean isAutoselectSetInitialized(AutoSelectSet autoSelectSet) {
		return autoSelectSet.getAutoSelectType().length() > 0 && autoSelectSet.getAutoSelectValue() >= 0; 
	}

	private void initSpinner() {
		typeSpinner = (Spinner) findViewById(R.id.autoselect_dialog_type_spinner);
		valueSpinner = (Spinner) findViewById(R.id.autoselect_dialog_value_spinner);
		
		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				
				if(lastSelectedPosition > -1 && lastSelectedPosition != position) {
					String selectedTypeEntry = typeEntries[position];
					initSpinner(valueSpinner, spinnerWire.get(selectedTypeEntry));
					
					autoSelectSet.setAutoSelectType(selectedTypeEntry);
				}
				
				lastSelectedPosition = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		valueSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ViewType viewType = spinnerWire.get(autoSelectSet.getAutoSelectType()).get(position);
				autoSelectSet.setAutoSelectValue(viewType.getId());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}
	
	private int getTypeSpinnerPos(String type) {
		int typeSpinnerPos = -1;
		int i = 0;
		for(String key : typeEntries) {
			if(key.equals(type)) {
				typeSpinnerPos = i;
				break;
			}
			i++;
		}
		return typeSpinnerPos;
	}
	
	private int getValueSpinnerPos(List<? extends ViewType> viewTypeList, int autoSelectId) {
		int valueSpinnerPos = -1;
		for(int i = 0; i < viewTypeList.size(); i++) {
			if(viewTypeList.get(i).getId() == autoSelectId) {
				valueSpinnerPos = i;
				break;
			}
		}
		return valueSpinnerPos;
	}
	
	private void initSpinner(Spinner spinner, List<? extends ViewType> list) {
		List<String> spinnerList = new ArrayList<String>();
		for (ViewType viewType : list) {
			spinnerList.add(viewType.getName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		adapter.notifyDataSetChanged();
	}

	private void initEnabledButton() {
		enabledButton = (ToggleButton) findViewById(R.id.autoselect_dialog_enabled);
		enabledButton.setChecked(autoSelectSet.isEnabled());
		
		enabledButton.setOnClickListener(new ToggleButton.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				autoSelectSet.setEnabled(enabledButton.isChecked());
			}
		});
	}

	private void setViewTypeLists(List<SchoolClass> classList, List<SchoolTeacher> teacherList, List<SchoolRoom> roomList, List<SchoolSubject> subjectList) {
		spinnerWire.put(getString(R.string.settings_autoselect_type_class), classList);
		spinnerWire.put(getString(R.string.settings_autoselect_type_teacher), teacherList);
		spinnerWire.put(getString(R.string.settings_autoselect_type_room), roomList);
		spinnerWire.put(getString(R.string.settings_autoselect_type_subject), subjectList);
	}
	
	private String getString (int resId) {
		return context.getString(resId);
	}

	public void setAutoSelectSet(AutoSelectSet autoSelectSet) {
		this.autoSelectSet = autoSelectSet;
	}
}
