package nibbles.ui;

import java.util.*;

import nibbles.game.Vector2D;

import android.view.KeyEvent;

public class KeyMapper {
	public static final KeyMapper STANDARD_MAPPER = new KeyMapper(
			new ArrayList<Map<Integer, Vector2D>>() {
				{
					add(new HashMap<Integer, Vector2D>() {
						{
							put(KeyEvent.KEYCODE_DPAD_LEFT, Vector2D.LEFT);
							put(KeyEvent.KEYCODE_DPAD_RIGHT, Vector2D.RIGHT);
							put(KeyEvent.KEYCODE_DPAD_UP, Vector2D.UP);
							put(KeyEvent.KEYCODE_DPAD_DOWN, Vector2D.DOWN);
						}
					});
					add(new HashMap<Integer, Vector2D>() {
						{
							put(KeyEvent.KEYCODE_A, Vector2D.LEFT);
							put(KeyEvent.KEYCODE_D, Vector2D.RIGHT);
							put(KeyEvent.KEYCODE_W, Vector2D.UP);
							put(KeyEvent.KEYCODE_S, Vector2D.DOWN);
						}
					});
				}
			});

	private final List<Map<Integer, Vector2D>> keyMap;

	public KeyMapper(List<Map<Integer, Vector2D>> keyMap) {
		this.keyMap = keyMap;
	}
	
	public SnakeEvent map(int keyCode) {
		for (int i = 0; i < keyMap.size(); i++) {
			Vector2D direction = keyMap.get(i).get(keyCode);
			if (direction != null) {
				return new SnakeEvent(i, direction);
			}
		}
		return null;
	}
	
	public static class SnakeEvent {
		private final int snakeId;
		private final Vector2D direction;
		
		public SnakeEvent(int snakeId, Vector2D direction) {
			this.snakeId = snakeId;
			this.direction = direction;
		}

		public int getSnakeId() {
			return snakeId;
		}

		public Vector2D getDirection() {
			return direction;
		}
	}
}
