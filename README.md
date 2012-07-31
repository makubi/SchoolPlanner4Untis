![Logo](http://www.schoolplanner.at/images/header_logo.png) [SchoolPlanner4Untis](http://www.schoolplanner.at/)
==================================================

ANDROID PROJEKT IN ECLIPSE EINBINDEN
------------------------------------

* Einrichten der Entwicklungsumgebung
	* Download und Entpacken von Eclipse (Classic wird empfohlen; aktuell 4.2)
	* Installation von Maven 3
		* Download und Entpacken von Maven 3 (aktuell 3.0.3)
			-> http://maven.apache.org/download.html
	* Hinzufügen des entpackten Mavens in Eclipse
		* In Eclipse
			* Window -> Preferences -> Maven -> Installations -> Add... -> Vorher entpacktes Maven auswählen
	* Download und Entpacken des Android SDKs (aktuell 20.0.1)
	* Hinzufügen der ADT Update Site und installieren des Plugins
		* http://developer.android.com/sdk/installing/installing-adt.html#Download
	* SDK in Eclipse einrichten
		* http://developer.android.com/sdk/installing/installing-adt.html#Configure
	* SDK updaten / APIs installieren
	* Installation von m2e direkt aus Eclipse
	* In Eclipse "Problems" -> "Discover new m2e connectors"
	* "Android Connector" installieren 
* Hinzufügen des Projekts
	* Setzen der Variable
		* ANDROID_HOME=$SDK_ROOT
	* Projekt als Android Project importieren
	* Maven-Nature zum Projekt hinzufuegen
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
