package yuku.ambilwarna;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;
import edu.htl3r.schoolplanner.R;

public class AmbilWarnaKotak extends View {
	
	Paint paint;
	Shader dalam;
	Shader luar;
	float hue;
	float satudp;
	float ukuranUiDp = 240.f;
	float ukuranUiPx; // diset di constructor
	float[] tmp00 = new float[3];

	public AmbilWarnaKotak(Context context) {
		this(context, null);
	}

	public AmbilWarnaKotak(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AmbilWarnaKotak(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		satudp = context.getResources().getDimension(R.dimen.ambilwarna_satudp);
		ukuranUiPx = ukuranUiDp * satudp;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (paint == null) {
			paint = new Paint();
			luar = new LinearGradient(0.f, 0.f, 0.f, ukuranUiPx, 0xffffffff, 0xff000000, TileMode.CLAMP);
		}

		tmp00[1] = tmp00[2] = 1.f;
		tmp00[0] = hue;
		int rgb = Color.HSVToColor(tmp00);

		dalam = new LinearGradient(0.f, 0.f, ukuranUiPx, 0.f, 0xffffffff, rgb, TileMode.CLAMP);
		ComposeShader shader = new ComposeShader(luar, dalam, PorterDuff.Mode.MULTIPLY);

		paint.setShader(shader);
		
		canvas.drawRect(0.f, 0.f, ukuranUiPx, ukuranUiPx, paint);
	}
	
	void setHue(float hue) {
		this.hue = hue;
		invalidate();
	}
}
