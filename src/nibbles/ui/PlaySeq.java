package nibbles.ui;

import java.util.List;

import nibbles.game.SoundSeq.FreqDuration;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class PlaySeq implements Runnable {
	private static final int SAMPLE_RATE = 8000;
	private static final double PAUSE_FRACTION = 0.1;

	private AudioTrack audioTrack;

//	private final List<FreqDuration> seq;

	public PlaySeq(List<FreqDuration> seq) {
		init(seq);
		new Thread(this).start();
	}

	public synchronized void play() {
		notify();
	}

	private void init(List<FreqDuration> seq) {
		Tone[] tones = new Tone[seq.size()];
		int totalNumSamples = 0;
		for (int i = 0; i < tones.length; i++) {
			tones[i] = new Tone(seq.get(i));
			totalNumSamples += tones[i].nSamples;
		}
		short[] buffer = new short[totalNumSamples];
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
				2 * totalNumSamples, AudioTrack.MODE_STATIC);
		int offset = 0;
		for (Tone tone : tones) {
			tone.generatePCM(buffer, offset);
			offset += tone.nSamples;
		}
		audioTrack.write(buffer, 0, totalNumSamples);
	}

	@Override
	public synchronized void run() {
//		init(seq);
		while (true) {
			try {
				wait();
			} catch (InterruptedException e) {
			}

			if (audioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
				audioTrack.stop();
				audioTrack.reloadStaticData();
				audioTrack.play();
			} else {
				audioTrack.play();
			}
		}
	}

	public static class Tone {
		private double frequency;
		private final int nSamples;

		public Tone(FreqDuration tone) {
			this.frequency = tone.getFreq();
			nSamples = (int) (tone.getDuration() / 1000.0 * SAMPLE_RATE);
		}

		public void generatePCM(short[] buffer, int offset) {
			int numNonNullSamples = (int) ((1 - PAUSE_FRACTION) * nSamples);
			for (int i = 0; i < numNonNullSamples; i++) {
				double dVal = Math.sin(2 * Math.PI * i
						/ (SAMPLE_RATE / frequency));
				buffer[offset + i] = (short) ((dVal * 32767));
			}
		}
	}
}
