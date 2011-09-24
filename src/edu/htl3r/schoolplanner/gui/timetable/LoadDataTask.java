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
package edu.htl3r.schoolplanner.gui.timetable;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.preferences.Settings;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUIContentManager;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUIWeek;

class LoadDataTask extends AsyncTask<Void, String, Void> {

	private Context context;
	private Cache cache;
	private GUIContentManager contentmanager = new GUIContentManager();
	private WeekView viewbasti;
	private ViewType viewtype;
	private BlockingDownloadQueue downloadschlange;
	private Settings settings;

	@Override
	protected void onPreExecute() {
		publishProgress(getString(R.string.timetable_create_objects), "true");	// TODO: Ist diese Anzeige fuer den Benutzer wichtig?
		contentmanager.setNeededData(context, cache);
		contentmanager.setViewType(viewtype);
		contentmanager.setSettings(settings);
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {


		while (!downloadschlange.isInterrupted()) {
			TransferObject d = null;
			try {
				d = downloadschlange.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(d.isBomb())
				return null;
			
			publishProgress(getString(R.string.timetable_load_data), "true");
			InputTransferObject input = (InputTransferObject)d;
			GUIWeek timeTable4GUI = contentmanager.getTimeTable4GUI(input.getDate());

			publishProgress(getString(R.string.timetable_loading_display), "true");
			Message m = new Message();
			OutputTransferObject r= new OutputTransferObject(timeTable4GUI,input.getPos());
			m.obj = r;
			viewbasti.h.sendMessage(m);
			publishProgress("", "false");
		}
		return null;

	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		String trennzeichen = (values[0].length() == 0)? "" : "| " ;
		viewbasti.setInProgress(viewtype.getName() +" "+ trennzeichen + values[0], Boolean.parseBoolean(values[1]));
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
	}

	public void setData(Context context, Cache c, WeekView vb, ViewType vt,BlockingDownloadQueue bdq, Settings settings) {
		this.context = context;
		cache = c;
		viewtype = vt;
		downloadschlange = bdq;
		viewbasti = vb;
		this.settings = settings;
	}
	
	private String getString(int resId) {
		return context.getString(resId);
	}

}