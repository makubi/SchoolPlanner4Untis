package edu.htl3r.schoolplanner.gui.basti;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;
import edu.htl3r.schoolplanner.R;
import edu.htl3r.schoolplanner.gui.DummyBackend;

public class WeekView extends SurfaceView{

	private DummyBackend data;
	private Paint paint;

	public WeekView(Context context, DummyBackend d) {
		super(context);
		data = d;
		paint = new Paint();
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		setBackgroundColor(getResources().getColor(R.color.background_stundenplan));
		printTimeTableGrid(canvas);
		
		super.onDraw(canvas);
	}
	
	
	private void printTimeTableGrid(Canvas c){
		
	}

}
