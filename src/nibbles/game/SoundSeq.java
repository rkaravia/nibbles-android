package nibbles.game;

import java.util.ArrayList;
import java.util.List;

public class SoundSeq {
	private static final int BASE_FREQ = 110;
	private static final double HALF_NOTE = Math.pow(2, 1.0 / 12.0);
	private static final int TUNE = 12;

	private static final SoundSeqElem[][] PREDEF = {
			{ new Tempo(160), new Octave(2), new NoteLength(20),
					new Notes("CDEDCD"), new NoteLength(10), new Notes("ECC") },
			{ new Octave(1), new NoteLength(16), new Notes("CCCE") },
			{ new Octave(0), new NoteLength(32), new Notes("EFGEFDC") } };

	public static final int START_ROUND = 0;
	public static final int HIT_NUMBER = 1;
	public static final int DEATH = 2;

	public static void init(Speaker speaker) {
		for (int i = 0; i < PREDEF.length; i++) {
			speaker.prepareSoundSeq(SoundSeq.convertSeq(PREDEF[i]));
		}
	}

	private static class Params {
		private int tempo = 120;
		private int length = 4;
		private int octave = 4;

		public int getTempo() {
			return tempo;
		}

		public void setTempo(int tempo) {
			this.tempo = tempo;
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public int getOctave() {
			return octave;
		}

		public void setOctave(int octave) {
			this.octave = octave;
		}
	}

	public static class FreqDuration {
		private final double freq;
		private final int duration;

		public FreqDuration(double freq, int duration) {
			this.freq = freq;
			this.duration = duration;
		}

		public int getDuration() {
			return duration;
		}

		public double getFreq() {
			return freq;
		}
	}

	public static List<FreqDuration> convertSeq(SoundSeqElem[] seq) {
		List<FreqDuration> result = new ArrayList<FreqDuration>();
		Params params = new Params();
		for (SoundSeqElem elem : seq) {
			elem.execute(params, result);
		}
		return result;
	}

	abstract private static class SoundSeqElem {
		abstract public void execute(Params params, List<FreqDuration> result);
	}

	private static class NoteLength extends SoundSeqElem {
		private final int length;

		public NoteLength(int length) {
			this.length = length;
		}

		@Override
		public void execute(Params params, List<FreqDuration> result) {
			params.setLength(length);
		}
	}

	private static class Octave extends SoundSeqElem {
		private final int octave;

		public Octave(int octave) {
			this.octave = octave;
		}

		@Override
		public void execute(Params params, List<FreqDuration> result) {
			params.setOctave(octave);
		}
	}

	private static class Tempo extends SoundSeqElem {
		private final int tempo;

		public Tempo(int tempo) {
			this.tempo = tempo;
		}

		@Override
		public void execute(Params params, List<FreqDuration> result) {
			params.setTempo(tempo);
		}
	}

	private static class Notes extends SoundSeqElem {
		private final String notes;

		public Notes(String notes) {
			this.notes = notes;
		}

		@Override
		public void execute(Params params, List<FreqDuration> result) {
			int duration = 240 * 1000 / (params.getTempo() * params.getLength());
			for (int i = 0; i < notes.length(); i++) {
				int note = 0;
				switch (notes.charAt(i)) {
				case 'A':
					note = 9;
					break;
				case 'B':
					note = 11;
					break;
				case 'C':
					note = 0;
					break;
				case 'D':
					note = 2;
					break;
				case 'E':
					note = 4;
					break;
				case 'F':
					note = 5;
					break;
				case 'G':
					note = 7;
					break;
				}
				int halfTone = 0;
				if (i < notes.length() - 1) {
					if (notes.charAt(i + 1) == '+') {
						halfTone = 1;
						i++;
					} else if (notes.charAt(i + 1) == '-') {
						halfTone = -1;
						i++;
					}
				}
				int noteId = (note - 9) + halfTone + 12 * (params.getOctave())
						+ TUNE;
				double frequency = BASE_FREQ * Math.pow(HALF_NOTE, noteId);

				result.add(new FreqDuration(frequency, duration));
			}
		}
	}
}