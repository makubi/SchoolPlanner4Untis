package edu.htl3r.schoolplanner.gui.timetable.baactionbar;

import edu.htl3r.schoolplanner.R;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class BADropdown extends RelativeLayout{
	
	private ListView list;
	
	public BADropdown(Context context) {
		super(context);
		init();
	}
	
	public BADropdown(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public BADropdown(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		init();
	}
	
	
	public void init(){
		list = new ListView(getContext());
		
		String items[] = {getResources().getString(R.string.selectscreen_class),getResources().getString(R.string.selectscreen_teacher),getResources().getString(R.string.selectscreen_rooms),getResources().getString(R.string.selectscreen_subjects)};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.baactionbar_dropdown_item, items);
		list.setAdapter(adapter);
		list.setCacheColorHint(Color.parseColor("#00000000"));
		this.addView(list);
	}
	
	
}
