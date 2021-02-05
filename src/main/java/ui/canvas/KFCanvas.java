package ui.canvas;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;

import javax.swing.JPanel;

import datatypes.DrawPoint;
import ui.UIComponent;

//KF stands for key frames, because there is already a java.awt canvas type
public interface KFCanvas extends UIComponent {
	
	public static final int defaultMinWidth = 0;
	public static final int defaultPreferredWidth = 1800;
	public static final int defaultMinHeight = 200;
	public static final int defaultPreferredHeight = 500;
	
	@Override
	public JPanel getSwingComponent();
	
	public int getCanvasWidth();
	public int getCanvasHeight();
	
	public Color getBackgroundColor();
	public void setBackgroundColor(Color color);
	
	public void refresh();
	
	
	public static void drawAndErasePoint(Graphics2D g2d, DrawPoint p) {
		Point point = p.point;
		float xPos = (float)point.getX();
		float yPos = (float)point.getY();
		float radius = p.size;
		g2d.setPaint(p.color);
		//Center the ellipse, ellipse created from top left coord
		Ellipse2D.Float newPoint = new Ellipse2D.Float(xPos-(radius/2), yPos-(radius/2), radius, radius);
		g2d.fill(newPoint);
	}
	
	
	public static void drawAndErasePath(Graphics2D g2d, ArrayList<DrawPoint> points) {
		GeneralPath gp =  new GeneralPath();
		BasicStroke s = null;
		
		boolean firstPoint = true;
		for(DrawPoint dp : points) {
			if(firstPoint) {
				gp.moveTo(dp.point.getX(), dp.point.getY());
				firstPoint = false;
			} else {
				g2d.setPaint(dp.color);
				s = new BasicStroke(dp.size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
				g2d.setStroke(s);
				gp.lineTo(dp.point.getX(), dp.point.getY());
			}
		}
		g2d.draw(gp);
	}
	
}
