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


/**
 * Klassen, die sich mit diesem Listener an einem {@link UpdatePublishingAsyncTask} angemeldet haben, erhalten Benachrichtigungen ueber den Login-Vorgang.
 * @see LoginTask
 */
public interface OnUpdatePublishingAsyncTaskListener<Result> {
	
	public void onCancelled();
	
	public void onPostExecute(Result result);
	
	public void onProgressUpdate(AsyncTaskProgress progress);

	public void onPreExecute();
}
