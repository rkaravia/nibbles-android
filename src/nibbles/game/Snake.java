package nibbles.game;

import java.io.Serializable;
import java.util.*;

import nibbles.game.Colors.ColorKey;

public class Snake implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int INIT_N_LIVES = 5;
	public static final int SCORE_MULTIPLIER = 100;
	public static final int DEATH_DEDUCTION = 10;

	public static final SnakeData[] SNAKE_DATA = {
			new SnakeData("SAMMY", "%1$s-->  Lives: %2$1d     %3$9d",
					new Point(48, 0)),
			new SnakeData("JAKE", "%3$9d  Lives: %2$1d  <--%1$s", new Point(0,
					0)) };

	private final int id;
	private final Arena arena;
	private final Colors colorTable;

	private int nLives;
	private int score;

	private LinkedList<Point> body;
	private int targetLength;

	private Point headPosition;

	private final DirectionBuffer directionBuffer = new DirectionBuffer();

	private final byte color;

	public Snake(int id, Arena arena, Colors colorTable) {
		this.id = id;
		this.arena = arena;
		this.colorTable = colorTable;
		color = colorTable.getSnake(id);
	}

	public void init() {
		nLives = INIT_N_LIVES;
		score = 0;
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

		public synchronized void add(Point direction) {
			if (buffer.isEmpty()
					|| !direction.absValues().equals(
							buffer.getLast().absValues())) {
				buffer.add(direction);
			}
		}

		public synchronized Point get() {
			return buffer.getFirst();
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
	}

	public void startLevel(Level level) {
		headPosition = level.getSnakeInitialHeadPosition(id);
		Point direction = level.getSnakeInitialDirection(id);
		directionBuffer.init(direction);
		targetLength = 2;
		body = new LinkedList<Point>();

		Point tailPosition = headPosition.subtract(direction);
		addBodyPart(tailPosition);
		addBodyPart(headPosition);
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

	private void addBodyPart(Point position) {
		arena.setContent(position, color);
		body.add(position);
	}

	private void removeBodyPart() {
		arena.setContent(body.removeFirst(), colorTable.get(ColorKey.BG));
	}

	private boolean doesCollide(Snake[] snakes) {
		boolean headCollision = false;
		for (Snake snake : snakes) {
			if (snake != this && snake.headPosition.equals(headPosition)) {
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

	public DirectionBuffer direction() {
		return directionBuffer;
	}

	public byte getColor() {
		return color;
	}
}
