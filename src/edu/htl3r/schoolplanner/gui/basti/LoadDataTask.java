package edu.htl3r.schoolplanner.gui.basti;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.preferences.Settings;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIContentManager;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIWeek;

class LoadDataTask extends AsyncTask<Void, String, Void> {

	private Context context;
	private Cache cache;
	private GUIContentManager contentmanager = new GUIContentManager();
	private ViewBasti viewbasti;
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
		viewbasti.setInProgress(values[0], Boolean.parseBoolean(values[1]));
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
	}

	public void setData(Context context, Cache c, ViewBasti vb, ViewType vt,BlockingDownloadQueue bdq, Settings settings) {
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