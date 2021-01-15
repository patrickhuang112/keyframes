package ui.canvas;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import datatypes.DrawFrame;
import datatypes.DrawPoint;
import datatypes.Layer;
import datatypes.SessionObject;
import factories.EnumFactory;
import keyframes.Controller;
import keyframes.MagicValues;
import keyframes.Session;
import ui.UIComponent;

public class StandardKFCanvas extends JPanel implements KFCanvas{
	
	private static final long serialVersionUID = -4890309349957259630L;

	boolean cursorInScreen = true;
	//VERY BROKEN
	//Transparency (maybe use some other time?)
	//https://www.rgagnon.com/javadetails/java-0265.html
	
	ArrayList<DrawPoint> currentDraggedPoints = null;
	
	StandardKFCanvas () {
		super();
		int defaultMinW = MagicValues.drawablePanelDefaultMinWidth;
		int defaultMinH = MagicValues.drawablePanelDefaultMinHeight;
		int defaultPrefW = MagicValues.drawablePanelDefaultPreferredWidth;
		int defaultPrefH = MagicValues.drawablePanelDefaultPreferredHeight;
		
		
		// Set default heights
		setMinimumSize(new Dimension(defaultMinW, defaultMinH));
		setPreferredSize(new Dimension(defaultPrefW, defaultPrefH));
		
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) { 
				if(cursorInScreen) {
					EnumFactory.PaintSetting setting = EnumFactory.PaintSetting.NONE;
					int drawSize = -1;
					Color pointColor;
					if(currentDraggedPoints == null) {
						setting = Controller.getController().getPaintSetting();
						currentDraggedPoints = new ArrayList<>();
					} else {
						setting = currentDraggedPoints.get(0).setting;
					}
					
					drawSize = setting == EnumFactory.PaintSetting.DRAW 
										? Controller.getController().getBrushSize() 
										: Controller.getController().getEraserSize();
										
					pointColor = setting == EnumFactory.PaintSetting.DRAW 
							? Controller.getController().getBrushColor()
							: Controller.getController().getEraserColor();	
					
					currentDraggedPoints.add(new DrawPoint(e.getPoint(), drawSize, setting, pointColor));
					repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				//Do nothing rn
			}
		});
		
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(cursorInScreen) {
					EnumFactory.PaintSetting setting;
					int drawSize;
					Point point = e.getPoint();
					Color pointColor;
					setting = Controller.getController().getPaintSetting();
					
					if (setting == EnumFactory.PaintSetting.FILLSINGLE) {
						floodFillCurrentLayer(point);
					} else if (setting == EnumFactory.PaintSetting.DRAW || setting == EnumFactory.PaintSetting.ERASE) {
						ArrayList<DrawPoint> singlePointCollection = new ArrayList<>();
						drawSize = setting == EnumFactory.PaintSetting.DRAW 
								? Controller.getController().getBrushSize()
								: Controller.getController().getEraserSize();
						pointColor = setting == EnumFactory.PaintSetting.DRAW 
								? Controller.getController().getBrushColor()
								: Controller.getController().getEraserColor();	
						singlePointCollection.add(new DrawPoint(point, drawSize, setting, pointColor));
						Controller.getController().addToCurrentLayerFrameAtCurrentTime(singlePointCollection);
					}
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
					Controller.getController().addToCurrentLayerFrameAtCurrentTime(currentDraggedPoints);
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
					Controller.getController().addToCurrentLayerFrameAtCurrentTime(currentDraggedPoints);
					currentDraggedPoints = null;
				}
				repaint();
			}
			
		});
		
		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				Controller.getController().updateDrawFrameDimensions(getCanvasWidth(), getCanvasHeight());
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			
		});
		
		// Using an invoke later because for some reason just doing it first here won't cause the panel to repaint
		// initially. Same with getting the dimensions
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Color backgroundColor = Session.defaultDrawPanelBackgroundColor;
				Controller.getController().setDrawablePanelBackgroundColor(backgroundColor);
				Controller.getController().setEraserColor(backgroundColor);
				// Once we now have access to the draw panel, we should also update all the draw frames in
				// the layers with the new dimensions
				Controller.getController().updateDrawFrameDimensions(defaultPrefW, defaultPrefH);
			}
			
		});
		
	}
	
	public int getImageWidth() {
		return this.getWidth();
	}
	
	public int getImageHeight() {
		return this.getHeight();
	}
	
	public void clearAll() {
		currentDraggedPoints = null;
		Controller.getController().setCurrentLayerFrameAtCurrentTime(new DrawFrame(getImageWidth(), getImageHeight()));
		refresh();
	}
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
	
	private void floodFillCurrentLayer(Point p) {
		floodFillSpecifiedLayer(p, Controller.getController().getCurrentLayerNum());
	}
	
	private void floodFillSpecifiedLayer(Point p, int layerNum) {
		int[] dxs = new int[] {-1,0,1,0};
		int[] dys = new int[] {0,-1,0,1};
  		Color newColor = Controller.getController().getBrushColor();
		DrawFrame df = Controller.getController().getSpecifiedLayerFrameAtCurrentTime(layerNum);
		Color oldColor = df.getColorAtPoint(p.x, p.y);
		if (newColor.equals(oldColor)) {
			return;
		}
		Queue<Point> q = new LinkedList<Point>();
		int width = getImageWidth();
		int height = getImageHeight();
		boolean[][] visited = new boolean[height][width];
		for(int r = 0; r < height; r++) {
			for(int c = 0; c < width; c++) {
				visited[r][c] = false;
			}
		}
		q.add(p);
		while (!q.isEmpty() ) {
			Point np = q.remove();
			visited[np.y][np.x] = true;
			df.setColorAtPixelArrayPoint(newColor, np.x, np.y);
			
			
			for (int i = 0; i < dxs.length; i++) {
				int nx = np.x + dxs[i];
				int ny = np.y + dys[i];
				Point newp = new Point(nx, ny);
				if(nx >= 0 && nx < width && ny >= 0 && ny < height && !visited[ny][nx] 
						   && df.getColorAtPoint(nx, ny).equals(oldColor)) {
					visited[ny][nx] = true;
					q.add(newp);
				}
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		// Because of pass by reference, this when we update pointCollection down below in the mouse listeners
		// it will actually directly update the pointCollection that is in the session object,
		// which means now calling this is just getting the most up to date point collection, I'm not actually
		// overwriting (which it may seem like I'm doing at first)
		// pointCollection = parent.Controller.getController().getCurrentLayerFrameAtCurrentTime();
		//BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		//Graphics2D imgG = img.createGraphics();
		//g2d.setPaint(parent.Controller.getController().getDrawablePanelBackgroundColor());
		//g2d.fillRect(0,0, this.getWidth(), this.getHeight());
		

		ArrayList<Layer> layers = Controller.getController().getLayers();
		// Start from the bottom layer, thats the first one we want to draw, and draw higher layers
		// on top of bottom layers
		for(int i = layers.size() - 1; i >= 0; i--) {
			Layer layer = layers.get(i);
			int time = Controller.getController().getCurrentTimepoint();
			DrawFrame layerImg = layer.getPointCollectionAtTime(time);
			if (layerImg != null) {
				g2d.drawImage(layerImg,0,0,this);
			}			
 			
 			// The currently dragged stuff
 			if(Controller.getController().getCurrentLayerNum() == i && currentDraggedPoints != null) {
 				StandardKFCanvas.drawAndErasePath(g2d, currentDraggedPoints);
 			}
		}
 			
	}

	@Override
	public JPanel getSwingComponent() {
		return this;
	}

	@Override
	public int getCanvasWidth() {
		return this.getWidth();
	}

	@Override
	public int getCanvasHeight() {
		return this.getHeight();
	}

	@Override
	public Color getBackgroundColor() {
		return getBackground();
	}

	@Override
	public void setBackgroundColor(Color color) {
		setBackground(color);
	}

	@Override
	public void refresh() {
		repaint();
		revalidate();
	}

}
