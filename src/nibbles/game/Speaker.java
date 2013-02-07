package nibbles.game;

import java.util.List;

import nibbles.game.SoundSeq.FreqDuration;

abstract public class Speaker {
	private boolean muted = false;

	public void playSoundSeq(int id) {
		if (!muted) {
			outputSoundSeq(id);
		}
	}

	abstract public int prepareSoundSeq(List<FreqDuration> seq);

	abstract protected void outputSoundSeq(int id);

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}
}
