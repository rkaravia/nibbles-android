package nibbles.game;

public class Colors {
	public static final Colors MONOCOLORS = new Colors(
			new byte[]{15, 7},
			(byte)7,
			(byte)0,
			(byte)15,
			(byte)0,
			(byte)15
	);
	
	public static final Colors NORMALCOLORS = new Colors(
			new byte[]{14, 13},
			(byte)12,
			(byte)1,
			(byte)15,
			(byte)4,
			(byte)14
	);
	
	private byte[] snakes;
	private byte walls;
	private byte background;
	private byte dialogsForeground;
	private byte dialogsBackground;
	private byte food;
	
	public Colors(byte[] snakes, byte walls, byte background, byte dialogsForeground, byte dialogsBackground, byte food){
		this.snakes = snakes;
		this.walls = walls;
		this.background = background;
		this.dialogsForeground = dialogsForeground;
		this.dialogsBackground = dialogsBackground;
		this.food = food;
	}
	
	public byte getSnake(int i){
		return snakes[i];
	}
	
	public byte getWalls(){
		return walls;
	}
	
	public byte getBackground(){
		return background;
	}
	
	public byte getDialogsForeground(){
		return dialogsForeground;
	}
	
	public byte getDialogsBackground(){
		return dialogsBackground;
	}
	
	public byte getFood(){
		return food;
	}
}
