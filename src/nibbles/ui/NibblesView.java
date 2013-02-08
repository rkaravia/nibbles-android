package nibbles.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class NibblesView extends SurfaceView implements SurfaceHolder.Callback {
	private final NibblesThread thread;

	public NibblesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		thread = new NibblesThread(holder, getContext(), this);
	}

	public void doTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			thread.doTouch(event.getX() / getWidth());
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		synchronized (thread) {
			thread.setRunning(true);
			if (!thread.isAlive()) {
				thread.start();
			}
			thread.notify();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		synchronized (thread) {
			thread.setRunning(false);
			try {
				thread.wait();
			} catch (InterruptedException e) {
			}
		}
	}

	public NibblesThread getThread() {
		return thread;
	}
}