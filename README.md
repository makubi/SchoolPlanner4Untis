![Logo](http://schoolplanner.makubi.at/images/header_logo.png)&nbsp;&nbsp;&nbsp;[SchoolPlanner4Untis](https://github.com/makubi/SchoolPlanner4Untis/)
==================================================

IMPORT ANDROID PROJECT IN ECLIPSE
----------------------------------

* Used software
	* Eclipse Classic
	* Maven 3
	* m2e
	* Android SDK
	* ADT Plugin
	* Android SDK 7
* Import project to Eclipse
	* Import as Android or Maven project
	* Add Maven/Android nature to project
	* Refresh project
	* In Eclipse "Problems" -> "Discover new m2e connectors"
	* Install "Android Connector"

PREPARE RELEASE
---------------
* mvn android:manifest-update replacer:replace
* versionCode + 1

ADDITIONAL INFORMATION
----------------------
* Android Market: https://play.google.com/store/apps/details?id=edu.htl3r.schoolplanner
* Bugtracker: https://github.com/makubi/SchoolPlanner4Untis/issues
* Continuous Integration System: http://jenkins.coding4coffee.org/job/SchoolPlanner4Untis/

CONTACT
-------
* Mathias Kub (Project leader) - mail[at]makubi.at
* Sebastian Chlan (Developer) - schoolplanner[at]droelf.at

LICENSE
-------
* This app is published under the GPLv3
