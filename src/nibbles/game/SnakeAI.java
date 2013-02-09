package nibbles.game;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;

public class SnakeAI implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final Snake snake;
	private final Arena arena;
	
	public SnakeAI(Snake snake, Arena arena) {
		this.snake = snake;
		this.arena = arena;
	}
	
	public void stepBFS(Point food) {
		int foodDst[][] = new int[Arena.WIDTH][Arena.HEIGHT];
		foodDst[food.getX()][food.getY()] = 1;
		LinkedList<Point> queue = new LinkedList<Point>();
		queue.add(food);
		Point snakeHead = snake.getHeadPosition();
		boolean foundSnake = false;
		while (!queue.isEmpty() && !foundSnake) {
			Point p = queue.removeFirst();
			int dst = foodDst[p.getX()][p.getY()];
			for (Point d : Point.DIRECTIONS) {
				Point cand = p.add(d);
				foundSnake = foundSnake || cand.equals(snakeHead);
				if (arena.isEmpty(cand) && foodDst[cand.getX()][cand.getY()] == 0) {
					foodDst[cand.getX()][cand.getY()] = dst + 1;
					queue.add(cand);
				}
			}
		}
		
		HashSet<Point> minDirs = new HashSet<Point>();
		HashSet<Point> possibleDirs = new HashSet<Point>();
		int minDst = Integer.MAX_VALUE;
		for (Point d : Point.DIRECTIONS) {
			Point cand = snakeHead.add(d);
			if (arena.isEmpty(cand)) {
				possibleDirs.add(d);
			}
			int dst = foodDst[cand.getX()][cand.getY()];
			if (dst != 0) {
				if (dst < minDst) {
					minDst = dst;
					minDirs.clear();
				}
				if (dst <= minDst) {
					minDirs.add(d);
				}
			}
		}
		
		Point currDirection = snake.getDirectionBuffer().get();
		
		if (minDirs.isEmpty()) {
			if (!possibleDirs.isEmpty() && !possibleDirs.contains(currDirection))
			snake.addDirection((Point) possibleDirs.toArray()[0]);
		} else if (!minDirs.contains(currDirection)) {
			snake.addDirection((Point) minDirs.toArray()[0]);
		}
	}
	

}
