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
