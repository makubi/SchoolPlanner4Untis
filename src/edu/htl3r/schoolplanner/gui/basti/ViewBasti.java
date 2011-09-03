/* SchoolPlanner4Untis - Android app to manage your Untis timetable
    Copyright (C) 2011  Mathias Kub <mail@makubi.at>
			Sebastian Chlan <sebastian@schoolplanner.at>
			Christian Pascher <christian@schoolplanner.at>
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
package edu.htl3r.schoolplanner.gui.basti;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.RelativeLayout;
import edu.htl3r.schoolplanner.DateTime;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.SchoolPlannerApp;
import edu.htl3r.schoolplanner.backend.Cache;
import edu.htl3r.schoolplanner.backend.schoolObjects.ViewType;
import edu.htl3r.schoolplanner.gui.BundleConstants;
import edu.htl3r.schoolplanner.gui.SchoolPlannerActivity;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIContentManager;
import edu.htl3r.schoolplanner.gui.basti.GUIData.GUIWeek;
import edu.htl3r.schoolplanner.gui.basti.Week.WeekLayout;

public class ViewBasti extends SchoolPlannerActivity{
	
	
	private Cache cache;
	private GUIContentManager contentmanager = new GUIContentManager();
	private RelativeLayout container;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    
		setContentView(R.layout.basti_weekview);
		container = (RelativeLayout)findViewById(R.id.week_rel);
		DateTime date = new DateTime();
		
		date.set(14, 2, 2011);
		
		LoadWeekData loadweek = new LoadWeekData();
		loadweek.setContext(this);
		loadweek.execute(date);

	}
	

	
	public class LoadWeekData extends AsyncTask<DateTime, String, GUIWeek>{

		private Context context;
			
		@Override
		protected void onPreExecute() {
			publishProgress("Erzeuge Objekte","true");

			cache = ((SchoolPlannerApp)getApplication()).getData();
			contentmanager.setNeededData(context, cache);
			contentmanager.setViewType((ViewType)getIntent().getExtras().getSerializable(BundleConstants.SELECTED_VIEW_TYPE));
			super.onPreExecute();
		}
		
		@Override
		protected GUIWeek doInBackground(DateTime... date) {
			publishProgress("Lade Daten","true");
			GUIWeek timeTable4GUI = contentmanager.getTimeTable4GUI(date[0]);
			return timeTable4GUI;
		}
	
		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			setInProgress(values[0], Boolean.parseBoolean(values[1]));
		}
		
		@Override
		protected void onPostExecute(GUIWeek result) {
			if(result != null){
				WeekLayout w = new WeekLayout(context);
				w.setWeekData(result);
				container.addView(w);
				publishProgress("","false");
			}else{
				publishProgress("Error","false");
			}
			super.onPostExecute(result);
		}

		public void setContext(Context context) {
			this.context = context;
		}
		
		
	}
	
}