/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
			Sebastian Chlan <sebastian@schoolplanner.at>
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package edu.htl3r.schoolplanner.gui.loginTask;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

/**
 * Implementiert einen {@link AsyncTask}, an den sich {@link OnUpdatePublishingAsyncTaskListener} anmelden koennen, um bei Status-Aenderungen (onCancelled, onProgressUpdate, onPostExecute, onPreExecute) des Async-Tasks informiert zu werden.<br>
 * Als <b>Progress</b>-Parameter werden {@link AsyncTaskProgress} verwendet.
 * @param <Params>
 * @param <Result>
 */
public abstract class UpdatePublishingAsyncTask<Params, Result> extends AsyncTask<Params, AsyncTaskProgress, Result> {

	private List<OnUpdatePublishingAsyncTaskListener<Result>> listener = new ArrayList<OnUpdatePublishingAsyncTaskListener<Result>>();
	
	public void addListener(OnUpdatePublishingAsyncTaskListener<Result> listener) {
		this.listener.add(listener);
	}
	
	public void removeListener(OnUpdatePublishingAsyncTaskListener<Result> listener) {
		this.listener.remove(listener);
	}
	
	/** Fuehrt {@link OnUpdatePublishingAsyncTaskListener#onCancelled()} fuer alle angemeldeten {@link OnUpdatePublishingAsyncTaskListener} und danach {@link AsyncTask#onCancelled()} aus.
	 * @see android.os.AsyncTask#onCancelled()
	 */
	@Override
	protected void onCancelled() {
		for(OnUpdatePublishingAsyncTaskListener<Result> listener : this.listener) {
			listener.onCancelled();
		}
		
		super.onCancelled();
	}
	
	/** Fuehrt {@link OnUpdatePublishingAsyncTaskListener#onProgressUpdate(AsyncTaskProgress)} fuer alle angemeldeten {@link OnUpdatePublishingAsyncTaskListener} und danach {@link AsyncTask#onProgressUpdate(AsyncTaskProgress)} aus.
	 * @see android.os.AsyncTask#onProgressUpdate(AsyncTaskProgress)
	 */
	@Override
	protected void onProgressUpdate(AsyncTaskProgress... values) {
		for(OnUpdatePublishingAsyncTaskListener<Result> listener : this.listener) {
			listener.onProgressUpdate(values[0]);
		}
		
		super.onProgressUpdate(values);
	}
	
	/** Fuehrt {@link OnUpdatePublishingAsyncTaskListener#onPostExecute(Object)} fuer alle angemeldeten {@link OnUpdatePublishingAsyncTaskListener} und danach {@link AsyncTask#onPostExecute(Object)} aus.
	 * @see android.os.AsyncTask#onPostExecute(Object)
	 */
	@Override
	protected void onPostExecute(Result result) {
		for(OnUpdatePublishingAsyncTaskListener<Result> listener : this.listener) {
			listener.onPostExecute(result);
		}
		
		super.onPostExecute(result);
	};
	
	/** Fuehrt {@link AsyncTask#onPreExecute()} und danach {@link OnUpdatePublishingAsyncTaskListener#onPreExecute()} fuer alle angemeldeten {@link OnUpdatePublishingAsyncTaskListener} aus.
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		for(OnUpdatePublishingAsyncTaskListener<Result> listener : this.listener) {
			listener.onPreExecute();
		}
	}
	
	/** Fuehrt {@link AsyncTask#cancel(boolean)} mit dem Boolean-Wert <b>true</b> aus.
	 * @see android.os.AsyncTask#cancel(boolean)
	 */
	public void cancel() {
		cancel(true);
	}
}
