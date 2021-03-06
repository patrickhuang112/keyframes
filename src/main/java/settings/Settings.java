package settings;

import java.awt.Color;
import java.io.Serializable;

import keyframes.Utils;

public class Settings implements Serializable {
	
	private static final long serialVersionUID = 8311131669814537994L;
	public static String settingsPath = "Settings" + Utils.fileExtension;
	
	
	public Settings() {
		super();
	}
	
	public final int brushMin = 0;
	public final int brushMax = 30;
	public final int eraserMin = 0;
	public final int eraserMax = 30;
	public final int fpsMin = 0;
	public final int fpsMax = 30;
	public final int lengthMin = 0;
	public final int lengthMax = 20;
	
	private Color brushColor = Color.red;
	private Color eraserColor = Color.white;
	private int brushSize = 5;
	private int eraserSize = 5;
	private int fps = 5;
	private int compLength = 5;
	
	
	public void setCompLength(int length) {
		compLength = length;
	}
	
	public int getCompLength() {
		return compLength;
	}
	
	public void setFps(int fps) {
		this.fps = fps;
	}
	
	public int getFps() {
		return fps;
	}
	
	public Color getBrushColor() {
		return this.brushColor;
	}
	
	public void setBrushColor(Color color) {
		this.brushColor = color;
	}
	
	public Color getEraserColor() {
		return this.eraserColor;
	}
	
	public void setEraserColor(Color color) {
		this.eraserColor = color;
	}
	
	//DRAWABLE BRUSH/ERASER SETTINGS
	public int getBrushSize() {
		return brushSize;
	}
	
	public void setBrushSize(int brushSize) {
		this.brushSize = brushSize;
	}
	
	public int getEraserSize() {
		return eraserSize;
	}
	
	public void setEraserSize(int eraserSize) {
		this.eraserSize = eraserSize;
	}
}
