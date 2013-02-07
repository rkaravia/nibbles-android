package nibbles.game;

import android.graphics.Color;

abstract public class NibblesScreen {
	public static final int WIDTH = 640;
	public static final int HEIGHT = 400;
	public static final double ASPECTRATIO = 4.0 / 3.0;

	private static final int DOSCHARWIDTH = 8;
	private static final int DOSCHARHEIGHT = 16;
	private static final int UPPERPIXELHEIGHT = 7;
//	private static final int LOWERPIXELHEIGHT = 9;

	private static final int[] QB_COLORS = { Color.rgb(0, 0, 0),
			Color.rgb(0, 0, 168), Color.rgb(0, 168, 0), Color.rgb(0, 168, 168),
			Color.rgb(168, 0, 0), Color.rgb(168, 0, 168),
			Color.rgb(168, 84, 0), Color.rgb(168, 168, 168),
			Color.rgb(84, 84, 84), Color.rgb(84, 84, 252),
			Color.rgb(84, 252, 84), Color.rgb(84, 252, 252),
			Color.rgb(252, 84, 84), Color.rgb(252, 84, 252),
			Color.rgb(252, 252, 84), Color.rgb(252, 252, 252), };

	private static int qb2rgb(int qbColor) {
		return QB_COLORS[qbColor];
	}

	abstract protected void drawRect(int left, int top, int right, int bottom,
			int color);

	abstract protected void writeChar(char c, int x, int y, int color);

	private Vector2D toPixelCoordinates(int x, int y) {
		x = x * DOSCHARWIDTH;
		int part = y % 2;
		y = (y - part) / 2 * DOSCHARHEIGHT + part * UPPERPIXELHEIGHT;
		return new Vector2D(x, y);
	}

	public void update(Vector2D p, int color) {
		update(p, p, color);
	}

	public void update(Vector2D p1, Vector2D p2, int qbColor) {
		int left = Math.min(p1.getX(), p2.getX());
		int top = Math.min(p1.getY(), p2.getY());
		int right = Math.max(p1.getX(), p2.getX()) + 1;
		int bottom = Math.max(p1.getY(), p2.getY()) + 1;

		p1 = toPixelCoordinates(left, top);
		p2 = toPixelCoordinates(right, bottom);

		drawRect(p1.getX(), p1.getY(), p2.getX(), p2.getY(), qb2rgb(qbColor));
	}

	public void write(String text, Vector2D p, int color) {
		for (int i = 0; i < text.length(); i++) {
			Vector2D pp = toPixelCoordinates(p.getX() + i, p.getY());
			writeChar(text.charAt(i), pp.getX(), pp.getY(), qb2rgb(color));
		}
	}
}
