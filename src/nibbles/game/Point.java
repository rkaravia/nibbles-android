package nibbles.game;

import java.io.Serializable;

public class Point implements Serializable {
	private static final long serialVersionUID = 1L;

	private final int x;
	private final int y;

	public static final Point LEFT = new Point(-1, 0);
	public static final Point RIGHT = new Point(1, 0);
	public static final Point UP = new Point(0, -1);
	public static final Point DOWN = new Point(0, 1);

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(Point p) {
		x = p.getX();
		y = p.getY();
	}

	public Point add(Point p) {
		return new Point(x + p.x, y + p.y);
	}

	public Point subtract(Point p) {
		return new Point(x - p.x, y - p.y);
	}

	public Point scalarProd(int f) {
		return new Point(x * f, y * f);
	}

	public Point absValues() {
		return new Point(Math.abs(x), Math.abs(y));
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean equals(Point other) {
		if (other == null) {
			return false;
		} else {
			return (x == other.getX() && y == other.y);
		}
	}

	public Point rotateLeft() {
		return new Point(y, -x);
	}

	public Point rotateRight() {
		return new Point(-y, x);
	}

	public String toString() {
		return "[" + x + "," + y + "]";
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}
}