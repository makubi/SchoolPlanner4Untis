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

package edu.htl3r.schoolplanner.backend;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ResourceHandler {

	private static ResourceHandler INSTANCE = new ResourceHandler();
	
	private final String resourceFolder = "res/";
	
	private ResourceHandler() {}
	
	public static ResourceHandler getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Gibt den passenden BufferedReader zurueck, egal ob sich die angeforderte Datei in einem Jar-File befindet oder nicht.
	 * Es muss ein '/' verwendet werden, da unter Windows die Dateien sonst nicht gefunden werden.
	 * @param fileName der Dateiname fuer den der BufferedReader geoeffnet werden soll
	 * @return der BufferedReader fuer das File
	 */
	public BufferedReader getBufferedReaderForResource(String fileName){
		BufferedReader in = null;
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(resourceFolder + fileName), "UTF-8"));
		} catch (Exception e) {
			try {
				return new BufferedReader(new InputStreamReader(Object.class.getResourceAsStream("/" + resourceFolder + fileName), "UTF-8"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			finally {
				closeStream(in);
			}
		}
		return in;
	}
	
	/**
	 * schließt ein Closeable-Object
	 * @param closeable das zu schliessende Objekt
	 */
	public void closeStream(Closeable closeable){
		if(closeable != null){
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public BufferedReader getBufferedReader(File inFile){
		BufferedReader in = null;
			try {
				return new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			finally {
				closeStream(in);
			}
		return in;
	}
	
}