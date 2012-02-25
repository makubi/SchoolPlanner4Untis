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
package edu.htl3r.schoolplanner.backend.preferences.loginSets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebUntisUrlParser {
	
	/**
	 * Liefert die URL oder einen Leerstring, wenn die URL nicht geparst werden konnte.
	 * @param url URL, die geparst werden soll
	 * @return Die geparste URL oder einen Leerstring, wenn sie nicht geparst werden konnte
	 */
	public String parseUrl(String url) {
		if(url.length() <= 0) {
			return "";
		}
		String applicableUrl = url.replaceAll("(?i)/WebUntis(/*)(jsonrpc.do|index.do)?$", "");
		Pattern p = Pattern.compile("^([a-zA-Z]+://)?(.*)$");
		Matcher m = p.matcher(applicableUrl);
		if(m.matches()) {
			applicableUrl = m.group(2);
			
			while(applicableUrl.endsWith("/")) {
				applicableUrl = applicableUrl.substring(0, applicableUrl.length()-1);
			}
			
			return applicableUrl;
		}
		
		return "";
	}
}
