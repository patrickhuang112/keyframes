package datatypes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import keyframes.MagicValues;
import ui.layer.KFLayer;
import ui.layer.KFLayerFactory;
import ui.layer.rectangles.KFLayerRectangles;
import ui.layer.rectangles.KFLayerRectanglesFactory;

public class Layer implements Serializable {
	
	private static int MaxWidth = MagicValues.layerDefaultMaxWidth;
	private static int MaxHeight = MagicValues.layerDefaultMaxHeight;
	// Layer num is the priority of the list being drawn
	private int layerNum;
	// All the timestamped points for a layer
	
	private HashMap<Integer, int[][]> pixelsMap;
	private boolean inSerializedState;
	private int serializedWidth;
	private int serializedHeight;
	private transient KeyFrames frames;
	//FOR SERIALIZATION OF A KEYFRAMES
	private String layerName;
	
	private KFLayerRectangles layerUI;
	
	private int UIheight = MagicValues.layerUIDefaultHeight;
	private Color color = Color.black;

	ArrayList<LayerBoundingBox> boundingBoxes =  new ArrayList<>();
	
	
	
	private boolean isVisible = true;
	private boolean isSelected = false;
	private boolean isGhost = false;
	
	private final int layerInitialFrames = 0;
	
	public Layer(int layerNum, KeyFrames frames) {
		this.layerNum = layerNum;
		this.frames = frames;
		
		int boundx = 0;
		int boundy = layerNum * UIheight;
		
		//Default rectangle just to fill the whole screen right now, I'll figure out how to change
		//this when I actually implement acutal boxes for each point
		Rectangle box = new Rectangle(boundx, boundy, Layer.MaxWidth, Layer.MaxHeight);
		boundingBoxes.add(new LayerBoundingBox(layerInitialFrames, box));
		layerUI = KFLayerRectanglesFactory.createStandardKFLayerRectangles(this);
	}
	
	public void prepareForSerialization() {
		pixelsMap = new HashMap<Integer, int[][]>();
		boolean setSerializedWidthHeight = false;
		for(Integer key : frames.keySet()) {
			DrawFrame img = frames.get(key);
			int[][] pixels = img.getPixelArray();
			pixelsMap.put(key, pixels);
			if (!setSerializedWidthHeight) {
				serializedWidth = img.getWidth();
				serializedHeight = img.getHeight();
				setSerializedWidthHeight = true;
			}
		}
		inSerializedState = true;
	}
	
	public void updateFromDeserialization() {
		if (inSerializedState) {
			frames = new KeyFrames();
			for(Integer key : pixelsMap.keySet()) {
				int[][] pixels = pixelsMap.get(key);
				DrawFrame image = new DrawFrame(serializedWidth, serializedHeight);
				for(int r = 0; r < serializedHeight; r++) {
					for (int c = 0; c < serializedWidth; c++) {
						image.setRGB(c, r, pixels[r][c]);
					}
				}
				/*
		        WritableRaster raster = (WritableRaster) image.getData();
		        raster.setPixels(0,0, serializedWidth, serializedHeight, pixels);
		        image.setData(raster);
		        */
				frames.put(key, image);
			}
			inSerializedState = false;	
		}
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
	
	public ArrayList<LayerBoundingBox> getLayerBoundingBoxes() {
		return boundingBoxes;
	}
	
	public void setLayerBoundingBoxes(ArrayList<LayerBoundingBox> boxes) {
		this.boundingBoxes = boxes;
		this.layerUI.refresh();
	}
	
	public JPanel getUIContainer() {
		return layerUI.getSwingComponent();
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
	
	public DrawFrame getPointCollectionAtTime(Integer time) {
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
