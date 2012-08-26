package edu.htl3r.schoolplanner.gui.startup_wizard.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import edu.htl3r.schoolplanner.backend.preferences.loginSets.LoginSetConstants;
import edu.htl3r.schoolplanner.gui.startup_wizard.expert.StartupWizardLoginInformationExpert;

public class QRCodeUrlAnalyser {

	private final int STARTUP_WIZARD_INTRODUCTION_REQUEST_CODE = 1;

	public void startWizardCauseOfUriInput(String url, Activity activity){
			Uri uri = Uri.parse(Uri.decode(Uri.decode(url)));
			Log.d("basti", uri.toString());

			String school_url = uri.getQueryParameter(LoginSetConstants.serverUrlKey);
			String school = uri.getQueryParameter(LoginSetConstants.schoolKey);
			String user = uri.getQueryParameter(LoginSetConstants.usernameKey);
			String pass = uri.getQueryParameter(LoginSetConstants.passwordKey);
			String name = uri.getQueryParameter(LoginSetConstants.nameKey);
			boolean ssl = Boolean.parseBoolean(uri.getQueryParameter(LoginSetConstants.sslOnlyKey));
//			Log.d("basti",uri.toString());
//			Log.d("basti","URL: " + school_url +
//					"Schule: " + school +
//					"\nUser: " + user +
//					"\nPasswort: " + pass +
//					"\nName: " + name +
//					"\nSSL: " + ssl);
   	
	    	Intent loginSetEditor = new Intent(activity, StartupWizardLoginInformationExpert.class);
	    	loginSetEditor.putExtra(LoginSetConstants.nameKey, name);
	    	loginSetEditor.putExtra(LoginSetConstants.serverUrlKey, school_url);
	    	loginSetEditor.putExtra(LoginSetConstants.schoolKey, school);
	    	loginSetEditor.putExtra(LoginSetConstants.usernameKey, user);
	    	loginSetEditor.putExtra(LoginSetConstants.passwordKey, pass);
	    	loginSetEditor.putExtra(LoginSetConstants.sslOnlyKey, ssl);
	    	
	    	activity.startActivityForResult(loginSetEditor, STARTUP_WIZARD_INTRODUCTION_REQUEST_CODE);
	}
}
