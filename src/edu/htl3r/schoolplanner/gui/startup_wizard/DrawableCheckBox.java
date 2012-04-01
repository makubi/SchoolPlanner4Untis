package edu.htl3r.schoolplanner.gui.startup_wizard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import edu.htl3r.schoolplanner.R;

public class DrawableCheckBox extends ImageView {
	
	private boolean checked;
	private final int BUTTON_CHECKED_OFF_RESOURCE = R.drawable.btn_check_buttonless_off;
	private final int BUTTON_CHECKED_ON_RESOURCE = R.drawable.btn_check_buttonless_on;
	
	public DrawableCheckBox(Context context) {
		super(context);
		
		initView();
	}
	
	public DrawableCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initView();
	}

	public DrawableCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initView();
	}
	
	private void initView() {
		checked = false;
		setImageResource(BUTTON_CHECKED_OFF_RESOURCE);
	}
	
	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
		setImageResource(checked ? BUTTON_CHECKED_ON_RESOURCE : BUTTON_CHECKED_OFF_RESOURCE);
	}
	
}
