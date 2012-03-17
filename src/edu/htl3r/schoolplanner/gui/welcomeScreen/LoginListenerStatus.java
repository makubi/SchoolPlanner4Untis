package edu.htl3r.schoolplanner.gui.welcomeScreen;

public interface LoginListenerStatus {
	// TODO: connection + ssl check
	
	public static final String LOGIN_SUCCESS = "loginSuccess";
	
	public static final String CLASSLIST_SUCCESS = "classListSuccess";
	public static final String TEACHERLIST_SUCCESS = "teacherListSuccess";
	public static final String ROOMLIST_SUCCESS = "roomListSuccess";
	public static final String SUBJECTLIST_SUCCESS = "subjectListSuccess";

	public static final String LOGIN_BAD_CREDENTIALS = "loginBadCredentials";

	public static final String MASTERDATA_SUCCESS = "masterdataSuccess";
}
