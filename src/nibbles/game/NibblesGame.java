package nibbles.game;

import java.io.Serializable;
import java.util.*;

public class NibblesGame implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// Game variables
	private int nPlayers;
	private Colors colorTable;
	private int nLevel;
	private int currentFoodNumber;
	private Vector2D currentFoodPosition;
	public static final int MAX_FOOD_NUMBER = 9;
	private boolean startRoundSound;
	private final NibblesSpeaker speaker;

	private Arena arena;
	private Snake[] snakes;

	// Time variables
	private final LogicTimer logicTimer;

	// Random class instance
	private Random rnd = new Random();

	public NibblesGame(int nPlayers, int speed, boolean isMonochrome,
			NibblesSpeaker speaker) {
		this.nPlayers = nPlayers;
		this.speaker = speaker;
		colorTable = isMonochrome ? Colors.MONOCOLORS : Colors.NORMALCOLORS;
		logicTimer = new LogicTimer((1000 * 2) / (speed + 10));
		SoundSequence.init(speaker);
		resetGame();
		startLevel();
	}

	public boolean step() {
		boolean nextBeat = logicTimer.step();

		if (nextBeat) {
			if (startRoundSound) {
				startRoundSound = false;
				speaker.playSoundSeq(SoundSequence.START_ROUND);
			}

			for (Snake snake : snakes) {
				snake.step();
			}

			boolean snakeLostLife = false;
			boolean snakeDied = false;
			boolean snakeAte = false;
			for (Snake snake : snakes) {
				if (!snakeAte
						&& snake.checkForFood(currentFoodPosition,
								currentFoodNumber)) {
					snakeAte = true;
					speaker.playSoundSeq(SoundSequence.HIT_NUMBER);
				}
				if (!snake.draw(snakes)) {
					snakeLostLife = true;
					snakeDied = snakeDied || (snake.getNLives() == 0);
				}
			}
			if (snakeLostLife) {
				if (snakeDied) {
					resetGame();
				}
				speaker.playSoundSeq(SoundSequence.DEATH);
				startLevel();
			} else if (snakeAte) {
				if (currentFoodNumber == MAX_FOOD_NUMBER) {
					if (nLevel < Level.N_LEVELS - 1) {
						nLevel++;
					}
					nextLevel();
				} else {
					nextFood();
				}
			}
		}

		return nextBeat;
	}

	private void nextLevel() {
		pause();
		Level level = Level.LEVELS[nLevel];
		arena.setLevel(level);

		for (int i = 0; i < nPlayers; i++) {
			snakes[i].createSnakeBody(level.getSnakeInitialHeadPosition(i),
					level.getSnakeInitialDirection(i));
		}
		currentFoodNumber = 0;
		nextFood();
	}

	private void startLevel() {
		nextLevel();
		startRoundSound = true;
	}

	private void resetGame() {
		arena = new Arena(colorTable, logicTimer);
		nLevel = 0;
		snakes = new Snake[nPlayers];
		for (int i = 0; i < nPlayers; i++) {
			snakes[i] = new Snake(colorTable.getSnake(i),
					colorTable.getBackground(), arena, Snake.PREDEF[i]);
		}
	}

	private void nextFood() {
		currentFoodNumber++;
		do {
			currentFoodPosition = new Vector2D(
					rnd.nextInt(Arena.WIDTH - 2) + 1,
					rnd.nextInt((Arena.HEIGHT - 6) / 2) * 2 + 4);
		} while (!arena.placeFood(currentFoodPosition));
	}

	public void addDirection(int snakeId, Vector2D direction) {
		snakes[snakeId].addDirection(direction);
	}

	public void turnLeft() {
		snakes[0].turnLeft();
	}

	public void turnRight() {
		snakes[0].turnRight();
	}

	public void output(NibblesScreen screen) {
		arena.output(screen);
		for (Snake snake : snakes) {
			snake.output(screen, colorTable.getDialogsForeground());
		}
		screen.write(Integer.toString(currentFoodNumber), currentFoodPosition, colorTable.getFood());
//		screen.update(currentFoodPosition, colorTable.getFood());
//		screen.update(Arena.getSisterPoint(currentFoodPosition),
//				colorTable.getFood());
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
		for (Snake snake : snakes) {
			snake.clearDirectionBuffer();
		}
		logicTimer.start();
	}
}
