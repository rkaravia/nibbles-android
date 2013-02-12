package nibbles.ui;

import java.util.List;

import nibbles.game.SoundSeq.FreqDuration;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class PlaySeq implements Runnable {
	private static final int SAMPLE_RATE = 8000;
	private static final int FADE_SAMPLES = 50;
	private static final int AMPLITUDE = 32767;
	private static final double PAUSE_FRACTION = 0.1;

	private AudioTrack audioTrack;

	public PlaySeq(List<FreqDuration> seq) {
		init(seq);
		new Thread(this).start();
	}

	public synchronized void play() {
		notify();
	}

	private void init(List<FreqDuration> seq) {
		SineTone[] tones = new SineTone[seq.size()];
		int totalNumSamples = 0;
		for (int i = 0; i < tones.length; i++) {
			tones[i] = new SineTone(seq.get(i));
			totalNumSamples += tones[i].nSamples;
		}
		short[] buffer = new short[totalNumSamples];
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
				2 * totalNumSamples, AudioTrack.MODE_STATIC);
		int offset = 0;
		for (SineTone tone : tones) {
			tone.generatePCM(buffer, offset);
			offset += tone.nSamples;
		}
		audioTrack.write(buffer, 0, totalNumSamples);
	}

	@Override
	public synchronized void run() {
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

	public static class SineTone {
		private double frequency;
		private final int nSamples;

		public SineTone(FreqDuration tone) {
			this.frequency = tone.getFreq();
			nSamples = (int) (tone.getDuration() / 1000.0 * SAMPLE_RATE);
		}

		public void generatePCM(short[] buffer, int offset) {
			int numNonNullSamples = (int) ((1 - PAUSE_FRACTION) * nSamples);
			Log.v("PCM", "f: " + frequency + "nnns: " + numNonNullSamples);
			double x = 2 * Math.PI / (SAMPLE_RATE / frequency);
			if (numNonNullSamples > 2 * FADE_SAMPLES) {
				// fade in
				for (int i = 0; i < FADE_SAMPLES; i++) {
					double fade = (i + 1) / (double) FADE_SAMPLES;
					double sample = Math.sin(x * i) * fade;
					buffer[offset + i] = (short) (sample * AMPLITUDE);
				}
				// main part
				for (int i = FADE_SAMPLES; i < numNonNullSamples - FADE_SAMPLES; i++) {
					double sample = Math.sin(x * i);
					buffer[offset + i] = (short) (sample * AMPLITUDE);
				}
				// fade out
				for (int i = numNonNullSamples - FADE_SAMPLES; i < numNonNullSamples; i++) {
					double fade = (numNonNullSamples - i) / (double) FADE_SAMPLES;
					double sample = Math.sin(x * i) * fade;
					buffer[offset + i] = (short) (sample * AMPLITUDE);
				}
			}
		}
	}
}
