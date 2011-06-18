package edu.htl3r.schoolplanner.gui;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;
import edu.htl3r.schoolplanner.R;

public class WelcomeScreen extends Activity{
	
	private TableLayout tl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		//setContentView(R.layout.welcome2);
		
		tl = (TableLayout) findViewById(R.id.tableLayout1);

		addEntry("HTL Rennweg", "urania.webuntis.com", "htl3r", "htl3r");
		addEntry("WebUntis Testschule", "webuntis.grupet.at:8080", "demo", "user");
	}
	
	private void addEntry(String name, String url, String school, String user) {
		Map<String, String> dataEntry = new HashMap<String, String>();
		dataEntry.put("name", name);
		dataEntry.put("url", "webuntis.grupet.at:8080");
		dataEntry.put("school", "demo");
		dataEntry.put("user", "user");
		
		DataSetEntry entry = new DataSetEntry(this);
		entry.setTableLayout(tl);
		entry.setDataEntry(dataEntry);
		entry.draw();
	}
}

