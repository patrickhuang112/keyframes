package keyframes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

import datatypes.DrawPoint;
import datatypes.KeyFrames;
import datatypes.Layer;
import factories.EnumFactory;
import settings.Settings;

public class Session implements Serializable {
	
	
	private static final long serialVersionUID = 3817282456552806335L;
	
	public final int brushMin = 1;
	public final int brushMax = 30;
	public final int eraserMin = 1;
	public final int eraserMax = 30;
	public final int fpsMin = 10;
	public final int fpsMax = 30;
	public final int lengthMin = 5;
	public final int lengthMax = 30;
	

	public Session(Settings settings) {
		this();
		brushSize = settings.getBrushSize();
		eraserSize = settings.getEraserSize();
		longestTimeInSeconds = settings.getCompLength();
		framesPerSecond = settings.getFps();
		longestTimepoint = longestTimeInSeconds * framesPerSecond;
	}
	
	public Session() {
		super();
		// Initialize new HashMap of times and frames for default layer
		KeyFrames drawFrames = 
			new KeyFrames();
		
		drawFrames.put(0, new ArrayList<>());
		
		drawLayers.add(new Layer(0, drawFrames));
		brushSize = 5;
		eraserSize = 5;
		
		longestTimeInSeconds = 10;
		framesPerSecond = 15;
		longestTimepoint = longestTimeInSeconds * framesPerSecond;
	}
	
	private Integer currentLayerNum = 0;
	private String savePath = null;
	public boolean isPlaying = false;
	private DrawablePanel drawPanel = null;
	private TimelineSlider timelineSlider = null;
	private Hashtable<Integer, JLabel> timelineLabelDict = new Hashtable<Integer, JLabel>();
	
	private Color brushColor = Color.red;
	private Color eraserColor = Color.white;
	private int brushSize;
	private int eraserSize;
	private EnumFactory.PaintSetting paintSetting = EnumFactory.PaintSetting.DRAW;
	// This contains layer info, and time info
	private ArrayList<Layer> drawLayers = new ArrayList<Layer>();
	// This just contains time info
	
	private HashMap<Integer, BufferedImage> drawImages = new HashMap<Integer, BufferedImage>();
	
	//REPLACE BY SETTINGS
	private int currentTimepoint = 0;
	
	private int longestTimeInSeconds;
	private int framesPerSecond;
	private int minTimepoint = 0;
	private int longestTimepoint = longestTimeInSeconds * framesPerSecond;
	// Current frames copied to clipboard
	// Hashmap key: the number of the layer
	// Hashmap entry: the frames associated with that layer
	private KeyFrames clipboardFrames = null;
	
	
	public Color getDrawablePanelBackgroundColor () {
		return drawPanel.getBackground();
	}
	
	public String getSavePath() {
		return savePath;
	}
	
	public void setSavePath(String path) {
		savePath = path;
	}
	
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
	
	
	public int getDrawablePaneWidth() {
		return drawPanel.getWidth();
	}
	
	public int getDrawablePaneHeight() {
		return drawPanel.getHeight();
	}
	
	public void setTimelineSlider(TimelineSlider ts) {
		this.timelineSlider = ts;
	}
	
	public void updateTimelineSliderPosition() {
		timelineSlider.setValue(currentTimepoint);
	}
	
	public Hashtable<Integer, JLabel> getTimelineLabelDict() {
		return timelineLabelDict;
	}
	
	
	public void setDrawPanel(DrawablePanel dp) {
		this.drawPanel = dp;
	}
	
	
	public int getLongestTimepoint() {
		return this.longestTimepoint;
	}
	
	public void incrementTimepoint() {
		if(currentTimepoint == longestTimepoint) {
			currentTimepoint = 0;
		} else {
			currentTimepoint++;
		}
	}
	
	public void refreshDrawPanel(){
		drawPanel.repaint();
		drawPanel.revalidate();
		
	}
	
	private ArrayList<ArrayList<DrawPoint>> deepCopyFrames(ArrayList<ArrayList<DrawPoint>> originalFrame) {
		ArrayList<ArrayList<DrawPoint>> res = new ArrayList<>();
		for(ArrayList<DrawPoint> curInstruct : originalFrame) {
			ArrayList<DrawPoint> newInstruct = new ArrayList<>();
			for(DrawPoint curPoint : curInstruct) {
				DrawPoint newPoint = curPoint.createCopy();
				newInstruct.add(newPoint);
			}
			res.add(newInstruct);
		}
		return res;
	}
	
	public void copyFramesFromCurrentLayerAndCurrentTime() {
		clipboardFrames = new KeyFrames();
		clipboardFrames.put(getCurrentLayerNum(), getCurrentLayerFrameAtCurrentTime());
	}
	
	public void pasteFramesToCurrentLayerAndCurrentTime() {
		if (clipboardFrames.containsKey(getCurrentLayerNum())) {
			setCurrentLayerFrameAtCurrentTime(deepCopyFrames(clipboardFrames.get(getCurrentLayerNum())));
			refreshDrawPanel();
		}
	}	
	
	// Set the longest timepoint for the composition (from the slider)
	public void setLongestTimeInSeconds(int time) {
		if(time >= lengthMin && time <= lengthMax) {
			this.longestTimeInSeconds = time;
			longestTimepoint = framesPerSecond * longestTimeInSeconds;
			updateTimeline(framesPerSecond, framesPerSecond);
			updateAllLayerFramesFromFPS(framesPerSecond, framesPerSecond);
			
			refreshDrawPanel();
		}
	}
	
