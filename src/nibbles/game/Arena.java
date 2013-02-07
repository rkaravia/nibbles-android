package nibbles.game;

import java.io.Serializable;

import nibbles.game.Colors.ColorKey;

public class Arena implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 80;
	public static final int HEIGHT = 50;
	public static final Wall[] BORDER_WALLS = {
			new Wall(new Point(0, 2), WIDTH, Point.RIGHT),
			new Wall(new Point(WIDTH - 1, 2), HEIGHT - 2, Point.DOWN),
			new Wall(new Point(WIDTH - 1, HEIGHT - 1), WIDTH, Point.LEFT),
			new Wall(new Point(0, HEIGHT - 1), HEIGHT - 2, Point.UP), };

	private byte[][] arena;
	private long[][] timeDrawn;
	private final Colors colorTable;
	private final LogicTimer logicTimer;
	private int levelId;

	public Arena(Colors colorTable, LogicTimer logicTimer) {
		arena = new byte[WIDTH][HEIGHT];
		timeDrawn = new long[WIDTH][HEIGHT];
		this.colorTable = colorTable;
		this.logicTimer = logicTimer;
	}

	public void setLevel(int levelId) {
		this.levelId = levelId;

		// reset whole arena to background color
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				setContent(x, y, colorTable.get(ColorKey.BG));
			}
		}

		// draw border walls
		for (int i = 0; i < BORDER_WALLS.length; i++) {
			BORDER_WALLS[i].draw(this, colorTable.get(ColorKey.WALL));
		}

		// draw level
		Level.get(levelId).draw(this, colorTable.get(ColorKey.WALL));
	}

	private void setContent(int x, int y, byte color) {
		arena[x][y] = color;
		timeDrawn[x][y] = logicTimer.getLogicTime();
	}

	public void setContent(Point point, byte color) {
		setContent(point.getX(), point.getY(), color);
	}

	public boolean placeFood(Point point) {
		Point sibling = getSiblingPoint(point);
		return (getContent(point) == colorTable.get(ColorKey.BG) && getContent(sibling) == colorTable
				.get(ColorKey.BG));
	}

	private byte getContent(Point p) {
		return arena[p.getX()][p.getY()];
	}

	public boolean isEmpty(Point p) {
		return getContent(p) == colorTable.get(ColorKey.BG);
	}

	private long getTimeDrawn(Point p) {
		return timeDrawn[p.getX()][p.getY()];
	}

	public int getColor(Point p) {
		byte result = getContent(p);

		// get original nibbles wall-darkening effect:
		if (result > 7) {
			Point sibling = getSiblingPoint(p);
			byte siblingColor = getContent(sibling);
			if (siblingColor > 7 && siblingColor != result
					&& getTimeDrawn(sibling) > getTimeDrawn(p)) {
				result -= 8;
			}
		}

		return result;
	}

	public static Point getSiblingPoint(Point p) {
		int y = p.getY();
		y = y - (y % 2) * 2 + 1;
		return new Point(p.getX(), y);
	}

	public void draw(Screen screen) {
		screen.draw(new Point(0, 0), new Point(WIDTH - 1, HEIGHT - 1),
				colorTable.get(ColorKey.BG));

		// draw border walls
		for (int i = 0; i < BORDER_WALLS.length; i++) {
			BORDER_WALLS[i].output(screen, colorTable.get(ColorKey.WALL));
		}

		// draw level
		Level.get(levelId).output(screen, colorTable.get(ColorKey.WALL));
	}
}
