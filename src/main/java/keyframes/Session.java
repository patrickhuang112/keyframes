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
import datatypes.KeyFrames;
import datatypes.Layer;
import datatypes.LayerBoundingBox;
import ui.progressbar.ProgressBar;
import ui.slider.StandardTimelineSlider;
import ui.slider.StandardTimelineSliderUI;
import ui.slider.TimelineSlider;
import ui.timeline.TimelineLayersPanel;
import factories.EnumFactory;
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
	public int minTimepoint = 0;

	
	static Session createSessionFromSettings(Settings settings) {
		return new Session(settings);
	}
	
	static Session createSessionFromSessionSave(SessionSave ss) {
		return new Session(ss);
	}
	
	private Session(SessionSave ss) {
		this();
		this.drawLayers = ss.drawLayers;
		this.framesPerSecond = ss.framesPerSecond;
		this.longestTimeInSeconds = ss.longestTimeInSeconds;
		this.longestTimepoint = ss.longestTimepoint;
		this.spaceBetweenTicks = ss.spaceBetweenTicks;
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
		// Initialize new HashMap of times and frames for default layer
		KeyFrames drawFrames = 
			new KeyFrames();
		
		int dw = MagicValues.drawablePanelDefaultPreferredWidth;
		int dh = MagicValues.drawablePanelDefaultPreferredHeight;
		
		drawFrames.put(0, new DrawFrame(dw, dh));
		
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
	private ProgressBar progressBar = null;
	private KFCanvas canvas = null;
	private StandardTimelineLayersPanel timelineLayersPanel = null;
	private TimelineSlider timelineSlider = null;
	
	private Color brushColor = Color.red;
	private Color eraserColor;
	private int brushSize;
	private int eraserSize;
	private EnumFactory.PaintSetting paintSetting = EnumFactory.PaintSetting.DRAW;
	// This contains layer info, and time info
	private ArrayList<Layer> drawLayers = new ArrayList<Layer>();
	
	
	//REPLACE BY SETTINGS
	private int currentTimepoint = 0;
	
	private int longestTimeInSeconds;
	private int framesPerSecond;
	//Dependent on frames persecond and time in seconds, updated when we set the timeline slider
	private Integer spaceBetweenTicks = null;
	
	
	private int longestTimepoint = longestTimeInSeconds * framesPerSecond;
	// Current frames copied to clipboard
	// SHould be an ArrayList of array list of draw points
	
	//WHY IS IT LIKE THIS INSTEAD OF JUST A SINGLE LAYER? 
	//
	private DrawFrame clipboardFrames = null;
	
	
	//-------------------------------------------------------------------------------------------------
	// File saving stuff
	public String getSavePath() {
		return savePath;
	}
	
	public void setSavePath(String path) {
		savePath = path;
	}
	
	
	// With Rendering stuff
	// We used to keep the hashmap as an actual variable in session, but its not serializable...
	// So we will just create it when we want to render so we can actually still serializable the stuff
	// with local saves
	
	//Now that I serialize and save SessionSave instead of save, I can maybe revert this change...
	//We'll see...
	public HashMap<Integer, BufferedImage> getImages() {
		HashMap<Integer, BufferedImage> drawImages = new HashMap<Integer, BufferedImage>();
		
		// Start from the bottom layer, thats the first one we want to draw, and draw higher layers
		// on top of bottom layers
		
		int drawWidth = canvas.getCanvasWidth();
		int drawHeight = canvas.getCanvasHeight();
		Color backgroundColor = getDrawablePanelBackgroundColor();
		
		for(int t = 0; t <= getLongestTimepoint(); t++) {
			BufferedImage img = new BufferedImage(drawWidth, drawHeight, 
					BufferedImage.TYPE_INT_RGB);
			Graphics2D gr = img.createGraphics();
			gr.setPaint(backgroundColor);
			gr.fillRect(0,0, drawWidth, drawHeight);
			for(int i = drawLayers.size() - 1; i >= 0; i--) {
				Layer layer = drawLayers.get(i);
				DrawFrame df = layer.getPointCollectionAtTime(t);
				gr.drawImage(df, 0, 0, null);
			}
			drawImages.put(t, img);
		}
		
		return drawImages;
	}
	
	//-------------------------------------------------------------------------------------------------
	//Draw settings
	
	public void setDrawablePanelBackgroundColor (Color color) {
		canvas.setBackgroundColor(color);
		refreshUI();
	}
	
	public Color getDrawablePanelBackgroundColor () {
		return canvas.getBackgroundColor();
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
	
	public void updateProgressBar(int progress) {
		progressBar.updateProgressBar(progress);
	}
	
	public void setProgressBarVisible(boolean visible) {
		progressBar.setVisibility(visible);
	}
	
	public void resetProgressBar() {
		progressBar.resetProgressBar();
	}
	
	//-------------------------------------------------------------------------------------------------
	// INITIALIZE UI Components in Session
	// These should only be called at the beginning
	
	// This should only be called once when we set up everything, so we can update the 
	// spacing between ticks variable for the first time here as well
	public void setTimelineSlider(TimelineSlider ts) {
		this.timelineSlider = ts;
		this.spaceBetweenTicks = timelineSlider.getSpacingBetweenTicks();
	}
	
	public void setCanvas(KFCanvas dp) {
		this.canvas = dp;
	}
	
	public void setLayersPanel(StandardTimelineLayersPanel pane) {
		this.timelineLayersPanel = pane;
	}
	
	public void setProgressBar(ProgressBar pb) {
		this.progressBar = pb;
	}
	
	//-------------------------------------------------------------------------------------------------
	//Composition settings (FPS, timepoints)
	
	public int getShortestTimepoint() {
		return this.minTimepoint;
	}
	
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
		timelineSlider.updateSlider(time);
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
				refreshProgressBarEndpoint();
				refreshUI();
			}
		}
		
		public int getLongestTimeInSeconds() {
			return this.longestTimeInSeconds;
		}
		
		private void refreshProgressBarEndpoint() {
			this.progressBar.setProgressBarEndpoint(longestTimepoint);
		}
		
		//FPS
		private void updateTimeline(int oldFps, int newFps) {
			/*
			int newTimepoint = calculateNewTimelinePointerPositionFromOldFps(oldFps, newFps, currentTimepoint);
			timelineSlider.setMaximum(longestTimeInSeconds * newFps);
			timelineSlider.setMajorTickSpacing(newFps);
			timelineSlider.setMinorTickSpacing(1);
			Hashtable<Integer, JLabel> timelineLabelsDict = timelineSlider.getUI().getTimelineLabelsDict();
			timelineLabelsDict.clear();
			//
			timelineLabelsDict = new Hashtable<Integer, JLabel>();
			
			for(Integer i = 0; i < longestTimeInSeconds; i++) {
				timelineLabelsDict.put(i * newFps, new JLabel(i.toString()));
			}
			//ADD THE ENDPOINT LABEL
			timelineLabelsDict.put(longestTimeInSeconds * newFps, 
					new JLabel(((Integer)longestTimeInSeconds).toString()));
			timelineSlider.setLabelTable(timelineLabelsDict);

			setCurrentTimepoint(newTimepoint);
			refreshUI();
			*/
		}
		
		public void setFramesPerSecond(int fps) {
			if(fps >= fpsMin && fps <= fpsMax) {
				int oldFps = framesPerSecond;
				int newFps = fps;
				
				updateTimeline(oldFps, newFps);
				updateAllLayerFramesFromFPS(oldFps, newFps);
				
				framesPerSecond = fps;
				longestTimepoint = fps * longestTimeInSeconds;
				
				refreshProgressBarEndpoint();
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
		canvas.refresh();
	}
	
	private void refreshTimelineUI() {
		// UPDATES the line on the layers panel
		double sliderBarx = timelineSlider.getThumbMidX();
	    timelineLayersPanel.setSliderBarx(sliderBarx);
	    
	    //Updates the layers order on the layers panel
	    timelineLayersPanel.updateTimelineLayersPanelLayerNumbers();
	    
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
	
	public void copyFramesFromSpecifiedLayerAndCurrentTime(int layerNum) {
		clipboardFrames = getSpecifiedLayerFrameAtCurrentTime(layerNum);
	}
	
	public void pasteFramesToSpecifiedLayerAndCurrentTime(int layerNum) {
		if (clipboardFrames != null) {
			setSpecifiedLayerFrameAtCurrentTime(clipboardFrames, layerNum);
			refreshUI();
		}
	}
	
	public void copyFramesFromCurrentLayerAndCurrentTime() {
		copyFramesFromSpecifiedLayerAndCurrentTime(getCurrentLayerNum());
	}
	
	public void pasteFramesToCurrentLayerAndCurrentTime() {
		pasteFramesToSpecifiedLayerAndCurrentTime(getCurrentLayerNum());
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
	
	private Layer getCurrentLayer() {
		return this.drawLayers.get(this.currentLayerNum);
	}
	
	private Layer getSpecifiedLayer(int layerNum) {
		return this.drawLayers.get(layerNum);
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
		timelineLayersPanel.updateTimelineLayersPanelLayerNumbers();
		refreshUI();
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
		//DrawablePanel.drawAndErasePath(g2d, newPoints);
		//dps.refreshPixelArray();
	}
	
	public void eraseAllLayersAtCurrentFrame() {
		int dw = MagicValues.drawablePanelDefaultPreferredWidth;
		int dh = MagicValues.drawablePanelDefaultPreferredHeight;
		for (Layer layer : drawLayers) {
			KeyFrames frames = layer.getFrames();
			frames.put(getCurrentTimepoint(), new DrawFrame(dw, dh));
		}
		refreshUI();
	}
	
	//-------------------------------------------------------------------------------------------------
	// Other stuff category
	
	public Integer getSpacingBetweenTicks() {
		return spaceBetweenTicks;
	}
	
	public int getDrawablePaneWidth() {
		return canvas.getCanvasWidth();
	}
	
	public int getDrawablePaneHeight() {
		return canvas.getCanvasHeight();
	}
	
	// This is called whenever the draw panel is resized or when it is first created
	public void updateDrawFrameDimensions() {
		for (Layer layer : getLayers()) {
			KeyFrames kf = layer.getFrames();
			for (Integer t : kf.keySet()) {
				DrawFrame df = kf.get(t);
				int newWidth = getDrawablePaneWidth();
				int newHeight = getDrawablePaneHeight();
				DrawFrame newDf = DrawFrame.createResizedCopy(df, newWidth, newHeight);
				kf.put(t, newDf);
			}
		}

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
