package nibbles.game;

/*
 *	Author:      Roman Zoller
 *	Date:        20.03.2008
 */

import java.util.*;

public class Snake {
	public static final int N_LIVES_START = 5;
	public static final int SCOREMULTIPLIER = 100;
	public static final int DEATHDEDUCTION = 10;

	public static final SnakeData[] PREDEF = new SnakeData[] {
			new SnakeData("SAMMY", "%1$s-->  Lives: %2$1d     %3$9d",
					new Vector2D(48, 0)),
			new SnakeData("JAKE", "%3$9d  Lives: %2$1d  <--%1$s", new Vector2D(
					0, 0)) };

	// public static final String[] NAMES = new String[] { "SAMMY", "JAKE" };
	// public static final String[] FORMAT_STRINGS = new String[] {
	// "%1$s-->  Lives: %2$1d     %3$9d", "%3$9d  Lives: %2$1d  <--%1$s" };
	// public static final Vector2D[] INFO_OUTPUT_LOCATIONS = new Vector2D[] {
	// new Vector2D(48, 0), new Vector2D(0, 0), };

	private int nLives = N_LIVES_START;
	private int score = 0;

	private byte snakeColor;
	private byte backgroundColor;
	private Arena arena;

	private Vector<Vector2D> body;
	private int targetLength;

	private Vector2D headPosition;

	private boolean validState = false;
	private final SnakeData snakeData;
	
	private final DirectionBuffer directionBuffer = new DirectionBuffer();

	public Snake(byte snakeColor, byte backgroundColor, Arena arena,
			SnakeData snakeData) {
		this.snakeColor = snakeColor;
		this.backgroundColor = backgroundColor;
		this.arena = arena;
		this.snakeData = snakeData;
	}

	private static class SnakeData {
		private final String name;
		private final String format;
		private final Vector2D infoLocation;

		public SnakeData(String name, String format, Vector2D infoLocation) {
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

		public Vector2D getInfoLocation() {
			return infoLocation;
		}
	}
	
	private static class DirectionBuffer {
		private final LinkedList<Vector2D> directions = new LinkedList<Vector2D>();
		
		private synchronized void update() {
			if (directions.size() > 1) {
				directions.removeFirst();
			}
		}
		
		private synchronized void add(Vector2D direction) {
			if (directions.isEmpty() || !direction.getAbsValues().equals(
					directions.getLast().getAbsValues())) {
				directions.add(direction);
			}
		}
		
		private synchronized void init(Vector2D direction) {
			directions.clear();
			directions.add(direction);
		}
		
		private synchronized void reset() {
			init(get());
		}
		
		private synchronized Vector2D get() {
			return directions.getFirst();
		}

		private synchronized void turnLeft() {
			if (!directions.isEmpty()) {
				directions.add(directions.getLast().rotateLeft());
			}
		}
		
		private synchronized void turnRight() {
			if (!directions.isEmpty()) {
				directions.add(directions.getLast().rotateRight());
			}
		}
	}

	public void createSnakeBody(Vector2D initialHeadPosition,
			Vector2D direction) {
//		directions = new LinkedList<Vector2D>();
//		directions.add(direction);
		directionBuffer.init(direction);
		targetLength = 2;
		body = new Vector<Vector2D>();

		Vector2D initialTailPosition = initialHeadPosition.subtract(direction);
		addPixel(initialTailPosition);
		addPixel(initialHeadPosition);
		validState = true;
	}

	public synchronized void step() {
//		if (directions.size() > 1) {
//			directions.removeFirst();
//		}
		directionBuffer.update();
		headPosition = ((Vector2D) body.lastElement()).add(directionBuffer.get());
	}

	public boolean draw(Snake[] snakes) {
		if (checkForCollision(snakes)) {
			nLives--;
			score -= DEATHDEDUCTION;
			return false;
		} else {
			addPixel(headPosition);
			if (body.size() > targetLength) {
				removePixel();
			}
			return true;
		}
	}

	public Vector2D getHeadPosition() {
		return headPosition;
	}

	public void addDirection(Vector2D direction) {
//		if (!direction.getAbsValues().equals(
//				directions.getLast().getAbsValues())) {
//			directions.add(direction);
//		}
		directionBuffer.add(direction);
	}

	private void addPixel(Vector2D position) {
		arena.setContent(position, snakeColor);
		body.addElement(position);
	}

	private void removePixel() {
		arena.setContent((Vector2D) body.firstElement(), backgroundColor);
		body.removeElementAt(0);
	}

	private boolean checkForCollision(Snake[] snakes) {
		boolean headCollision = false;
		for (int i = 0; i < snakes.length; i++) {
			if (snakes[i] != this
					&& snakes[i].headPosition.equals(headPosition)) {
				headCollision = true;
			}
		}

		return (headCollision || arena.getContent(headPosition) != backgroundColor);
	}

	public boolean checkForFood(Vector2D p1, int foodNumber) {
		Vector2D p2 = Arena.getSisterPoint(p1);
		boolean result = headPosition.equals(p1) || headPosition.equals(p2);
		if (result) {
			score += foodNumber;
			targetLength += 4 * foodNumber;
		}
		return result;
	}

	public int getNLives() {
		return nLives;
	}

	public void output(NibblesScreen screen, int textColor) {
		for (Vector2D bodyPart : body) {
			screen.update(bodyPart, arena.getColor(bodyPart));
			Vector2D sisterPoint = Arena.getSisterPoint(bodyPart);
			screen.update(sisterPoint, arena.getColor(sisterPoint));
		}

		String info = String.format(snakeData.getFormat(), snakeData.getName(),
				nLives, score * 100);
		Vector2D infoLocation = snakeData.getInfoLocation();
		screen.write(info, infoLocation, textColor);
	}

	public void turnLeft() {
		directionBuffer.turnLeft();
//		directions.add(directions.getLast().rotateLeft());
	}

	public void turnRight() {
		directionBuffer.turnRight();
//		directions.add(directions.getLast().rotateRight());
	}

	public void clearDirectionBuffer() {
//		Vector2D direction = directions.getFirst();
//		directions.clear();
//		directions.add(direction);
		directionBuffer.reset();
	}
}
