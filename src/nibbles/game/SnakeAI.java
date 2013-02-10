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

	private final int[][] f = new int[Arena.WIDTH][Arena.HEIGHT];

	private final PriorityQueue<Point> queue = new PriorityQueue<Point>(Arena.WIDTH * Arena.HEIGHT,
			new Comparator<Point>() {
				@Override
				public int compare(Point p1, Point p2) {
					return f[p1.getX()][p1.getY()] - f[p2.getX()][p2.getY()];
				}
			});

	private static final int DST_UNKNOWN = -1;

	public SnakeAI(Snake snake, Arena arena) {
		this.snake = snake;
		this.arena = arena;
	}

	private static int manhattanDist(Point p1, Point p2) {
		return Math.abs(p1.getX() - p2.getX())
				+ Math.abs(p1.getY() - p2.getY());
	}
	
	private void setGoal(Point p) {
		f[p.getX()][p.getY()] = manhattanDist(p, snake.getHeadPosition());
		queue.add(p);
	}

	public void stepASTAR(Point food) {
		DirectionBuffer dirBuffer = snake.direction();
		Point head = snake.getHeadPosition();
		Point futureHead = head.add(dirBuffer.get());
		for (int x = 0; x < Arena.WIDTH; x++) {
			Arrays.fill(f[x], DST_UNKNOWN);
		}
		queue.clear();
		setGoal(food);
		setGoal(Arena.getSiblingPoint(food));
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
					int candX = cand.getX();
					int candY = cand.getY();
					if (cand.equals(futureHead)) {
						foundHead = true;
					} else if (f[candX][candY] == DST_UNKNOWN) {
						f[candX][cand.getY()] = g + 1
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
			if (!possibleDirs.isEmpty()
					&& !possibleDirs.contains(dirBuffer.get())) {
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
