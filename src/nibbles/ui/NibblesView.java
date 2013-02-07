package nibbles.ui;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class NibblesView extends SurfaceView implements SurfaceHolder.Callback {
//	private static final String TAG = "SView";

	private NibblesThread thread;
	
    public NibblesView(Context context) {
        super(context);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        thread = new NibblesThread(holder, context);
    }
    
    public void doTouch (float x) {
    	thread.doTouch(x / getWidth());
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        //if (!hasWindowFocus) thread.pause();
    }

    @Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        //thread.setSurfaceSize(width, height);
    }

    @Override
	public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

	public void doKeyDown(int keyCode) {
		thread.doKeyDown(keyCode);
	}
}