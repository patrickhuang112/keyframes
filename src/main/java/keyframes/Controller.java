package keyframes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import datatypes.DrawFrame;
import datatypes.DrawPoint;
import datatypes.KeyFrames;
import datatypes.Layer;
import factories.EnumFactory;
import ui.MainView;
import ui.button.Button;
import ui.button.ButtonFactory;
import ui.canvas.KFCanvas;
import ui.canvas.KFCanvasFactory;
import ui.menubar.MenuBar;
import ui.menubar.MenuBarFactory;
import ui.menubar.menu.Menu;
import ui.menubar.menu.MenuFactory;
import ui.menubar.menu.menuitem.MenuItem;
import ui.menubar.menu.menuitem.MenuItemFactory;
import ui.pane.Pane;
import ui.pane.PaneFactory;
import ui.progressbar.ProgressBar;
import ui.progressbar.ProgressBarFactory;
import ui.slider.SliderFactory;
import ui.slider.StandardTimelineSlider;
import ui.slider.TimelineSlider;
import ui.splitpane.SplitPane;
import ui.splitpane.SplitPaneFactory;
import ui.timeline.Timeline;
import ui.timeline.TimelineFactory;
import ui.timeline.layerspanel.TimelineLayersPanel;
import ui.timeline.layerspanel.TimelineLayersPanelFactory;
import ui.toolbar.ToolBar;
import ui.toolbar.ToolBarFactory;

public class Controller {

	private static Controller instance;
	
	private Session session;
	private JFrame frame;
	private MainView mainView = null;
	
	private SplitPane timelineCanvasSplitPane;
	private TimelineSlider timelineSlider;
	private TimelineLayersPanel timelineLayersPanel;
	
	private KFCanvas canvas;
	private Timeline timeline;
	
	//Top MainView
	private Pane mainViewToolBarAndProgressBarContainer;
	private Pane mainViewTopContainer;
	private ProgressBar progressBar;
	
	//ToolBar
	private ToolBar toolBar;
	private Button brushButton;
	private Button eraseButton;
	private Button eraseAllButton;
	private Button playButton;
	private Button pauseButton;
	private Button colorPickerButton;
	private Button fillButton;
	
	//Menubar
	private MenuBar menuBar;
	private Menu fileMenu;
	private Menu editMenu;
	private MenuItem newProjectMenuItem;
	private MenuItem openProjectMenuItem;
	private MenuItem saveProjectMenuItem;
	private MenuItem saveProjectAsMenuItem;
	private MenuItem renderMP4MenuItem;
	private MenuItem renderGIFMenuItem;
	private MenuItem settingsMenuItem;
	
	private MenuItem editFPSMenuItem;
	private MenuItem editCompLengthMenuItem;
	private MenuItem editBackgroundColorMenuItem;
	
	
	
	
	private Controller () {
		mainView = MainView.getInstance();
		mainViewTopContainer = PaneFactory.createEmptyPane();
		initializeUI();
		buildUI();
	}
	

	public static Controller getController() {
		if (Controller.instance == null) {
			Controller.instance = new Controller();
		}
		return Controller.instance;
	}
	
	//----------------------------------------------------------------------------------------------------
	//Build UI Elements put its children components together to make it complete
	
	private void buildUI() {
		buildTimeline();
		buildTopContainer();
		timelineCanvasSplitPane.addTopLeftComponent(canvas);
		timelineCanvasSplitPane.addTopLeftComponent(timeline);
		mainView.add(timelineCanvasSplitPane.getSwingComponent());
		mainView.add(mainViewTopContainer.getSwingComponent(), BorderLayout.PAGE_START);
		frame.add(mainView, BorderLayout.CENTER);
	}
	
	private void buildTimeline() {
		timeline.getSwingComponent().add(timelineSlider.getSwingComponent(), BorderLayout.NORTH);
		timeline.getSwingComponent().add(timelineLayersPanel.getSwingComponent());
	}
	
