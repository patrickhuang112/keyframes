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
	private Integer layerNum;
	// All the timestamped points for a layer
	private KeyFrames frames;
	private String layerName;
	
	private int UIwidth = 400;
	private int UIheight = 40;
	private Color color = Color.black;

	ArrayList<LayerBoundingBox> boundingBoxes =  new ArrayList<>();
	private boolean isVisible = true;
	private boolean isSelected = false;
	
	public Layer() {
		super();
	}
		
	public Layer(Integer layerNum, KeyFrames frames) {
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
		setMaximumSize(new Dimension(Layer.MaxWidth,Layer.MaxHeight));
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
	
	public ArrayList<LayerBoundingBox> getLayerBoundingBoxes() {
		return boundingBoxes;
	}
	
	public void setLayerBoundingBoxes(ArrayList<LayerBoundingBox> boxes) {
		this.boundingBoxes = boxes;
		repaint();
	}
	
	public Integer getLayerNum() {
		return layerNum;
	}
	
	public void setLayerNum(Integer num) {
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
