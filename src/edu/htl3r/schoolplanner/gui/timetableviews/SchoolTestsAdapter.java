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

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.htl3r.schoolplanner.CalendarUtils;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTest;

/**
 * @author Philip Woelfel <philip[at]woelfel[dot]at>
 *
 */
public class SchoolTestsAdapter extends BaseAdapter {
	Context context;
	List<SchoolTest> testList;

	public SchoolTestsAdapter(Context cont, List<SchoolTest> testlist) {
		context = cont;
		testList = testlist;
		Log.d("Philip", getClass().getSimpleName() + ": testlist: " + testlist);
	}

	@Override
	public int getCount() {
		return testList.size()+1;
	}

	@Override
	public Object getItem(int position) {
		if(position==testList.size()){ // --> letzte position is laenge der liste weil ja getCount() testlist.size+1 zrueckgibt
			return context.getString(R.string.testlist_showmore);
		}
		return testList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv;
		Object item = getItem(position);
		String text = "";
		if(item instanceof SchoolTest){
			SchoolTest test = (SchoolTest) item;
			text = CalendarUtils.getDateString(test.getDate(), true) +" - " +test.getTitle();
		}
		else{
			text = (String) item;
		}
		if (convertView == null) {
			tv = new TextView(context);
		}
		else {
			tv = (TextView) convertView;
		}
		tv.setText(text);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(context.getResources().getDimension(R.dimen.testlist_fontsize));
		//tv.setHeight(50);
		return tv;
	}
	

}
