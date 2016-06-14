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
package edu.htl3r.schoolplanner.gui.timetable.baactionbar;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import edu.htl3r.schoolplanner.R;

public class BADropdown extends RelativeLayout implements OnItemClickListener{
	
	private ListView list;
	private BastisAwesomeActionBar actionabar;
	private HashMap <String, Integer> items = new HashMap<String, Integer>();
	
	public BADropdown(Context context) {
		super(context);
		init();
	}
	
	public BADropdown(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public BADropdown(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		init();
	}
	
	
	public void setActionBar(BastisAwesomeActionBar action){
		this.actionabar = action;
	}
	
	public void init(){
		list = new ListView(getContext());
		list.setOnItemClickListener(this);
		
		items.put(getResources().getString(R.string.selectscreen_class),BastisAwesomeActionBar.LIST_CLASS);
		items.put(getResources().getString(R.string.selectscreen_teacher), BastisAwesomeActionBar.LIST_TEACHER);
		items.put(getResources().getString(R.string.selectscreen_rooms), BastisAwesomeActionBar.LIST_ROOMS);
		items.put(getResources().getString(R.string.selectscreen_subjects), BastisAwesomeActionBar.LIST_SUBJECTS);
				
		String [] out = {getResources().getString(R.string.selectscreen_class),getResources().getString(R.string.selectscreen_teacher),getResources().getString(R.string.selectscreen_rooms),getResources().getString(R.string.selectscreen_subjects)};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.baactionbar_dropdown_item,out);
		
		list.setAdapter(adapter);
		list.setCacheColorHint(Color.parseColor("#00000000"));
		this.addView(list);
	}

	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long arg3) {
		actionabar.fireActionBarEvent(items.get(parent.getItemAtPosition(pos)));
		actionabar.closeDropDown();
	}
	
	
}
