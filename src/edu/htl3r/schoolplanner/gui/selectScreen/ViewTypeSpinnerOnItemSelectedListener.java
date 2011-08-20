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

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;

public class ViewTypeSpinnerOnItemSelectedListener implements OnItemSelectedListener {

	private Activity parent;
	private Intent intent;
	private List<? extends ViewType> list;
	
	private boolean init = true;

	public ViewTypeSpinnerOnItemSelectedListener(Activity parent, Intent intent, List<? extends ViewType> list) {
		this.parent = parent;
		this.intent = intent;
		this.list = list;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// Pruefung auf Leeritem
		if (!init) {			
			ViewType item = getViewType(position);
			Toast.makeText(this.parent.getApplicationContext(), "Selected "+item.toString(), Toast.LENGTH_LONG).show();
			// TODO Start activity here
			//this.parent.startActivity(intent);
			
		}
		else {
			init = false;
		}
	}

	protected ViewType getViewType(int position) {
		return list.get(position-1);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {}


	public void setSkipNextAction(boolean init) {
		this.init = init;
	}
}
