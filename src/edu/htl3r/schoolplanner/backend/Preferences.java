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

import java.net.URISyntaxException;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolplannerContext;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.timetableviews.DayView;
import edu.htl3r.schoolplanner.gui.timetableviews.ViewActivity;

/**
 * 
 * Diese Klasse verwaltet die Einstellungen, die fuer das Programm noetig sind.
 * Eintraege, wie Login-Information, werden aus dieser Klasse gelesen bzw. ueber
 * diese gesetzt.
 * 
 */
public class Preferences {

	private String SCHOOL = "school";
	private String USERNAME = "username";
	private String PASSWORD = "password";
	private String AUTOLOGIN = "autologin";
	private String AUTOCHOOSE = "autochoose";
	private String VIEWTYPE = "viewType";
	private String BGCOLOR = "bgColor";
	private String TOUCHCOLOR = "touchColor";
	private String BORDERCOLOR = "borderColor";
	private String HEADERCOLOR = "headerColor";
	private String SELECTION = "selection";
	private String SCHOOLVIEW = "schoolView";
	private String SHOW_SATURDAY = "showSaturday";
	private String SHOW_DATES = "showHeaderDates";
	private String SHOW_DAYNAMES = "showHeaderDayNames";
	private String URL = "serverURL";
	private String SHOW_TIMEGRID = "showTimegrid";
	private String SHOW_ZEROHOUR = "showZerohour";

	private Authentication authentification = new Authentication();
	private boolean autologin = false;
	private int type; // siehe arrays.xml
	private String selection; // is es 5AN, 1BI, 263B, BRE etc.
	private ViewActivity view = new DayView();
	private boolean autochoose = false;
	private int bgColor;
	private int borderColor;
	private int headerColor;
	private int touchColor;
	private boolean show_saturday = false;
	private boolean show_dates = false;
	private boolean show_daynames = false;
	private boolean show_timegrid = false;
	private boolean show_zerohour = false;
	
	private SharedPreferences preferences = null;
	private SharedPreferences.Editor editor = null;

	/**
	 * Default-Konstruktor
	 * @param context Kontext der Applikation
	 */
	public Preferences(){
		Context context = SchoolplannerContext.context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		editor = preferences.edit();
		
		SCHOOL = context.getString(R.string.pref_key_school);
		USERNAME = context.getString(R.string.pref_key_username);
		PASSWORD = context.getString(R.string.pref_key_password);
		AUTOLOGIN = context.getString(R.string.pref_key_autologin);
		AUTOCHOOSE = context.getString(R.string.pref_key_autochoose);
		VIEWTYPE = context.getString(R.string.pref_key_viewtype);
		BGCOLOR = context.getString(R.string.pref_key_color_bg);
		TOUCHCOLOR = context.getString(R.string.pref_key_color_touch);
		BORDERCOLOR = context.getString(R.string.pref_key_color_border);
		HEADERCOLOR = context.getString(R.string.pref_key_color_header);
		SELECTION = context.getString(R.string.pref_key_selection);
		SCHOOLVIEW = context.getString(R.string.pref_key_schoolview);
		SHOW_SATURDAY = context.getString(R.string.pref_key_show_saturday);
		SHOW_DATES = context.getString(R.string.pref_key_show_header_dates);
		SHOW_DAYNAMES = context.getString(R.string.pref_key_show_header_daynames);
		URL = context.getString(R.string.pref_key_serverurl);
		SHOW_TIMEGRID = context.getString(R.string.pref_key_show_timegrid);
		SHOW_ZEROHOUR = context.getString(R.string.pref_key_show_zerohour);
		
		updateData();
	}

