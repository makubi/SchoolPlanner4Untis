package edu.htl3r.schoolplanner.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import edu.htl3r.schoolplanner.R;

public class SelectScreen extends Activity{
	
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
					text+="classes";
					break;
				case 1:
					text+="teacher";
					break;
				case 2:
					text+="rooms";
					break;
				case 3:
					text+="subjects";
					break;
				default:
					break;
				}
	            Toast.makeText(SelectScreen.this, text, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
}
