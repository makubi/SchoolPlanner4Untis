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
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.tani.app.ui.IconContextMenu;

import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetManager;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.asyncUpdateTasks.LoginSetUpdateAsyncTask;
import edu.htl3r.schoolplanner.constants.LoginSetConstants;
import edu.htl3r.schoolplanner.constants.WelcomeScreenConstants;
import edu.htl3r.schoolplanner.gui.listener.LoginListener;

public class WelcomeScreen extends SchoolPlannerActivity{

	private final String nameKey = LoginSetConstants.nameKey;
	private final String urlKey = LoginSetConstants.serverUrlKey;
	private final String schoolKey = LoginSetConstants.schoolKey;
	private final String userKey = LoginSetConstants.usernameKey;

	private ProgressBar progressWheel;
	private TextView loginProgressText;
	private ListView mainListView;
	
	private LoginSetDialog dialog;
	
	private RelativeLayout mainLayout;
	private TextView emptyListTextView;
	
	private LoginSetManager loginmanager;
	
	private final int CONTEXT_MENU_ID = 1;
	private final int CONTEXT_MENU_EDIT = 1;
	private final int CONTEXT_MENU_REMOVE = 2;
	
	private IconContextMenu contextMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_screen);
		
		progressWheel = (ProgressBar) findViewById(R.id.loginProgress);
		loginProgressText = (TextView) findViewById(R.id.loginProgressText);
		mainListView = (ListView) findViewById(R.id.loginList);
		
		buildEmptyListTextView();
		mainLayout = (RelativeLayout) findViewById(R.id.welcome_main_layout);
		
		loginmanager = new LoginSetManager();
		((SchoolPlannerApp) getApplication()).setLoginManager(loginmanager);
				
		// TODO: temporarily for easier login
		if(loginmanager.getAllLoginSets().size() <= 0)
			loginmanager.addLoginSet("WebUntis Testschule", "http://webuntis.grupet.at:8080", "demo", "user", "", false);
		
		registerForContextMenu(mainListView);
		
		initList();
		mainListView.setOnItemClickListener(new LoginListener(this));
		
		initContextMenu();
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
		// TODO: Text auslagern
		emptyListTextView.setText("Your list of login sets is empty!" +
				"\n\nYou need a login set to be able to log into the app." +
				"\n\nA login set contains of five information:" +
				"\n - name (arbitrary): e.g. My School Login" + 
				"\n - server url: e.g. urania.webuntis.com:1234" +
				"\n - school name: e.g. my_school" +
				"\n - user name: my_login_name" +
				"\n - password (optional): my_password" +
				"\n\n + You have to provide a name to differentiate between multiple login sets." +
				"\n + The server url is the same url you use to log into WebUntis via a webbrowser." +
				"\n + You have to use the same login information you use to log into the WebUntis website." +
				"\n + If your school provides SSL, you can enforce to log in securely.");
		emptyListTextView.setLayoutParams(p);
		emptyListTextView.setTag(WelcomeScreenConstants.EMPTY_LIST_TEXTVIEW_ADDED);
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
	
	

	private void editLoginSet(int id) {
		dialog = new LoginSetDialog(this,loginmanager.getLoginSetOnPosition(id));
    	dialog.setParent(this);
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
		
		String [] list_keys = new String[] {nameKey, urlKey, schoolKey, userKey};
		int [] list_ids =  new int[] {R.id.txt_name, R.id.txt_url, R.id.txt_school, R.id.txt_user};
		
		SimpleAdapter aa = new SimpleAdapter(this, allLoginSetsForListAdapter, R.layout.login_table_row, list_keys, list_ids);
		
		mainListView.setAdapter(aa);
	}

	
	/**
	 * @param active 'true' if the login is currently in progress
	 */
	@Deprecated
	public void setOnLogin(boolean active) {
		setLoginListEnabled(!active);
		if (active) {
			loginProgressText.setText("Logging in...");
			progressWheel.setVisibility(View.VISIBLE);
		} else {
			loginProgressText.setText("");
			progressWheel.setVisibility(View.INVISIBLE);
		}
	}
	
	public void setInProgress(String message, boolean active) {
		setLoginListEnabled(!active);
		if (active) {
			loginProgressText.setText(message);
			progressWheel.setVisibility(View.VISIBLE);
		} else {
			loginProgressText.setText("");
			progressWheel.setVisibility(View.INVISIBLE);
		}
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

	private void removeLoginSet(final int id) {
		LoginSetUpdateAsyncTask task = new LoginSetUpdateAsyncTask(this) {
			
			@Override
			protected Boolean doInBackground(Void... params) {
				loginmanager.removeLoginEntry(id);
				return true;
			}
		};
		  task.execute();
		
	}

	public void editLoginSet(final String name, final String serverUrl, final String school,
			final String username, final String password, final boolean checked) {
		LoginSetUpdateAsyncTask task = new LoginSetUpdateAsyncTask(this) {
			@Override
			protected Boolean doInBackground(Void... params) {
				loginmanager.editLoginSet(name, serverUrl, school, username, password, checked);
				return true;
			}
		};
		
		task.execute();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == CONTEXT_MENU_ID) {
			return contextMenu.createMenu("Manage Login Sets");
		}
		return super.onCreateDialog(id);
	}
	
	private void initContextMenu() {
		contextMenu = new IconContextMenu(this,CONTEXT_MENU_ID);
		contextMenu.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {

			@Override
			public void onClick(int menuId) {
				switch(menuId) {
				case CONTEXT_MENU_EDIT:
					editLoginSet((int) contextMenu.getSelectedItem());
					break;
				case CONTEXT_MENU_REMOVE:
					removeLoginSet((int) contextMenu.getSelectedItem());
					break;
				}
			}
			
		});
		
		mainListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				contextMenu.setSelectedItem(id);
				showDialog(CONTEXT_MENU_ID);
				return true;
			}
		});
		
		Resources resources = getResources();
		
		contextMenu.addItem(resources, getString(R.string.menu_edit_login_set), R.drawable.ic_menu_preferences, CONTEXT_MENU_EDIT);
		contextMenu.addItem(resources, getString(R.string.menu_remove_login_set), R.drawable.ic_menu_preferences,  CONTEXT_MENU_REMOVE);
	}
	
}