	private void buildTopContainer() {
		buildMenuBar();
		buildToolBarAndProgressBarContainer();
		mainViewTopContainer.getSwingComponent().add(menuBar.getSwingComponent(), BorderLayout.PAGE_START);
		mainViewTopContainer.getSwingComponent().add(mainViewToolBarAndProgressBarContainer.getSwingComponent(),
																				BorderLayout.PAGE_END);
	}
	
	private void buildToolBarAndProgressBarContainer() {
		buildToolBar();
		mainViewToolBarAndProgressBarContainer.getSwingComponent().add(toolBar.getSwingComponent());
		mainViewToolBarAndProgressBarContainer.getSwingComponent().add(Box.createHorizontalGlue());
		mainViewToolBarAndProgressBarContainer.getSwingComponent().add(progressBar.getSwingComponent());
	}
	
	private void buildToolBar() {
		toolBar.getSwingComponent().add(brushButton.getSwingComponent());
		toolBar.getSwingComponent().add(eraseButton.getSwingComponent());
		toolBar.getSwingComponent().add(eraseAllButton.getSwingComponent());
		toolBar.getSwingComponent().add(playButton.getSwingComponent());
		toolBar.getSwingComponent().add(pauseButton.getSwingComponent());
		toolBar.getSwingComponent().add(colorPickerButton.getSwingComponent());
		toolBar.getSwingComponent().add(fillButton.getSwingComponent());
	}
	
	private void buildMenuBar() {
		buildFileMenu();
		buildEditMenu();
		menuBar.getSwingComponent().add(fileMenu.getSwingComponent());
		menuBar.getSwingComponent().add(editMenu.getSwingComponent());
	}
	
	private void buildFileMenu() {
		fileMenu.getSwingComponent().add(newProjectMenuItem.getSwingComponent());
		fileMenu.getSwingComponent().add(openProjectMenuItem.getSwingComponent());
		fileMenu.getSwingComponent().add(saveProjectMenuItem.getSwingComponent());
		fileMenu.getSwingComponent().add(saveProjectAsMenuItem.getSwingComponent());
		fileMenu.getSwingComponent().add(renderMP4MenuItem.getSwingComponent());
		fileMenu.getSwingComponent().add(renderGIFMenuItem.getSwingComponent());
		fileMenu.getSwingComponent().add(settingsMenuItem.getSwingComponent());
	}
	
	private void buildEditMenu() {
		editMenu.getSwingComponent().add(editFPSMenuItem.getSwingComponent());
		editMenu.getSwingComponent().add(editCompLengthMenuItem.getSwingComponent());
		editMenu.getSwingComponent().add(editBackgroundColorMenuItem.getSwingComponent());
	}
	
	//----------------------------------------------------------------------------------------------------
	//Initialize UI Elements (just create them)
	
	private void initializeUI() {
		initializeFrame();
		initializeMainView();
		initializeTimelineUIComponents();
		initializeCanvasUIComponents();
		initializeOtherMainViewUIComponents();
	}
	
	private void initializeFrame() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Keyframes");
		frame.setVisible(true);
		frame.setMinimumSize(new Dimension(MagicValues.windowMinimumWidth, MagicValues.windowMinimumHeight));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	private void initializeMainView() {
		mainView = MainView.getInstance();
	}
	
	private void initializeOtherMainViewUIComponents() {
		timelineCanvasSplitPane = SplitPaneFactory.createHorizontalSplitPane();
		mainViewTopContainer = PaneFactory.createMainViewTopContainer();
		mainViewToolBarAndProgressBarContainer = PaneFactory.createMainViewToolBarAndProgressBarContainer();
		progressBar = ProgressBarFactory.createRenderingProgressBar();
		initializeToolBarUIComponents();
		initializeMenuBarUIComponents();
	}
	
	private void initializeToolBarUIComponents() {
		toolBar = ToolBarFactory.createMainViewTopToolBar();
		try {
			brushButton = ButtonFactory.createBrushButton();
			eraseButton = ButtonFactory.createEraseButton();
			eraseAllButton = ButtonFactory.createEraseAllButton();
			playButton = ButtonFactory.createPlayButton();
			pauseButton = ButtonFactory.createPauseButton();
			colorPickerButton = ButtonFactory.createColorPickerButton();
			fillButton = ButtonFactory.createFillButton();
		} catch(Exception e) {
			System.out.println("Button icon creation failed");
		}
	}
	
