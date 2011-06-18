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
	
	private ListView mainListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		//setContentView(R.layout.welcome2);

		//tl = (LinearLayout) findViewById(R.id.linearLayout1);
		
		mainListView = (ListView) findViewById(R.id.main_listview);
		

		//DataSetEntry a = addEntry("HTL Rennweg", "urania.webuntis.com", "htl3r", "htl3r");
		//DataSetEntry b = addEntry("WebUntis Testschule", "webuntis.grupet.at:8080", "demo", "user");
		
		List<Map<String, String>> myList = new ArrayList<Map<String, String>>();
		Map<String, String> dataEntry = new HashMap<String, String>();
		dataEntry.put("name", "HTL Rennweg");
		dataEntry.put("url", "urania.webuntis.com");
		dataEntry.put("school", "htl3r");
		dataEntry.put("user", "htl3r");
		
		myList.add(dataEntry);
		
		SimpleAdapter aa = new SimpleAdapter(this, myList, R.layout.table_row,
                new String[] {"name", "url", "school", "user"},
                new int[] {R.id.txt_name, R.id.txt_url, R.id.txt_school, R.id.txt_user});
		mainListView.setAdapter(aa);
	}
	
	private DataSetEntry addEntry(String name, String url, String school, String user) {
		Map<String, String> dataEntry = new HashMap<String, String>();
		dataEntry.put("name", name);
		dataEntry.put("url", url);
		dataEntry.put("school", school);
		dataEntry.put("user", user);
		
		DataSetEntry entry = new DataSetEntry(this);
		entry.setDataEntry(dataEntry);
		entry.draw();
		
		
		
		//tl.addView(entry, new LinearLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT,
		//		LinearLayout.LayoutParams.WRAP_CONTENT));
		/*tl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("app",""+v.getClass());
				
			}
		});*/
		
		return entry;
	}
}

