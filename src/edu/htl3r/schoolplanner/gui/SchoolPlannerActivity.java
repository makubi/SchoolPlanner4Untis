package edu.htl3r.schoolplanner.gui;

import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.settings.SettingsScreen;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class SchoolPlannerActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.default_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.menu_settings:
	    	Intent i = new Intent(getApplicationContext(),SettingsScreen.class);
	    	startActivity(i);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
