package nibbles.ui;

import java.util.ArrayList;
import java.util.List;

import nibbles.ui.R;

import nibbles.game.Game;
import nibbles.game.Screen;
import nibbles.game.Speaker;
import nibbles.game.SoundSeq.FreqDuration;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

public class NibblesThread extends Thread {
	private static final String KEY_NIBBLES_GAME = "NibblesGame";
	private static final String KEY_MUTED = "Muted";

	private static final String TAG = "NT";

	private final SurfaceHolder surfaceHolder;
	private final Context context;
	private NibblesActivity nibblesActivity;

	private boolean running = false;
	private Game nibblesGame;
	private AsciiCharWriter asciiWriter;
	private final List<PlaySeq> playSeqPool = new ArrayList<PlaySeq>();

	private final Speaker speaker = new Speaker() {
		private boolean muted = true;

		@Override
		public void outputSoundSeq(int id) {
			if (!muted) {
				playSeqPool.get(id).play();
			}
		}

		@Override
		public int prepareSoundSeq(List<FreqDuration> seq) {
			playSeqPool.add(new PlaySeq(seq));
			return playSeqPool.size() - 1;
		}
	};

	public NibblesThread(SurfaceHolder surfaceHolder, Context context) {
		this.surfaceHolder = surfaceHolder;
		this.context = context;
	}

	public void init(Bundle savedInstanceState, NibblesActivity nibblesActivity) {
		this.nibblesActivity = nibblesActivity;
		if (savedInstanceState == null) {
			nibblesGame = new Game(2, 20, false);
		} else {
			Log.v(TAG, "Restore");
			nibblesGame = (Game) savedInstanceState
					.getSerializable(KEY_NIBBLES_GAME);
			speaker.setMuted(savedInstanceState.getBoolean(KEY_MUTED));
		}
		nibblesGame.initSpeaker(speaker);
		nibblesGame.initAI(0, 1);
	}

	private void doDraw(final Canvas canvas) {
		int w = canvas.getWidth();
		int h = canvas.getHeight();
		if (asciiWriter == null) {
			initAsciiWriter(w, h);
		}
		int usedWidth;
		int usedHeight;
		if (w / (float) canvas.getHeight() > Screen.ASPECT_RATIO) {
			usedWidth = (int) (h * Screen.ASPECT_RATIO);
			usedHeight = h;
		} else {
			usedWidth = w;
			usedHeight = (int) (w / Screen.ASPECT_RATIO);
		}
		canvas.translate((w - usedWidth) / 2, (h - usedHeight) / 2);
		canvas.scale(usedWidth / (float) Screen.WIDTH, usedHeight
				/ (float) Screen.HEIGHT);
		Screen screen = new Screen() {
			Paint paint = new Paint();

			@Override
			protected void drawRect(int left, int top, int right, int bottom,
					int color, float alpha) {
				paint.setColor(color);
				paint.setAlpha((int) (alpha * 255));
				canvas.drawRect(left, top, right, bottom, paint);
			}

			@Override
			protected void writeChar(char c, int x, int y, int color) {
				asciiWriter.write(canvas, c, x, y, color);
			}
		};
		nibblesGame.draw(screen);
	}

	private void initAsciiWriter(int w, int h) {
		Bitmap charset = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.charset);
		float scaleX = w / (float) Screen.WIDTH;
		float scaleY = h / (float) Screen.HEIGHT;
		asciiWriter = new AsciiCharWriter(charset, 8, 16, scaleX, scaleY);
	}

	private synchronized void waitForSurface() {
		if (!running) {
			boolean retry = true;
			while (retry) {
				try {
					wait();
					retry = false;
				} catch (InterruptedException e) {
				}
			}
		}
	}

	private synchronized void notifySurface() {
		notify();
	}

	private void reDraw() {
		Canvas c = null;
		try {
			c = surfaceHolder.lockCanvas();
			synchronized (surfaceHolder) {
				doDraw(c);
			}
		} finally {
			if (c != null) {
				surfaceHolder.unlockCanvasAndPost(c);
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			waitForSurface();
			reDraw();
			// TODO check if nibblesGame is null?
			while (running) {
				if (nibblesGame.step()) {
					reDraw();
				}
			}
			notifySurface();
		}
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void doTouch(float x) {
		if (x >= 2.0 / 3.0) {
			nibblesGame.turnRight(0);
		} else if (x >= 1.0 / 3.0) {
			nibblesGame.toggleState();
		} else {
			nibblesGame.turnLeft(0);
		}
	}

	public void doKeyDown(int keyCode) {
		KeyMapper.SnakeEvent snakeEvent = KeyMapper.STANDARD_MAPPER
				.map(keyCode);
		if (snakeEvent != null) {
			nibblesGame.addDirection(snakeEvent.getSnakeId(),
					snakeEvent.getDirection());
		} else {
			switch (keyCode) {
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				setMuted(true);
				break;
			case KeyEvent.KEYCODE_VOLUME_UP:
				setMuted(false);
				break;
			case KeyEvent.KEYCODE_MUTE:
				setMuted(!speaker.isMuted());
				break;
			default:
				nibblesGame.toggleState();
				break;
			}
		}
	}

	private void setMuted(boolean muted) {
		speaker.setMuted(muted);
		if (nibblesActivity != null) {
			int viewId;
			if (muted) {
				viewId = R.id.muteImage;
			} else {
				viewId = R.id.unmuteImage;
			}
			Notification.show(nibblesActivity.findViewById(viewId));
		}
	}

	public void doPause() {
		nibblesGame.pause();
	}

	public void saveState(Bundle outState) {
		Log.v(TAG, "Save");
		outState.putSerializable(KEY_NIBBLES_GAME, nibblesGame);
		outState.putBoolean(KEY_MUTED, speaker.isMuted());
	}
}
