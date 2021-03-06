package ui.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import commands.CommandFactory;
import datatypes.DrawFrame;
import datatypes.DrawPoint;
import datatypes.Enums;
import datatypes.Layer;
import keyframes.Controller;
import keyframes.Session;

class AbstractKFCanvas extends JPanel implements KFCanvas {
	public static final Color defaultDrawPanelBackgroundColor = Color.gray;
	boolean cursorInScreen = true;
	ArrayList<DrawPoint> currentDraggedPoints = null;
	
	AbstractKFCanvas(int w, int h) {
		int defaultMinW = KFCanvas.defaultMinWidth;
		int defaultMinH = KFCanvas.defaultMinHeight;

		// Set default heights
		setMinimumSize(new Dimension(defaultMinW, defaultMinH));
		setPreferredSize(new Dimension(w, h));
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) { 
				if(cursorInScreen) {
					Enums.PaintSetting setting = Enums.PaintSetting.NONE;
					int drawSize = -1;
					Color pointColor;
					if(currentDraggedPoints == null) {
						setting = Controller.getController().getPaintSetting();
						currentDraggedPoints = new ArrayList<>();
					} else {
						setting = currentDraggedPoints.get(0).setting;
					}
					
					drawSize = setting == Enums.PaintSetting.DRAW 
										? Controller.getController().getBrushSize() 
										: Controller.getController().getEraserSize();
										
					pointColor = setting == Enums.PaintSetting.DRAW 
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
					Enums.PaintSetting setting;
					int drawSize;
					Point point = e.getPoint();
					Color pointColor;
					setting = Controller.getController().getPaintSetting();
					
					if (setting == Enums.PaintSetting.FILL) {
						floodFillCurrentLayer(point);
					} else if (setting == Enums.PaintSetting.DRAW || setting == Enums.PaintSetting.ERASE) {
						
						ArrayList<DrawPoint> singlePointCollection = new ArrayList<>();
						drawSize = setting == Enums.PaintSetting.DRAW 
								? Controller.getController().getBrushSize()
								: Controller.getController().getEraserSize();
						pointColor = setting == Enums.PaintSetting.DRAW 
								? Controller.getController().getBrushColor()
								: Controller.getController().getEraserColor();	
						singlePointCollection.add(new DrawPoint(point, drawSize, setting, pointColor));
						Controller.getController().addAndExecuteCommand(
								CommandFactory.createDrawOnCanvasCommand(singlePointCollection));
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
					Controller.getController().addAndExecuteCommand(
							CommandFactory.createDrawOnCanvasCommand(currentDraggedPoints));
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
					Controller.getController().addAndExecuteCommand(
							CommandFactory.createDrawOnCanvasCommand(currentDraggedPoints));
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
				Color backgroundColor = defaultDrawPanelBackgroundColor;
				Controller.getController().setBackgroundColor(backgroundColor);
				Controller.getController().setEraserColor(backgroundColor);
				// Once we now have access to the draw panel, we should also update all the draw frames in
				// the layers with the new dimensions
				Controller.getController().updateDrawFrameDimensions(KFCanvas.defaultPreferredWidth,
																	 KFCanvas.defaultPreferredHeight);
			}
			
		});
		
	}
	
	public void clearAll() {
		currentDraggedPoints = null;
		Controller.getController().setCurrentLayerFrameAtCurrentTime(new DrawFrame(getCanvasWidth(), getCanvasHeight()));
		refresh();
	}
	
	private void floodFillCurrentLayer(Point p) {
		//floodFillSpecifiedLayer(p, Controller.getController().getCurrentLayerNum());
		int[] dxs = new int[] {-1,0,1,0};
		int[] dys = new int[] {0,-1,0,1};
  		Color newColor = Controller.getController().getBrushColor();
		DrawFrame df = Controller.getController().getCurrentLayerFrameAtCurrentTime().deepCopy();
		Color oldColor = df.getColorAtPoint(p.x, p.y);
		if (newColor.equals(oldColor)) {
			return;
		}
		Queue<Point> q = new LinkedList<Point>();
		int width = getCanvasWidth();
		int height = getCanvasHeight();
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
		Controller.getController().addAndExecuteCommand(CommandFactory.createFillOnCanvasCommand(df));
	}
	
	//For later if i want it for specific layers, currently should only ever be on current layer
	/*
	private void floodFillSpecifiedLayer(Point p, int layerNum) {
		int[] dxs = new int[] {-1,0,1,0};
		int[] dys = new int[] {0,-1,0,1};
  		Color newColor = Controller.getController().getBrushColor();
		DrawFrame df = Controller.getController().getSpecifiedLayerFrameAtCurrentTime(layerNum).deepCopy();
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
		Controller.getController().setSpecifiedLayerFrameAtCurrentTime(df, layerNum);
	}
	*/
	
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
 				KFCanvas.drawAndErasePath(g2d, currentDraggedPoints);
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
