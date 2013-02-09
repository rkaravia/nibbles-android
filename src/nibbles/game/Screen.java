package nibbles.game;

import android.graphics.Color;

abstract public class Screen {
	public static final int WIDTH = 640;
	public static final int HEIGHT = 400;
	public static final double ASPECT_RATIO = 4.0 / 3.0;

	private static final int CHAR_WIDTH = 8;
	private static final int CHAR_HEIGHT = 16;
	private static final int UPPER_PART_HEIGHT = 7;
	// private static final int LOWERPIXELHEIGHT = 9;

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
			int color, float alpha);

	abstract protected void writeChar(char c, int x, int y, int color);

	private Point toPixelCoordinates(int x, int y) {
		x = x * CHAR_WIDTH;
		int part = y % 2;
		y = (y - part) / 2 * CHAR_HEIGHT + part * UPPER_PART_HEIGHT;
		return new Point(x, y);
	}

	public void draw(Point p, int qbColor) {
		draw(p, p, qbColor);
	}
	
	public void draw(Point p, int qbColor, float alpha) {
		draw(p, p, qbColor, alpha);
	}

	public void draw(Point p1, Point p2, int qbColor) {
		draw(p1, p2, qbColor, 1);
	}
	
	public void draw(Point p1, Point p2, int qbColor, float alpha) {
		int left = Math.min(p1.getX(), p2.getX());
		int top = Math.min(p1.getY(), p2.getY());
		int right = Math.max(p1.getX(), p2.getX()) + 1;
		int bottom = Math.max(p1.getY(), p2.getY()) + 1;

		p1 = toPixelCoordinates(left, top);
		p2 = toPixelCoordinates(right, bottom);

		drawRect(p1.getX(), p1.getY(), p2.getX(), p2.getY(), qb2rgb(qbColor), alpha);
	}

	public void write(String text, Point p, int color) {
		for (int i = 0; i < text.length(); i++) {
			Point p0 = toPixelCoordinates(p.getX() + i, p.getY());
			writeChar(text.charAt(i), p0.getX(), p0.getY(), qb2rgb(color));
		}
	}
}
