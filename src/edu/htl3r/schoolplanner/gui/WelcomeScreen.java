package edu.htl3r.schoolplanner.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import edu.htl3r.schoolplanner.R;

public class WelcomeScreen extends Activity{
	
	private final String nameKey = Constants.nameKey;
	private final String urlKey = Constants.serverUrlKey;
	private final String schoolKey = Constants.schoolKey;
	private final String userKey = Constants.usernameKey;
	
	private ProgressBar progressWheel;
	private TextView loginProgressText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		progressWheel = (ProgressBar) findViewById(R.id.loginProgress);
		loginProgressText = (TextView) findViewById(R.id.loginProgressText);
		
		final ListView mainListView = (ListView) findViewById(R.id.loginList);
		
		final List<Map<String, String>> entrySetList = getEntrySetList();
		
		SimpleAdapter aa = new SimpleAdapter(this, entrySetList, R.layout.login_table_row,
                new String[] {nameKey, urlKey, schoolKey, userKey},
                new int[] {R.id.txt_name, R.id.txt_url, R.id.txt_school, R.id.txt_user});
		mainListView.setAdapter(aa);
		
		mainListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Map<String, String> selectedEntry = entrySetList.get(position);
				
				
				
				AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
					
					@Override
					protected Void doInBackground(Void... params) {
						
						// TODO: Do login here
						try {
							Thread.sleep(3000);
							Intent t = new Intent(getApplicationContext(), SelectScreen.class);
							
							LoginEntry loginEntry = new LoginEntry(selectedEntry);
							
							Bundle extras = new Bundle();
							extras.putSerializable("entry", loginEntry);
							t.putExtras(extras);
							startActivity(t);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}
					
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						mainListView.setEnabled(false);
						mainListView.setBackgroundColor(Color.GRAY);
						loginProgressText.setText("Logging in...");
						progressWheel.setVisibility(View.VISIBLE);
					}
					
					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						mainListView.setEnabled(true);
						mainListView.setBackgroundColor(Color.TRANSPARENT);
						loginProgressText.setText("");
						progressWheel.setVisibility(View.INVISIBLE);
					}
				};
				
				task.execute();
			}
		});
	}
	
	private List<Map<String, String>> getEntrySetList() {
		List<Map<String, String>> entrySetList = new ArrayList<Map<String, String>>();
		
		entrySetList.add(getDataEntry("HTL Rennweg", "urania.webuntis.com", "htl3r", "htl3r"));
		entrySetList.add(getDataEntry("WebUntis Testschule", "webuntis.grupet.at:8080", "demo", "user"));
		
		return entrySetList;
	}
	
	private Map<String, String> getDataEntry(String name, String url, String school, String user) {
		Map<String, String> dataEntry = new HashMap<String, String>();
		dataEntry.put(nameKey, name);
		dataEntry.put(urlKey, url);
		dataEntry.put(schoolKey, school);
		dataEntry.put(userKey, user);
		
		return dataEntry;
	}
}

