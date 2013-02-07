package nibbles.game;

public class Wall {
	private Point start;
	private int length;
	private Point direction;
	private Point end;

	public Wall(Point start, int length, Point direction) {
		this.start = start;
		this.length = length;
		this.direction = direction;
		end = start.add(direction.scalarProd(length - 1));
	}

	public void draw(Arena arena, byte wallColor) {
		Point p = start;
		for (int i = 0; i < length; i++) {
			arena.setContent(p, wallColor);
			p = p.add(direction);
		}
	}

	public void output(Screen screen, byte color) {
		if (direction.length() == 1) {
			screen.draw(start, end, color);
		} else {
			Point p = start;
			for (int i = 0; i < length; i++) {
				screen.draw(p, color);
				p = p.add(direction);
			}
		}
	}
}
