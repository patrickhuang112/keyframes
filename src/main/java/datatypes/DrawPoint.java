package datatypes;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

import datatypes.Enums.PaintSetting;

public class DrawPoint implements Serializable {

	private static final long serialVersionUID = -4439471652129107246L;
	
	public Color color;
	public Point point;
	public int size;
	public Enums.PaintSetting setting;
	
	public DrawPoint(Point point, int size, Enums.PaintSetting setting, Color color) {
		this.point = point;
		this.size = size;
		this.setting = setting;
		this.color = color;
	}
	
	public DrawPoint deepCopy() {
		Point newPoint = new Point(this.point.x, this.point.y);
		Enums.PaintSetting newSetting;
		switch(this.setting) {
			case DRAW: newSetting = Enums.PaintSetting.DRAW;
			case ERASE: newSetting = Enums.PaintSetting.ERASE;
			default: newSetting = Enums.PaintSetting.NONE;
		}
		int newSize = this.size;
		Color newColor = this.color;
		return new DrawPoint(newPoint, newSize, newSetting, newColor);
	}
}
