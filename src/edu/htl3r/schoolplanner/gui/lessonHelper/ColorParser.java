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
package edu.htl3r.schoolplanner.gui.lessonHelper;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ColorParser {
	
	/**
	 * Liefert die Farbe eines JSON-Objekts.
	 * Sollte das uebergebene Objekt den mitgegebenen Key nicht enthalten, wird als Standardfarbe 'FFFFFF' (entspricht weiss) verwendet.
	 * @param jsonObject JSONObjekt, von dem die Farbe bestimmt werden soll
	 * @param jsonKey Key des Farb-Strings (z.B. 'color', 'foreColor', 'backColor')
	 * @return Den Farbstring der Farbe oder 'FFFFFF', wenn keine Farbe gefunden
	 * @throws JSONException 
	 */
	public String parseColor(JSONObject jsonObject, String jsonKey) {
		String color = "FFFFFF";
		if(jsonObject.has(jsonKey))
			try {
				color = jsonObject.getString(jsonKey);
			} catch (JSONException e) {
				Log.i("JSON","Error occured while parsing color, using key: "+jsonKey,e);
			}
		
		return color;
	}
}
