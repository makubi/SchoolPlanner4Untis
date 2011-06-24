package edu.htl3r.schoolplanner.gui;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetManager;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetUpdateObserver;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.asyncUpdateTasks.LoginSetUpdateAsyncTask;
import edu.htl3r.schoolplanner.gui.listener.LoginListener;

public class WelcomeScreen extends SchoolPlannerActivity implements LoginSetUpdateObserver{

	private final String nameKey = Constants.nameKey;
	private final String urlKey = Constants.serverUrlKey;
	private final String schoolKey = Constants.schoolKey;
	private final String userKey = Constants.usernameKey;

	private ProgressBar progressWheel;
	private TextView loginProgressText;
	private ListView mainListView;
	
	private LoginSetManager loginmanager;
	private AddLoginSetDialog dialog;
	
	private RelativeLayout mainLayout;
	private TextView emptyListTextView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_screen);
		
		progressWheel = (ProgressBar) findViewById(R.id.loginProgress);
		loginProgressText = (TextView) findViewById(R.id.loginProgressText);
		mainListView = (ListView) findViewById(R.id.loginList);
		
		buildEmptyListTextView();
		mainLayout = (RelativeLayout) findViewById(R.id.welcome_main_layout);
		
		
		mainLayout.removeView(emptyListTextView);
		
		loginmanager = new LoginSetManager();
		loginmanager.addSetUpdateObserver(this);
		
		registerForContextMenu(mainListView);
		
		initList();
		mainListView.setOnItemClickListener(new LoginListener(this));
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
		emptyListTextView.setTag("added");
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
	    	dialog = dialog == null ? new AddLoginSetDialog(this) : dialog;
	    	dialog.setParent(this);
	    	dialog.show();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.welcome_screen_context, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	  
	  switch (item.getItemId()) {
	  case R.id.remove_login_set:
		  removeLoginSet((int)info.id);
		  return true;
	  default:
		  return super.onContextItemSelected(item);
	  }
	}

	private void initList(){
		
		List<Map<String, String>> allLoginSetsForListAdapter = loginmanager.getAllLoginSetsForListAdapter();
		
		if (allLoginSetsForListAdapter.size() > 0 && "added".equals(emptyListTextView.getTag())) {
				emptyListTextView.setTag("removed");
				mainLayout.removeView(emptyListTextView);
		}
		else {
			emptyListTextView.setTag("added");
			mainLayout.addView(emptyListTextView);
		}
		
		String [] list_keys = new String[] {nameKey, urlKey, schoolKey, userKey};
		int [] list_ids =  new int[] {R.id.txt_name, R.id.txt_url, R.id.txt_school, R.id.txt_user};
		
		SimpleAdapter aa = new SimpleAdapter(this, allLoginSetsForListAdapter, R.layout.login_table_row, list_keys, list_ids);
		
		mainListView.setAdapter(aa);
	}

	
	/**
	 * @param active 'true' if the login is currently in progress
	 */
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
	
	public void loginSetListUpdated() {
		initList();
		((SimpleAdapter)(mainListView.getAdapter())).notifyDataSetChanged();
	}
	
	public LoginSetManager getLoginManager(){
		return loginmanager;
	}

	@Override
	public void loginSetAdded() {
		  
	}

	public void setLoginListEnabled(boolean enabled) {
		mainListView.setEnabled(enabled);
		if (enabled) {
			mainListView.setBackgroundResource(R.color.element_background);
		}
		else {
			mainListView.setBackgroundResource(R.color.disabled_element_background);
		}
	}

	public void addLoginSet(final String name, final String serverUrl, final String school,
			final String username, final String password, final boolean sslOnly) {
		LoginSetUpdateAsyncTask task = new LoginSetUpdateAsyncTask(this) {
			
			@Override
			protected Void doInBackground(Void... params) {
				loginmanager.addLoginSet(name, serverUrl, school, username, password, sslOnly);
				return null;
			}
		};
		  task.execute();
	}

	private void removeLoginSet(final int id) {
		LoginSetUpdateAsyncTask task = new LoginSetUpdateAsyncTask(this) {
			
			@Override
			protected Void doInBackground(Void... params) {
				loginmanager.removeLoginEntry(id);
				return null;
			}
		};
		  task.execute();
		
	}
	
}
