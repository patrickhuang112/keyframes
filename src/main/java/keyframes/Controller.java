package keyframes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	
	
	private DrawFrame clipboardFrames = null;
	
	private Controller () {
		//Creates mode first
		newSessionFromSettings();
	}
	
	//Initialize UI
	public void initialize() {
		initializeUI();
		buildUI();
	}
	
	public static void createController() {
		if (Controller.instance == null) {
			Controller.instance = new Controller();
		}
	}
	
	public static Controller getController() {
		return Controller.instance;
	}
	
	//----------------------------------------------------------------------------------------------------
	//Build UI Elements put its children components together to make it complete
	
	private void buildUI() {
		buildTimeline();
		buildTopContainer();
		timelineCanvasSplitPane.addTopLeftComponent(canvas);
		timelineCanvasSplitPane.addBottomRightComponent(timeline);
		mainView.add(timelineCanvasSplitPane.getSwingComponent());
		mainView.add(mainViewTopContainer.getSwingComponent(), BorderLayout.PAGE_START);
		frame.add(mainView, BorderLayout.CENTER);
		frame.revalidate();
		frame.repaint();
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
																	session.getCurrentTimepoint(),
																	session.getLongestTimeInSeconds(),
																	session.getFramesPerSecond());
		timelineLayersPanel = TimelineLayersPanelFactory.createStandardTimelineLayersPanel();
	}
	
	public void newSessionFromSettings() {
		session = Session.createSessionFromSettings(Utils.getSettings());
	}
	
	public void newSessionFromSessionSave(SessionSave ss) {
		session = Session.createSessionFromSessionSave(ss);
	}
	
	public SessionSave createCurrentSessionSave() {
		return new SessionSave(session);
	}
	
	public String getSavePath() {
		return session.getSavePath();
	}
	
	public void setSavePath(String path) {
		session.setSavePath(path);
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
			ArrayList<Layer> drawLayers = getLayers();
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
	}
	
	public Color getDrawablePanelBackgroundColor () {
		return canvas.getBackgroundColor();
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
	//Composition settings (FPS, timepoints)
	
	public int getShortestTimepoint() {
		return session.getShortestTimepoint();
	}
	
	public int getLongestTimepoint() {
		return session.getLongestTimepoint();
	}
	
	//TIMELINE CURRENT TIME
	public void setCurrentTimepoint(int time) {
		//Model
		boolean successful = session.setCurrentTimepoint(time); 
		if (successful) {
			//View
			timelineSlider.updateSlider(time);
		}
	}
	
	public int getCurrentTimepoint() {
		return session.getCurrentTimepoint();
	}
	
	// Set the longest timepoint for the composition (from the slider)
	public void setLongestTimeInSeconds(int time) {
		//Model
		boolean successful = session.setLongestTimeInSeconds(time);
		if (successful) {
			//View
			updateTimelineUIFromChangeInFPSOrTime(session.getFramesPerSecond());
			refreshUI();
		}
	}
	
	private void updateProgressBarEndpointFromChangeInFPSOrTime() {
		progressBar.setProgressBarEndpoint(session.getLongestTimepoint());
	}
	
	//FPS
	private void updateTimelineUIFromChangeInFPSOrTime(int newFPS) {
		timelineSlider.updateSliderUIFromFPSOrLengthChange(newFPS, session.getLongestTimeInSeconds());
		updateProgressBarEndpointFromChangeInFPSOrTime();
	}
	
	public int getLongestTimeInSeconds() {
		return session.getLongestTimeInSeconds();
	}
	
	public void setFramesPerSecond(int fps) {
		//Model
		boolean successful = session.setFramesPerSecond(fps);
		if (successful) {
			//View
			updateTimelineUIFromChangeInFPSOrTime(session.getFramesPerSecond());
			refreshUI();
		}
	}
	
	public int getFramesPerSecond() {
		
		return session.getFramesPerSecond();
	}
	
	
	//-------------------------------------------------------------------------------------------------
	//Movie preview stuff
	
	
	//Space bar
	public void playOrPauseMovie() {
		if (session.isPlaying()) {
			pauseMovie();
		} else {
			playMovie();
		}
	}
	
	//Play button
	public void playMovie() {
		session.setPlaying(true);
		SwingWorker sw = new SwingWorker() {
			@Override
			protected Object doInBackground() throws Exception {
				while(session.isPlaying()) {
					playOneFrame();
					// Sleep the thread by a frame
					long increment = 1000 / getFramesPerSecond();
					Thread.sleep(increment);
					System.out.println("Playing movie...");
				}
				return null;
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
		session.setPlaying(false);
	}
	
	private void playOneFrame() {
		session.incrementTimepoint();
		refreshUI();
	}
	
	//-------------------------------------------------------------------------------------------------
	//Refresh and update UI components from session

	public void updateTimelineFromMouseClick(MouseEvent e) {
		int value = timelineSlider.valueAtX(e.getX());
		setCurrentTimepoint(value);
	    refreshUI();
	    timelineSlider.getSwingComponent().requestFocus();
	}
	
	private void refreshUI() {
		refreshCanvas();
		refreshTimelineUI();
	}
	
	// REFRESH and REDRAW DRAW PANEL (usually when we update our drawpanel in someway)
	private void refreshCanvas(){
		canvas.refresh();
	}
	
	private void refreshTimelineUI() {
		// UPDATES the line on the layers panel
		double sliderBarx = timelineSlider.getThumbMidX();
		timelineLayersPanel.updateLayersPanelUI(sliderBarx);
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
		ArrayList<Layer> drawLayers = session.getLayers();
		for (Layer layer : drawLayers) {
			newLayers.add(layer.deepCopy());
		}
		return newLayers;
	}
	
	// CURRENT LAYER
	public ArrayList<Layer> getLayers() {
		return session.getLayers();
	}
	
	// Return what the session thinks is the currently selected layer (num)
	// Was an Integer (object) but I think it should be an int because there should always be 
	// a current layer (otherwise what do we draw to?)
	public int getCurrentLayerNum () {
		return session.getCurrentLayerNum();
	}
	
	// Update the session's track of what is the currently selected layer (num)
	public void setCurrentLayerNum (Integer num) {
		session.setCurrentLayerNum(num);
	}
	
	// ADD LAYERS
	public void addNewLayer() {
		//Model
		session.addNewLayer();
		//View
		refreshTimelineUI();
	}
	
	// Returns an empty arraylist of draw instructions if there isn't anything currently at this time on
	// the current layer
	public DrawFrame getLayerFrameAtTime(Integer layerNum, Integer timePoint) {
		return session.getLayerFrameAtTime(layerNum, timePoint);
	}
	
	public DrawFrame getCurrentLayerFrameAtTime(Integer timePoint) {
		return session.getCurrentLayerFrameAtTime(timePoint);
	}
	
	public DrawFrame getSpecifiedLayerFrameAtCurrentTime(Integer layerNum) {
		return session.getSpecifiedLayerFrameAtCurrentTime(layerNum);
	}
	
	public void setSpecifiedLayerFrameAtCurrentTime(DrawFrame drawing, int layerNum) {
		session.setSpecifiedLayerFrameAtCurrentTime(drawing, layerNum);
	}
	
	public void setCurrentLayerFrameAtCurrentTime(DrawFrame drawing) {
		session.setCurrentLayerFrameAtCurrentTime(drawing);
	}
	
	
	public void addToCurrentLayerFrameAtCurrentTime(ArrayList<DrawPoint> newPoints) {
		session.addToCurrentLayerFrameAtCurrentTime(newPoints);
	}
	
	public void eraseAllLayersAtCurrentFrame() {
		session.eraseAllLayersAtCurrentFrame();
		refreshUI();
	}
	
	//-------------------------------------------------------------------------------------------------
	// Utils
	public void newProject() {
		Utils.newFile(frame);
	}
	public void openProject() {
		Utils.openFile(frame);
	}
	public void saveProject() {
		Utils.saveFile(frame);
	}
	public void saveAsProject() {
		Utils.saveAsFile(frame);
	}
	public void renderMP4() {
		Utils.renderMP4(frame, session.getFramesPerSecond());
	}
	public void renderGIF() {
		Utils.renderGIF(frame, session.getFramesPerSecond());
	}
	
	//-------------------------------------------------------------------------------------------------
	// Other stuff category
	
	public int getCanvasWidth() {
		return canvas.getCanvasWidth();
	}
	
	public int getCanvasHeight() {
		return canvas.getCanvasHeight();
	}
	
	// This is called whenever the draw panel is resized or when it is first created
	public void updateDrawFrameDimensions(int w, int h) {
		for (Layer layer : getLayers()) {
			KeyFrames kf = layer.getFrames();
			for (Integer t : kf.keySet()) {
				DrawFrame df = kf.get(t);
				DrawFrame newDf = DrawFrame.createResizedCopy(df, w, h);
				kf.put(t, newDf);
			}
		}

	}
	
}
