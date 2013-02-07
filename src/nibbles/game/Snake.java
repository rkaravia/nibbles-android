package nibbles.game;

import java.io.Serializable;
import java.util.*;

public class Snake implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int INIT_N_LIVES = 5;
	public static final int SCORE_MULTIPLIER = 100;
	public static final int DEATH_DEDUCTION = 10;

	public static final SnakeData[] SNAKE_DATA = new SnakeData[] {
			new SnakeData("SAMMY", "%1$s-->  Lives: %2$1d     %3$9d",
					new Point(48, 0)),
			new SnakeData("JAKE", "%3$9d  Lives: %2$1d  <--%1$s", new Point(0,
					0)) };

	private int nLives = INIT_N_LIVES;
	private int score = 0;

	private byte snakeColor;
	private byte backgroundColor;
	private Arena arena;

	private LinkedList<Point> body;
	private int targetLength;

	private Point headPosition;

	private final int id;

	private final DirectionBuffer directionBuffer = new DirectionBuffer();

	public Snake(byte snakeColor, byte backgroundColor, Arena arena, int id) {
		this.snakeColor = snakeColor;
		this.backgroundColor = backgroundColor;
		this.arena = arena;
		this.id = id;
	}

	private static class SnakeData {
		private final String name;
		private final String format;
		private final Point infoLocation;

		public SnakeData(String name, String format, Point infoLocation) {
			this.name = name;
			this.format = format;
			this.infoLocation = infoLocation;
		}

		public String getName() {
			return name;
		}

		public String getFormat() {
			return format;
		}

		public Point getInfoLocation() {
			return infoLocation;
		}
	}

	public static class DirectionBuffer implements Serializable {
		private static final long serialVersionUID = 1L;

		private final LinkedList<Point> buffer = new LinkedList<Point>();

		public synchronized void reset() {
			init(get());
		}

		public synchronized void turnLeft() {
			if (!buffer.isEmpty()) {
				buffer.add(buffer.getLast().rotateLeft());
			}
		}

		public synchronized void turnRight() {
			if (!buffer.isEmpty()) {
				buffer.add(buffer.getLast().rotateRight());
			}
		}

		private synchronized void init(Point direction) {
			buffer.clear();
			buffer.add(direction);
		}

		private synchronized void update() {
			if (buffer.size() > 1) {
				buffer.removeFirst();
			}
		}

		private synchronized void add(Point direction) {
			if (buffer.isEmpty()
					|| !direction.absValues().equals(
							buffer.getLast().absValues())) {
				buffer.add(direction);
			}
		}

		private synchronized Point get() {
			return buffer.getFirst();
		}
	}

	public void init(Point initialHeadPosition, Point direction) {
		directionBuffer.init(direction);
		targetLength = 2;
		body = new LinkedList<Point>();

		Point initialTailPosition = initialHeadPosition.subtract(direction);
		addBodyPart(initialTailPosition);
		addBodyPart(initialHeadPosition);
	}

	public synchronized void prepareStep() {
		directionBuffer.update();
		headPosition = body.getLast().add(directionBuffer.get());
	}

	public boolean step(Snake[] snakes) {
		if (doesCollide(snakes)) {
			nLives--;
			score -= DEATH_DEDUCTION;
			return false;
		} else {
			addBodyPart(headPosition);
			if (body.size() > targetLength) {
				removeBodyPart();
			}
			return true;
		}
	}

	public Point getHeadPosition() {
		return headPosition;
	}

	public void addDirection(Point direction) {
		directionBuffer.add(direction);
	}

	private void addBodyPart(Point position) {
		arena.setContent(position, snakeColor);
		body.add(position);
	}

	private void removeBodyPart() {
		arena.setContent(body.removeFirst(), backgroundColor);
	}

	private boolean doesCollide(Snake[] snakes) {
		boolean headCollision = false;
		for (int i = 0; i < snakes.length; i++) {
			if (snakes[i] != this
					&& snakes[i].headPosition.equals(headPosition)) {
				headCollision = true;
			}
		}

		return (headCollision || !arena.isEmpty(headPosition));
	}

	public boolean doesEat(Point p, int foodNumber) {
		Point sibling = Arena.getSiblingPoint(p);
		boolean result = headPosition.equals(p) || headPosition.equals(sibling);
		if (result) {
			score += foodNumber;
			targetLength += 4 * foodNumber;
		}
		return result;
	}

	public int getNLives() {
		return nLives;
	}

	public void draw(Screen screen, int textColor) {
		for (Point bodyPart : body) {
			screen.draw(bodyPart, arena.getColor(bodyPart));
			Point sisterPoint = Arena.getSiblingPoint(bodyPart);
			screen.draw(sisterPoint, arena.getColor(sisterPoint));
		}

		String info = String.format(SNAKE_DATA[id].getFormat(),
				SNAKE_DATA[id].getName(), nLives, score * 100);
		Point infoLocation = SNAKE_DATA[id].getInfoLocation();
		screen.write(info, infoLocation, textColor);
	}

	public DirectionBuffer getDirectionBuffer() {
		return directionBuffer;
	}
}
