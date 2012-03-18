package edu.htl3r.schoolplanner.gui.loginTask;

import android.os.Bundle;

/**
 * Klassen, die sich mit diesem Listener am {@link LoginTask} angemeldet haben, erhalten Benachrichtigungen ueber den Login-Vorgang.
 */
public interface OnLoginTaskUpdateListener {
	/**
	 * Diese Methode wird aufgerufen, wenn sich der Status des {@link LoginTask} aendert.<br />
	 * Dies ist z.B. der Fall, wenn der Login oder das Herunterladen der Stammdaten erfolgreich durchgefuehrt werden konnten.
	 * @param status Status des {@link LoginTask}
	 * @see LoginTaskStatus
	 */
	public void statusChanged(String status);
	
	/**
	 * Diese Methode wird verwendet, um das Beenden des {@link LoginTask} zu kennzeichnen.<br />
	 * Daten, die beim Login geladen wurden (z.B. Stammdaten) werden in einem {@link Bundle} uebergeben.
	 * @param data Daten, die beim Login geladen wurden oder {@code null}, wenn der Login nicht erfolgreich war
	 */
	public void loginTaskFinished(Bundle data);
}
