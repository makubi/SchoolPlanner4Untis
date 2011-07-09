package edu.htl3r.schoolplanner.backend.preferences.loginSets;

import java.io.Serializable;
import java.util.Map;

import edu.htl3r.schoolplanner.constants.LoginSetConstants;

public class LoginSet implements Serializable {
	
	private static final long serialVersionUID = 6417138756120086971L;

	private String name;
	private String serverUrl;
	private String school;
	private String username;
	private String password;
	private boolean sslOnly;
	
	public LoginSet(Map<String, String> data) {
		name = data.get(LoginSetConstants.nameKey);
		serverUrl = data.get(LoginSetConstants.serverUrlKey);
		school = data.get(LoginSetConstants.schoolKey);
		username = data.get(LoginSetConstants.usernameKey);
		
		String password = data.get(LoginSetConstants.passwordKey);
		this.password = password != null ? password : "";
		
		this.sslOnly = Integer.parseInt(data.get(LoginSetConstants.sslOnlyKey))>0;
	}
	
	public LoginSet(String name, String serverUrl, String school, String username, String password, boolean sslOnly) {
		this.name = name;
		this.serverUrl = serverUrl;
		this.school = school;
		this.username = username;
		this.password = password != null ? password : "";
		this.sslOnly = sslOnly;
	}

	public String getName() {
		return name;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public String getSchool() {
		return school;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean isSslOnly() {
		return sslOnly;
	}
}
