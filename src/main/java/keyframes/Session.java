package keyframes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import javax.swing.plaf.basic.BasicSliderUI;

import datatypes.DrawFrame;
import datatypes.DrawPoint;
import datatypes.Enums;
import datatypes.KeyFrames;
import datatypes.Layer;
import datatypes.LayerBoundingBox;
import ui.progressbar.ProgressBar;
import ui.slider.StandardTimelineSlider;
import ui.slider.StandardTimelineSliderUI;
import ui.slider.TimelineSlider;
import settings.Settings;
import ui.canvas.KFCanvas;

public class Session implements Serializable {
	
	
	private static final long serialVersionUID = 3817282456552806335L;
	
	public static final int brushMin = 0;
	public static final int brushMax = 30;
	public static final int eraserMin = 0;
	public static final int eraserMax = 30;
	public static final int fpsMin = 0;
	public static final int fpsMax = 30;
	public static final int lengthMin = 0;
	public static final int lengthMax = 20;
	public static final Color defaultDrawPanelBackgroundColor = Color.gray;
	

	
	static Session createSessionFromSettings(Settings settings) {
		return new Session(settings);
	}
	
	static Session createSessionFromSessionSave(SessionSave ss) {
		return new Session(ss);
	}
	
	private Session(SessionSave ss) {
		this();
		this.layers = ss.drawLayers;
		this.framesPerSecond = ss.framesPerSecond;
		this.longestTimeInSeconds = ss.longestTimeInSeconds;
		this.longestTimepoint = ss.longestTimepoint;
		this.savePath = ss.savePath;
	}
	
	private Session(Settings settings) {
		this();
		brushSize = settings.getBrushSize();
		eraserSize = settings.getEraserSize();
		longestTimeInSeconds = settings.getCompLength();
		framesPerSecond = settings.getFps();
		longestTimepoint = longestTimeInSeconds * framesPerSecond;
	}
	
	private Session() {
		super();
		initializeLayers();
		// Defaults
		brushSize = 5;
		eraserSize = 5;
		longestTimeInSeconds = 5;
		framesPerSecond = 10;
		longestTimepoint = longestTimeInSeconds * framesPerSecond;
	}
	
	void initializeLayers() {
		layers = new ArrayList<Layer>();
		// Initialize new HashMap of times and frames for default layer
		KeyFrames drawFrames = 
			new KeyFrames();
		
		int dw = MagicValues.drawablePanelDefaultPreferredWidth;
		int dh = MagicValues.drawablePanelDefaultPreferredHeight;
		
		drawFrames.put(0, new DrawFrame(dw, dh));
		
		Layer newLayer = new Layer(0, drawFrames);
		newLayer.setSelected(true);
		layers.add(newLayer);
	}
	
	private Integer currentLayerNum = 0;
	private String savePath = null;
	private boolean isPlaying = false;
	
	private Color brushColor = Color.red;
	private Color eraserColor;
	private int brushSize;
	private int eraserSize;
	private Enums.PaintSetting paintSetting = Enums.PaintSetting.DRAW;
	// This contains layer info, and time info
	private ArrayList<Layer> layers;
	private Layer layerBeingDragged = null;
	
	//REPLACE BY SETTINGS
	private int currentTimepoint = 0;
	private int minTimepoint = 0;
	private int longestTimeInSeconds;
	private int framesPerSecond;
	
	
	private int longestTimepoint = longestTimeInSeconds * framesPerSecond;
	// Current frames copied to clipboard
	// SHould be an ArrayList of array list of draw points
	
	//WHY IS IT LIKE THIS INSTEAD OF JUST A SINGLE LAYER? 
	//
	private DrawFrame clipboardFrames = null;
	
	
	Session createDeepCopy() {
		Session copy = new Session();
		copy.setCurrentLayerNum(this.currentLayerNum);
		copy.setBrushColor(this.brushColor);
		copy.setEraserColor(this.eraserColor);
		copy.setBrushSize(this.brushSize);
		copy.setEraserSize(this.eraserSize);
		copy.setPaintSetting(this.paintSetting);
		copy.setCurrentTimepoint(this.currentTimepoint);
		copy.setLongestTimeInSeconds(this.longestTimeInSeconds);
		copy.setFramesPerSecond(this.framesPerSecond);
		copy.setSavePath(this.savePath);
		copy.setShortestTimepoint(this.minTimepoint);
		copy.setLayers(deepCopyLayers());
		return copy;
	}
	
