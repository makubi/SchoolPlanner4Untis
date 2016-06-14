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
package edu.htl3r.schoolplanner.gui;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.cache.Cache;
import edu.htl3r.schoolplanner.backend.preferences.Settings;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetManager;
import edu.htl3r.schoolplanner.gui.loginTask.LoginTask;
import edu.htl3r.schoolplanner.gui.startup_wizard.StartupWizardIntroduction;
import edu.htl3r.schoolplanner.gui.startup_wizard.expert.StartupWizardLoginInformationExpert;
import edu.htl3r.schoolplanner.gui.startup_wizard.qrcode.QRCodeUrlAnalyser;
import edu.htl3r.schoolplanner.gui.welcomeScreen.LoginSetUpdateAsyncTask;
import edu.htl3r.schoolplanner.gui.welcomeScreen.WelcomeScreenContextMenu;
import edu.htl3r.schoolplanner.gui.welcomeScreen.WelcomeScreenLoginTaskListener;

public class WelcomeScreen extends SchoolPlannerActivity {
	
	private final String NFC_INTENT = "android.nfc.action.NDEF_DISCOVERED";
	
	private ListView mainListView;
	
	private ScrollView emptyListView;
	
	private LoginSetManager loginmanager;
	private LoginTask loginListener;
	
	private final int CONTEXT_MENU_ID = 1;
	
	private final int STARTUP_WIZARD_INTRODUCTION_REQUEST_CODE = 1;
	private final int LOGIN_SET_EDITOR_REQUEST_CODE = 2;

