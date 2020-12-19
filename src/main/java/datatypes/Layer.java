package datatypes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

public class Layer extends JPanel{
	
	private static int MaxWidth = 1534;
	private static int MaxHeight = 40;
	// Layer num is the priority of the list being drawn
	private int layerNum;
	// All the timestamped points for a layer
	private KeyFrames frames;
	private String layerName;
	
	private int UIwidth = 400;
	private int UIheight = 40;
	private Color color = Color.black;

	ArrayList<LayerBoundingBox> boundingBoxes =  new ArrayList<>();
	private boolean isVisible = true;
	private boolean isSelected = false;
	private boolean isGhost = false;
	
	public Layer() {
		super();
	}
		
	public Layer(int layerNum, KeyFrames frames) {
		this.layerNum = layerNum;
		this.frames = frames;
		
		int boundx = 0;
		int boundy = layerNum * UIheight;
		
		//Default rectangle just to fill the whole screen right now, I'll figure out how to change
		//this when I actually implement acutal boxes for each point
		Rectangle box = new Rectangle(boundx, boundy, Layer.MaxWidth, Layer.MaxHeight);
		boundingBoxes.add(new LayerBoundingBox(0, box));
		
		// THIS IS PURELY TO MAKE SURE THERE IS NO SPACE BETWEEN LAYERS
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setMinimumSize(new Dimension(Layer.MaxWidth,Layer.MaxHeight));
		setPreferredSize(new Dimension(Layer.MaxWidth,Layer.MaxHeight));
		setMaximumSize(new Dimension(Layer.MaxWidth,Layer.MaxHeight));
	}
	
	public Layer deepCopy() {
		KeyFrames newFrames = this.frames.deepCopy();
		Layer newLayer = new Layer(this.layerNum, newFrames);
		newLayer.setName(this.layerName);
		// Hopefully this doesn't cause problems
		newLayer.setColor(this.color);
		newLayer.setVisible(this.isVisible);
		newLayer.setSelected(this.isSelected);
		
		// All the timestamped points for a layer
		
		ArrayList<LayerBoundingBox> newBoundingBoxes =  deepCopyBoundingBoxes();
		newLayer.setLayerBoundingBoxes(newBoundingBoxes);
		return newLayer;
		
	}

	public ArrayList<LayerBoundingBox> deepCopyBoundingBoxes() {
		ArrayList<LayerBoundingBox> newBoundingBoxes =  new ArrayList<>();
		for (LayerBoundingBox box : this.boundingBoxes) {
			LayerBoundingBox newBox = box.deepCopy();
			newBoundingBoxes.add(newBox);
		}
		return newBoundingBoxes;
	}
	
	
	public boolean inBounds(int x, int y) {
		for (LayerBoundingBox bbox : boundingBoxes) {
			Rectangle box = bbox.getBox();
			int x1 = box.x;
			int y1 = box.y;
			int x2 = box.x + box.width;
			int y2 = box.y + box.height;
			if ((x >= x1 && x <= x2) && (y >= y1 && y <= y2)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		if (isGhost) {
			g2d.setPaint(Color.LIGHT_GRAY);
			for (LayerBoundingBox bbox : boundingBoxes) {
				// These coordinates I think are relative to our layer coordinates, so 
				// y coordinates should basically always start at 0
				
				//LIGHT GREY INSIDE
				Rectangle box = bbox.getBox();
				g2d.fillRect(box.x, 0, box.width, box.height);
				
				//YELLOW DASHES?
				g2d.setPaint(Color.yellow);
				float dash1[] = { 10.0f };
				BasicStroke dashed = new BasicStroke(1.0f,
					      BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
				g2d.setStroke(dashed);
				g2d.drawRoundRect(box.x, 0, box.width, box.height, 2, 2);
				}
			}
		else {
			g2d.setPaint(color);
			for (LayerBoundingBox bbox : boundingBoxes) {
				// These coordinates I think are relative to our layer coordinates, so 
				// y coordinates should basically always start at 0
				Rectangle box = bbox.getBox();
				g2d.setPaint(color);
				g2d.fillRect(box.x, 0, box.width, box.height);
				if (isSelected) {
					g2d.setPaint(Color.black);
					Stroke s = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
					g2d.setStroke(s);
					g2d.drawRoundRect(box.x, 0, box.width, box.height, 2, 2);
				}
			}
		}
		
	}
	
	public ArrayList<LayerBoundingBox> getLayerBoundingBoxes() {
		return boundingBoxes;
	}
	
	public void setLayerBoundingBoxes(ArrayList<LayerBoundingBox> boxes) {
		this.boundingBoxes = boxes;
		repaint();
	}
	
	public int getLayerNum() {
		return layerNum;
	}
	// Setting a new layer num should also update its bounding boxes
	public void setLayerNum(int num) {
		int diff = num - layerNum;
		for(LayerBoundingBox bbox : boundingBoxes) {
			bbox.getBox().y += diff * UIheight;
		}
		this.layerNum = num;
	}
	
	public ArrayList<ArrayList<DrawPoint>> getPointCollectionAtTime(Integer time) {
		return frames.get(time);
	}
	
	public KeyFrames getFrames() {
		return frames;
	}
	
	public void setFrames(KeyFrames frames) {
		this.frames = frames;
	}
	
	public void setName(String name) {
		this.layerName = name;
	} 
	
	public String getName() {
		return layerName;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setSelected(boolean option) {
		isSelected = option;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public void setGhost(boolean option) {
		isGhost = option;
	}
	
	public boolean isGhost() {
		return isGhost;
	}
	
	public void setVisible(boolean option) {
		isVisible = option;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public static void setMaxWidth(int newWidth) {
		Layer.MaxWidth = newWidth;
	}
	
	public static int getMaxWidth() {
		return Layer.MaxWidth;
	}
	
	public static void setMaxHeight(int newHeight) {
		Layer.MaxHeight = newHeight;
	}
	
	public static int getMaxHeight() {
		return Layer.MaxHeight;
	}
	
}