	//-------------------------------------------------------------------------------------------------
	// File saving stuff
	public String getSavePath() {
		return savePath;
	}
	
	public void setSavePath(String path) {
		savePath = path;
	}
	
	//-------------------------------------------------------------------------------------------------
	//Draw settings
	
	public Color getBrushColor() {
		return this.brushColor;
	}
	
	public void setBrushColor(Color color) {
		this.brushColor = color;
	}
	
	public Color getEraserColor() {
		return this.eraserColor;
	}
	
	public void setEraserColor(Color color) {
		this.eraserColor = color;
	}
	
	//DRAWABLE PAINT SETTING
	public void setPaintSetting(Enums.PaintSetting paintSetting) {
		this.paintSetting = paintSetting;
	}
	
	public Enums.PaintSetting getPaintSetting() {
		return this.paintSetting;
	}
	
	
	//DRAWABLE BRUSH/ERASER SETTINGS
	public int getBrushSize() {
		return brushSize;
	}
	
	public void setBrushSize(int brushSize) {
		if(brushSize >= brushMin && brushSize <= brushMax) {
			this.brushSize = brushSize;
		}
	}
	
	public int getEraserSize() {
		return eraserSize;
	}
	
	public void setEraserSize(int eraserSize) {
		if(eraserSize >= eraserMin && eraserSize <= eraserMax) {
			this.eraserSize = eraserSize;
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	//Composition settings (FPS, timepoints)
	
	void setShortestTimepoint(int min) {
		this.minTimepoint = min;
	}
	
	int getShortestTimepoint() {
		return this.minTimepoint;
	}
	
	int getLongestTimepoint() {
		return this.longestTimepoint;
	}
	
	void incrementTimepoint() {
		if(currentTimepoint == longestTimepoint) {
			setCurrentTimepoint(0);
		} else {
			setCurrentTimepoint(currentTimepoint + 1);
		}
	}
	
	//TIMELINE CURRENT TIME
	//True means successful
	//False means unsuccessful
	boolean setCurrentTimepoint(int time) {
		if(time >= minTimepoint && time <= longestTimepoint) {
			this.currentTimepoint = time;
			return true;
		}
		return false;
	}
	
	int getCurrentTimepoint() {
		return this.currentTimepoint;
	}
	
	// Set the longest timepoint for the composition (from the slider)
	boolean setLongestTimeInSeconds(int time) {
		if(time >= lengthMin && time <= lengthMax) {
			this.longestTimeInSeconds = time;
			this.longestTimepoint = framesPerSecond * longestTimeInSeconds;
			this.currentTimepoint = Session.calculateNewTimelinePointerPositionFromOldFPS(framesPerSecond, 
					   				framesPerSecond, currentTimepoint);
			Session.updateAllLayerFramesFromChangeInFPSOrLength(framesPerSecond, framesPerSecond, longestTimepoint,
																	layers);
			return true;
		}
		return false;
	}
	
	int getLongestTimeInSeconds() {
		return this.longestTimeInSeconds;
	}
		
	boolean setFramesPerSecond(int fps) {
		if(fps >= Session.fpsMin && fps <= Session.fpsMax) {
			int oldFPS = framesPerSecond;
			int newFPS = fps;
			this.framesPerSecond = fps;
			this.longestTimepoint = fps * longestTimeInSeconds;
			this.currentTimepoint = Session.calculateNewTimelinePointerPositionFromOldFPS(oldFPS, newFPS, 
																							currentTimepoint);
			Session.updateAllLayerFramesFromChangeInFPSOrLength(oldFPS, newFPS, longestTimepoint,
					layers);
			return true;
		}
		return false;
	}
	
	int getFramesPerSecond() {
		return this.framesPerSecond;
	}
		
	private static int calculateNewTimelinePointerPositionFromOldFPS(int oldFps, int newFps, int timePoint) {
		int sec = timePoint / oldFps;
		double rem = timePoint % oldFps;
		double frac = rem / ((double)oldFps);
		int newTimepoint = (sec * newFps) + (int)(frac * (double)(newFps));
		return newTimepoint;
	}
	
	// A function to update all frames in all layers, where the change is due to FPS change
	private static void updateAllLayerFramesFromChangeInFPSOrLength(int oldFps, int newFps, int endPoint, 
																	ArrayList<Layer> drawLayers) {
		for (Layer layer : drawLayers) {
			Session.updateLayerFramesFromChangeInFPSOrLength(oldFps, newFps, endPoint, layer);
		}
	}
	
	private static void updateLayerFramesFromChangeInFPSOrLength(int oldFps, int newFps, int endPoint, 
																	Layer layer) {
		KeyFrames newFrames = 
				new KeyFrames();
		KeyFrames currentDrawFrames = 
				layer.getFrames();
		for(Integer i : currentDrawFrames.keySet()) {
			int newKey = Session.calculateNewTimelinePointerPositionFromOldFPS(oldFps, newFps, i);
			if(!newFrames.containsKey(newKey) && i <= endPoint) {
				newFrames.put(newKey, currentDrawFrames.get(i));
			} 
		}
		
		layer.setFrames(newFrames);
	}
	
	//-------------------------------------------------------------------------------------------------
	//Movie preview stuff
	
	boolean isPlaying() {
		return isPlaying;
	}
	
	void setPlaying(boolean play) {
		isPlaying = play;
	}
		
	
	//-------------------------------------------------------------------------------------------------
	
	void copyFramesFromSpecifiedLayerAndCurrentTime(int layerNum) {
		clipboardFrames = getSpecifiedLayerFrameAtCurrentTime(layerNum);
	}
	
	void pasteFramesToSpecifiedLayerAndCurrentTime(int layerNum) {
		if (clipboardFrames != null) {
			setSpecifiedLayerFrameAtCurrentTime(clipboardFrames, layerNum);
		}
	}
	
	void copyFramesFromCurrentLayerAndCurrentTime() {
		copyFramesFromSpecifiedLayerAndCurrentTime(getCurrentLayerNum());
	}
	
	void pasteFramesToCurrentLayerAndCurrentTime() {
		pasteFramesToSpecifiedLayerAndCurrentTime(getCurrentLayerNum());
	}	
	
	//-------------------------------------------------------------------------------------------------
	// Layers management
	
	boolean isLayerBeingDragged() {
		return layerBeingDragged != null;
	}
	
	// Needs to trhow an exception if its null, will update this later
	Layer getCurrentDraggedLayer() {
		return layerBeingDragged;
	}
	
	void setCurrentDraggedLayer(Layer layer) {
		layerBeingDragged = layer;
	}
	
	// Means reset layerBeingDragged to null
	void resetCurrentDraggedLayer() {
		layerBeingDragged = null;
	}
	
	Layer selectLayer(MouseEvent e) {
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			if(layer.inBounds(e.getX(), e.getY())) {
				int curLayNum = getCurrentLayerNum();
				layers.get(curLayNum).setSelected(false);
				layer.setSelected(true);
				setCurrentLayerNum(i);
				return layer;
			}
		}
		return null;
	}
	
	//Layers can only be deleted if at least one layer remains
	boolean layersCanBeDeleted(ArrayList<Integer> layerNums) {
		return layerNums.size() < layers.size();
	}
	
	void deleteLayers(ArrayList<Integer> layerNums) {
		for (int i = layerNums.size() - 1; i >= 0; i--) {
			this.layers.remove((int)layerNums.get(i));
		}
		//This is a bandaid fix right now, preferably, we would allow no selected layer.
		setCurrentLayerNum(0);
		recalibrateLayerNums();
	}
	
	private void recalibrateLayerNums() {
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			layer.setLayerNum(i);
		}
	}
	
