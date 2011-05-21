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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.Preferences;
import edu.htl3r.schoolplanner.backend.schoolObjects.SchoolTestType;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.elements.SpinnerMultiAdapter;

/**
 * @author Philip Woelfel <philip@woelfel.at>
 * 
 */
public abstract class SchoolplannerActivity extends Activity implements Runnable {
	protected SchoolPlannerApp app;
	protected Preferences prefs;
	
	protected static final int MENU_SETTINGS = 0;
	protected static final int MENU_HELP = 1;
	protected static final int MENU_CREDITS = 2;
	private static final int MENU_PRESETS = 34769;
	

	protected static final int DIALOG_PROGRESS = 13513513;
	protected static final int DIALOG_PROGRESS_CANCELABLE = 135135131;
	

	private String dialog_msg = "";
	private String dialog_title = "";
	private OnCancelListener dialog_cancel = null;


	protected Thread thisThread;
	protected Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (SchoolPlannerApp) getApplication();
		prefs = app.getPrefs();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MENU_SETTINGS) {
			prefs.updateData();
			app.setCurrentView(prefs.getView().getClass());
			app.getData().setPreferences(prefs);
			Log.d("Philip", "updatedata!");
		}
	}

	
	

	/**
	 * erstellt das Menue
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!(this instanceof LoginScreen)) {
			menu.add(0, MENU_SETTINGS, 0, R.string.settings).setIcon(R.drawable.ic_menu_preferences);
		}
		menu.add(0, MENU_HELP, 1, R.string.help).setIcon(R.drawable.ic_menu_help);
		menu.add(0, MENU_CREDITS, 2, R.string.info).setIcon(R.drawable.ic_menu_info_details);

		return true;
	}

	/**
	 * wird aufgerufen wenn ein Menuepunkt ausgewaehlt wird
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_SETTINGS:
				Intent myIntent = new Intent(this, PrefScreen.class);
				startActivityForResult(myIntent, MENU_SETTINGS);
				return true;
			case MENU_HELP:
				Intent help = new Intent(this, HelpScreen.class);
				help.putExtra(ExtrasStrings.HELPCLASS, getClass().getSimpleName());
				startActivity(help);
				return true;
			case MENU_CREDITS:
				Intent credits = new Intent(this, AboutScreen.class);
				startActivity(credits);
				return true;
			case MENU_PRESETS:
				Intent presets = new Intent(this, LoginPresetScreen.class);
				startActivity(presets);
				return true;
		}
		return false;
	}

	
	
	
	
	
	/**
	 * Zeigt einen {@link ProgressDialog} an, waehrend im Hintergrund ein Thread aufgerufen wird der eine Aktion ausfuehrt
	 * dazu muss die {@link #run()}-Methode ueberschrieben werden und danach mittels des {@link #handler}s ein {@link #dismissDialog(int)}
	 * mit dem Parameter {@link #DIALOG_PROGRESS} aufgerufen werden.
	 * @param title der Titel des ProgressDialogs
	 * @param msg der Text des ProgressDialogs
	 */
	protected void startDialogAction(String title, String msg) {
		dialog_msg = msg;
		dialog_title = title;
		showDialog(DIALOG_PROGRESS);
//		progressDialog = ProgressDialog.show(this, title, msg, false, false);
		thisThread = new Thread(this);
		thisThread.start();
	}

	/**
	 * Zeigt einen {@link ProgressDialog} an, waehrend im Hintergrund ein Thread aufgerufen wird der eine Aktion ausfuehrt
	 * dazu muss die {@link #run()}-Methode ueberschrieben werden und danach mittels des {@link #handler}s ein {@link #dismissDialog(int)}
	 * mit dem Parameter {@link #DIALOG_PROGRESS_CANCELABLE} aufgerufen werden.
	 * Dieser Dialog ist <i>cancelable</i>, dass heisst er kann mittels des Zurueck-Buttons des Geraetes abgebrochen werden, 
	 * dabei wird dann der mitgegebene {@link OnCancelListener} aufgerufen
	 * @param title der Titel des ProgressDialogs
	 * @param msg der Text des ProgressDialogs
	 * @param cancelListener ein {@link OnCancelListener} der beim abbrechen aufgerufen werden soll
	 */
	protected void startDialogAction(String title, String msg, OnCancelListener cancelListener) {
		dialog_msg = msg;
		dialog_title = title;
		dialog_cancel = cancelListener;
		showDialog(DIALOG_PROGRESS_CANCELABLE);
//		progressDialog = ProgressDialog.show(this, title, msg, false, true);
//		progressDialog.setOnCancelListener(cancelListener);
		thisThread = new Thread(this);
		thisThread.start();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Log.d("Philip", getClass().getSimpleName() + ": oncreatedialog: " +id);
		if (id == DIALOG_PROGRESS) {
			ProgressDialog loadingDialog = new ProgressDialog(this);
			loadingDialog.setMessage(dialog_msg);
			loadingDialog.setTitle(dialog_title);
			loadingDialog.setIndeterminate(true);
			loadingDialog.setCancelable(false);
			return loadingDialog;
		}
		else if (id == DIALOG_PROGRESS_CANCELABLE) {
			ProgressDialog loadingDialog = new ProgressDialog(this);
			loadingDialog.setMessage(dialog_msg);
			loadingDialog.setTitle(dialog_title);
			loadingDialog.setIndeterminate(true);
			loadingDialog.setCancelable(true);
			loadingDialog.setOnCancelListener(dialog_cancel);
			return loadingDialog;
		}
		return super.onCreateDialog(id);
	}
	
	
	
	
	
	
	/**
	 * initialisiert ein Dropdownmenue
	 * 
	 * @param spinner
	 *            der Spinner der initialisert werden soll
	 * @param vtlist
	 *            die Liste von {@link ViewType}s mit der, der Spinner initialisert werden soll
	 * @param listener
	 *            der Listener fuer den Spinner
	 * @param emptyElement
	 *            gibt an ob ein Leerelement am Anfang in die Liste eingefuegt werden soll
	 */
	protected void initializeAdapter(Spinner spinner, List<? extends ViewType> vtlist, OnItemSelectedListener listener, boolean emptyElement) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getElementNames(vtlist, emptyElement));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setOnItemSelectedListener(listener);
		spinner.setAdapter(adapter);
	}

	/**
	 * initialisiert ein Dropdownmenue
	 * 
	 * @param spinner
	 *            der Spinner der initialisert werden soll
	 * @param vtlist
	 *            die Liste mit der, der Spinner initialisert werden soll
	 * @param listener
	 *            der Listener fuer den Spinner
	 */
	protected void initializeAdapter(Spinner spinner, List<String> list, OnItemSelectedListener listener) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setOnItemSelectedListener(listener);
		spinner.setAdapter(adapter);
	}

	/**
	 * initialisiert ein Dropdownmenue
	 * 
	 * @param spinner
	 *            der Spinner der initialisert werden soll
	 * @param resource
	 *            eine Array Resource mit der, der Spinner initialisert werden soll
	 * @param listener
	 *            der Listener fuer den Spinner
	 */
	protected void initializeAdapter(Spinner spinner, int resource, OnItemSelectedListener listener) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, resource, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setOnItemSelectedListener(listener);
		spinner.setAdapter(adapter);
	}
	
	
	/**
	 * initialisiert ein Dropdownmenue
	 * 
	 * @param spinner
	 *            der Spinner der initialisert werden soll
	 * @param vtlist
	 *            die Liste von {@link ViewType}s mit der, der Spinner initialisert werden soll
	 * @param listener
	 *            der Listener fuer den Spinner
	 * @param emptyElement
	 *            gibt an ob ein Leerelement am Anfang in die Liste eingefuegt werden soll
	 */
	protected void initializeMultiAdapter(Spinner spinner, List<? extends ViewType> vtlist, OnItemSelectedListener listener, boolean emptyElement) {
		SpinnerMultiAdapter adapter = new SpinnerMultiAdapter(this, getElementNames(vtlist, emptyElement));
		spinner.setOnItemSelectedListener(listener);
		spinner.setAdapter(adapter);
	}

	/**
	 * initialisiert ein Dropdownmenue
	 * 
	 * @param spinner
	 *            der Spinner der initialisert werden soll
	 * @param vtlist
	 *            die Liste von {@link SchoolTestType}s mit der, der Spinner initialisert werden soll
	 * @param listener
	 *            der Listener fuer den Spinner
	 * @param emptyElement
	 *            gibt an ob ein Leerelement am Anfang in die Liste eingefuegt werden soll
	 */
	protected void initializeTestTypeAdapter(Spinner spinner, List<SchoolTestType> vtlist, OnItemSelectedListener listener, boolean emptyElement) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getTestTypeElementNames(vtlist, emptyElement));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setOnItemSelectedListener(listener);
		spinner.setAdapter(adapter);
	}

	
	
	
	
	/**
	 * erstellt aus einer {@link ViewType}-Liste eine ArrayList mit den Namen
	 * 
	 * @param vtlist
	 *            die Liste von {@link ViewType}s
	 * @param emptyElement
	 *            gibt an ob ein Leerelement am Anfang in die Liste eingefuegt werden soll
	 * @return die ArrayList mit den Elementnamen
	 */

	protected static ArrayList<String> getElementNames(List<? extends ViewType> vtlist, boolean emptyElement) {
		ArrayList<String> elementNames = new ArrayList<String>();
		// Leerelement zur Liste hinzufuegen
		if (emptyElement) {
			elementNames.add("");
		}
		if (vtlist != null) {
			for (ViewType sc : vtlist) {
				if (sc.getName() != null) {
					elementNames.add(sc.getName());
				}
			}
		}

		return elementNames;
	}
	
	/**
	 * erstellt aus einer {@link ViewType}-Liste ein CharSequence-Array mit den Namen
	 * 
	 * @param vtlist
	 *            die Liste von {@link ViewType}s
	 * @param emptyElement
	 *            gibt an ob ein Leerelement am Anfang in die Liste eingefuegt werden soll
	 * @return die ArrayList mit den Elementnamen
	 */
	protected static CharSequence[] getElementNamesArray(List<? extends ViewType> vtlist, boolean emptyElement) {
		CharSequence[] elementNames = new CharSequence[emptyElement ? vtlist.size() + 1 : vtlist.size()];
		// Leerelement zur Liste hinzufuegen
		if (emptyElement) {
			elementNames[0] = "";
		}
		if (vtlist != null) {
			int i = emptyElement ? 1 : 0;
			for (ViewType sc : vtlist) {
				if (sc.getName() != null) {
					elementNames[i] = sc.getName();
					i++;
				}
			}
		}

		return elementNames;
	}
	/**
	 * erstellt aus einer {@link SchoolTestType}-Liste eine ArrayList mit den Namen
	 * 
	 * @param vtlist
	 *            die Liste von {@link SchoolTestType}s
	 * @param emptyElement
	 *            gibt an ob ein Leerelement am Anfang in die Liste eingefuegt werden soll
	 * @return die ArrayList mit den Elementnamen
	 */
	protected static ArrayList<String> getTestTypeElementNames(List<SchoolTestType> vtlist, boolean emptyElement) {
		ArrayList<String> elementNames = new ArrayList<String>();
		// Leerelement zur Liste hinzufuegen
		if (emptyElement) {
			elementNames.add("");
		}
		if (vtlist != null) {
			for (SchoolTestType sc : vtlist) {
				if (sc.getTitle() != null) {
					elementNames.add(sc.getTitle());
				}
			}
		}

		return elementNames;
	}

	
	
	
	/**
	 * waehlt den mitgegebenen Wert von einem Spinner aus
	 * 
	 * @param spin
	 *            der Spinner bei dem der Wert ausgewaehlt wird
	 * @param value
	 *            der Wert der ausgewehlt werden soll
	 * @return true wenn eine Element gewaehlt wurde, wenn nicht false
	 */
	protected boolean selectSpinnerByValue(Spinner spin, String value) {
		if (spin != null && value != null) {
			for (int i = 0; i < spin.getCount(); i++) {
				String elem = (String) spin.getAdapter().getItem(i);
				if (value.equals(elem)) {
					Log.d("Philip", "spinner: selected " + value);
					spin.setSelection(i);
					return true;
				}
			}
		}
		Log.d("Philip", "spinner: couldn't select " + value);
		return false;
	}

	
	
	
	/**
	 * zeigt eine Benachrichtung (eine {@link android.widget.Toast}-Message)  an
	 * @param denToastBitte die Nachricht, die angezeigt werden soll
	 * @param wieGutDurch wie lang die Nachricht angezeigt werden soll
	 */
	public void bitteToasten(String denToastBitte, int wieGutDurch) {
		Toast toast = Toast.makeText(getApplicationContext(), denToastBitte, wieGutDurch);
		toast.show();
	}
	
}
