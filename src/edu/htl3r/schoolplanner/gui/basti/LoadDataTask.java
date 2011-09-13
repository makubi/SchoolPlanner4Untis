package edu.htl3r.schoolplanner.gui.basti;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import edu.htl3r.schoolplanner.backend.Cache;
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

	@Override
	protected void onPreExecute() {
		publishProgress("Erzeuge Objekte", "true");
		contentmanager.setNeededData(context, cache);
		contentmanager.setViewType(viewtype);
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
			
			publishProgress("Lade Daten", "true");
			InputTransferObject input = (InputTransferObject)d;
			GUIWeek timeTable4GUI = contentmanager.getTimeTable4GUI(input.getDate());

			publishProgress("zaubere UI", "true");
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

	public void setData(Context context, Cache c, ViewBasti vb, ViewType vt,BlockingDownloadQueue bdq) {
		this.context = context;
		cache = c;
		viewtype = vt;
		downloadschlange = bdq;
		viewbasti = vb;
	}

}