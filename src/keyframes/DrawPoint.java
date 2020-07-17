package keyframes;

import java.awt.Point;

public class DrawPoint {

	
	public Point point;
	public int size;
	public EnumFactory.PaintSetting setting;
	
	public DrawPoint(Point point, int size, EnumFactory.PaintSetting setting) {
		this.point = point;
		this.size = size;
		this.setting = setting;
	}
}
