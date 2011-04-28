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

import java.io.IOException;
import java.util.Calendar;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.AdapterView.OnItemClickListener;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTest;

/**
 * anzeige der terminliste
 * 
 * @author Philip Woelfel <philip@woelfel.at>
 *
 */

public class DateListView extends ViewActivity implements OnItemClickListener {
	private int monthadd = 1;
	private SchoolTestsAdapter adap;
	private static final int MENU_DELETE = 0;

	private ListView lv;

	public DateListView() {
		settingsId = 2;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		settingsId = 2;
		super.onCreate(savedInstanceState);
	}

	@Override
	public View createLayout() {
		ScrollView sv = new ScrollView(this);
		sv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		lv = new ListView(this);
		lv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		initAdapter(lv);
		lv.setOnItemClickListener(this);
		registerForContextMenu(lv);
		sv.addView(lv);
		return sv;
	}

	private void initAdapter(ListView lv) {
		Calendar endDate = (Calendar) getDate().clone();
		endDate.add(Calendar.MONTH, monthadd);

		try {
			adap = new SchoolTestsAdapter(this, app.getData().getSchoolTestList(getSelectedViewType(), getDate(), endDate));
			lv.setAdapter(adap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == parent.getChildCount() - 1) {
			monthadd++;
			initAdapter(lv);
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo ci = (AdapterView.AdapterContextMenuInfo) menuInfo;
		if (ci.position != adap.getCount() - 1) {
			menu.setHeaderTitle(R.string.test_context_header);
			menu.add(Menu.NONE, MENU_DELETE, 0, R.string.test_context_delete);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo ci = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		if (ci.position != adap.getCount() - 1) {
			switch (item.getItemId()) {
				case MENU_DELETE:
					SchoolTest test = (SchoolTest) adap.getItem(ci.position);
					Log.d("Philip", getClass().getSimpleName() + ": deleting pos: " + ci.position + ", title: " + test.getTitle() + ", id: " + test.getId());
					// TODO delete test
					return true;
			}
		}
		return false;
	}

	@Override
	public void nextDate() {
		// do nothing, sollten ned aufgrufen werden
		
	}

	@Override
	public void prevDate() {
		// do nothing, sollten ned aufgrufen werden
		
	}

}
