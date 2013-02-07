package nibbles.game;

import java.io.Serializable;

public class Colors implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final Colors NORMAL = new Colors(0);
	public static final Colors MONO = new Colors(1);

	public static enum ColorKey {
		WALL(12, 7), BG(1, 0), DIALOG_FG(15, 15), DIALOG_BG(4, 0), FOOD(14, 15), SNAKE_0(
				14, 15), SNAKE_1(13, 7);
		private final int[] colors;

		private ColorKey(int... colors) {
			this.colors = colors;
		}

		public byte getColor(int i) {
			return (byte) colors[i];
		}
	}

	private final int id;

	private Colors(int id) {
		this.id = id;
	}

	public byte get(ColorKey colorKey) {
		return colorKey.getColor(id);
	}

	public byte getSnake(int snakeId) {
		return get(ColorKey.values()[ColorKey.SNAKE_0.ordinal() + snakeId]);
	}
}
