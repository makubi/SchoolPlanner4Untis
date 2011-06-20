package edu.htl3r.schoolplanner.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.listener.LoginListener;

public class WelcomeScreen extends SchoolPlannerActivity {

	private final String nameKey = Constants.nameKey;
	private final String urlKey = Constants.serverUrlKey;
	private final String schoolKey = Constants.schoolKey;
	private final String userKey = Constants.usernameKey;

	private ProgressBar progressWheel;
	private TextView loginProgressText;
	private ListView mainListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		progressWheel = (ProgressBar) findViewById(R.id.loginProgress);
		loginProgressText = (TextView) findViewById(R.id.loginProgressText);
		mainListView = (ListView) findViewById(R.id.loginList);

		initList();
		
	}

	private void initList(){
		final List<Map<String, String>> entrySetList = getEntrySetList();
		
		String [] list_keys = new String[] {nameKey, urlKey, schoolKey, userKey};
		int [] list_ids =  new int[] {R.id.txt_name, R.id.txt_url, R.id.txt_school, R.id.txt_user};
		
		SimpleAdapter aa = new SimpleAdapter(this, entrySetList, R.layout.login_table_row,list_keys ,list_ids);
		
		mainListView.setAdapter(aa);
		mainListView.setOnItemClickListener(new LoginListener(this));
	}

	private Map<String, String> getDataEntry(String name, String url, String school, String user) {
		Map<String, String> dataEntry = new HashMap<String, String>();
		dataEntry.put(nameKey, name);
		dataEntry.put(urlKey, url);
		dataEntry.put(schoolKey, school);
		dataEntry.put(userKey, user);

		return dataEntry;
	}

	public List<Map<String, String>> getEntrySetList() {
		List<Map<String, String>> entrySetList = new ArrayList<Map<String, String>>();

		entrySetList.add(getDataEntry("HTL Rennweg", "urania.webuntis.com", "htl3r", "htl3r"));
		entrySetList.add(getDataEntry("WebUntis Testschule", "webuntis.grupet.at:8080", "demo", "user"));

		return entrySetList;
	}
	
	public void setOnLogin(boolean active) {
		if (active) {
			mainListView.setEnabled(false);
			mainListView.setBackgroundColor(Color.GRAY);
			loginProgressText.setText("Logging in...");
			progressWheel.setVisibility(View.VISIBLE);
		} else {
			mainListView.setEnabled(true);
			mainListView.setBackgroundColor(Color.parseColor("#efebef"));
			loginProgressText.setText("");
			progressWheel.setVisibility(View.INVISIBLE);
		}
	}
}
