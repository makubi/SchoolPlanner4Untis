package yuku.ambilwarna;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import edu.htl3r.schoolplanner.R;

public class AmbilWarnaDialog {
	private static final String TAG = AmbilWarnaDialog.class.getSimpleName();
	
	public interface OnAmbilWarnaListener {
		void onCancel(AmbilWarnaDialog dialog);
		void onOk(AmbilWarnaDialog dialog, int color);
	}
	
	AlertDialog dialog;
	OnAmbilWarnaListener listener;
	View viewHue;
	AmbilWarnaKotak viewKotak;
	ImageView panah;
	View viewWarnaLama;
	View viewWarnaBaru;
	ImageView viewKeker;
	
	float satudp;
	int warnaLama;
	int warnaBaru;
	float hue;
	float sat;
	float val;
	float ukuranUiDp = 240.f;
	float ukuranUiPx; // diset di constructor
	
	public AmbilWarnaDialog(Context context, int color, OnAmbilWarnaListener listener) {
		this.listener = listener;
		this.warnaLama = color;
		this.warnaBaru = color;
		Color.colorToHSV(color, tmp01);
		hue = tmp01[0];
		sat = tmp01[1];
		val = tmp01[2];
		
		satudp = context.getResources().getDimension(R.dimen.ambilwarna_satudp);
		ukuranUiPx = ukuranUiDp * satudp;
		Log.d(TAG, "satudp = " + satudp + ", ukuranUiPx=" + ukuranUiPx);  //$NON-NLS-1$//$NON-NLS-2$
		
		View view = LayoutInflater.from(context).inflate(R.layout.ambilwarna_dialog, null);
		viewHue = view.findViewById(R.id.ambilwarna_viewHue);
		viewKotak = (AmbilWarnaKotak) view.findViewById(R.id.ambilwarna_viewKotak);
		panah = (ImageView) view.findViewById(R.id.ambilwarna_panah);
		viewWarnaLama = view.findViewById(R.id.ambilwarna_warnaLama);
		viewWarnaBaru = view.findViewById(R.id.ambilwarna_warnaBaru);
		viewKeker = (ImageView) view.findViewById(R.id.ambilwarna_keker);

		letakkanPanah();
		letakkanKeker();
		viewKotak.setHue(hue);
		viewWarnaLama.setBackgroundColor(color);
		viewWarnaBaru.setBackgroundColor(color);

		viewHue.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE 
						|| event.getAction() == MotionEvent.ACTION_DOWN
						|| event.getAction() == MotionEvent.ACTION_UP) {
					
					float y = event.getY(); // dalam px, bukan dp
					if (y < 0.f) y = 0.f;
					if (y > ukuranUiPx) y = ukuranUiPx - 0.001f;
					
					hue = 360.f - 360.f / ukuranUiPx * y;
					if (hue == 360.f) hue = 0.f;
					
					warnaBaru = hitungWarna();
					// update view
					viewKotak.setHue(hue);
					letakkanPanah();
					viewWarnaBaru.setBackgroundColor(warnaBaru);
					
					return true;
				}
				return false;
			}
		});
		viewKotak.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE 
						|| event.getAction() == MotionEvent.ACTION_DOWN
						|| event.getAction() == MotionEvent.ACTION_UP) {
					
					float x = event.getX(); // dalam px, bukan dp
					float y = event.getY(); // dalam px, bukan dp
					
					if (x < 0.f) x = 0.f;
					if (x > ukuranUiPx) x = ukuranUiPx;
					if (y < 0.f) y = 0.f;
					if (y > ukuranUiPx) y = ukuranUiPx;

					sat = (1.f / ukuranUiPx * x);
					val = 1.f - (1.f / ukuranUiPx * y);

					warnaBaru = hitungWarna();
					// update view
					letakkanKeker();
					viewWarnaBaru.setBackgroundColor(warnaBaru);
					
					return true;
				}
				return false;
			}
		});
		
		dialog = new AlertDialog.Builder(context)
		.setView(view)
		.setPositiveButton(R.string.ambilwarna_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (AmbilWarnaDialog.this.listener != null) {
					AmbilWarnaDialog.this.listener.onOk(AmbilWarnaDialog.this, warnaBaru);
				}
			}
		})
		.setNegativeButton(R.string.ambilwarna_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (AmbilWarnaDialog.this.listener != null) {
					AmbilWarnaDialog.this.listener.onCancel(AmbilWarnaDialog.this);
				}
			}
		})
		.create();
		
	}
	
	@SuppressWarnings("deprecation")
	protected void letakkanPanah() {
		float y = ukuranUiPx - (hue * ukuranUiPx / 360.f);
		if (y == ukuranUiPx) y = 0.f;
		
		AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) panah.getLayoutParams();
		layoutParams.y = (int) (y + 4);
		panah.setLayoutParams(layoutParams);
	}

	@SuppressWarnings("deprecation")
	protected void letakkanKeker() {
		float x = sat * ukuranUiPx;
		float y = (1.f - val) * ukuranUiPx;
		
		AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) viewKeker.getLayoutParams();
		layoutParams.x = (int) (x + 3);
		layoutParams.y = (int) (y + 3);
		viewKeker.setLayoutParams(layoutParams);
	}

	float[] tmp01 = new float[3];
	private int hitungWarna() {
		tmp01[0] = hue;
		tmp01[1] = sat;
		tmp01[2] = val;
		return Color.HSVToColor(tmp01);
	}

	public void show() {
		dialog.show();
	}
}
