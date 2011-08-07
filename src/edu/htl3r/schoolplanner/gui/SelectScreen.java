package edu.htl3r.schoolplanner.gui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.basti.ViewBasti;
import edu.htl3r.schoolplanner.gui.chris.ViewChris;

public class SelectScreen extends SchoolPlannerActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_screen);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(this));
	    
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	String text = "Selected ";
	        	switch (position) {
				case 0:
					Intent chris = new Intent(SelectScreen.this, ViewChris.class);
					startActivity(chris);
					text+="Chris";
					break;
				case 1:
					text+="teacher";
					break;
				case 2:
					text+="rooms";
					break;
				case 3:
					Intent basti = new Intent(SelectScreen.this, ViewBasti.class);
					startActivity(basti);
					text+="Basti";
					break;
				default:
					break;
				}
	            Toast.makeText(SelectScreen.this, text, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
}