	private void initializeMenuBarUIComponents() {
		menuBar = MenuBarFactory.createMainViewTopMenuBar();
		
		fileMenu = MenuFactory.createFileMenu();
		editMenu = MenuFactory.createEditMenu();
		
		newProjectMenuItem = MenuItemFactory.createNewProjectMenuItem();
		openProjectMenuItem = MenuItemFactory.createOpenProjectMenuItem();
		saveProjectMenuItem = MenuItemFactory.createSaveMenuItem();
		saveProjectAsMenuItem = MenuItemFactory.createSaveAsMenuItem();
		renderMP4MenuItem = MenuItemFactory.createRenderMP4MenuItem();
		renderGIFMenuItem = MenuItemFactory.createRenderGIFMenuItem();
		settingsMenuItem = MenuItemFactory.createSettingsMenuItem();
		
		editFPSMenuItem = MenuItemFactory.createEditFPSMenuItem();
		editCompLengthMenuItem = MenuItemFactory.createEditCompositionLengthMenuItem();
		editBackgroundColorMenuItem = MenuItemFactory.createEditBackgroundColorMenuItem();
	}
	
	private void initializeCanvasUIComponents() {
		canvas = KFCanvasFactory.createStandardKFCanvas();
	}
	
	private void initializeTimelineUIComponents() {
		timeline = TimelineFactory.createStandardTimeline();
		timelineSlider = SliderFactory.createStandardTimelineSlider(session.getShortestTimepoint(), 
																	session.getLongestTimepoint(), 
																	session.getCurrentTimepoint());
		timelineLayersPanel = TimelineLayersPanelFactory.createStandardTimelineLayersPanel();
	}
	
	private void newSessionFromSettings() {
		session = Session.createSessionFromSettings(Utils.getSettings());
	}
	
	private void newSessionFromSessionSave(SessionSave ss) {
		session = Session.createSessionFromSessionSave(ss);
	}
	
	
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
		session.setDrawablePanelBackgroundColor(color);
	}
	
	public Color getDrawablePanelBackgroundColor () {
		return session.getDrawablePanelBackgroundColor();
	}
	
	public Color getBrushColor() {
		return session.getBrushColor();
	}
	
	public void setBrushColor(Color color) {
		session.setBrushColor(color);
	}
	
	public Color getEraserColor() {
		return session.getEraserColor();
	}
	
	public void setEraserColor(Color color) {
		session.setEraserColor(color);
	}
	
	//DRAWABLE PAINT SETTING
	public void setPaintSetting(EnumFactory.PaintSetting paintSetting) {
		session.setPaintSetting(paintSetting);
	}
	
	public EnumFactory.PaintSetting getPaintSetting() {
		return session.getPaintSetting();
	}
	
	
	public int getBrushSize() {
		return session.getBrushSize();
	}
	
	public void setBrushSize(int brushSize) {
		session.setBrushSize(brushSize);
	}
	
	public int getEraserSize() {
		return session.getEraserSize();
	}
	
	public void setEraserSize(int eraserSize) {
		session.setEraserSize(eraserSize);
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
		return session.getShortestTimepoint();
	}
	
	public int getLongestTimepoint() {
		return session.getLongestTimepoint();
	}
	
	//TIMELINE CURRENT TIME
	public void setCurrentTimepoint(int time) {
		session.setCurrentTimepoint(time);
	}
	
	public int getCurrentTimepoint() {
		return session.getCurrentTimepoint();
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
	// Utils
	public void newProject() {
		Utils.newFile(session, frame);
	}
	public void openProject() {
		Utils.openFile(session, frame);
	}
	public void saveProject() {
		Utils.saveFile(session, frame);
	}
	public void saveAsProject() {
		Utils.saveAsFile(session, frame);
	}
	public void renderMP4() {
		Utils.renderMP4(session, frame, session.getFramesPerSecond());
	}
	public void renderGIF() {
		Utils.renderGIF(session, frame, session.getFramesPerSecond());
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
	
}