	public int getLongestTimeInSeconds() {
		return this.longestTimeInSeconds;
	}
	
	
	//FPS
	private void updateTimeline(int oldFps, int newFps) {
		int newTimepoint = calculateNewTimelinePointerPositionFromOldFps(oldFps, newFps, currentTimepoint);
		timelineSlider.setMaximum(longestTimeInSeconds * newFps);
		timelineSlider.setMajorTickSpacing(newFps);
		timelineSlider.setMinorTickSpacing(1);
		timelineLabelDict = new Hashtable<Integer, JLabel>();
		
		for(Integer i = 0; i < longestTimeInSeconds; i++) {
			timelineLabelDict.put(i * newFps, new JLabel(i.toString()));
		}
		//ADD THE ENDPOINT LABEL
		timelineLabelDict.put(longestTimeInSeconds * newFps, 
				new JLabel(((Integer)longestTimeInSeconds).toString()));
		timelineSlider.setLabelTable(timelineLabelDict);
		
		timelineSlider.setValue(newTimepoint);
		setCurrentTimepoint(newTimepoint);
		timelineSlider.repaint();
		timelineSlider.revalidate();
	}
	
	public void setFramesPerSecond(int fps) {
		if(fps >= fpsMin && fps <= fpsMax) {
			int oldFps = framesPerSecond;
			int newFps = fps;
			
			updateTimeline(oldFps, newFps);
			updateAllLayerFramesFromFPS(oldFps, newFps);
			
			
			framesPerSecond = fps;
			longestTimepoint = fps * longestTimeInSeconds;
			
			refreshDrawPanel();
		}
		
	}
	
	public int getFramesPerSecond() {
		return this.framesPerSecond;
	}
	
	private int calculateNewTimelinePointerPositionFromOldFps(int oldFps, int newFps, int timePoint) {
		int sec = timePoint / oldFps;
		double rem = timePoint % oldFps;
		double frac = rem / ((double)oldFps);
		int newTimepoint = (sec * newFps) + (int)(frac * (double)(newFps));
		return newTimepoint;
	}
	
	// A function to update all frames in all layers, where the change is due to FPS change
	private void updateAllLayerFramesFromFPS(int oldFps, int newFps) {
		for (Layer layer : drawLayers) {
			updateLayerFramesFromFPS(oldFps, newFps, layer);
		}
	}
	
	private void updateLayerFramesFromFPS(int oldFps, int newFps, Layer layer) {
		KeyFrames newFrames = 
				new KeyFrames();
		KeyFrames currentDrawFrames = 
				layer.getFrames();
		for(Integer i : currentDrawFrames.keySet()) {
			int newKey = calculateNewTimelinePointerPositionFromOldFps(oldFps, newFps, i);
			if(!newFrames.containsKey(newKey) && i <= longestTimepoint) {
				newFrames.put(newKey, currentDrawFrames.get(i));
			} 
		}
		
		layer.setFrames(newFrames);
	}
	
	
	//TIMELINE CURRENT TIME
	public void setCurrentTimepoint(int time) {
		if(time >= minTimepoint && time <= longestTimepoint) {
			this.currentTimepoint = time;
		}
	}
	
	public int getCurrentTimepoint() {
		return this.currentTimepoint;
	}
	
	// CURRENT LAYER
	public ArrayList<Layer> getLayers() {
		return this.drawLayers;
	}
	
	public Integer getCurrentLayerNum () {
		return this.currentLayerNum;
	}
	
	public void setCurrentLayerNum (Integer num) {
		this.currentLayerNum = num;
	}
	
	public void setCurrentLayer (Layer layer) {
		this.drawLayers.set(this.currentLayerNum, layer);
	}
	
	public Layer getCurrentLayer () {
		return this.drawLayers.get(this.currentLayerNum);
	}
	
	//DRAWABLE GET/SET FRAMEs
	// Set the frame at a certain time point for the current layer frame
	public void setCurrentLayerFrameAtTime(Integer timePoint, ArrayList<ArrayList<DrawPoint>> drawing) {
		Layer curLayer = getCurrentLayer();
		curLayer.getFrames().put(timePoint, drawing);
	}
	

	public ArrayList<ArrayList<DrawPoint>> getCurrentLayerFrameAtTime(Integer timePoint) {
		Layer curLayer = getCurrentLayer();
		HashMap<Integer,ArrayList<ArrayList<DrawPoint>>> drawFrames = curLayer.getFrames();
		if(drawFrames.containsKey(timePoint)) {
			return drawFrames.get(timePoint);
		} else {
			setCurrentLayerFrameAtTime(timePoint, new ArrayList<>());
			return drawFrames.get(timePoint);
		}
	}
	
	public void setCurrentLayerFrameAtCurrentTime(ArrayList<ArrayList<DrawPoint>> drawing) {
		setCurrentLayerFrameAtTime(currentTimepoint, drawing);
	}
	
	
	public ArrayList<ArrayList<DrawPoint>> getCurrentLayerFrameAtCurrentTime() {
		return getCurrentLayerFrameAtTime(currentTimepoint);
	}
	
	
	// With Rendering stuff
	public void setCurrentImage(BufferedImage img) {
		drawImages.put(currentTimepoint, img);
	}
	
	public HashMap<Integer, BufferedImage> getImages() {
		return drawImages;
	}

	
	
	//BYE BYE ALL FRAMES
	public void eraseAllLayerFrames() {
		for (Layer layer : drawLayers) {
			eraseAllFramesFromLayer(layer);
		}
		
	}
	
	public void eraseAllFramesFromLayer(Layer layer) {
		layer.setFrames(new KeyFrames());
	}
	
	
	
	//DRAWABLE PAINT SETTING
	public void setPaintSetting(EnumFactory.PaintSetting paintSetting) {
		this.paintSetting = paintSetting;
	}
	
	public EnumFactory.PaintSetting getPaintSetting() {
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
	
}
