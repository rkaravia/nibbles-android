package nibbles.game;

import java.util.List;

import nibbles.game.SoundSequence.FrequencyDuration;

abstract public class NibblesSpeaker {
	abstract public void playSoundSeq(int id);

	abstract public int prepareSoundSeq(List<FrequencyDuration> seq);
}
