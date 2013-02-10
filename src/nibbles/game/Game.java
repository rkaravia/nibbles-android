package nibbles.game;

import java.io.Serializable;
import java.util.*;

import nibbles.game.Colors.ColorKey;

public class Game implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int MAX_FOOD_NUMBER = 9;

	private static final String TEXT_LAGGING = "LAGGING";
	private static final Point P_LAGGING = new Point(35, 0);

	private final int nPlayers;
	private final LogicTimer logicTimer;
	private final Colors colorTable;

	private int nLevel = 0;
	private boolean gameOver = false;

	private Arena arena;
	private Snake[] snakes;
	transient private List<SnakeAI> snakeAIs = new ArrayList<SnakeAI>();
	transient private Speaker speaker;
	transient private GameOverListener gameOverListener;

	private int foodNumber;
	private Point foodPosition;
	private boolean startLevelSound;

	private Random rnd = new Random();

	public Game(int nPlayers, int speed, boolean isMonochrome) {
		this.nPlayers = nPlayers;
		colorTable = isMonochrome ? Colors.MONO : Colors.NORMAL;
		logicTimer = new LogicTimer((1000 * 2) / (speed + 10));
		initGame();
		restartLevel();
	}

	public void setGameOverListener(GameOverListener gameOverListener) {
		this.gameOverListener = gameOverListener;
	}

	public void initSpeaker(Speaker speaker) {
		this.speaker = speaker;
		SoundSeq.init(speaker);
	}

	public void initAI(int... playerIds) {
		for (int i = 0; i < playerIds.length; i++) {
			snakeAIs.add(new SnakeAI(snakes[playerIds[i]], arena));
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
		}

		return nextBeat;
	}

	private void moveSnakes() {
		for (SnakeAI snakeAI : snakeAIs) {
			snakeAI.stepASTAR(foodPosition);
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
				snakeLostLife = true;
				gameOver = gameOver || (snake.getNLives() == 0);
			}
		}
		if (snakeLostLife) {
			speaker.playSoundSeq(SoundSeq.DEATH);
			if (gameOver) {
				pause();
				gameOverListener.gameOver();
			} else {
				restartLevel();
			}
		} else if (snakeAte) {
			if (foodNumber == MAX_FOOD_NUMBER) {
				nLevel++;
				startLevel();
			} else {
				nextFood();
			}
		}
	}

	private void startLevel() {
		pause();
		arena.setLevel(nLevel);
		for (Snake snake : snakes) {
			snake.startLevel(Level.get(nLevel));
		}
		foodNumber = 0;
		nextFood();
	}

	private void restartLevel() {
		startLevel();
		startLevelSound = true;
	}

	private void initGame() {
		arena = new Arena(colorTable, logicTimer);
		snakes = new Snake[nPlayers];
		for (int i = 0; i < nPlayers; i++) {
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
		if (snakeId < nPlayers) {
			snakes[snakeId].direction().add(direction);
		}
	}

	public void turnLeft(int snakeId) {
		if (snakeId < nPlayers) {
			snakes[snakeId].direction().turnLeft();
		}
	}

	public void turnRight(int snakeId) {
		if (snakeId < nPlayers) {
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
	}

	public void toggleState() {
		if (logicTimer.isPaused()) {
			unpause();
		} else {
			pause();
		}
	}

	public void pause() {
		logicTimer.pause();
	}

	public void unpause() {
		if (!gameOver) {
			for (Snake snake : snakes) {
				snake.direction().reset();
			}
			logicTimer.start();
		}
	}
}
