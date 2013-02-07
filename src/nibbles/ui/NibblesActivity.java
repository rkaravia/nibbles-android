package nibbles.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class NibblesActivity extends Activity {
	private NibblesView nibblesView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// enable fullscreen mode
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// create view
		nibblesView = new NibblesView(this, savedInstanceState);
		setContentView(nibblesView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		nibblesView.getThread().doPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		nibblesView.getThread().saveState(outState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		nibblesView.getThread().doKeyDown(keyCode);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		nibblesView.doTouch(event);
		return true;
	}
}
