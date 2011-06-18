package edu.htl3r.schoolplanner.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import edu.htl3r.schoolplanner.R;

public class WelcomeScreen extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		//setContentView(R.layout.welcome2);
		
		List<Map<String,String>> data = new ArrayList<Map<String,String>>();
		
		Map<String, String> data1 = new HashMap<String, String>();
		data1.put("name", "HTL Rennweg");
		data1.put("url", "urania.webuntis.com");
		data1.put("school", "htl3r");
		data1.put("user", "htl3r");
		
		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("name", "WebUntis Testschule");
		data2.put("url", "webuntis.grupet.at:8080");
		data2.put("school", "demo");
		data2.put("user", "user");

		data.add(data1);
		data.add(data2);
		
		TableLayout tl = (TableLayout) findViewById(R.id.tableLayout1);

		for(Map<String, String> dataEntry : data) {
			DataSetEntry entry = new DataSetEntry(this);
			entry.setTableLayout(tl);
			entry.setDataEntry(dataEntry);
			entry.draw();
		}
		
		
	}
}

