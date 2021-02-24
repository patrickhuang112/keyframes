package datatypes;

import java.io.Serializable;

public class Enums implements Serializable{

	private static final long serialVersionUID = 3760753600226324779L;

	public enum MouseButton {
		NONE,
		LEFTMOUSE,
		RIGHTMOUSE
	}
	
	public enum PaintSetting {
		NONE,
		DRAW,
		ERASE,
		//Floodfill with respect to a single layer
		FILL
	}
}
