/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
			Sebastian Chlan <sebastian@schoolplanner.at>
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
package edu.htl3r.schoolplanner.gui.timetable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class GUIErrorHandler extends BroadcastReceiver {

	public static final String ERRORINTENT = "edu.htl3r.schoolplanner.gui.ERRORMSG";
	public static final String ERROR_TITLE_FIELD = "etitle";
	public static final String ERROR_MESSAGE_FIELD = "msg";
	public static final String ERROR_MESSAGE_TYPE_FIELD = "type";
	public static final int ERROR_MESSAGE_TYPE_TOAST = 0;
	public static final int ERROR_MESSAGE_TYPE_DIALOG = 1;

	@Override
	  public void onReceive(Context context, Intent intent) {
		  
		 int type = intent.getIntExtra(ERROR_MESSAGE_TYPE_FIELD, ERROR_MESSAGE_TYPE_TOAST);
		  
		 
		 switch(type){
		 case ERROR_MESSAGE_TYPE_DIALOG:
			 break;
		 case ERROR_MESSAGE_TYPE_TOAST:
			    Toast toast = Toast.makeText(context, intent.getExtras().getString(ERROR_TITLE_FIELD), Toast.LENGTH_LONG);
			    toast.show();
			 break;
		 
		 }
		 

	  }
}
