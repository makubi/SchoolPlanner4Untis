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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.BundleConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import edu.htl3r.schoolplanner.gui.SelectScreen;

public class ViewTypeOnClickListener extends AnimatedOnClickListener{
	
	private SchoolPlannerActivity parent;
	private Intent intent;
	private List<? extends ViewType> list;
	private Spinner spinner;
	
	public ViewTypeOnClickListener(SchoolPlannerActivity parent, Intent intent, List<? extends ViewType> list, Spinner spinner) {
		super(parent.getApplicationContext());
		this.parent = parent;
		this.intent = intent;
		this.list = list;
		this.spinner = spinner;
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		AsyncTask<Void, Void, Void> backgroundTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(BundleConstants.SELECTED_VIEW_TYPE, list.get(spinner.getSelectedItemPosition()));
				intent.putExtras(bundle);
				parent.startActivity(intent);
				return null;
			}
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				parent.setInProgress(parent.getString(R.string.loading_next_screen), true);
			}
		};
		
		// Wird verwendet, damit der Stundenplan / die naechste Activity nur geladen wird, wenn nicht schon ein/e andere/r geladen wird. 
		if(parent instanceof SelectScreen && !((SelectScreen) parent).isLoadingTimetable()) {
			backgroundTask.execute();
		}
	}

}