	public ArrayList<Layer> deepCopyLayers() {
		ArrayList<Layer> newLayers = new ArrayList<Layer>();
		for (Layer layer : layers) {
			newLayers.add(layer.deepCopy());
		}
		return newLayers;
	}
	
	void setLayers(ArrayList<Layer> layers) {
		this.layers = layers;
	}
	
	// CURRENT LAYER
	ArrayList<Layer> getLayers() {
		return this.layers;
	}
	
	// Return what the session thinks is the currently selected layer (num)
	// Was an Integer (object) but I think it should be an int because there should always be 
	// a current layer (otherwise what do we draw to?)
	int getCurrentLayerNum () {
		return this.currentLayerNum;
	}
	
	// Update the session's track of what is the currently selected layer (num)
	public void setCurrentLayerNum (Integer num) {
		this.currentLayerNum = num;
	}
	
	Layer getCurrentLayer() {
		return this.layers.get(this.currentLayerNum);
	}
	
	private Layer getSpecifiedLayer(int layerNum) {
		return this.layers.get(layerNum);
	}
	
	// ADD LAYERS
	public void addNewLayer() {
		KeyFrames newDrawFrames = new KeyFrames();
		int numLayers = layers.size();
		Layer newLayer = new Layer(numLayers, newDrawFrames);
		
		// The new layer should be selected when created
		for (Layer layer : layers) {
			if(layer.isSelected()) {
				layer.setSelected(false);
			}
		}
		newLayer.setSelected(true);
		this.setCurrentLayerNum(numLayers);
		layers.add(newLayer);
	}
	
	
	//DRAWABLE GET/SET FRAMEs
	// Set the frame at a certain time point for the current layer frame
	public void setLayerFrameAtTime(int layerNum, Integer timePoint, DrawFrame drawing) {
		Layer specifiedLayer = getSpecifiedLayer(layerNum);
		specifiedLayer.getFrames().put(timePoint, drawing);
	}
	
