package edu.htl3r.schoolplanner.gui;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.LoginSetManager;
import edu.htl3r.schoolplanner.gui.listener.LoginListener;

public class WelcomeScreen extends SchoolPlannerActivity {

	private final String nameKey = Constants.nameKey;
	private final String urlKey = Constants.serverUrlKey;
	private final String schoolKey = Constants.schoolKey;
	private final String userKey = Constants.usernameKey;

	private ProgressBar progressWheel;
	private TextView loginProgressText;
	private ListView mainListView;
	private LoginSetManager loginmanager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		progressWheel = (ProgressBar) findViewById(R.id.loginProgress);
		loginProgressText = (TextView) findViewById(R.id.loginProgressText);
		mainListView = (ListView) findViewById(R.id.loginList);
		
		loginmanager = new LoginSetManager();
		Log.d("basti",loginmanager.getAllLoginSets()+"");
		initList();
		

	}

	private void initList(){
		final List<Map<String, String>> entrySetList = loginmanager.getAllLoginSetsForListAdapter();
		
		String [] list_keys = new String[] {nameKey, urlKey, schoolKey, userKey};
		int [] list_ids =  new int[] {R.id.txt_name, R.id.txt_url, R.id.txt_school, R.id.txt_user};
		
		SimpleAdapter aa = new SimpleAdapter(this, entrySetList, R.layout.login_table_row,list_keys ,list_ids);
		
		mainListView.setAdapter(aa);
		mainListView.setOnItemClickListener(new LoginListener(this));
	}

	
	/**
	 * @param active 'true' if the login is currently in progress
	 */
	public void setOnLogin(boolean active) {
		mainListView.setEnabled(!active);
		if (active) {
			mainListView.setBackgroundResource(R.color.disabled_element_background);
			loginProgressText.setText("Logging in...");
			progressWheel.setVisibility(View.VISIBLE);
		} else {
			mainListView.setBackgroundResource(R.color.element_background);
			loginProgressText.setText("");
			progressWheel.setVisibility(View.INVISIBLE);
		}
	}
	
	public LoginSetManager getLoginManager(){
		return loginmanager;
	}
}
