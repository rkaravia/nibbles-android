package nibbles.game;

public class Vector2D {
	private final int x;
	private final int y;
	
	public static final Vector2D LEFT = new Vector2D(-1, 0);
	public static final Vector2D RIGHT = new Vector2D(1, 0);
	public static final Vector2D UP = new Vector2D(0, -1);
	public static final Vector2D DOWN = new Vector2D(0, 1);
	
	public static final Vector2D UP_LEFT = new Vector2D(-1, -1);
	public static final Vector2D UP_RIGHT = new Vector2D(1, -1);
	public static final Vector2D DOWN_LEFT = new Vector2D(-1, 1);
	public static final Vector2D DOWN_RIGHT = new Vector2D(1, 1);
	
	public static final Vector2D DOWN_2 = new Vector2D(0, 2);
	
	public Vector2D(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(Vector2D vector2D){
		x = vector2D.getX();
		y = vector2D.getY();
	}
	
	public Vector2D add(Vector2D vector2D){
		return new Vector2D(x + vector2D.x, y + vector2D.y);
	}
	
	public Vector2D subtract(Vector2D vector2D){
		return new Vector2D(x - vector2D.x, y - vector2D.y);
	}
	
	public Vector2D scalarProd(int f) {
		return new Vector2D(x * f, y * f);
	}
	
	public Vector2D getAbsValues(){
		return new Vector2D(Math.abs(x), Math.abs(y));
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean equals(Vector2D vector2D){
		if(vector2D == null){
			return false;
		}
		else{
			return (x == vector2D.getX() && y == vector2D.y);
		}
	}
	
	public Vector2D rotateLeft() {
		return new Vector2D(y, -x);
	}

	public Vector2D rotateRight() {
		return new Vector2D(-y, x);
	}

	public String toString() {
		return "[" + x + "," + y + "]";
	}
	
	public double length() {
		return Math.sqrt(x*x + y*y);
	}
}