package nibbles.game;

public class Arena {
	public static final int WIDTH = 80;
	public static final int HEIGHT = 50;
	public static final Wall[] borderWalls = {
			new Wall(new Vector2D(0, 2), WIDTH, Vector2D.RIGHT),
			new Wall(new Vector2D(WIDTH - 1, 2), HEIGHT - 2, Vector2D.DOWN),
			new Wall(new Vector2D(WIDTH - 1, HEIGHT - 1), WIDTH, Vector2D.LEFT),
			new Wall(new Vector2D(0, HEIGHT - 1), HEIGHT - 2, Vector2D.UP),
	};
	
	private byte[][] arena;
	private long[][] timeDrawn;
	private final Colors colorTable;
	private final LogicTimer logicTimer;
	private Level level;
	
	public Arena(Colors colorTable, LogicTimer logicTimer){
		arena = new byte[WIDTH][HEIGHT];
		timeDrawn = new long[WIDTH][HEIGHT];
		this.colorTable = colorTable;
		this.logicTimer = logicTimer;
	}
	
	public void setLevel(Level level){
		this.level = level;
		
		//reset whole arena to background color
		for(int y = 0; y < HEIGHT; y++){
			for(int x = 0; x < WIDTH; x++){
				setContent(x, y, colorTable.getBackground());
			}
		}
		
		//draw border walls
		for(int i = 0; i < borderWalls.length; i++){
			borderWalls[i].draw(this, colorTable.getWalls());
		}
		
		//draw level
		level.draw(this, colorTable.getWalls());
	}
	
	private void setContent(int x, int y, byte color){
		arena[x][y] = color;
		timeDrawn[x][y] = logicTimer.getLogicTime();
	}
	
	public void setContent(Vector2D point, byte color){
		setContent(point.getX(), point.getY(), color);
	}
	
	public boolean placeFood(Vector2D point){
		Vector2D sister = getSisterPoint(point);
		return (getContent(point) == colorTable.getBackground() && getContent(sister) == colorTable.getBackground());
	}
	
	public int getContent(Vector2D point){
		return arena[point.getX()][point.getY()];
	}
	
	public int getColor(Vector2D point){
		int x = point.getX();
		int y = point.getY();

		byte qbColor = arena[x][y];

		//get original nibbles wall-darkening effect:
		if(qbColor > 7){
			int sisterY = getSisterY(y);
			if(arena[x][sisterY] > 7 && arena[x][sisterY] != qbColor && timeDrawn[x][sisterY] > timeDrawn[x][y]){
				qbColor -= 8;
			}
		}

		return qbColor;
	}
	
	public static Vector2D getSisterPoint(Vector2D point){
		return new Vector2D(point.getX(), getSisterY(point.getY()));
	}
	
	private static int getSisterY(int y){
		return y - (y % 2) * 2 + 1;
	}
	
	public void output(NibblesScreen screen) {
		screen.update(new Vector2D(0, 0), new Vector2D(WIDTH-1, HEIGHT-1), colorTable.getBackground());
		
		//draw border walls
		for(int i = 0; i < borderWalls.length; i++){
			borderWalls[i].output(screen, colorTable.getWalls());
		}
		
		//draw level
		level.output(screen, colorTable.getWalls());
	}
}
