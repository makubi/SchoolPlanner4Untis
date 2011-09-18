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
