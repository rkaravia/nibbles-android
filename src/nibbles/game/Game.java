package nibbles.game;

import java.io.Serializable;
import java.util.*;

import nibbles.game.Colors.ColorKey;

public class Game implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MAX_FOOD_NUMBER = 9;

	private static final String TEXT_LAGGING = "LAGGING";
	private static final Point P_LAGGING = new Point(35, 0);
	private static final String LEVEL_NOTIFICATION = "Level %d,  Push Space";
	private static final String PAUSED_NOTIFICATION = "Game Paused ... Push Space";

	private final int nHumans;
	private final int nAI;
	private final LogicTimer logicTimer;
	private final Colors colorTable;

	private int nLevel = 0;
	private boolean startLevelSound = true;
	private boolean gameOver = false;
	private boolean notificationChanged = false;  //TODO thread safety

	private Arena arena;
	private Snake[] snakes;
	transient private List<SnakeAI> snakeAIs = new ArrayList<SnakeAI>();
	transient private Speaker speaker;
	transient private GameOverListener gameOverListener;

	private int foodNumber;
	private Point foodPosition;
	private LinkedList<String> notifications = new LinkedList<String>();

	private Random rnd = new Random();

	public Game(int nHumans, int nAI, int speed, boolean isMonochrome) {
		this.nHumans = nHumans;
		this.nAI = nAI;
		colorTable = isMonochrome ? Colors.MONO : Colors.NORMAL;
		logicTimer = new LogicTimer((1000 * 2) / (speed + 10));
		initGame();
		startLevel(true);
	}

	public void setGameOverListener(GameOverListener gameOverListener) {
		this.gameOverListener = gameOverListener;
	}

	public void initSpeaker(Speaker speaker) {
		this.speaker = speaker;
		SoundSeq.init(speaker);
	}

	public void initAI() {
		for (int i = 0; i < nAI; i++) {
			snakeAIs.add(new SnakeAI(snakes[nHumans + i], arena));
		}
	}

	public boolean step() {
		boolean nextBeat = logicTimer.step();

		if (nextBeat) {
			if (startLevelSound) {
				startLevelSound = false;
				speaker.playSoundSeq(SoundSeq.START_ROUND);
			}
			moveSnakes();
			return true;
		} else if (notificationChanged) {
			notificationChanged = false;
			return true;
		}

		return nextBeat;
	}

	private void moveSnakes() {
		for (SnakeAI snakeAI : snakeAIs) {
			snakeAI.step(foodPosition);
		}
		for (Snake snake : snakes) {
			snake.prepareStep();
		}

		boolean snakeLostLife = false;
		boolean snakeAte = false;
		for (Snake snake : snakes) {
			if (!snakeAte && snake.doesEat(foodPosition, foodNumber)) {
				snakeAte = true;
				speaker.playSoundSeq(SoundSeq.HIT_NUMBER);
			}
			if (!snake.step(snakes)) {
				notifications.add(snake.getDeathNotification());
				notificationChanged = true;
				snakeLostLife = true;
				gameOver = gameOver || (snake.getNLives() == 0);
			}
		}
		if (snakeLostLife) {
			speaker.playSoundSeq(SoundSeq.DEATH);
			if (gameOver) {
				logicTimer.pause();
				gameOverListener.gameOver();
			} else {
				restartLevel();
			}
		} else if (snakeAte) {
			if (foodNumber == MAX_FOOD_NUMBER) {
				nLevel++;
				startLevel(true);
			} else {
				nextFood();
			}
		}
	}

	private void startLevel(boolean showNotification) {
		logicTimer.pause();
		if (showNotification) {
			notifications.add(String.format(LEVEL_NOTIFICATION, nLevel + 1));
			notificationChanged = true;
		}
		arena.setLevel(nLevel);
		for (Snake snake : snakes) {
			snake.startLevel(Level.get(nLevel));
		}
		foodNumber = 0;
		nextFood();
	}

	private void restartLevel() {
		startLevel(false);
		startLevelSound = true;
	}

	private void initGame() {
		arena = new Arena(colorTable, logicTimer);
		snakes = new Snake[nHumans + nAI];
		for (int i = 0; i < snakes.length; i++) {
			snakes[i] = new Snake(i, arena, colorTable);
		}
		for (Snake snake : snakes) {
			snake.init();
		}
	}

	private void nextFood() {
		foodNumber++;
		do {
			foodPosition = new Point(rnd.nextInt(Arena.WIDTH - 2) + 1,
					rnd.nextInt((Arena.HEIGHT - 6) / 2) * 2 + 4);
		} while (!arena.placeFood(foodPosition));
	}

	public void addDirection(int snakeId, Point direction) {
		if (snakeId < nHumans) {
			snakes[snakeId].direction().add(direction);
		}
	}

	public void turnLeft(int snakeId) {
		if (snakeId < nHumans) {
			snakes[snakeId].direction().turnLeft();
		}
	}

	public void turnRight(int snakeId) {
		if (snakeId < nHumans) {
			snakes[snakeId].direction().turnRight();
		}
	}

	public void draw(Screen screen) {
		arena.draw(screen);
		for (Snake snake : snakes) {
			snake.draw(screen, colorTable.get(ColorKey.DIALOG_FG));
		}
		// for (SnakeAI snakeAI : snakeAIs) {
		// snakeAI.draw(screen);
		// }
		screen.write(Integer.toString(foodNumber), foodPosition,
				colorTable.get(ColorKey.FOOD));
		if (logicTimer.isLagging()) {
			screen.write(TEXT_LAGGING, P_LAGGING,
					colorTable.get(ColorKey.DIALOG_FG));
		}
		
		if (!notifications.isEmpty()) {
			screen.notification(notifications.getFirst(), colorTable.get(ColorKey.DIALOG_FG), colorTable.get(ColorKey.DIALOG_BG));
		}
	}

	public void pushSpace() {
		if (!notifications.isEmpty()) {
			notifications.removeFirst();
			notificationChanged = true;
		}
		if (notifications.isEmpty()) {
			if (logicTimer.isPaused()) {
				unpause();
			} else {
				pause();
			}
		}
	}

	public void pause() {
		if (!logicTimer.isPaused()) {
			logicTimer.pause();
			notifications.add(PAUSED_NOTIFICATION);
			notificationChanged = true;
		}
	}

	private void unpause() {
		if (!gameOver) {
			for (Snake snake : snakes) {
				snake.direction().reset();
			}
			logicTimer.start();
		}
	}
}
