package nibbles.game;

public class Wall {
	private Vector2D startPosition;
	private int length;
	private Vector2D direction;
	private Vector2D endPosition;
	
	public Wall(Vector2D startPosition, int length, Vector2D direction){
		this.startPosition = startPosition;
		this.length = length;
		this.direction = direction;
		endPosition = startPosition.add(direction.scalarProd(length-1));
	}
	
	public void draw(Arena arena, byte wallColor){
		Vector2D currentPosition = startPosition;
		for(int i = 0; i < length; i++){
			arena.setContent(currentPosition, wallColor);
			currentPosition = currentPosition.add(direction);
		}
	}

	public void output(NibblesScreen screen, byte color) {
		if (direction.length() == 1) {
			screen.update(startPosition, endPosition, color);
		} else {
			Vector2D currentPosition = startPosition;
			for(int i = 0; i < length; i++){
				screen.update(currentPosition, color);
				currentPosition = currentPosition.add(direction);
			}
		}
	}
}
