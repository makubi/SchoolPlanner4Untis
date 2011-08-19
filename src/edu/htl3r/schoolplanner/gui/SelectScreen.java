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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.gui.basti.ViewBasti;
import edu.htl3r.schoolplanner.gui.chris.ViewChris;
import edu.htl3r.schoolplanner.gui.selectScreen.AnimatedOnClickListener;

public class SelectScreen extends SchoolPlannerActivity{
	
	private Cache data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_screen);
		
		data = ((SchoolPlannerApp)getApplication()).getData();
		
		initSpinner(savedInstanceState);
		addOnClickListener();
		
		/*GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(this));
	    
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	String text = "Selected ";
	        	switch (position) {
				case 0:
					Intent chris = new Intent(SelectScreen.this, ViewChris.class);
					startActivity(chris);
					text+="Chris";
					break;
				case 1:
					text+="teacher";
					break;
				case 2:
					text+="rooms";
					break;
				case 3:
					Intent basti = new Intent(SelectScreen.this, ViewBasti.class);
					startActivity(basti);
					text+="Basti";
					break;
				default:
					break;
				}
	            Toast.makeText(SelectScreen.this, text, Toast.LENGTH_SHORT).show();
	        }
	    });*/
	}
	
	private void initSpinner(Bundle bundle) {
		Spinner classSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerClass);
		DataFacade<List<SchoolClass>> classData = this.data.getSchoolClassList();
		if(classData.isSuccessful()) {
			initViewTypeSpinner(classSpinner, classData.getData());
		}
		else {
			ImageView imageClass = (ImageView) findViewById(R.id.selectScreen_imageClass);
			imageClass.setEnabled(false);
			classSpinner.setEnabled(false);
		}
		
		Spinner roomSpinner = (Spinner) findViewById(R.id.selectScreen_spinnerRoom);
		DataFacade<List<SchoolRoom>> roomData = this.data.getSchoolRoomList();
		if(roomData.isSuccessful()) {
			initViewTypeSpinner(roomSpinner, roomData.getData());
		}
		else {
			roomSpinner.setEnabled(false);
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
