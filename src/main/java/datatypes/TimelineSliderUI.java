package datatypes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicSliderUI;

public class TimelineSliderUI extends BasicSliderUI {
	
	public TimelineSliderUI(JSlider slider) {
		super(slider);
	}
	
	public int getXPosForValue(int value) {
		return super.xPositionForValue(value);
	}
	
	public double getThumbRectMidX() {
		return thumbRect.getCenterX();
	}
	
	public double getThumbRectMaxY() {
		return thumbRect.getMaxX();
	}
	
	@Override
	public void paintThumb(Graphics g) {
		super.paintThumb(g);
		Graphics2D g2d = (Graphics2D)g;
		Rectangle knobBounds = thumbRect;
		int knobLeftx = knobBounds.x;
		int knobY = (int)knobBounds.getMaxY();
        int w = knobBounds.width;
        
        int knobMidx = knobLeftx + w / 2;
        
		
		GeneralPath gp =  new GeneralPath();
		BasicStroke s = null;
		
		gp.moveTo(knobMidx, knobY);
		
		g2d.setPaint(Color.black);
		s = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2d.setStroke(s);
		gp.lineTo(knobMidx, knobY+50);
		
		g2d.draw(gp);
        
	}
}
