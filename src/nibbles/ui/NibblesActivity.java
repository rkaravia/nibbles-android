package nibbles.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class NibblesActivity extends Activity {
	private NibblesView nibblesView;
	private NibblesThread nibblesThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// enable full screen mode
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// init view and animation thread
		setContentView(R.layout.nibbles_layout);
		nibblesView = (NibblesView) findViewById(R.id.nibbles);
		nibblesThread = nibblesView.getThread();
		nibblesThread.init(savedInstanceState, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		nibblesThread.doPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		nibblesThread.saveState(outState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		nibblesThread.doKeyDown(keyCode);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		nibblesView.doTouch(event);
		return true;
	}
}
