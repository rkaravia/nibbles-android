package nibbles.ui;

import java.util.*;

import nibbles.game.Point;

import android.view.KeyEvent;

public class KeyMapper {
	public static final KeyMapper STANDARD_MAPPER = new KeyMapper(
			new ArrayList<Map<Integer, Point>>() {
				private static final long serialVersionUID = 1L;
				{
					add(new HashMap<Integer, Point>() {
						private static final long serialVersionUID = 1L;
						{
							put(KeyEvent.KEYCODE_DPAD_LEFT, Point.LEFT);
							put(KeyEvent.KEYCODE_DPAD_RIGHT, Point.RIGHT);
							put(KeyEvent.KEYCODE_DPAD_UP, Point.UP);
							put(KeyEvent.KEYCODE_DPAD_DOWN, Point.DOWN);
						}
					});
					add(new HashMap<Integer, Point>() {
						private static final long serialVersionUID = 1L;
						{
							put(KeyEvent.KEYCODE_A, Point.LEFT);
							put(KeyEvent.KEYCODE_D, Point.RIGHT);
							put(KeyEvent.KEYCODE_W, Point.UP);
							put(KeyEvent.KEYCODE_S, Point.DOWN);
						}
					});
				}
			});

	private final List<Map<Integer, Point>> keyMap;

	public KeyMapper(List<Map<Integer, Point>> keyMap) {
		this.keyMap = keyMap;
	}

	public SnakeEvent map(int keyCode) {
		for (int i = 0; i < keyMap.size(); i++) {
			Point direction = keyMap.get(i).get(keyCode);
			if (direction != null) {
				return new SnakeEvent(i, direction);
			}
		}
		return null;
	}

	public static class SnakeEvent {
		private final int snakeId;
		private final Point direction;

		public SnakeEvent(int snakeId, Point direction) {
			this.snakeId = snakeId;
			this.direction = direction;
		}

		public int getSnakeId() {
			return snakeId;
		}

		public Point getDirection() {
			return direction;
		}
	}
}
