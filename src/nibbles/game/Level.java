package nibbles.game;

public class Level {
	private Vector2D[] snakeInitialHeadPositions;
	private Vector2D[] snakeInitialDirections;
	private Wall[] walls;

	public static final Level[] LEVELS = {
		new Level(	//Level 1
				new Vector2D[]{
						new Vector2D(49, 24),
						new Vector2D(29, 24),
				},
				new Vector2D[]{
						Vector2D.RIGHT,
						Vector2D.LEFT,
				},
				new Wall[]{
				}
		),
		new Level(	//Level 2
				new Vector2D[]{
						new Vector2D(59, 6),
						new Vector2D(19, 42),
				},
				new Vector2D[]{
						Vector2D.LEFT,
						Vector2D.RIGHT,
				},
				new Wall[]{
						new Wall(new Vector2D(19, 24), 41, Vector2D.RIGHT),
				}
		),
		new Level(	//Level 3
				new Vector2D[]{
						new Vector2D(49, 24),
						new Vector2D(29, 24),
				},
				new Vector2D[]{
						Vector2D.UP,
						Vector2D.DOWN,
				},
				new Wall[]{
						new Wall(new Vector2D(19, 9), 31, Vector2D.DOWN),
						new Wall(new Vector2D(59, 9), 31, Vector2D.DOWN),
				}
		),
		new Level(	//Level 4
				new Vector2D[]{
						new Vector2D(59, 6),
						new Vector2D(19, 42),
				},
				new Vector2D[]{
						Vector2D.LEFT,
						Vector2D.RIGHT,
				},
				new Wall[]{
						new Wall(new Vector2D(19, 3), 27, Vector2D.DOWN),
						new Wall(new Vector2D(59, 48), 27, Vector2D.UP),
						new Wall(new Vector2D(1, 37), 39, Vector2D.RIGHT),
						new Wall(new Vector2D(78, 14), 39, Vector2D.LEFT),
				}
		),
		new Level(	//Level 5
				new Vector2D[]{
						new Vector2D(49, 24),
						new Vector2D(29, 24),
				},
				new Vector2D[]{
						Vector2D.UP,
						Vector2D.DOWN,
				},
				new Wall[]{
						new Wall(new Vector2D(20, 12), 27, Vector2D.DOWN),
						new Wall(new Vector2D(58, 12), 27, Vector2D.DOWN),
						new Wall(new Vector2D(22, 10), 35, Vector2D.RIGHT),
						new Wall(new Vector2D(22, 40), 35, Vector2D.RIGHT),
				}
		),
		new Level(	//Level 6
				new Vector2D[]{
						new Vector2D(64, 6),
						new Vector2D(14, 42),
				},
				new Vector2D[]{
						Vector2D.DOWN,
						Vector2D.UP,
				},
				new Wall[]{
						new Wall(new Vector2D(9, 3), 19, Vector2D.DOWN),
						new Wall(new Vector2D(19, 3), 19, Vector2D.DOWN),
						new Wall(new Vector2D(29, 3), 19, Vector2D.DOWN),
						new Wall(new Vector2D(39, 3), 19, Vector2D.DOWN),
						new Wall(new Vector2D(49, 3), 19, Vector2D.DOWN),
						new Wall(new Vector2D(59, 3), 19, Vector2D.DOWN),
						new Wall(new Vector2D(69, 3), 19, Vector2D.DOWN),
						new Wall(new Vector2D(9, 30), 19, Vector2D.DOWN),
						new Wall(new Vector2D(19, 30), 19, Vector2D.DOWN),
						new Wall(new Vector2D(29, 30), 19, Vector2D.DOWN),
						new Wall(new Vector2D(39, 30), 19, Vector2D.DOWN),
						new Wall(new Vector2D(49, 30), 19, Vector2D.DOWN),
						new Wall(new Vector2D(59, 30), 19, Vector2D.DOWN),
						new Wall(new Vector2D(69, 30), 19, Vector2D.DOWN),
				}
		),
		new Level(	//Level 7
				new Vector2D[]{
						new Vector2D(64, 6),
						new Vector2D(14, 42),
				},
				new Vector2D[]{
						Vector2D.DOWN,
						Vector2D.UP,
				},
				new Wall[]{
						new Wall(new Vector2D(39, 3), 23, Vector2D.DOWN_2),
				}
		),
		new Level(	//Level 8
				new Vector2D[]{
						new Vector2D(64, 6),
						new Vector2D(14, 42),
				},
				new Vector2D[]{
						Vector2D.DOWN,
						Vector2D.UP,
				},
				new Wall[]{
						new Wall(new Vector2D(9, 3), 37, Vector2D.DOWN),
						new Wall(new Vector2D(29, 3), 37, Vector2D.DOWN),
						new Wall(new Vector2D(49, 3), 37, Vector2D.DOWN),
						new Wall(new Vector2D(69, 3), 37, Vector2D.DOWN),
						new Wall(new Vector2D(19, 48), 37, Vector2D.UP),
						new Wall(new Vector2D(39, 48), 37, Vector2D.UP),
						new Wall(new Vector2D(59, 48), 37, Vector2D.UP),
				}
		),
		new Level(	//Level 9
				new Vector2D[]{
						new Vector2D(74, 39),
						new Vector2D(4, 14),
				},
				new Vector2D[]{
						Vector2D.UP,
						Vector2D.DOWN,
				},
				new Wall[]{
						new Wall(new Vector2D(5, 5), 42, Vector2D.DOWN_RIGHT),
						new Wall(new Vector2D(33, 5), 42, Vector2D.DOWN_RIGHT),
				}
		),
		new Level(	//Level 10+
				new Vector2D[]{
						new Vector2D(64, 6),
						new Vector2D(14, 42),
				},
				new Vector2D[]{
						Vector2D.DOWN,
						Vector2D.UP,
				},
				new Wall[]{
						new Wall(new Vector2D(9, 3), 23, Vector2D.DOWN_2),
						new Wall(new Vector2D(19, 4), 23, Vector2D.DOWN_2),
						new Wall(new Vector2D(29, 3), 23, Vector2D.DOWN_2),
						new Wall(new Vector2D(39, 4), 23, Vector2D.DOWN_2),
						new Wall(new Vector2D(49, 3), 23, Vector2D.DOWN_2),
						new Wall(new Vector2D(59, 4), 23, Vector2D.DOWN_2),
						new Wall(new Vector2D(69, 3), 23, Vector2D.DOWN_2),
				}
		),
	};
	
	public static final int N_LEVELS = LEVELS.length;

	public Level(Vector2D[] snakeInitialHeadPositions, Vector2D[] snakeInitialDirections, Wall[] walls){
		this.snakeInitialHeadPositions = snakeInitialHeadPositions;
		this.snakeInitialDirections = snakeInitialDirections;
		this.walls = walls;
	}
	
	public void draw(Arena arena, byte wallColor){
		for(int i = 0; i < walls.length; i++){
			walls[i].draw(arena, wallColor);
		}
	}
	
	public Vector2D getSnakeInitialHeadPosition(int i){
		return snakeInitialHeadPositions[i];
	}
	
	public Vector2D getSnakeInitialDirection(int i){
		return snakeInitialDirections[i];
	}

	public void output(NibblesScreen screen, byte color) {
		for(int i = 0; i < walls.length; i++){
			walls[i].output(screen, color);
		}
	}
}
