package edu.htl3r.schoolplanner.gui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSet;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetManager;
import edu.htl3r.schoolplanner.gui.settings.SettingsScreen;

public abstract class SchoolPlannerActivity extends Activity {

	protected LoginSetManager loginmanager;
	
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
	    	addLoginSetNamesToIntent(i);
	    	startActivity(i);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	private void addLoginSetNamesToIntent(Intent i) {
    	List<String> loginSetNames = new ArrayList<String>();
    	for(LoginSet loginSet : loginmanager.getAllLoginSets()) {
    		loginSetNames.add(loginSet.getName());
    	}
    	i.putExtra("loginSetNames", loginSetNames.toArray(new String[0]));		
	}
}
