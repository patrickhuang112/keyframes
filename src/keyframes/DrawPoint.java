package keyframes;

import java.awt.Color;
import java.awt.Point;

public class DrawPoint {

	public Color color;
	public Point point;
	public int size;
	public EnumFactory.PaintSetting setting;
	
	public DrawPoint(Point point, int size, EnumFactory.PaintSetting setting, Color color) {
		this.point = point;
		this.size = size;
		this.setting = setting;
		this.color = color;
	}
	
	public DrawPoint createCopy() {
		Point newPoint = new Point(this.point.x, this.point.y);
		EnumFactory.PaintSetting newSetting;
		switch(this.setting) {
			case DRAW: newSetting = EnumFactory.PaintSetting.DRAW;
			case ERASE: newSetting = EnumFactory.PaintSetting.ERASE;
			default: newSetting = EnumFactory.PaintSetting.NONE;
		}
		int newSize = this.size;
		Color newColor = this.color;
		return new DrawPoint(newPoint, newSize, newSetting, newColor);
	}
}
