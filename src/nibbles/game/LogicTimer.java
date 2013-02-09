package nibbles.game;

import java.io.Serializable;

public class LogicTimer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final long beatLength;
	private long startTime;
	private long logicTime;
	private boolean paused;
	private boolean lagging = false;
	
	public LogicTimer(long beatLength) {
		this.beatLength = beatLength;
		logicTime = 0;
		paused = true;
	}
	
	public void toggleState() {
		if (paused) {
			start();
		} else {
			pause();
		}
	}
	
	public void start() {
		startTime = System.currentTimeMillis() - logicTime * beatLength;
		paused = false;
	}
	
	public void pause() {
		paused = true;
	}
	
	public boolean step() {
		boolean changed = false;
		if (!paused) {
			long msTime = System.currentTimeMillis() - startTime;
			long targetTime = msTime / beatLength;
			if (targetTime > logicTime) {
				logicTime++;
				changed = true;
				lagging = targetTime > logicTime;
				if (lagging) {
					// reset time if lag occurs
					start();
				}
			}
		}
		return changed;
	}
	
	public long getLogicTime() {
		return logicTime;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isLagging() {
		return lagging;
	}
}
