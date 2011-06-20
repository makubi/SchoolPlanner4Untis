package edu.htl3r.schoolplanner.gui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import edu.htl3r.schoolplanner.R;

public class ImageAdapter extends BaseAdapter {

	 private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return mThumbIds.length;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {	    	
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(80, 100));
	            imageView.setScaleType(ImageView.ScaleType.CENTER);
	            imageView.setPadding(8, 8, 8, 8);
	            imageView.setBackgroundResource(R.drawable.border);
	        } else {
	            imageView = (ImageView) convertView;
	        }

	        imageView.setImageResource(mThumbIds[position]);
	        return imageView;
	    }

	    // references to our images
	    private Integer[] mThumbIds = {
	           R.drawable.ic_class, R.drawable.ic_teacher,
	           R.drawable.ic_door, R.drawable.ic_subject
	    };
}
