![Logo](http://www.schoolplanner.at/modx/assets/images/logo_square.png) [SchoolPlanner4Untis](http://www.schoolplanner.at/)
==================================================

ANDROID PROJEKT IN ECLIPSE EINBINDEN
------------------------------------

* Einrichten der Entwicklungsumgebung
	* Installation von Eclipse
	* Installation von ADT
		-> http://developer.android.com/sdk/eclipse-adt.html#installing
		* API 7 (2.1) wird benoetigt
	* Installation von Maven 3
		* Download Maven 3.0.3
			-> http://maven.apache.org/download.html
		* Entpacke Maven 3
		* Setzen der Variablen
			* M3_HOME=/path/to/maven
			* M3=$M3_HOME/bin
			* PATH=$M3:$PATH
	* Installation von M2Eclipse
		* Install new software
			* Update Site: http://m2eclipse.sonatype.org/sites/m2e
		* Restart (bei Nachfrage)
	* Hinzufügen des entpackten Mavens in Eclipse
		* In Eclipse
			* Window -> Preferences -> Maven -> Installations -> Add... -> Vorher entpacktes Maven auswählen
		* Restart (bei Nachfrage)
* Hinzufügen des Projekts
	* Setzen der Variable
		* ANDROID_HOME=$SDK_ROOT
	* In Eclipse
		* File -> Import... -> Maven --> Existing Maven Projects
	* Wenn es nicht als Android Projekt erkannt wird
		* Rechtsklick auf das Projekt -> Android -> Convert to Android Project
	* Refresh des Projekts

ERROR IM PROJEKT (in Eclipse)
-----------------------------

Sollte die Fehlermeldung

	.../gen already exists but is not a source folder. Convert to a source folder or rename it.

erscheinen:

* In Eclipse
* Ordner "gen/" löschen und Projekt refreshen

EINRICHTEN DER UMGEBUNGSVARIABLEN
---------------------------------
	
Sollte die Fehlermeldung:

	[ERROR] Error when generating sources. No Android SDK path could be found....

erscheinen, gibt es folgende Abhilfemaßnahmen:

* Starten von Eclipse über das Terminal (da hier die Umgebungsvariablen richtig gesetzt werden)
* ODER
	* In /etc/environment
			* export ANDROID_HOME=/path/to/android/sdk
* ODER
	* Script erstellen, von dem Eclipse gestartet wird und dieses über einen Launcher starten

MEHRERE ANDROID SDK-INSTALLATIONEN
----------------------------------

Sollte eine Fehlermeldung betreffend

	... Error reading ../android-sdk/platforms/android-3/source.properties -> [Help 1]
erscheinen:

* Entfernen alter Android-SDK-Installationen
* Hinzufügen des SDK 7 (2.1)

OPTION '-XX:+AggressiveOpts' IN 'eclipse.ini'
---------------------------------------------
Sollte die Fehlermeldung:

	Dx trouble writing output: null
	Conversion to Dalvik format failed with error 2
	Dx no classfiles specified
	Conversion to Dalvik format failed with error 1
		
angezeigt werden, muss die Option '-XX:+AggressiveOpts' in der Datei 'eclipse.ini' entfernt werden

java.lang.ClassNotFoundException: android.support.v4.view.ViewPager in loader dalvik.system.PathClassLoader
-----------------------------------------------------------------------------------------------------------
Sollte die App ueber das ADT-Plugin von Eclipse gestartet werden, muss das JAR-File der 'compatibility-library-v4' zum Classpath hinzugefuegt werden.

Z.B. ueber Rechtsklick auf das Projekt --> 'Properties' --> 'Java Build Path' --> 'Add External JARs' und dann das JAR auswaehlen.
Wird Maven verwendet, kann die Datei '~/.m2/repository/android-extras/compatibility-library/v4/compatibility-library-v4.jar' hinzugefuegt werden.

WEITERE INFORMATIONEN ZUM PROJEKT
---------------------------------
* Android Market: https://market.android.com/details?id=edu.htl3r.schoolplanner
* Bugtracker: http://jira.schoolplanner.at/
* Continuous Integration System: http://jenkins.coding4coffee.org/job/SchoolPlanner4Untis/
