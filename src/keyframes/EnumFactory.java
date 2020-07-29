package keyframes;

import java.io.Serializable;

public class EnumFactory implements Serializable{

	private static final long serialVersionUID = 3760753600226324779L;

	public enum MouseButton {
		NONE,
		LEFTMOUSE,
		RIGHTMOUSE
	}
	
	public enum PaintSetting {
		NONE,
		DRAW,
		ERASE
	}
}
