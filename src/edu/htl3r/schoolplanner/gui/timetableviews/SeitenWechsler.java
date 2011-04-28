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

import android.content.Context;
import android.os.Build;
import android.widget.ViewFlipper;

public class  SeitenWechsler  extends ViewFlipper{
	@Override
	public void onDetachedFromWindow() {

		int apiLevel = Integer.parseInt(Build.VERSION.SDK);
		if (apiLevel >= 7) {
			try {
				super.onDetachedFromWindow();
			} catch (IllegalArgumentException e) {
				// Android project issue 6191 workaround (http://code.google.com/p/android/issues/detail?id=6191)
			} finally {
				stopFlipping();
			}
		} else {
			super.onDetachedFromWindow();
		}
	}
	
	public SeitenWechsler(Context context) {
		super(context);
	}
	
}