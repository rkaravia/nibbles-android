package nibbles.ui;

import nibbles.game.GameOverListener;
import nibbles.settings.Settings;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class NibblesActivity extends Activity implements GameOverListener {
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
		setContentView(R.layout.nibbles_running);
		nibblesView = (NibblesView) findViewById(R.id.nibbles);
		nibblesThread = nibblesView.getThread();
		nibblesThread.init(
				savedInstanceState,
				this,
				(Settings.Values) getIntent().getSerializableExtra(
						Settings.KEY_SETTINGS), this);
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
		return nibblesThread.doKeyDown(keyCode);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		nibblesView.doTouch(event);
		return true;
	}

	@Override
	public void gameOver() {
		finish();
	}
}
