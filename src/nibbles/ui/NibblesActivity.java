package nibbles.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class NibblesActivity extends Activity {
	private static final String TAG = "FA";
	
	private NibblesView nibblesView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		nibblesView = new NibblesView(this);
		setContentView(nibblesView);

		Log.v(TAG, "INIT");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		nibblesView.doKeyDown(keyCode);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			nibblesView.doTouch(event.getX());
		}
		return true;
	}
}
