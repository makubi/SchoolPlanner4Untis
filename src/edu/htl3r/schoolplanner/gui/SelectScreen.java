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
		setContentView(R.layout.selectscreen);
		
		GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(this));
	    
	    GridView gridview2 = (GridView) findViewById(R.id.gridview2);
	    gridview2.setAdapter(new ImageAdapter2(this));
	    
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(SelectScreen.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
	}
}
