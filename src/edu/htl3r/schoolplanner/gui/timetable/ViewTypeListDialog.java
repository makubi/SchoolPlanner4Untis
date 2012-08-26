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
package edu.htl3r.schoolplanner.gui.timetable;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.cache.Cache;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolClass;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolRoom;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolSubject;
import edu.htl3r.schoolplanner.backend.schoolObjects.viewtypes.SchoolTeacher;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUIContentProvider;
import edu.htl3r.schoolplanner.gui.timetable.baactionbar.BastisAwesomeActionBar;

public class ViewTypeListDialog extends Dialog implements OnItemClickListener{
	
	private List<SchoolClass> classes = null;
	private List<SchoolRoom> rooms = null;
	private List<SchoolTeacher> teacher = null;
	private List<SchoolSubject> subjects = null;

	private Cache cache;
	private Context context;
	private WeekView weekview;
	
	private int mode;

	
	public ViewTypeListDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vt_list_dialog);
		setCanceledOnTouchOutside(true);
	}

	public void setData(Cache cache, Context context, WeekView weekview) {
		this.weekview = weekview;
		this.cache = cache;
		this.context = context;
		prefetchLists();
	}
	
	private void prefetchLists() {
		new ViewTypeListLoader().execute();
	}
	
	public void setList(int id){
		
		ListView vtlist = (ListView) findViewById(R.id.vt_dialog_list);
		vtlist.setOnItemClickListener(this);
		vtlist.setCacheColorHint(Color.parseColor("#00000000"));

		ArrayAdapter<String> vtadapter = new ArrayAdapter<String>(context, R.layout.baactionbar_dropdown_item);
		
		switch(id){
		case BastisAwesomeActionBar.LIST_CLASS:
			putDataToAdpater(classes, vtadapter);
			break;
		case BastisAwesomeActionBar.LIST_ROOMS:
			putDataToAdpater(rooms, vtadapter);
			break;
		case BastisAwesomeActionBar.LIST_SUBJECTS:
			putDataToAdpater(subjects, vtadapter);
			break;
		case BastisAwesomeActionBar.LIST_TEACHER:
			putDataToAdpater(teacher, vtadapter);
			break;
		}
		mode = id;
		vtlist.setAdapter(vtadapter);
	}
	
	private void putDataToAdpater(List<? extends ViewType> vt, ArrayAdapter<String> adapter){
		for(int i=0; i<vt.size(); i++){
			adapter.add(vt.get(i).getName());
		}
	}

	private class ViewTypeListLoader extends AsyncTask<Void, Void, Void> {
		private GUIContentProvider content;

		@Override
		protected void onPreExecute() {
			content = new GUIContentProvider(cache, context);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			classes = content.getAllSchoolClasses();
			teacher = content.getAllSchoolTeachers();
			subjects = content.getAllSchoolSubjects();
			rooms = content.getAllSchoolRooms();
			return null;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
		switch(mode){
		case BastisAwesomeActionBar.LIST_CLASS:
			weekview.changeViewType(classes.get(pos));
			dismiss();
			break;
		case BastisAwesomeActionBar.LIST_ROOMS:
			weekview.changeViewType(rooms.get(pos));
			dismiss();
			break;
		case BastisAwesomeActionBar.LIST_SUBJECTS:
			weekview.changeViewType(subjects.get(pos));
			dismiss();
			break;
		case BastisAwesomeActionBar.LIST_TEACHER:
			weekview.changeViewType(teacher.get(pos));
			dismiss();
			break;
		}
	}
}
