package nibbles.game;

public class Level {
	private Point[] snakeInitialHeadPositions;
	private Point[] snakeInitialDirections;
	private Wall[] walls;
	
	private static final Point DOWN_2 = new Point(0, 2);
	private static final Point DOWN_RIGHT = new Point(1, 1);

	private static final Level[] LEVELS = {
		new Level(	//Level 1
				new Point[]{
						new Point(49, 24),
						new Point(29, 24),
						new Point(39, 21),
						new Point(39, 27),
				},
				new Point[]{
						Point.RIGHT,
						Point.LEFT,
						Point.UP,
						Point.DOWN,
				},
				new Wall[]{
				}
		),
		new Level(	//Level 2
				new Point[]{
						new Point(59, 6),
						new Point(19, 42),
						new Point(19, 6),
						new Point(59, 42),
				},
				new Point[]{
						Point.LEFT,
						Point.RIGHT,
						Point.RIGHT,
						Point.LEFT,
				},
				new Wall[]{
						new Wall(new Point(19, 24), 41, Point.RIGHT),
				}
		),
		new Level(	//Level 3
				new Point[]{
						new Point(49, 24),
						new Point(29, 24),
						new Point(9, 24),
						new Point(69, 24),
				},
				new Point[]{
						Point.UP,
						Point.DOWN,
						Point.UP,
						Point.DOWN,
				},
				new Wall[]{
						new Wall(new Point(19, 9), 31, Point.DOWN),
						new Wall(new Point(59, 9), 31, Point.DOWN),
				}
		),
		new Level(	//Level 4
				new Point[]{
						new Point(59, 6),
						new Point(19, 42),
						new Point(69, 42),
						new Point(9, 6),
				},
				new Point[]{
						Point.LEFT,
						Point.RIGHT,
						Point.UP,
						Point.DOWN,
				},
				new Wall[]{
						new Wall(new Point(19, 3), 27, Point.DOWN),
						new Wall(new Point(59, 48), 27, Point.UP),
						new Wall(new Point(1, 37), 39, Point.RIGHT),
						new Wall(new Point(78, 14), 39, Point.LEFT),
				}
		),
		new Level(	//Level 5
				new Point[]{
						new Point(49, 24),
						new Point(29, 24),
						new Point(39, 16),
						new Point(39, 32),
				},
				new Point[]{
						Point.UP,
						Point.DOWN,
						Point.LEFT,
						Point.RIGHT,
				},
				new Wall[]{
						new Wall(new Point(20, 12), 27, Point.DOWN),
						new Wall(new Point(58, 12), 27, Point.DOWN),
						new Wall(new Point(22, 10), 35, Point.RIGHT),
						new Wall(new Point(22, 40), 35, Point.RIGHT),
				}
		),
		new Level(	//Level 6
				new Point[]{
						new Point(64, 6),
						new Point(14, 42),
						new Point(64, 42),
						new Point(14, 6),
				},
				new Point[]{
						Point.DOWN,
						Point.UP,
						Point.UP,
						Point.DOWN,
				},
				new Wall[]{
						new Wall(new Point(9, 3), 19, Point.DOWN),
						new Wall(new Point(19, 3), 19, Point.DOWN),
						new Wall(new Point(29, 3), 19, Point.DOWN),
						new Wall(new Point(39, 3), 19, Point.DOWN),
						new Wall(new Point(49, 3), 19, Point.DOWN),
						new Wall(new Point(59, 3), 19, Point.DOWN),
						new Wall(new Point(69, 3), 19, Point.DOWN),
						new Wall(new Point(9, 30), 19, Point.DOWN),
						new Wall(new Point(19, 30), 19, Point.DOWN),
						new Wall(new Point(29, 30), 19, Point.DOWN),
						new Wall(new Point(39, 30), 19, Point.DOWN),
						new Wall(new Point(49, 30), 19, Point.DOWN),
						new Wall(new Point(59, 30), 19, Point.DOWN),
						new Wall(new Point(69, 30), 19, Point.DOWN),
				}
		),
		new Level(	//Level 7
				new Point[]{
						new Point(64, 6),
						new Point(14, 42),
						new Point(64, 42),
						new Point(14, 6),
				},
				new Point[]{
						Point.DOWN,
						Point.UP,
						Point.UP,
						Point.DOWN,
				},
				new Wall[]{
						new Wall(new Point(39, 3), 23, DOWN_2),
				}
		),
		new Level(	//Level 8
				new Point[]{
						new Point(64, 6),
						new Point(14, 42),
						new Point(44, 42),
						new Point(34, 6),
				},
				new Point[]{
						Point.DOWN,
						Point.UP,
						Point.UP,
						Point.DOWN,
				},
				new Wall[]{
						new Wall(new Point(9, 3), 37, Point.DOWN),
						new Wall(new Point(29, 3), 37, Point.DOWN),
						new Wall(new Point(49, 3), 37, Point.DOWN),
						new Wall(new Point(69, 3), 37, Point.DOWN),
						new Wall(new Point(19, 48), 37, Point.UP),
						new Wall(new Point(39, 48), 37, Point.UP),
						new Wall(new Point(59, 48), 37, Point.UP),
				}
		),
		new Level(	//Level 9
				new Point[]{
						new Point(74, 39),
						new Point(4, 14),
						new Point(42, 6),
						new Point(36, 46),
				},
				new Point[]{
						Point.UP,
						Point.DOWN,
						Point.RIGHT,
						Point.LEFT,
				},
				new Wall[]{
						new Wall(new Point(5, 5), 42, DOWN_RIGHT),
						new Wall(new Point(33, 5), 42, DOWN_RIGHT),
				}
		),
		new Level(	//Level 10+
				new Point[]{
						new Point(64, 6),
						new Point(14, 42),
						new Point(64, 42),
						new Point(14, 6),
				},
				new Point[]{
						Point.DOWN,
						Point.UP,
						Point.UP,
						Point.DOWN,
				},
				new Wall[]{
						new Wall(new Point(9, 3), 23, DOWN_2),
						new Wall(new Point(19, 4), 23, DOWN_2),
						new Wall(new Point(29, 3), 23, DOWN_2),
						new Wall(new Point(39, 4), 23, DOWN_2),
						new Wall(new Point(49, 3), 23, DOWN_2),
						new Wall(new Point(59, 4), 23, DOWN_2),
						new Wall(new Point(69, 3), 23, DOWN_2),
				}
		),
	};
	
	public static Level get(int id) {
		return LEVELS[Math.min(id, LEVELS.length - 1)];
	}

	public Level(Point[] snakeInitialHeadPositions, Point[] snakeInitialDirections, Wall[] walls){
		this.snakeInitialHeadPositions = snakeInitialHeadPositions;
		this.snakeInitialDirections = snakeInitialDirections;
		this.walls = walls;
	}
	
	public void draw(Arena arena, byte wallColor){
		for(int i = 0; i < walls.length; i++){
			walls[i].draw(arena, wallColor);
		}
	}
	
	public Point getSnakeInitialHeadPosition(int i){
		return snakeInitialHeadPositions[i];
	}
	
	public Point getSnakeInitialDirection(int i){
		return snakeInitialDirections[i];
	}

	public void output(Screen screen, byte color) {
		for(int i = 0; i < walls.length; i++){
			walls[i].output(screen, color);
		}
	}
}
