package edu.htl3r.schoolplanner.gui;

import java.io.Serializable;
import java.util.Map;

public class LoginEntry implements Serializable {
	
	private static final long serialVersionUID = 6417138756120086971L;

	private String name;
	private String serverUrl;
	private String school;
	private String username;
	private String password;
	
	public LoginEntry(Map<String, String> data) {
		name = data.get(Constants.nameKey);
		serverUrl = data.get(Constants.serverUrlKey);
		school = data.get(Constants.schoolKey);
		username = data.get(Constants.usernameKey);
		password = data.get(Constants.passwordKey);
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
	
}
