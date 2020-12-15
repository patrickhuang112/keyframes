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
import java.awt.image.BufferedImage;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import datatypes.DrawPoint;
import datatypes.Layer;
import datatypes.SessionObject;
import datatypes.UIComponent;
import factories.EnumFactory;

public class DrawablePanel extends JPanel implements SessionObject, MouseMotionListener, MouseListener{
	
	private static final long serialVersionUID = -4890309349957259630L;

	private UIComponent parent;
	boolean cursorInScreen = true;
	//VERY BROKEN
	//Transparency (maybe use some other time?)
	//https://www.rgagnon.com/javadetails/java-0265.html
	
	ArrayList<DrawPoint> currentDraggedPoints = null;
	
	DrawablePanel(UIComponent parent) {
		super();
		this.parent = parent;
		addMouseListener(this);
		addMouseMotionListener(this);
		getSession().setDrawPanel(this);
	}
	
	public void clearAll() {
		currentDraggedPoints = null;
		getSession().setCurrentLayerFrameAtCurrentTime(new ArrayList<>());
		repaint();
	}
	private void drawAndErasePoint(Graphics2D g2d, DrawPoint p) {
		Point point = p.point;
		float xPos = (float)point.getX();
		float yPos = (float)point.getY();
		float radius = p.size;
		g2d.setPaint(p.color);
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
				g2d.setPaint(dp.color);
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
		
		// Because of pass by reference, this when we update pointCollection down below in the mouse listeners
		// it will actually directly update the pointCollection that is in the session object,
		// which means now calling this is just getting the most up to date point collection, I'm not actually
		// overwriting (which it may seem like I'm doing at first)
		// pointCollection = parent.getSession().getCurrentLayerFrameAtCurrentTime();
		BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), 
				BufferedImage.TYPE_INT_RGB);
		Graphics2D imgG = img.createGraphics();
		imgG.setPaint(parent.getSession().getDrawablePanelBackgroundColor());
		imgG.fillRect(0,0, this.getWidth(), this.getHeight());
		
		// WHy do i create two graphics here?
		// I create two graphics because imgG is for when rendering, i needed the Buffered image,
		// so this is why I draw to two different graphics
		// g2d is for the screen, imgG is for rendering later.
		Graphics2D[] allGraphics = new Graphics2D[] {g2d, imgG};
 		for(Graphics2D gr : allGraphics) {
 			ArrayList<Layer> layers = getSession().getLayers();
 			// Start from the bottom layer, thats the first one we want to draw, and draw higher layers
 			// on top of bottom layers
 			for(int i = layers.size() - 1; i >= 0; i--) {
 				Layer layer = layers.get(i);
 				int time = getSession().getCurrentTimepoint();
 				ArrayList<ArrayList<DrawPoint>> pointsList = layer.getPointCollectionAtTime(time);
 				if (pointsList != null) {
 					for(ArrayList<DrawPoint> points : pointsList) {
 	 	 				if(points.size() == 1) {
 	 	 					drawAndErasePoint(gr, points.get(0));
 	 	 				} else {
 	 	 					drawAndErasePath(gr, points);
 	 	 				}
 	 	 			}
 				}			
 	 			
 	 			// The currently dragged stuff
 	 			if(getSession().getCurrentLayerNum() == i && currentDraggedPoints != null) {
 	 				drawAndErasePath(gr, currentDraggedPoints);
 	 			}
 			}
 			
 		}
		updateSession(img);
	}

	private void updateSession(BufferedImage img) {
		getSession().setCurrentImage(img);
		getSession().refreshUI();
	}
	

	@Override
	public void mouseDragged(MouseEvent e) { 
		if(cursorInScreen) {
			EnumFactory.PaintSetting setting = EnumFactory.PaintSetting.NONE;
			int drawSize = -1;
			Color pointColor;
			if(currentDraggedPoints == null) {
				setting = parent.getSession().getPaintSetting();
				currentDraggedPoints = new ArrayList<>();
			} else {
				setting = currentDraggedPoints.get(0).setting;
			}
			
			drawSize = setting == EnumFactory.PaintSetting.DRAW 
								? parent.getSession().getBrushSize() 
								: parent.getSession().getEraserSize();
								
			pointColor = setting == EnumFactory.PaintSetting.DRAW 
					? parent.getSession().getBrushColor()
					: parent.getSession().getEraserColor();	
			
			currentDraggedPoints.add(new DrawPoint(e.getPoint(), drawSize, setting, pointColor));
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
			Color pointColor;
			
			ArrayList<DrawPoint> singlePointCollection = new ArrayList<>();
			setting = parent.getSession().getPaintSetting();
			drawSize = setting == EnumFactory.PaintSetting.DRAW 
					? parent.getSession().getBrushSize()
					: parent.getSession().getEraserSize();
			pointColor = setting == EnumFactory.PaintSetting.DRAW 
					? parent.getSession().getBrushColor()
					: parent.getSession().getEraserColor();	
			
			singlePointCollection.add(new DrawPoint(point, drawSize, setting, pointColor));
			getSession().addToCurrentLayerFrameAtCurrentTime(singlePointCollection);
			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//Nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(currentDraggedPoints != null) {
			getSession().addToCurrentLayerFrameAtCurrentTime(currentDraggedPoints);
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
			getSession().addToCurrentLayerFrameAtCurrentTime(currentDraggedPoints);
			currentDraggedPoints = null;
		}
		repaint();
	}

	@Override
	public Session getSession() {
		return parent.getSession();
	}
}
