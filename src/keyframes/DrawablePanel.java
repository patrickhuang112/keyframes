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
	
	private UIComponent parent;
	
	private Color paintColor = Color.red;
	boolean cursorInScreen = true;
	
	//VERY BROKEN
	//https://www.rgagnon.com/javadetails/java-0265.html
	//LOOK AT THAT WHEN I WANT TO IMPLEMENT LAYERS
	private Color eraseColor = Color.white;
	private float eraseSize = 1.0f;
	Point drawPoint;
	Graphics2D old;
	ArrayList<DrawPoint> currentDraggedPoints = null;
	ArrayList<ArrayList<DrawPoint>> pointCollection = new ArrayList<>();
	
	
	DrawablePanel(UIComponent parent) {
		super();
		this.parent = parent;
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void clearAll() {
		currentDraggedPoints = null;
		pointCollection = new ArrayList<>();
		repaint();
	}
	private void drawAndErasePoint(Graphics2D g2d, DrawPoint p) {
		Point point = p.point;
		EnumFactory.PaintSetting setting = p.setting;
		float xPos = (float)point.getX();
		float yPos = (float)point.getY();
		float radius = p.size;
		if(setting == EnumFactory.PaintSetting.DRAW) {
			g2d.setPaint(paintColor);
		} else if (setting == EnumFactory.PaintSetting.ERASE) {
			g2d.setPaint(eraseColor);
		}
		//Center the ellipse, ellipse created from top left coord
		Ellipse2D.Float newPoint = new Ellipse2D.Float(xPos-(radius/2), yPos-(radius/2), radius, radius);
		g2d.fill(newPoint);
	}
	
	private void drawAndErasePath(Graphics2D g2d, ArrayList<DrawPoint> points) {
		GeneralPath gp =  new GeneralPath();
		BasicStroke s = null;
		
		boolean firstPoint = true;
		for(DrawPoint dp : points) {
			if(firstPoint) {
				gp.moveTo(dp.point.getX(), dp.point.getY());
				firstPoint = false;
			} else {
				if(dp.setting == EnumFactory.PaintSetting.DRAW) {
					g2d.setPaint(paintColor);
					
				} else if (dp.setting == EnumFactory.PaintSetting.ERASE) {
					g2d.setPaint(eraseColor);
				}
				s = new BasicStroke(dp.size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
				g2d.setStroke(s);
				gp.lineTo(dp.point.getX(), dp.point.getY());
			}
		}
		g2d.draw(gp);
	}
	
	@Override
	public void paintComponent(Graphics g){
	super.paintComponent(g);
	Graphics2D g2d = (Graphics2D) g;
	
	
	for(ArrayList<DrawPoint> points : pointCollection) {
		if(points.size() == 1) {
			drawAndErasePoint(g2d, points.get(0));
		} else {
			drawAndErasePath(g2d, points);
		}
	}
	
	// The currently dragged stuff
	if(currentDraggedPoints != null) {
		drawAndErasePath(g2d, currentDraggedPoints);
	}
	
	}


	@Override
	public void mouseDragged(MouseEvent e) { 
		if(cursorInScreen) {
			EnumFactory.PaintSetting setting = EnumFactory.PaintSetting.NONE;
			int drawSize = -1;
			if(currentDraggedPoints == null) {
				setting = parent.getSession().getPaintSetting();
				currentDraggedPoints = new ArrayList<>();
			} else {
				setting = currentDraggedPoints.get(0).setting;
			}
			drawSize = setting == EnumFactory.PaintSetting.DRAW 
								? parent.getSession().getBrushSize() 
								: parent.getSession().getEraserSize();
			currentDraggedPoints.add(new DrawPoint(e.getPoint(), drawSize, setting));
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(cursorInScreen) {
			EnumFactory.PaintSetting setting;
			int drawSize;
			Point point = e.getPoint();
			
			ArrayList<DrawPoint> singlePointCollection = new ArrayList<>();
			setting = parent.getSession().getPaintSetting();
			drawSize = setting == EnumFactory.PaintSetting.DRAW 
					? parent.getSession().getBrushSize() 
					: parent.getSession().getEraserSize();
			singlePointCollection.add(new DrawPoint(point, drawSize, setting));
			pointCollection.add(singlePointCollection);
			repaint();
		}
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
		 cursorInScreen = true;
	 }

	@Override
	public void mouseExited(MouseEvent e) {
		cursorInScreen = false;
		if(currentDraggedPoints != null) {
			pointCollection.add(currentDraggedPoints);
			currentDraggedPoints = null;
		}
		repaint();
	}
}
