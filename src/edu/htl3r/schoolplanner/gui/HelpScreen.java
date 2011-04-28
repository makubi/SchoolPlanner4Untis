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

package edu.htl3r.schoolplanner.gui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.widget.ScrollView;
import android.widget.TextView;
import edu.htl3r.schoolplanner.R;

public class HelpScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ScrollView sv = new ScrollView(this);
		TextView tv = new TextView(this);
		String string = getString(R.string.help_text);
		if (getIntent().hasExtra(ExtrasStrings.HELPCLASS)) {
			int resId = getResources().getIdentifier("edu.htl3r.schoolplanner:string/help_" + (String) getIntent().getExtras().get(ExtrasStrings.HELPCLASS), null, null);
			if(resId != 0) {
				string = getString(resId);
			}
		}
		Spanned html = Html.fromHtml(string);
		tv.setText(html);
		sv.addView(tv);
		setContentView(sv);
	}

}
