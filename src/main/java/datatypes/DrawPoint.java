package datatypes;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

import keyframes.EnumFactory;
import keyframes.EnumFactory.PaintSetting;

public class DrawPoint implements Serializable {

	private static final long serialVersionUID = -4439471652129107246L;
	
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
