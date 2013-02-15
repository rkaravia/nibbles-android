package nibbles.game;

import java.io.Serializable;
import java.util.*;

import nibbles.game.Colors.ColorKey;

public class Game implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MAX_FOOD_NUMBER = 9;

	private static final String TEXT_LAGGING = "LAGGING";
	private static final Point P_LAGGING = new Point(35, 0);
	private static final String NOTIFICATION_LEVEL_N = "Level %d,  Push Space";
	private static final String NOTIFICATION_PAUSED = "Game Paused ... Push Space";
	private static final String NOTIFICATION_GAME_OVER = "Game Over";

	private final int nHumans;
	private final int nAI;
	private final LogicTimer logicTimer;
	private final Colors colorTable;

	private int nLevel = 0;
	private boolean startLevelSound = true;
	private boolean gameOver = false;
	private boolean notificationChanged = false;
	private boolean initialized = false;

	private Arena arena;
	private Snake[] snakes;

	transient private List<SnakeAI> snakeAIs;
	transient private Speaker speaker;
	transient private GameOverListener gameOverListener;

	private int foodNumber;
	private Point foodPosition;
	private LinkedList<String> notifications = new LinkedList<String>();

	private final Random rnd = new Random();

	public Game(int nHumans, int nAI, int speed, boolean isMonochrome) {
		this.nHumans = nHumans;
		this.nAI = nAI;
		colorTable = isMonochrome ? Colors.MONO : Colors.NORMAL;
		logicTimer = new LogicTimer((1000 * 2) / (speed + 10));
		initGame();
		startLevel(true);
	}

	public synchronized void init(GameOverListener gameOverListener,
			Speaker speaker) {
		this.gameOverListener = gameOverListener;
		this.speaker = speaker;
		SoundSeq.init(speaker);
		snakeAIs = new ArrayList<SnakeAI>();
		for (int i = 0; i < nAI; i++) {
			snakeAIs.add(new SnakeAI(snakes[nHumans + i], arena));
		}
		initialized = true;
	}

	public synchronized boolean step() {
		if (initialized) {
			if (logicTimer.step()) {
				if (startLevelSound) {
					startLevelSound = false;
					speaker.playSoundSeq(SoundSeq.START_ROUND);
				}
				moveSnakes();
				return true;
			} else if (notificationChanged) {
				notificationChanged = false;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private void moveSnakes() {
		ArrayList<Point> snakeHeads = new ArrayList<Point>(nHumans + nAI);
		for (int i = 0; i < nHumans; i++) {
			snakeHeads.add(snakes[i].prepareStep());
		}
		for (int i = 0; i < nAI; i++) {
			snakeHeads.add(snakeAIs.get(i).step(foodPosition, snakeHeads.toArray(new Point[snakeHeads.size()])));
		}

		boolean snakeLostLife = false;
		boolean snakeAte = false;
		for (Snake snake : snakes) {
			if (!snakeAte && snake.doesEat(foodPosition, foodNumber)) {
				snakeAte = true;
				speaker.playSoundSeq(SoundSeq.HIT_NUMBER);
			}
			if (!snake.step(snakes)) {
				addNotification(snake.getDeathNotification());
				snakeLostLife = true;
				gameOver = gameOver || (snake.getNLives() == 0);
			}
		}
		if (snakeLostLife) {
			speaker.playSoundSeq(SoundSeq.DEATH);
			if (gameOver) {
				logicTimer.pause();
				notifications.clear();
				addNotification(NOTIFICATION_GAME_OVER);
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

	private void addNotification(String notification) {
		notifications.add(notification);
		notificationChanged = true;
	}

	private void startLevel(boolean showNotification) {
		logicTimer.pause();
		if (showNotification) {
			addNotification(String.format(NOTIFICATION_LEVEL_N, nLevel + 1));
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

	public synchronized void addDirection(int snakeId, Point direction) {
		if (snakeId < nHumans) {
			snakes[snakeId].direction().add(direction);
		}
	}

	public synchronized void turnLeft(int snakeId) {
		if (snakeId < nHumans) {
			snakes[snakeId].direction().turnLeft();
		}
	}

	public synchronized void turnRight(int snakeId) {
		if (snakeId < nHumans) {
			snakes[snakeId].direction().turnRight();
		}
	}

	public synchronized void draw(Screen screen) {
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
			screen.notification(notifications.getFirst(),
					colorTable.get(ColorKey.DIALOG_FG),
					colorTable.get(ColorKey.DIALOG_BG));
		}
	}

	public synchronized void pushSpace() {
		if (!gameOver) {
			if (notifications.poll() != null) {
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
	}

	public synchronized void pause() {
		if (!logicTimer.isPaused()) {
			logicTimer.pause();
			addNotification(NOTIFICATION_PAUSED);
		}
	}

	private synchronized void unpause() {
		for (Snake snake : snakes) {
			snake.direction().reset();
		}
		logicTimer.start();

	}
}
