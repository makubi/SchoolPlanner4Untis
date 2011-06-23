/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
			Sebastian Chlan <sebastian@schoolplanner.at>
			Christian Pascher <christian@schoolplanner.at>
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package edu.htl3r.schoolplanner.gui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import edu.htl3r.schoolplanner.R;

public class AddLoginSetDialog extends Dialog{
	
	private EditText nameInput;
	private EditText serverUrlInput;
	private EditText schoolInput;
	private EditText usernameInput;
	private EditText passwordInput;
	private CheckBox sslOnly;
	
	private Toast errorMessage;

	private WelcomeScreen parent;
	
	public AddLoginSetDialog(Context context) {
		super(context);
		
		errorMessage = Toast.makeText(context, "Test", Toast.LENGTH_SHORT);
        
		setContentView(R.layout.login_set_add_dialog);
        setTitle(R.string.login_set_add_title);
        setCancelable(true);
        
        nameInput = (EditText) findViewById(R.id.login_set_add_name_input);
		serverUrlInput = (EditText) findViewById(R.id.login_set_add_server_url_input);
		schoolInput = (EditText) findViewById(R.id.login_set_add_school_input);
		usernameInput = (EditText) findViewById(R.id.login_set_add_username_input);
		passwordInput = (EditText) findViewById(R.id.login_set_add_password_input);
		sslOnly = (CheckBox) findViewById(R.id.login_set_add_ssl_only);
        
        final Button button = (Button) findViewById(R.id.login_set_add_save);
    	button.setOnClickListener(new android.widget.Button.OnClickListener() {
    	    public void onClick(View v) {    	    	
    	    	String name = nameInput.getText().toString();
    	    	String serverUrl = serverUrlInput.getText().toString();
    	    	String school = schoolInput.getText().toString();
    	    	String username = usernameInput.getText().toString();
    	    	String password = passwordInput.getText().toString();
    	    	
    	    	// TODO: Andere checks + SSL
    	    	if(name.length() > 0 && serverUrl.length() > 0 && school.length() > 0 && username.length() > 0) {
    	    		parent.addLoginSet(name, serverUrl, school, username, password, sslOnly.isChecked());
    	    		dismiss();
    	    	}
    	    	else {
    	    		errorMessage.show();
    	    	}
    	    }
    	});
	}
	
	
	public void setParent(WelcomeScreen parent) {
		this.parent = parent;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		nameInput.setText("");
		serverUrlInput.setText("");
		schoolInput.setText("");
		usernameInput.setText("");
		passwordInput.setText("");
		
		//sslOnly.setChecked(false);
	}

	
}