	/**
	 * Aktualisiert alle Einstellungen
	 */
	public void updateData() {
		try {
			setServerUrl(preferences.getString(URL, ""));
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setSchool(preferences.getString(SCHOOL, ""));
		setUsername(preferences.getString(USERNAME, ""));
		setPassword(preferences.getString(PASSWORD, ""));
		setAutologin(preferences.getBoolean(AUTOLOGIN, false));
		setAutochoose(preferences.getBoolean(AUTOCHOOSE, false));
		setType(Integer.parseInt(preferences.getString(VIEWTYPE, "0")));
		setBgColor(preferences.getInt(BGCOLOR, Color.GRAY));
		setBorderColor(preferences.getInt(BORDERCOLOR, Color.WHITE));
		setHeaderColor(preferences.getInt(HEADERCOLOR, Color.GRAY));
		setTouchColor(preferences.getInt(TOUCHCOLOR, Color.YELLOW));
		setSelection(preferences.getString(SELECTION, ""));
		setSaturdayEnabled(preferences.getBoolean(SHOW_SATURDAY, false));
		setHeaderDatesEnabled(preferences.getBoolean(SHOW_DATES, false));
		setHeaderDaynamesEnabled(preferences.getBoolean(SHOW_DAYNAMES, false));
		setTimegridEnabled(preferences.getBoolean(SHOW_TIMEGRID, false));
		setZerohourEnabled(preferences.getBoolean(SHOW_ZEROHOUR, false));
		
		
		String schoolView = preferences.getString(SCHOOLVIEW, "");
		if(!"".equals(schoolView)){
			Log.d("Philip", getClass().getSimpleName() + ": schoolView: " + schoolView);
			try {
				setView((ViewActivity) Class.forName(schoolView).newInstance());
			} catch (Exception e){
				e.printStackTrace();
				view = new DayView();
			}
		}
	}

	/**
	 * Speichert einen String
	 * @param key Key
	 * @param value Value
	 */
	private void saveString(String key, String value){
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * Speichert ein Boolean
	 * @param key Key
	 * @param value Value
	 */
	private void saveBoolean(String key, boolean value){
		editor.putBoolean(key, value);
		editor.commit();
	}
	/**
	 * Speichert ein Integer
	 * @param key Key
	 * @param value Value
	 */
	private void saveInt(String key, int value){
		editor.putInt(key, value);
		editor.commit();
	}
	
	/**
	 * Liefert die URL des zu verwendenden Servers.
	 * @return URI als String
	 */
	public String getServerUrl() {
		return authentification.getServerUrl();
	}
	
	
	/**
	 * Setzt die URL des Servers, z.B. urania.webuntis.at oder 12.34.56.89:8080
	 * @param serverUri URL des Servers inklusive zu verwendenden Port
	 * @throws URISyntaxException Wenn ein Problem beim Parsen der URI auftritt
	 */
	public void setServerUrl(String serverUri) throws URISyntaxException {
		authentification.setServerUrl(serverUri);
		saveString(URL, serverUri);
	}
	
	/**
	 * Liefert den Namen der aktuellen Schule, die beim Login eingeben wurde.
	 * @return Login-Name der aktuellen Schule
	 */
	public String getSchool() {
		return authentification.getSchool();
	}

	/**
	 * Setzt den Login-Namen der Schule, die zur Authentifizierung verwendet
	 * werden soll.
	 * @param school
	 *            Zu verwendenter Login-Name der Schule
	 */
	public void setSchool(String school) {
		authentification.setSchool(school);
		saveString(SCHOOL, school);
	}

	/**
	 * Liefert den Benutzernamen, mit dem der Login stattfand.
	 * @return Verwendeter Benutzername
	 */
	public String getUsername() {
		return authentification.getUsername();
	}

	/**
	 * Setzt den Benutzernamen, der zur Authentifizierung verwendet werden soll.
	 * @param username
	 *            Zu verwendenter Benutzername
	 */
	public void setUsername(String username) {
		authentification.setUsername(username);
		saveString(USERNAME, username);
	}

	/**
	 * Liefert das Passwort, das beim Login verwendet wurde.
	 * @return Verwendetes Passwort
	 */
	public String getPassword() {
		return authentification.getPassword();
	}

	/**
	 * Setzt das Passwort, das zur Authentifizierung verwendet werden soll.
	 * @param password
	 *            Zu verwendentes Passwort
	 */
	public void setPassword(String password) {
		authentification.setPassword(password);
		saveString(PASSWORD, password);
	}

	/**
	 * Liefert einen boolean, ob Autologin aktiviert ist.
	 * @return true, wenn Autologin aktiviert ist
	 */
	public boolean isAutologin() {
		return autologin;
	}

	/**
	 * Setzt den Status des Autologins.
	 * @param autologin
	 *            Status das Autologins
	 */
	public void setAutologin(boolean autologin) {
//		Log.d("Philip", "setautologin: " +autologin);
		this.autologin = autologin;
		saveBoolean(AUTOLOGIN, autologin);
	}

	/**
	 * Liefert den Ansichts-Typ, bzw. ein Objekt davon (sprich eine bestimmte
	 * Ansicht), der beim Start angezeigt werden soll.
	 * @return int wert
	 */
	public int getType() {
		return type;
	}

	/**
	 * Setzt den zu verwendenten Ansichts-Typen.
	 * @param type
	 *            Eine Implementierung von {@link ViewType}
	 */
	public void setType(int type) {
		this.type = type;
		saveString(VIEWTYPE, ""+type);
	}
	
	/**
	 * Liefert den Wert des ausgewaehlten Ansichtstypen
	 * @return Wert des aktuellen Ansichtstyps
	 */
	public String getSelection() {
		return selection;
	}

	/**
	 * Setzt den Wert des aktuellen Ansichtstypen
	 * @param selection Wert des Ansichtstyps
	 */
	public void setSelection(String selection) {
		this.selection = selection;
		saveString(SELECTION, selection);
	}

	/**
	 * Liefert die View, die standardgemaess verwendet werden soll.
	 * @return Eine Implementierung von {@link ViewActivity}
	 */
	public ViewActivity getView() {
		return view;
	}

	/**
	 * Setzt die View, die standardgemaess verwendet werden soll.
	 * 
	 * @param view
	 *            Eine Implementierung von {@link ViewActivity}
	 */
	public void setView(ViewActivity view) {
		this.view = view;
		saveString(SCHOOLVIEW, view.getClass().getCanonicalName());
	}

	/**
	 * Liefert zurueck, ob der Stundenplan automatisch gewaehlt werden soll
	 * @return true, wenn ja und false, wenn nicht
	 */
	public boolean isAutochoose() {
		return autochoose;
	}

	/**
	 * Setzt, ob der Stundenplan automatisch gewaehlt werden soll
	 * @param autochoose true, wenn ja und false, wenn nicht
	 */
	public void setAutochoose(boolean autochoose) {
		this.autochoose = autochoose;
		saveBoolean(AUTOCHOOSE, autochoose);
	}

	/**
	 * Liefert die Hintergrundfarbe zurueck
	 * @return Hintergrundfarbe als Integer
	 */
	public int getBgColor() {
		return bgColor;
	}

	/**
	 * Setzt die Hintergrundfarbe
	 * @param bgColor Hintergrundfarbe
	 */
	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
		saveInt(BGCOLOR, bgColor);
	}

	/**
	 * Liefert die Touchfarbe zurueck
	 * @return Touchfarbe als Hintergrund
	 */
	public int getTouchColor() {
		return touchColor;
	}

	/**
	 * Setzt die Touchfarbe
	 * @param touchColor Touchfarbe
	 */
	public void setTouchColor(int touchColor) {
		this.touchColor = touchColor;
		saveInt(TOUCHCOLOR, touchColor);
	}
	
	public int getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
		saveInt(BORDERCOLOR, borderColor);
	}

