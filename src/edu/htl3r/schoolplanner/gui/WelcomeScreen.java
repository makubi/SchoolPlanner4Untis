package edu.htl3r.schoolplanner.gui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetAddAsyncTask;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetManager;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetRemoveAsyncTask;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetUpdateAsyncTask;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetUpdateObserver;
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_screen);
		
		progressWheel = (ProgressBar) findViewById(R.id.loginProgress);
		loginProgressText = (TextView) findViewById(R.id.loginProgressText);
		mainListView = (ListView) findViewById(R.id.loginList);
		
		loginmanager = new LoginSetManager();
		loginmanager.addSetUpdateObserver(this);
		
		registerForContextMenu(mainListView);
		
		initList();
		mainListView.setOnItemClickListener(new LoginListener(this));
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
	    	dialog.setLoginSetManager(loginmanager);
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
	  
	  LoginSetUpdateAsyncTask task = new LoginSetRemoveAsyncTask(this, loginmanager, (int) info.id);
	  task.execute();
	  
	  return super.onContextItemSelected(item);
	}

	private void initList(){
		String [] list_keys = new String[] {nameKey, urlKey, schoolKey, userKey};
		int [] list_ids =  new int[] {R.id.txt_name, R.id.txt_url, R.id.txt_school, R.id.txt_user};
		
		SimpleAdapter aa = new SimpleAdapter(this, loginmanager.getAllLoginSetsForListAdapter(), R.layout.login_table_row,list_keys ,list_ids);
		
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
		  LoginSetUpdateAsyncTask task = new LoginSetAddAsyncTask(this);
		  task.execute();
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
	
}
