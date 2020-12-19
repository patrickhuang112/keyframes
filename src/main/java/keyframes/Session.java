package keyframes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import javax.swing.plaf.basic.BasicSliderUI;

import datatypes.DrawPoint;
import datatypes.KeyFrames;
import datatypes.Layer;
import datatypes.LayerBoundingBox;
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
		Layer newLayer = new Layer(0, drawFrames);
		newLayer.setSelected(true);
		drawLayers.add(newLayer);
		
		// Defaults
		brushSize = 5;
		eraserSize = 5;
		longestTimeInSeconds = 5;
		framesPerSecond = 10;
		longestTimepoint = longestTimeInSeconds * framesPerSecond;
	}
	
	
	private Integer currentLayerNum = 0;
	private String savePath = null;
	private boolean isPlaying = false;
	private DrawablePanel drawPanel = null;
	private TimelineLayersPanel timelineLayersPanel = null;
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
	//Dependent on frames persecond and time in seconds, updated when we set the timeline slider
	private int spaceBetweenTicks;
	
	private int minTimepoint = 0;
	private int longestTimepoint = longestTimeInSeconds * framesPerSecond;
	// Current frames copied to clipboard
	// Hashmap key: the number of the layer
	// Hashmap entry: the frames associated with that layer
	private KeyFrames clipboardFrames = null;
	
	
	//-------------------------------------------------------------------------------------------------
	// File saving stuff
	public String getSavePath() {
		return savePath;
	}
	
	public void setSavePath(String path) {
		savePath = path;
	}
	
	// With Rendering stuff
	public void setCurrentImage(BufferedImage img) {
		drawImages.put(currentTimepoint, img);
	}
	
	public HashMap<Integer, BufferedImage> getImages() {
		return drawImages;
	}
	
	//-------------------------------------------------------------------------------------------------
	//Draw settings
	
	public Color getDrawablePanelBackgroundColor () {
		return drawPanel.getBackground();
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
	
	//-------------------------------------------------------------------------------------------------	
	// These are for rendering stuff (what size was our drawings)
	
	public int getDrawablePaneWidth() {
		return drawPanel.getWidth();
	}
	
	public int getDrawablePaneHeight() {
		return drawPanel.getHeight();
	}
	
	//-------------------------------------------------------------------------------------------------
	// INITIALIZE UI Components in Session
	// These should only be called at the beginning
	
	// This should only be called once when we set up everything, so we can update the 
	// spacing between ticks variable for the first time here as well
	public void setTimelineSlider(TimelineSlider ts) {
		this.timelineSlider = ts;
		this.spaceBetweenTicks = timelineSlider.getUI().getSpacingBetweenTicks();
	}
	
	public void setDrawPanel(DrawablePanel dp) {
		this.drawPanel = dp;
	}
	
	public void setLayersPanel(TimelineLayersPanel pane) {
		this.timelineLayersPanel = pane;
	}
	
	//-------------------------------------------------------------------------------------------------
	//Composition settings (FPS, timepoints)
	
	public int getLongestTimepoint() {
		return this.longestTimepoint;
	}
	
	private void incrementTimepoint() {
		if(currentTimepoint == longestTimepoint) {
			setCurrentTimepoint(0);
		} else {
			setCurrentTimepoint(currentTimepoint + 1);
		}
	}
	
	//TIMELINE CURRENT TIME
	public void setCurrentTimepoint(int time) {
		if(time >= minTimepoint && time <= longestTimepoint) {
			this.currentTimepoint = time;
		}
		timelineSlider.setValue(time);
	}
	
	public int getCurrentTimepoint() {
		return this.currentTimepoint;
	}
	
	// Set the longest timepoint for the composition (from the slider)
		public void setLongestTimeInSeconds(int time) {
			if(time >= lengthMin && time <= lengthMax) {
				this.longestTimeInSeconds = time;
				longestTimepoint = framesPerSecond * longestTimeInSeconds;
				updateTimeline(framesPerSecond, framesPerSecond);
				updateAllLayerFramesFromFPS(framesPerSecond, framesPerSecond);
				
				refreshUI();
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

			setCurrentTimepoint(newTimepoint);
			refreshUI();
		}
		
		public void setFramesPerSecond(int fps) {
			if(fps >= fpsMin && fps <= fpsMax) {
				int oldFps = framesPerSecond;
				int newFps = fps;
				
				updateTimeline(oldFps, newFps);
				updateAllLayerFramesFromFPS(oldFps, newFps);
				
				framesPerSecond = fps;
				longestTimepoint = fps * longestTimeInSeconds;
				
				refreshUI();
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
	
	//-------------------------------------------------------------------------------------------------
	//Movie preview stuff
	
	private void playOneFrame() {
		incrementTimepoint();
		refreshUI();
	}
	
	//Space bar
	public void playOrPauseMovie() {
		if (isPlaying) {
			pauseMovie();
		} else {
			playMovie();
		}
	}
	
	//Play button
	public void playMovie() {
		isPlaying = true;
		SwingWorker sw = new SwingWorker() {
			@Override
			protected Object doInBackground() throws Exception {
				while(isPlaying) {
					playOneFrame();
					long increment = 1000 / getFramesPerSecond();
					Thread.sleep(increment);
					System.out.println("Playing movie...");
				}
				return "DONE";
			}
			
			@Override
			protected void done() {
				System.out.println("Finished playing movie");
			}
		};
		sw.execute();
	}
	
	//Pause button
	public void pauseMovie() {
		isPlaying = false;
	}
	
	//-------------------------------------------------------------------------------------------------
	//Refresh and update UI components from session
	
	// REFRESH and REDRAW DRAW PANEL (usually when we update our drawpanel in someway)
	private void refreshDrawPanel(){
		drawPanel.repaint();
		drawPanel.revalidate();
	}
	
	private void refreshTimelineUI() {
		// UPDATES the line on the layers panel
		TimelineSliderUI ui = timelineSlider.getUI();
		double sliderBarx = ui.getThumbMidX();
	    timelineLayersPanel.setSliderBarx(sliderBarx);
	    
	    //Updates the layers order on the layers panel
	    timelineLayersPanel.updateTimelineLayersPanel();
	    
	    timelineLayersPanel.repaint();
		timelineLayersPanel.revalidate();
		
		
	}
	
	// For another day when I have boxes that don't fill the whole timeline, leave it for now...
	/*
	private void refreshLayers() {
		for (Layer layer : drawLayers) {
			ArrayList<LayerBoundingBox> newBoxes = new ArrayList<>();
			ArrayList<LayerBoundingBox> bboxes = layer.getLayerBoundingBoxes();
			int newSpacing = timelineSlider.getUI().getSpacingBetweenTicks();
			for (LayerBoundingBox bbox : bboxes) {
				Rectangle oldBox = bbox.getBox();
				int numFrames= bbox.getNumFrames();
				int newWidth = newSpacing * numFrames;
				Rectangle newBox = new Rectangle(oldBox.x, oldBox.y, newWidth, oldBox.height);
				bbox.setBox(newBox);
			}
			
		}
	}
	*/
	
	public void updateTimelineFromValue(int value) {
		setCurrentTimepoint(value);
	    refreshUI();
	}
	
	public void refreshUI() {
		//refreshLayers();
		refreshDrawPanel();
		refreshTimelineUI();
	}
	
	//-------------------------------------------------------------------------------------------------
	//Copy stuff
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
			refreshUI();
		}
	}	
	
	//-------------------------------------------------------------------------------------------------
	// Layers management
	
	public ArrayList<Layer> deepCopyLayers() {
		ArrayList<Layer> newLayers = new ArrayList<Layer>();
		for (Layer layer : drawLayers) {
			newLayers.add(layer.deepCopy());
		}
		return newLayers;
	}
	
	// CURRENT LAYER
	public ArrayList<Layer> getLayers() {
		return this.drawLayers;
	}
	
	// Return what the session thinks is the currently selected layer (num)
	// Was an Integer (object) but I think it should be an int because there should always be 
	// a current layer (otherwise what do we draw to?)
	public int getCurrentLayerNum () {
		return this.currentLayerNum;
	}
	
	// Update the session's track of what is the currently selected layer (num)
	public void setCurrentLayerNum (Integer num) {
		this.currentLayerNum = num;
	}
	
	private Layer getCurrentLayer () {
		return this.drawLayers.get(this.currentLayerNum);
	}
	
	// ADD LAYERS
	public void addNewLayer() {
		KeyFrames newDrawFrames = new KeyFrames();
		int numLayers = drawLayers.size();
		Layer newLayer = new Layer(numLayers, newDrawFrames);
		
		// The new layer should be selected when created
		for (Layer layer : drawLayers) {
			if(layer.isSelected()) {
				layer.setSelected(false);
			}
		}
		newLayer.setSelected(true);
		this.setCurrentLayerNum(numLayers);
		
		drawLayers.add(newLayer);
		timelineLayersPanel.updateTimelineLayersPanel();
		refreshUI();
	}
	
	
	//DRAWABLE GET/SET FRAMEs
	// Set the frame at a certain time point for the current layer frame
	public void setCurrentLayerFrameAtTime(Integer timePoint, ArrayList<ArrayList<DrawPoint>> drawing) {
		Layer curLayer = getCurrentLayer();
		curLayer.getFrames().put(timePoint, drawing);
	}
	
	// Returns an empty arraylist of draw instructions if there isn't anything currently at this time on
	// the current layer
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
	
	public void addToCurrentLayerFrameAtCurrentTime(ArrayList<DrawPoint> newPoints) {
		ArrayList<ArrayList<DrawPoint>> dps = getCurrentLayerFrameAtCurrentTime();
		dps.add(newPoints);
	}
	
	public void eraseAllLayersAtCurrentFrame() {
		for (Layer layer : drawLayers) {
			KeyFrames frames = layer.getFrames();
			frames.put(getCurrentTimepoint(), new ArrayList<>());
		}
		refreshUI();
	}
	
	//These two basically just create a new composition..., are they really even needed
	//Maybe if we want to save to the old session object, but we'll see
	/*
	public void eraseAllLayerFrames() {
		for (Layer layer : drawLayers) {
			eraseAllFramesFromLayer(layer);
		}
		refreshUI();
	}
	
	//REmove all frames in specific layer
	private void eraseAllFramesFromLayer(Layer layer) {
		layer.setFrames(new KeyFrames());
	}
	*/
	
}
