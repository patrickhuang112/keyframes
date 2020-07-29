package keyframes;

import java.awt.Color;
import java.io.Serializable;

public class Settings implements Serializable {
	
	private static final long serialVersionUID = 8311131669814537994L;

	public Settings() {
		super();
	}
	
	private Color brushColor = Color.red;
	private Color eraserColor = Color.white;
	private int brushSize;
	private int eraserSize;
	
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