	public void setCurrentLayerFrameAtTime(Integer timePoint, DrawFrame drawing) {
		setLayerFrameAtTime(getCurrentLayerNum(), timePoint, drawing);
	}
	
	// Returns an empty arraylist of draw instructions if there isn't anything currently at this time on
	// the current layer
	public DrawFrame getLayerFrameAtTime(Integer layerNum, Integer timePoint) {
		Layer specifiedLayer = getSpecifiedLayer(layerNum);
		HashMap<Integer,DrawFrame> drawFrames = specifiedLayer.getFrames();
		if(drawFrames.containsKey(timePoint)) {
			return drawFrames.get(timePoint);
		} else {
			int dw = MagicValues.drawablePanelDefaultPreferredWidth;
			int dh = MagicValues.drawablePanelDefaultPreferredHeight;
			
			setCurrentLayerFrameAtTime(timePoint, new DrawFrame(dw, dh));
			return drawFrames.get(timePoint);
		}
	}
	
	public DrawFrame getCurrentLayerFrameAtTime(Integer timePoint) {
		return getLayerFrameAtTime(getCurrentLayerNum(), timePoint);
	}
	
	public DrawFrame getSpecifiedLayerFrameAtCurrentTime(Integer layerNum) {
		return getLayerFrameAtTime(layerNum, getCurrentTimepoint());
	}
	
	public void setSpecifiedLayerFrameAtCurrentTime(DrawFrame drawing, int layerNum) {
		setLayerFrameAtTime(layerNum, getCurrentTimepoint(), drawing);
	}
	
	public void setCurrentLayerFrameAtCurrentTime(DrawFrame drawing) {
		setLayerFrameAtTime(getCurrentLayerNum(), getCurrentTimepoint(), drawing);
	}
	
	public DrawFrame getCurrentLayerFrameAtCurrentTime() {
		return getCurrentLayerFrameAtTime(getCurrentTimepoint());
	}
	
	public void addToCurrentLayerFrameAtCurrentTime(ArrayList<DrawPoint> newPoints) {
		DrawFrame dps = getCurrentLayerFrameAtCurrentTime();
		Graphics2D g2d = dps.createGraphics();
		if (newPoints.size() == 1) {
			KFCanvas.drawAndErasePoint(g2d, newPoints.get(0));
		} else {
			KFCanvas.drawAndErasePath(g2d, newPoints);
		}
		
	}
	
	void eraseCurrentLayerAtCurrentFrame(int w, int h) {
		Layer curLayer = getCurrentLayer();
		KeyFrames frames = curLayer.getFrames();
		frames.put(getCurrentTimepoint(), new DrawFrame(w, h));
	}
	
	void eraseAllLayersAtCurrentFrame(int w, int h) {
		for (Layer layer : layers) {
			KeyFrames frames = layer.getFrames();
			frames.put(getCurrentTimepoint(), new DrawFrame(w, h));
		}
	}
	
	
}
