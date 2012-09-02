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
import android.util.Log;
import android.widget.Toast;
import edu.htl3r.schoolplanner.backend.cache.Cache;
import edu.htl3r.schoolplanner.backend.preferences.Settings;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.timetable.LoadDataTask.LogObject;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUIContentManager;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUIContentProvider;
import edu.htl3r.schoolplanner.gui.timetable.GUIData.GUIWeek;
import edu.htl3r.schoolplanner.gui.timetable.TransportClasses.InputTransferObject;
import edu.htl3r.schoolplanner.gui.timetable.TransportClasses.OutputTransferObject;
import edu.htl3r.schoolplanner.gui.timetable.TransportClasses.TransferObject;

public class LoadDataTask extends AsyncTask<Void, LogObject, Void> implements GUIContentProvider.ErrorHandler{

	private Context context;
	private Cache cache;
	private GUIContentManager contentmanager = new GUIContentManager();
	private WeekView viewbasti;
	private ViewType viewtype;
	private BlockingDownloadQueue downloadschlange;
	private Settings settings;
	private boolean forceNetwork = false;
	private int forceCount = 0;
	
	@Override
	protected void onPreExecute() {
		GUIContentProvider contentprovider = new GUIContentProvider(cache,context);
		contentprovider.addErrorHandler(this);
		contentmanager.setNeededData(context, contentprovider);
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
				return null;
			}
			
			if(d.isBomb())
				return null;
			
			publishProgress(new LogObjectProgress(true));
			InputTransferObject input = (InputTransferObject)d;
			GUIWeek timeTable4GUI;
			
			if(forceNetwork && forceCount < 3){
				Log.d("basti", "Force Network - Data Task");
				timeTable4GUI = contentmanager.getTimeTable4GUI(input.getDate(), true);
				forceCount++;
			}else{
				timeTable4GUI = contentmanager.getTimeTable4GUI(input.getDate(), false);
				forceNetwork = false;
			}
			
			Message m = new Message();
			OutputTransferObject r= new OutputTransferObject(timeTable4GUI,input.getPos());
			m.obj = r;
			viewbasti.h.sendMessage(m);
			publishProgress(new LogObjectProgress(false));
		}
		return null;

	}

	@Override
	protected void onProgressUpdate(LogObject... values) {
		super.onProgressUpdate(values);
		if(values[0] instanceof LogObjectProgress){
			viewbasti.setInProgress("",((LogObjectProgress)values[0]).getProgress());
		} else if(values[0] instanceof LogObjectError){
			Toast.makeText(context, (((LogObjectError)values[0]).getError()), Toast.LENGTH_LONG).show();
		}
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
	
	public void changeViewType(ViewType newvt){
		viewtype = newvt;
		contentmanager.setViewType(viewtype);
	}
	
	public void forceNetwork(){
		forceNetwork = true;
		forceCount = 0;
	}

	@Override
	public void logToUser(String msg) {
		publishProgress(new LogObjectError(msg));
	}
	
	public class LogObject{	}
	public class LogObjectProgress extends LogObject{
		private boolean progress;
		public LogObjectProgress(boolean prog){
			this.progress=prog;
		}
		public boolean getProgress(){
			return progress;
		}
	}
	public class LogObjectError extends LogObject{
		private String txt;
		public LogObjectError(String error){
			this.txt = error;
		}
		public String getError(){
			return txt;
		}
	}

}