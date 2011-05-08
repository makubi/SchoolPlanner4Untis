[SchoolPlanner4Untis](http://www.schoolplanner.at/) ![PlannerLogo](http://www.schoolplanner.at/wordpress/wp-content/themes/notepad-theme/img/logo.png)
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
		* Hinzufügen des entpackten Mavens in Eclipse
			* In Eclipse
				* Window -> Preferences -> Maven -> Installations -> Add... -> Vorher entpacktes Maven auswählen
			* Restart (bei Nachfrage)
			* Installation von M2Eclipse
				* Install new software
				* hinzufügen, Update Site: http://m2eclipse.sonatype.org/sites/m2e
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

* Sollte die Fehlermeldung ".../gen already exists but is not a source folder. Convert to a source folder or rename it." erscheinen:
	* In Eclipse
	* Ordner "gen/" löschen und Projekt refreshen


EINRICHTEN DER UMGEBUNGSVARIABLEN
---------------------------------
	
* Sollte die Fehlermeldung "[ERROR] Error when generating sources. No Android SDK path could be found...." erscheinen, gibt es folgende Abhilfemaßnahmen:
	* Starten von Eclipse über das Terminal (da hier die Umgebungsvariablen richtig gesetzt werden)
		
	* ODER
		
		* In /etc/environment
			* export ANDROID_HOME=/path/to/android/sdk
	
	* ODER
		
		* Script erstellen, von dem Eclipse gestartet wird und dieses über einen Launcher starten


MEHRERE ANDROID SDK-INSTALLATIONEN
----------------------------------

* Sollte eine Fehlermeldung betreffend "... Error reading ../android-sdk/platforms/android-3/source.properties -> [Help 1]" erscheinen
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
