package edu.htl3r.schoolplanner.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import edu.htl3r.schoolplanner.R;

public class WelcomeScreen extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		ListView mainListView = (ListView) findViewById(R.id.main_listview);
		
		
		List<Map<String, String>> myList = getEntrySetList();
		
		SimpleAdapter aa = new SimpleAdapter(this, myList, R.layout.table_row,
                new String[] {"name", "url", "school", "user"},
                new int[] {R.id.txt_name, R.id.txt_url, R.id.txt_school, R.id.txt_user});
		mainListView.setAdapter(aa);
	}
	
	private List<Map<String, String>> getEntrySetList() {
		List<Map<String, String>> entrySetList = new ArrayList<Map<String, String>>();
		
		entrySetList.add(getDataEntry("HTL Rennweg", "urania.webuntis.com", "htl3r", "htl3r"));
		entrySetList.add(getDataEntry("WebUntis Testschule", "webuntis.grupet.at:8080", "demo", "user"));
		
		return entrySetList;
	}
	
	private Map<String, String> getDataEntry(String name, String url, String school, String user) {
		Map<String, String> dataEntry = new HashMap<String, String>();
		dataEntry.put("name", name);
		dataEntry.put("url", url);
		dataEntry.put("school", school);
		dataEntry.put("user", user);
		
		return dataEntry;
	}
}

