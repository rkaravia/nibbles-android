package nibbles.game;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import nibbles.game.Snake.DirectionBuffer;

public class SnakeAI implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Snake snake;
	private final Arena arena;
	// private final int[][] foodDst = new int[Arena.WIDTH][Arena.HEIGHT];

	private final int[][] f = new int[Arena.WIDTH][Arena.HEIGHT];

	private static final int DST_UNKNOWN = -1;

	public SnakeAI(Snake snake, Arena arena) {
		this.snake = snake;
		this.arena = arena;
	}

	// public void stepBFS(Point food) {
	// for (int x = 0; x < Arena.WIDTH; x++) {
	// Arrays.fill(foodDst[x], -1);
	// }
	// foodDst[food.getX()][food.getY()] = 0;
	// LinkedList<Point> queue = new LinkedList<Point>();
	// queue.add(food);
	// Point snakeHead = snake.getHeadPosition();
	// boolean foundSnake = false;
	// while (!queue.isEmpty() && !foundSnake) {
	// Point p = queue.removeFirst();
	// int dst = foodDst[p.getX()][p.getY()];
	// for (Point d : Point.DIRECTIONS) {
	// Point cand = p.add(d);
	// foundSnake = foundSnake || cand.equals(snakeHead);
	// if (arena.isEmpty(cand) && foodDst[cand.getX()][cand.getY()] ==
	// DST_UNKNOWN) {
	// foodDst[cand.getX()][cand.getY()] = dst + 1;
	// queue.add(cand);
	// }
	// }
	// }
	//
	// HashSet<Point> minDirs = new HashSet<Point>();
	// HashSet<Point> possibleDirs = new HashSet<Point>();
	// int minDst = Integer.MAX_VALUE;
	// for (Point d : Point.DIRECTIONS) {
	// Point cand = snakeHead.add(d);
	// if (arena.isEmpty(cand)) {
	// possibleDirs.add(d);
	// }
	// int dst = foodDst[cand.getX()][cand.getY()];
	// if (dst != DST_UNKNOWN) {
	// if (dst < minDst) {
	// minDst = dst;
	// minDirs.clear();
	// }
	// if (dst <= minDst) {
	// minDirs.add(d);
	// }
	// }
	// }
	//
	// Point currDirection = snake.getDirectionBuffer().get();
	//
	// if (minDirs.isEmpty()) {
	// if (!possibleDirs.isEmpty() && !possibleDirs.contains(currDirection))
	// snake.addDirection((Point) possibleDirs.toArray()[0]);
	// } else if (!minDirs.contains(currDirection)) {
	// snake.addDirection((Point) minDirs.toArray()[0]);
	// }
	// }

	private int manhattanDist(Point p1, Point p2) {
		return Math.abs(p1.getX() - p2.getX())
				+ Math.abs(p1.getY() - p2.getY());
	}

	public void stepASTAR(Point food) {
		DirectionBuffer dirBuffer = snake.direction();
		Point head = snake.getHeadPosition();
		Point futureHead = head.add(dirBuffer.get());
		for (int x = 0; x < Arena.WIDTH; x++) {
			Arrays.fill(f[x], -1);
		}
		PriorityQueue<Point> queue = new PriorityQueue<Point>(10,
				new Comparator<Point>() {
					@Override
					public int compare(Point p1, Point p2) {
						return f[p1.getX()][p1.getY()]
								- f[p2.getX()][p2.getY()];
					}
				});
		f[food.getX()][food.getY()] = manhattanDist(food, head);
		queue.add(food);
		boolean foundHead = false;
		while (!queue.isEmpty() && !foundHead) {
			Point p = queue.poll();
			int g = f[p.getX()][p.getY()] - manhattanDist(p, head);
			for (Point d : Point.DIRECTIONS) {
				Point cand = p.add(d);
				if (cand.equals(head)) {
					dirBuffer.add(d.rotate180());
					foundHead = true;
				} else if (arena.isEmpty(cand)) {
					if (cand.equals(futureHead)) {
						foundHead = true;
					} else if (f[cand.getX()][cand.getY()] == DST_UNKNOWN) {
						f[cand.getX()][cand.getY()] = g + 1
								+ manhattanDist(cand, head);
						queue.add(cand);
					}
				}
			}
		}
		if (!foundHead) {
			HashSet<Point> possibleDirs = new HashSet<Point>();
			for (Point d : Point.DIRECTIONS) {
				if (arena.isEmpty(head.add(d))) {
					possibleDirs.add(d);
				}
			}
			if (!possibleDirs.isEmpty() && !possibleDirs.contains(dirBuffer.get())) {
				dirBuffer.add((Point) possibleDirs.toArray()[0]);
			}
		}
	}

	public void draw(Screen screen) {
		for (int x = 0; x < Arena.WIDTH; x++) {
			for (int y = 0; y < Arena.HEIGHT; y++) {
				if (f[x][y] > 0) {
					screen.draw(new Point(x, y), snake.getColor(), 0.2f);
				}
			}
		}

	}
}