	public int getHeaderColor() {
		return headerColor;
	}

	public void setHeaderColor(int headerColor) {
		this.headerColor = headerColor;
		saveInt(HEADERCOLOR, headerColor);
	}

	/**
	 * Liefert true, wenn Schule, Benutzername und Passwort gesetzt sind.
	 * @return true, wenn alle Logindaten gesetzt sind, sonst false
	 */
	public boolean hasPreferences() {
//		Log.d("Philip", "getschool: \"" +getSchool() +"\"");
//		Log.d("Philip", "getusername: \"" +getUsername() +"\"");
//		Log.d("Philip", "getpassword: \"" +getPassword() +"\"");
		return getSchool() != null && !getSchool().equals("") && getUsername() != null && !getUsername().equals("") && getPassword() != null  && getServerUrl()!=null && !getServerUrl().equals("");
	}

	public String toString() {
		// TODO neue werte dazu
		StringBuilder sb = new StringBuilder();

		sb.append("== Authentification\n");
		sb.append("School: " + getSchool() + "\n");
		sb.append("Username: " + getUsername() + "\n");
		sb.append("Password: " + getPassword() + "\n");
		sb.append("--\n");
		sb.append("Autologin: " + autologin + "\n");
		sb.append("Autochoose: " + autochoose + "\n");
		sb.append("Type: " + type + "\n");
		
		
		if(view != null){
			sb.append("View: " + view.getClass().getSimpleName() +"\n");
			sb.append("View ID: " + view.settingsId + "\n");
		}

		return sb.toString();
	}

	public boolean isSaturdayEnabled() {
		return show_saturday;
	}

	public void setSaturdayEnabled(boolean showSaturday) {
		show_saturday = showSaturday;
		saveBoolean(SHOW_SATURDAY, showSaturday);
	}

	public boolean headerDatesEnabled() {
		return show_dates;
	}

	public void setHeaderDatesEnabled(boolean showDates) {
		show_dates = showDates;
		saveBoolean(SHOW_DATES, showDates);
	}

	public boolean headerDaynamesEnabled() {
		return show_daynames;
	}

	public void setHeaderDaynamesEnabled(boolean showDaynames) {
		show_daynames = showDaynames;
		saveBoolean(SHOW_DAYNAMES, showDaynames);
	}

	public boolean isTimegridEnabled() {
		return show_timegrid;
	}

	public void setTimegridEnabled(boolean showTimegrid) {
		show_timegrid = showTimegrid;
		saveBoolean(SHOW_TIMEGRID, showTimegrid);
	}
	
	public boolean zerohourEnabled() {
		return show_zerohour;
	}

	public void setZerohourEnabled(boolean showZerohour) {
		show_zerohour = showZerohour;
		saveBoolean(SHOW_ZEROHOUR, showZerohour);
	}
}