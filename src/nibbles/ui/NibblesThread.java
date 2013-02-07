package nibbles.ui;

import java.util.ArrayList;
import java.util.List;

import nibbles.ui.R;

import nibbles.game.NibblesGame;
import nibbles.game.NibblesScreen;
import nibbles.game.NibblesSpeaker;
import nibbles.game.SoundSequence.FrequencyDuration;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class NibblesThread extends Thread {
	private boolean running = false;
	private final SurfaceHolder surfaceHolder;
	private NibblesGame nibblesGame;
//	private static final String TAG = "NT";
	private AsciiWriter asciiWriter;
	private final Context context;
	private final List<PlaySeq> playSeqPool = new ArrayList<PlaySeq>();
	
	private final NibblesSpeaker speaker = new NibblesSpeaker() {
		@Override
		public void playSoundSeq(int id) {
			playSeqPool.get(id).play();
		}

		@Override
		public int prepareSoundSeq(List<FrequencyDuration> seq) {
			playSeqPool.add(new PlaySeq(seq));
			return playSeqPool.size() - 1;
		}
	};

	public NibblesThread(SurfaceHolder surfaceHolder, Context context) {
		this.surfaceHolder = surfaceHolder;
		this.context = context;
		nibblesGame = new NibblesGame(1, 20, false, speaker);
	}

	private void doDraw(final Canvas canvas) {
		int w = canvas.getWidth();
		int h = canvas.getHeight();
		if (asciiWriter == null) {
			initAsciiWriter(w, h);
		}
		int usedWidth;
		int usedHeight;
		if (w / (float) canvas.getHeight() > NibblesScreen.ASPECTRATIO) {
			usedWidth = (int) (h * NibblesScreen.ASPECTRATIO);
			usedHeight = h;
		} else {
			usedWidth = w;
			usedHeight = (int) (w / NibblesScreen.ASPECTRATIO);
		}
		canvas.translate((w - usedWidth) / 2, (h - usedHeight) / 2);
		canvas.scale(usedWidth / (float) NibblesScreen.WIDTH, usedHeight
				/ (float) NibblesScreen.HEIGHT);
		NibblesScreen screen = new NibblesScreen() {
			@Override
			protected void drawRect(int left, int top, int right, int bottom, int color) {
				Paint p = new Paint();
				p.setColor(color);
				canvas.drawRect(left, top, right, bottom, p);
			}
			
			@Override
			protected void writeChar(char c, int x, int y, int color) {
				asciiWriter.write(canvas, c, x, y, color);
			}
		};
		nibblesGame.output(screen);
//		asciiWriter.write(canvas, "SAMMY DIES!!!", 128, 128, Color.WHITE);
	}
	
	private void initAsciiWriter(int w, int h) {
		Bitmap charset = BitmapFactory.decodeResource(context.getResources(), R.drawable.charset);
		float scaleX = w / (float) NibblesScreen.WIDTH;
		float scaleY = h / (float) NibblesScreen.HEIGHT;
		asciiWriter = new AsciiWriter(charset, 8, 16, scaleX, scaleY);
	}

	@Override
	public void run() {
		while (running) {
			Canvas c = null;
			try {
				c = this.surfaceHolder.lockCanvas();
				synchronized (surfaceHolder) {
					nibblesGame.step();
					doDraw(c);
				}
			} finally {
				if (c != null) {
					surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void doTouch(float x) {
		if (x >= 2.0 / 3.0) {
			nibblesGame.turnRight();
		} else if (x >= 1.0 / 3.0) {
			nibblesGame.toggleState();
		} else {
			nibblesGame.turnLeft();
		}
	}

	public void doKeyDown(int keyCode) {
		KeyMapper.SnakeEvent snakeEvent = KeyMapper.STANDARD_MAPPER
				.map(keyCode);
		if (snakeEvent != null) {
			nibblesGame.addDirection(snakeEvent.getSnakeId(),
					snakeEvent.getDirection());
		} else {
			nibblesGame.toggleState();
		}
	}
}
