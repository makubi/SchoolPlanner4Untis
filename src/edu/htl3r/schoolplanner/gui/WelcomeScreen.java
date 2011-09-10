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
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.DataFacade;
import edu.htl3r.schoolplanner.backend.preferences.Settings;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetManager;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.asyncUpdateTasks.LoginSetUpdateAsyncTask;
import edu.htl3r.schoolplanner.constants.LoginSetConstants;
import edu.htl3r.schoolplanner.constants.WelcomeScreenConstants;
import edu.htl3r.schoolplanner.gui.listener.LoginListener;
import edu.htl3r.schoolplanner.gui.welcomeScreen.WelcomeScreenContextMenu;

public class WelcomeScreen extends SchoolPlannerActivity{
	
	private ListView mainListView;
	
	private LoginSetDialog dialog;
	
	private RelativeLayout mainLayout;
	private TextView emptyListTextView;
	
	private LoginSetManager loginmanager;
	private LoginListener loginListener;
	
	private final int CONTEXT_MENU_ID = 1;
	
	private WelcomeScreenContextMenu contextMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_screen);
		
		mainListView = (ListView) findViewById(R.id.loginList);
		
		buildEmptyListTextView();
		mainLayout = (RelativeLayout) findViewById(R.id.welcome_main_layout);
		
		loginmanager = ((SchoolPlannerApp) getApplication()).getLoginSetManager();
				
		// TODO: temporarily for easier login
		if(loginmanager.getAllLoginSets().size() <= 0)
			loginmanager.addLoginSet("WebUntis Testschule", "webuntis.grupet.at:8080", "demo", "user", "", false);
		
		registerForContextMenu(mainListView);
		
		initList();
		
		loginListener = new LoginListener(this);
		mainListView.setOnItemClickListener(loginListener);
		
		initContextMenu();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
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
	
	private void buildEmptyListTextView() {
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
		        ViewGroup.LayoutParams.WRAP_CONTENT);

		// Set parent view
		p.addRule(RelativeLayout.BELOW, R.id.welcome_title);
		
		emptyListTextView = new TextView(this);
		emptyListTextView.setTextSize(14);
		emptyListTextView.setPadding(5, 0, 0, 0);
		emptyListTextView.setTextColor(getResources().getColor(R.color.text));
		emptyListTextView.setText(R.string.login_sets_list_empty);
		emptyListTextView.setLayoutParams(p);
		emptyListTextView.setTag(WelcomeScreenConstants.EMPTY_LIST_TEXTVIEW_ADDED);
	}
	
	@Override
	public void onBackPressed() {
		AsyncTask<Void, AsyncTaskProgress, Void> loginTask = loginListener.getLoginTask();
		if(!loginTask.isCancelled() && !(loginTask.getStatus() == Status.FINISHED)) {
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
	    	dialog = new LoginSetDialog(this);
	    	dialog.setParent(this);
	    	dialog.show();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	

	public void editLoginSet(int id) {
		dialog = new LoginSetDialog(this,loginmanager.getLoginSetOnPosition(id));
    	dialog.setParent(this);
    	dialog.setTitle(getString(R.string.login_set_edit_title));
    	dialog.show();
	}

	private void initList(){
		
		List<Map<String, String>> allLoginSetsForListAdapter = loginmanager.getAllLoginSetsForListAdapter();
		
		if (allLoginSetsForListAdapter.size() <= 0) {
			emptyListTextView.setTag(WelcomeScreenConstants.EMPTY_LIST_TEXTVIEW_ADDED);
			mainLayout.addView(emptyListTextView);
		}
		else if(WelcomeScreenConstants.EMPTY_LIST_TEXTVIEW_ADDED.equals(emptyListTextView.getTag())) {
				emptyListTextView.setTag(WelcomeScreenConstants.EMPTY_LIST_TEXTVIEW_REMOVED);
				mainLayout.removeView(emptyListTextView);
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
	
	public LoginSetManager getLoginManager(){
		return loginmanager;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				DataFacade<Boolean> resyncMasterData = null;
				if(cache.authenticate().isSuccessful() && cache.authenticate().getData()) {
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
	
	public void showToastMessage(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
	
}
