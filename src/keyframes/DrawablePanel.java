package keyframes;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DrawablePanel extends JPanel implements MouseMotionListener, MouseListener{
	
	private Color paintColor = Color.red;
	
	//VERY BROKEN
	//https://www.rgagnon.com/javadetails/java-0265.html
	//LOOK AT THAT WHEN I WANT TO IMPLEMENT LAYERS
	private Color eraseColor = Color.white;
	private float brushSize = 5.0f;
	private float eraseSize = 1.0f;
	Point drawPoint;
	Graphics2D old;
	SimpleEntry<EnumFactory.PaintSetting, ArrayList<Point>> currentDraggedPoints = null;
	ArrayList<SimpleEntry<EnumFactory.PaintSetting, ArrayList<Point>>> pointCollection = new ArrayList<>();
	
	
	DrawablePanel() {
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void clearAll() {
		currentDraggedPoints = null;
		pointCollection = new ArrayList<>();
	}
	private void drawAndErasePoint(Graphics2D g2d, Point p, EnumFactory.PaintSetting setting) {
		float xPos = (float)p.getX();
		float yPos = (float)p.getY();
		float radius = brushSize;
		if(setting == EnumFactory.PaintSetting.DRAW) {
			g2d.setPaint(paintColor);
		} else if (setting == EnumFactory.PaintSetting.ERASE) {
			g2d.setPaint(eraseColor);
		}
		Ellipse2D.Float point = new Ellipse2D.Float(xPos, yPos, radius, radius);
		g2d.fill(point);
	}
	
	private void drawAndErasePath(Graphics2D g2d, ArrayList<Point> points, EnumFactory.PaintSetting setting) {
		GeneralPath gp =  new GeneralPath();
		BasicStroke s = null;
		if(setting == EnumFactory.PaintSetting.DRAW) {
			g2d.setPaint(paintColor);
			s = new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
			
		} else if (setting == EnumFactory.PaintSetting.ERASE) {
			g2d.setPaint(eraseColor);
			s = new BasicStroke(eraseSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		}
		g2d.setStroke(s);
		boolean firstPoint = true;
		for(Point p : points) {
			if(firstPoint) {
				gp.moveTo(p.getX(), p.getY());
				firstPoint = false;
			} else {
				gp.lineTo(p.getX(), p.getY());
			}
		}
		
		g2d.draw(gp);

	}
	
	@Override
	public void paintComponent(Graphics g){
	super.paintComponent(g);
	Graphics2D g2d = (Graphics2D) g;
	
	
	for(SimpleEntry<EnumFactory.PaintSetting, ArrayList<Point>> points : pointCollection) {
		if(points.getValue().size() == 1) {
			drawAndErasePoint(g2d, points.getValue().get(0), points.getKey());
		} else {
			drawAndErasePath(g2d, points.getValue(), points.getKey());
		}
	}
	
	// The currently dragged stuff
	if(currentDraggedPoints != null) {
		drawAndErasePath(g2d, currentDraggedPoints.getValue(), currentDraggedPoints.getKey());
	}
	
	}


	@Override
	public void mouseDragged(MouseEvent e) { 
		if(currentDraggedPoints == null) {
		EnumFactory.PaintSetting mouseNum = EnumFactory.PaintSetting.NONE;
			if(SwingUtilities.isLeftMouseButton(e)) {
				mouseNum = EnumFactory.PaintSetting.DRAW;
			} else if(SwingUtilities.isRightMouseButton(e)) {
				mouseNum =EnumFactory.PaintSetting.ERASE;
			}
			currentDraggedPoints = new SimpleEntry<>(mouseNum, new ArrayList<>());
		}
		currentDraggedPoints.getValue().add(e.getPoint());
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		EnumFactory.PaintSetting mouseNum = EnumFactory.PaintSetting.NONE;
		ArrayList<Point> singlePointCollection = new ArrayList<>();
		singlePointCollection.add(e.getPoint());
		if(SwingUtilities.isLeftMouseButton(e)) {
			mouseNum = EnumFactory.PaintSetting.DRAW;
		} else if(SwingUtilities.isRightMouseButton(e)) {
			mouseNum = EnumFactory.PaintSetting.ERASE;
		}
		pointCollection.add(new SimpleEntry<>(mouseNum, singlePointCollection));
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(currentDraggedPoints != null) {
			pointCollection.add(currentDraggedPoints);
			currentDraggedPoints = null;
		}
		repaint();
	}

	 @Override
	 public void mouseEntered(MouseEvent e) {

	 }

	 @Override
	 public void mouseExited(MouseEvent e) {

	}
}