	private WelcomeScreenContextMenu contextMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_screen);
		initTitle(getResources().getString(R.string.app_name));

		mainListView = (ListView) findViewById(R.id.loginList);
		
		initEmptyListTextView();
		
		loginmanager = ((SchoolPlannerApp) getApplication()).getLoginSetManager();
		
		registerForContextMenu(mainListView);
		
		initList();
		
		loginListener = new LoginTask(this);
		loginListener.addListener(new WelcomeScreenLoginTaskListener(this));
		mainListView.setOnItemClickListener(loginListener);
		initContextMenu();
		
		
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		
		
		Intent intent = getIntent();
			QRCodeUrlAnalyser qrCodeUrlAnalyser = new QRCodeUrlAnalyser();
			if(Intent.ACTION_VIEW.equals(intent.getAction()) || NFC_INTENT.equals(intent.getAction())){
			qrCodeUrlAnalyser.startWizardCauseOfUriInput(intent.getDataString(), this);			
		}else if (loginmanager.getAllLoginSets().size() < 1) {
			showStartupWizard();
		}
		
		Settings settings = ((SchoolPlannerApp)getApplication()).getSettings();
		String autoLoginSetString = settings.getAutoLoginSet();
		
		if(settings.isAutoLogin() && !autoLoginSetString.equals("")) {
			LoginSet loginSet = loginmanager.getLoginSet(autoLoginSetString);
			// Kann auftreten, wenn LoginSet als AutoLogin ausgewaehlt und dann geloescht wurde
			if(loginSet != null) {
				loginListener.performLogin(loginSet);
			}
		}
	}
	
	
	
	private void showStartupWizard() {
		Intent t = new Intent(this, StartupWizardIntroduction.class);
		startActivityForResult(t, STARTUP_WIZARD_INTRODUCTION_REQUEST_CODE);
	}

	private void initEmptyListTextView() {
		emptyListView = (ScrollView) findViewById(R.id.login_set_list_empty_view);
	}
	
	@Override
	public void onBackPressed() {
		AsyncTask<?, ?, ?> loginTask = loginListener.getAsyncTask();
		if(loginTask != null && !loginTask.isCancelled() && !(loginTask.getStatus() == Status.FINISHED)) {
			loginTask.cancel(true);
		}
		else super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.welcome_screen, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.add_login_set:
	    	showStartupWizard();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	

	public void editLoginSet(int id) {
    	LoginSet selectedLoginSet = loginmanager.getLoginSetOnPosition(id);
    	
    	Intent loginSetEditor = new Intent(this, LoginSetEditor.class);
    	loginSetEditor.putExtra(LoginSetConstants.nameKey, selectedLoginSet.getName());
    	loginSetEditor.putExtra(LoginSetConstants.serverUrlKey, selectedLoginSet.getServerUrl());
    	loginSetEditor.putExtra(LoginSetConstants.schoolKey, selectedLoginSet.getSchool());
    	loginSetEditor.putExtra(LoginSetConstants.usernameKey, selectedLoginSet.getUsername());
    	loginSetEditor.putExtra(LoginSetConstants.passwordKey, selectedLoginSet.getPassword());
    	loginSetEditor.putExtra(LoginSetConstants.sslOnlyKey, selectedLoginSet.isSslOnly());
    	
    	startActivityForResult(loginSetEditor, LOGIN_SET_EDITOR_REQUEST_CODE);
	}

	private void initList(){
		
		List<Map<String, String>> allLoginSetsForListAdapter = loginmanager.getAllLoginSetsForListAdapter();
		
		if (allLoginSetsForListAdapter.size() <= 0) {
			emptyListView.setVisibility(View.VISIBLE);
		}
		else if(emptyListView.getVisibility() == View.VISIBLE) {
				emptyListView.setVisibility(View.INVISIBLE);
		}
		
		String [] list_keys = new String[] {LoginSetConstants.nameKey, LoginSetConstants.serverUrlKey, LoginSetConstants.schoolKey, LoginSetConstants.usernameKey};
		int [] list_ids =  new int[] {R.id.txt_name, R.id.txt_url, R.id.txt_school, R.id.txt_user};
		
		SimpleAdapter aa = new SimpleAdapter(this, allLoginSetsForListAdapter, R.layout.login_table_row, list_keys, list_ids);
		
		mainListView.setAdapter(aa);
	}
	
	@Override
	public void setInProgress(String message, boolean active) {
		super.setInProgress(message, active);
		setLoginListEnabled(!active);
	}
	
	public void loginSetListUpdated() {
		initList();
		((SimpleAdapter)(mainListView.getAdapter())).notifyDataSetChanged();
	}

	private void setLoginListEnabled(boolean enabled) {
		mainListView.setEnabled(enabled);
		if (enabled) {
			mainListView.setBackgroundResource(R.color.element_background);
		}
		else {
			mainListView.setBackgroundResource(R.color.disabled_element_background);
		}
	}

	
	/**
	 * @param name
	 * @param serverUrl
	 * @param school
	 * @param username
	 * @param password
	 * @param sslOnly
	 * @return 'true', wenn das Speichern erfolgreich war, 'false', wenn es zum Namen des Login-Sets schon ein anderes Login-Set gibt.
	 */
	public boolean addLoginSet(final String name, final String serverUrl, final String school,
			final String username, final String password, final boolean sslOnly) {
		LoginSetUpdateAsyncTask task = new LoginSetUpdateAsyncTask(this) {
			
			@Override
			protected Boolean doInBackground(Void... params) {
				return loginmanager.addLoginSet(name, serverUrl, school, username, password, sslOnly);
			}
		};
		task.execute();
		
		try {
			return task.get();
		} catch (InterruptedException e) {
			Log.w("login_sets","Trying to add login set: "+name+","+serverUrl+","+school+","+username+","+password+","+sslOnly,e);
		} catch (ExecutionException e) {
			Log.w("login_sets","Trying to add login set: "+name+","+serverUrl+","+school+","+username+","+password+","+sslOnly,e);
		}
		return false;
	}

	public void removeLoginSet(final int id) {
		LoginSetUpdateAsyncTask task = new LoginSetUpdateAsyncTask(this) {
			
			@Override
			protected Boolean doInBackground(Void... params) {
				loginmanager.removeLoginEntry(id);
				return true;
			}
		};
		  task.execute();
		
	}

	public boolean editLoginSet(final String name, final String serverUrl, final String school,
			final String username, final String password, final boolean checked, final String oldName, final String oldServerUrl, final String oldSchool) {
		LoginSetUpdateAsyncTask task = new LoginSetUpdateAsyncTask(this) {
			@Override
			protected Boolean doInBackground(Void... params) {
				return loginmanager.editLoginSet(name, serverUrl, school, username, password, checked, oldName, oldServerUrl, oldSchool);
			}
		};
		
		task.execute();
		
		try {
			return task.get();
		} catch (InterruptedException e) {
			Log.w("login_sets","Trying to edit login set: "+oldName+"/"+name+","+oldServerUrl+"/"+serverUrl+","+oldSchool+"/"+school+","+username+","+password+","+checked,e);
		} catch (ExecutionException e) {
			Log.w("login_sets","Trying to edit login set: "+oldName+"/"+name+","+oldServerUrl+"/"+serverUrl+","+oldSchool+"/"+school+","+username+","+password+","+checked,e);
		}
		return false;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == CONTEXT_MENU_ID) {
			return contextMenu.createMenu(getString(R.string.context_menu_loginsets));
		}
		return super.onCreateDialog(id);
	}
	
	private void initContextMenu() {
		contextMenu = new WelcomeScreenContextMenu(this,CONTEXT_MENU_ID);
		contextMenu.setListView(mainListView);
		contextMenu.init();
	}

	public void resyncMasterData(int selectedItem) {
		final LoginSet loginSet = loginmanager.getLoginSetOnPosition(selectedItem);
		
		AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				Cache cache = ((SchoolPlannerApp)getApplication()).getData();
				cache.setLoginCredentials(loginSet);
				cache.clearInternalCache();
				DataFacade<Boolean> authenticate = cache.authenticate();
				DataFacade<Boolean> resyncMasterData = null;
				
				if(authenticate.isSuccessful() && authenticate.getData()) {
					resyncMasterData = cache.resyncMasterData();
				}
				
				return resyncMasterData != null ? resyncMasterData.isSuccessful() : false;
			}
			
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				setInProgress(getString(R.string.resync_masterdata_in_progress), true);
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				
				setInProgress("", false);
				if(result) {
					showToastMessage(getString(R.string.resync_masterdata_finished_successful));
				}
				else {
					showToastMessage(getString(R.string.resync_masterdata_finished_error));
				}
			}
			
		};
		task.execute();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(loginListener.getAsyncTask() != null && loginListener.getAsyncTask().getStatus() != Status.RUNNING) setInProgress("", false);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case STARTUP_WIZARD_INTRODUCTION_REQUEST_CODE:
			loginSetListUpdated();
			break;
		case LOGIN_SET_EDITOR_REQUEST_CODE:
			if(data != null) {
				editLoginSet(data.getStringExtra(LoginSetEditor.LOGIN_SET_EDIT_NAME_KEY), data.getStringExtra(LoginSetEditor.LOGIN_SET_EDIT_SERVER_URL_KEY), data.getStringExtra(LoginSetEditor.LOGIN_SET_EDIT_SCHOOL_KEY), data.getStringExtra(LoginSetEditor.LOGIN_SET_EDIT_USERNAME_KEY), data.getStringExtra(LoginSetEditor.LOGIN_SET_EDIT_PASSWORD_KEY), data.getBooleanExtra(LoginSetEditor.LOGIN_SET_EDIT_SSL_ONLY_KEY, false), data.getStringExtra(LoginSetEditor.LOGIN_SET_EDIT_OLD_NAME_KEY), data.getStringExtra(LoginSetEditor.LOGIN_SET_EDIT_OLD_SERVER_URL_KEY), data.getStringExtra(LoginSetEditor.LOGIN_SET_EDIT_OLD_SCHOOL_KEY));
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}
