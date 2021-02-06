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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import commands.Command;
import datatypes.DrawFrame;
import datatypes.DrawPoint;
import datatypes.Enums;
import datatypes.KeyFrames;
import datatypes.Layer;
import ui.MainView;
import ui.button.KFButton;
import ui.button.KFButtonFactory;
import ui.canvas.KFCanvas;
import ui.canvas.KFCanvasFactory;
import ui.menubar.KFMenuBar;
import ui.menubar.KFMenuBarFactory;
import ui.menubar.menu.KFMenu;
import ui.menubar.menu.KFMenuFactory;
import ui.menubar.menu.menuitem.KFMenuItem;
import ui.menubar.menu.menuitem.KFMenuItemFactory;
import ui.pane.KFPane;
import ui.pane.KFPaneFactory;
import ui.pane.timeline.layerspanel.RectanglesKFLayerPane;
import ui.pane.timeline.layerspanel.RectanglesKFLayerPaneFactory;
import ui.pane.timeline.namespanel.NamesKFLayerPane;
import ui.pane.timeline.namespanel.NamesKFLayerPaneFactory;
import ui.progressbar.KFProgressBar;
import ui.progressbar.KFProgressBarFactory;
import ui.scroll.KFScrollPane;
import ui.scroll.KFScrollPaneFactory;
import ui.slider.KFSliderFactory;
import ui.slider.StandardKFTimelineSlider;
import ui.slider.KFTimelineSlider;
import ui.splitpane.KFSplitPane;
import ui.splitpane.KFSplitPaneFactory;
import ui.timeline.KFTimeline;
import ui.timeline.KFTimelineFactory;
import ui.toolbar.KFToolBar;
import ui.toolbar.KFToolBarFactory;

public class Controller {

	private static Controller instance;
	
	private Session session;
	private JFrame frame;
	private MainView mainView = null;
	
	private KFSplitPane timelineCanvasSplitPane;
	private KFCanvas canvas;
	
	//Timeline
	private KFTimeline timeline;
	private KFScrollPane rectanglesScrollPane;
	private KFScrollPane namesScrollPane;
	private KFSplitPane rectanglesNamesSplitPane;
	private KFTimelineSlider timelineSlider;
	private KFPane timelineSliderAndRectanglesContainerPanel;
	private KFPane namesAndAdditionalInfoContainerPanel;
	private KFPane additionalInfoPanel;
	private RectanglesKFLayerPane timelineLayersPanel;
	private NamesKFLayerPane timelineNamesPanel;
	
	//Top MainView
	private KFPane mainViewToolBarAndProgressBarContainer;
	private KFPane mainViewTopContainer;
	private KFProgressBar progressBar;
	
	//ToolBar
	private KFToolBar toolBar;
	private KFButton brushButton;
	private KFButton eraseButton;
	private KFButton eraseAllButton;
	private KFButton playButton;
	private KFButton pauseButton;
	private KFButton colorPickerButton;
	private KFButton fillButton;
	
	//Menubar
	private KFMenuBar menuBar;
	private KFMenu fileMenu;
	private KFMenu editMenu;
	private KFMenuItem newProjectMenuItem;
	private KFMenuItem openProjectMenuItem;
	private KFMenuItem saveProjectMenuItem;
	private KFMenuItem saveProjectAsMenuItem;
	private KFMenuItem renderMP4MenuItem;
	private KFMenuItem renderGIFMenuItem;
	private KFMenuItem settingsMenuItem;
	
	private KFMenuItem editFPSMenuItem;
	private KFMenuItem editCompLengthMenuItem;
	private KFMenuItem editBackgroundColorMenuItem;
	
	
	private int defaultTimeBeforeProgressBarDisappearsAfterRender = 3000;
	private int defaultTimeBeforeProgressBarDisappearsAfterOpenAndSave = 3000;
		
	
	
	private ArrayList<Command> commandStack;
	
	//The command pointer will be on the command where we will undo, not the one to redo
	private int currentCommand;
	private int currentState;
	
	// Example relation between state and command:
	// C1 -- C2 -- C3
	//             ^
	// S0 -- S1 -- S2 -- S3
	//  				 ^
	// This means that we are at the latest possible state
	// If we are at Command C3, execute will bring us to state S3 and unexecute will bring us to state S2
	
	// the 
	
	private DrawFrame clipboardFrames = null;
	
	private Controller () {
		//Creates mode first
		newSessionFromSettings();
	}
	
	//Initialize UI
	public void initialize() {
		initializeCommandStack();
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
	//Command Stack
	
	private void initializeCommandStack() {
		commandStack = new ArrayList<>();
		currentCommand = -1;
		currentState = 0;
	}
	
	public void addAndExecuteCommand(Command c) {
		if (currentState != commandStack.size()) {
			deleteCommandsFromEndToIndex(currentCommand);
		}
		
		commandStack.add(c);
		c.execute();
		currentCommand++;
		currentState++;
		refreshUI();
	}
	
	public void undoCommand() {
		if (currentState > 0) {
			commandStack.get(currentCommand).unExecute();			
			currentCommand--;
			currentState--;
			refreshUI();
		}
	}
	
	public void redoCommand() {
		if (currentState < commandStack.size()) {
			currentCommand++;
			currentState++;
			commandStack.get(currentCommand).execute();
			refreshUI();
		}
		
		// WE have to update the pointer first
		// This is because the only time a redo is possible is when we have undo at least once
		// If we have undoed at least once, that means our pointer is pointing to the command for the next undo, 
		// which means if we want to go back to the more future state, we have to move the pointer up and THEN
		// execute that command, because just executing the command we are currently pointing at would just get us 
		// to the same state we are currently at
	}
	
	private void deleteCommandsFromEndToIndex(int ind) {
		int initialStackSize = commandStack.size();
		for (int i = initialStackSize - 1; i > ind; i--) {
			commandStack.remove(i);
		}
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
		buildNamesAndAdditionalInfo();
		buildTimelineSliderAndRectangles();
		rectanglesNamesSplitPane.addTopLeftComponent(namesAndAdditionalInfoContainerPanel);
		rectanglesNamesSplitPane.addBottomRightComponent(timelineSliderAndRectanglesContainerPanel);
		connectNamesScrollPaneAndRectanglesScrollPane();
		timeline.getSwingComponent().add(rectanglesNamesSplitPane.getSwingComponent());
	}
	
	private void buildNamesAndAdditionalInfo() {
		buildNamesScrollPane();
		timelineNamesPanel.refresh();
		namesAndAdditionalInfoContainerPanel.getSwingComponent().add(
				additionalInfoPanel.getSwingComponent());
		namesAndAdditionalInfoContainerPanel.getSwingComponent().add(namesScrollPane.getSwingComponent());
	}
	
	private void buildNamesScrollPane() {
		namesScrollPane.addScrollComponent(timelineNamesPanel);
	}
	
	private void buildTimelineSliderAndRectangles() {
		buildRectanglesScrollPane();
		timelineLayersPanel.refresh();
		timelineSliderAndRectanglesContainerPanel.getSwingComponent().add(
				timelineSlider.getSwingComponent());
		timelineSliderAndRectanglesContainerPanel.getSwingComponent().add(rectanglesScrollPane.getSwingComponent());
	}
	
	private void buildRectanglesScrollPane() {
		rectanglesScrollPane.addScrollComponent(timelineLayersPanel);
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
		int framemw = 1600;
		int framemh = 900;
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Keyframes");
		frame.setVisible(true);
		frame.setMinimumSize(new Dimension(framemw, framemh));
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	private void initializeMainView() {
		mainView = MainView.getInstance();
	}
	
	private void initializeOtherMainViewUIComponents() {
		timelineCanvasSplitPane = KFSplitPaneFactory.createVerticalSplitPane();
		mainViewTopContainer = KFPaneFactory.createMainViewTopContainer();
		mainViewToolBarAndProgressBarContainer = KFPaneFactory.createMainViewToolBarAndProgressBarContainer();
		progressBar = KFProgressBarFactory.createMainViewProgressBar(session.getShortestTimepoint(),
																	session.getLongestTimepoint());
		initializeToolBarUIComponents();
		initializeMenuBarUIComponents();
	}
	
	private void initializeToolBarUIComponents() {
		toolBar = KFToolBarFactory.createMainViewTopToolBar();
		try {
			brushButton = KFButtonFactory.createBrushButton();
			eraseButton = KFButtonFactory.createEraseButton();
			eraseAllButton = KFButtonFactory.createEraseAllButton();
			playButton = KFButtonFactory.createPlayButton();
			pauseButton = KFButtonFactory.createPauseButton();
			colorPickerButton = KFButtonFactory.createColorPickerButton();
			fillButton = KFButtonFactory.createFillButton();
		} catch(Exception e) {
			System.out.println("Button icon creation failed");
		}
	}
	
	private void initializeMenuBarUIComponents() {
		menuBar = KFMenuBarFactory.createMainViewTopMenuBar();
		
		fileMenu = KFMenuFactory.createFileMenu();
		editMenu = KFMenuFactory.createEditMenu();
		
		newProjectMenuItem = KFMenuItemFactory.createNewProjectMenuItem();
		openProjectMenuItem = KFMenuItemFactory.createOpenProjectMenuItem();
		saveProjectMenuItem = KFMenuItemFactory.createSaveMenuItem();
		saveProjectAsMenuItem = KFMenuItemFactory.createSaveAsMenuItem();
		renderMP4MenuItem = KFMenuItemFactory.createRenderMP4MenuItem();
		renderGIFMenuItem = KFMenuItemFactory.createRenderGIFMenuItem();
		settingsMenuItem = KFMenuItemFactory.createSettingsMenuItem();
		
		editFPSMenuItem = KFMenuItemFactory.createEditFPSMenuItem();
		editCompLengthMenuItem = KFMenuItemFactory.createEditCompositionLengthMenuItem();
		editBackgroundColorMenuItem = KFMenuItemFactory.createEditBackgroundColorMenuItem();
	}
	
	private void initializeCanvasUIComponents() {
		canvas = KFCanvasFactory.createStandardKFCanvas();
	}
	
	private void initializeTimelineUIComponents() {
		timeline = KFTimelineFactory.createStandardTimeline();
		timelineSlider = KFSliderFactory.createStandardTimelineSlider(session.getShortestTimepoint(), 
																	session.getLongestTimepoint(), 
																	session.getCurrentTimepoint(),
																	session.getLongestTimeInSeconds(),
																	session.getFramesPerSecond());
		timelineLayersPanel = RectanglesKFLayerPaneFactory.createStandardTimelineLayersPanel();
		
		rectanglesScrollPane = KFScrollPaneFactory.createStandardTimelineKFScrollPane();
		namesScrollPane = KFScrollPaneFactory.createStandardTimelineKFScrollPane();
		rectanglesNamesSplitPane = KFSplitPaneFactory.createHorizontalSplitPane();
		
		additionalInfoPanel = KFPaneFactory.createTimelineAdditionalInfoPane();
		timelineNamesPanel = NamesKFLayerPaneFactory	.createStandardTimelineNamesPanel();
		
		timelineSliderAndRectanglesContainerPanel = KFPaneFactory.createTimelineSliderAndRectanglesContainerPane();
		namesAndAdditionalInfoContainerPanel = KFPaneFactory.createNamesAndAdditionalInfoContainerPane();
		
	}
	
	public void newSessionLayers() {
		session.initializeLayers();
		refreshUI();
	}
	
	public void newSessionFromSettings() {
		session = Session.createSessionFromSettings(Utils.getSettings());
	}
	
	public void newSessionFromSessionSave(SessionSave ss) {
		session = Session.createSessionFromSessionSave(ss);
		updateLayersFromDeserialization();
		refreshUI();
	}
	
	public void newSessionFromUndo(Session s) {
		session = s;
		refreshUI();
	}
	
	public Session createSessionDeepCopy() {
		return session.createDeepCopy();
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
	//Timeline scroll panel configuration stuff
	public void connectNamesScrollPaneAndRectanglesScrollPane() {
		this.namesScrollPane.getSwingComponent().getVerticalScrollBar().setModel(
				this.rectanglesScrollPane.getSwingComponent().getVerticalScrollBar().getModel());
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
	public void setPaintSetting(Enums.PaintSetting paintSetting) {
		session.setPaintSetting(paintSetting);
	}
	
	public Enums.PaintSetting getPaintSetting() {
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
	
	public void prepareRenderingProgressBar() {
		progressBar.setIsIndeterminate(false);
		progressBar.setProgressLabelAndFinishedText("Rendering progress:  ", "Rendering finished!  ");
		progressBar.resetProgressBar();
		progressBar.setVisibility(true);
	}
	
	public void prepareSavingFileProgressBar() {
		progressBar.setIsIndeterminate(true);
		progressBar.setProgressLabelAndFinishedText("Saving project...:  ", "Saved successfully!  ");
		progressBar.resetProgressBar();
		progressBar.setVisibility(true);
	}
	
	public void prepareOpeningFileProgressBar() {
		progressBar.setIsIndeterminate(true);
		progressBar.setProgressLabelAndFinishedText("Opening project...:  ", "Opened successfully!  ");
		progressBar.resetProgressBar();
		progressBar.setVisibility(true);
	}
	
	public void displayCompletedIndeterminateProgressBar() {
		progressBar.displayCompletedIndeterminateBar();
		// After 3 seconds of finishing, toggle off the progress bar
		new Timer().schedule( 
	        new TimerTask() {
	            @Override
	            public void run() {
	            	progressBar.setVisibility(false);
	            }
	        }, this.defaultTimeBeforeProgressBarDisappearsAfterOpenAndSave);
	}
	
	public void displayCompletedDeterminateProgressBar() {
		progressBar.displayCompletedDeterminateBar();
		// After 3 seconds of finishing, toggle off the progress bar
		new Timer().schedule( 
            new TimerTask() {
                @Override
                public void run() {
                	progressBar.setVisibility(false);
                }
            }, this.defaultTimeBeforeProgressBarDisappearsAfterRender);
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
		boolean firstTimeClicked = !session.isPlaying();
		session.setPlaying(true);
		SwingWorker sw = new SwingWorker() {
			@Override
			protected Object doInBackground() throws Exception {
				while(session.isPlaying()) {
					playOneFrame();
					// Sleep the thread by a frame
					long increment = 1000 / getFramesPerSecond();
					Thread.sleep(increment);
					
				}
				return null;
			}
			
			@Override
			protected void done() {
				System.out.println("Finished playing movie");
			}
		};
		// This prevents the bug where clicking the play button multiple times would suddenly speed it up by a
		// ton.
		if (firstTimeClicked) {
			sw.execute();
			System.out.println("Playing movie...");
		}
	}
	
	//Pause button
	public void pauseMovie() {
		session.setPlaying(false);
	}
	
	private void playOneFrame() {
		incrementTimepoint();
		refreshUI();
	}
	
	private void incrementTimepoint() {
		//Model 
		session.incrementTimepoint();
		//View 
		timelineSlider.updateSlider(session.getCurrentTimepoint());
		timelineSlider.getSwingComponent().requestFocus();
	}
	
	//-------------------------------------------------------------------------------------------------
	//Refresh and update UI components from session

	public void updateTimelineFromMouseClick(MouseEvent e) {
		int value = timelineSlider.valueAtX(e.getX());
		setCurrentTimepoint(value);
	    refreshUI();
	    timelineSlider.getSwingComponent().requestFocus();
	}
	
	//Another bandaid fix as long as I can't find how to actually resize stuff in scrollpanes
	public void updateTimelineFromSplitPaneResize() {
		setCurrentTimepoint(getCurrentTimepoint());
		refreshUI();
	}
	
	public void refreshUI() {
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
		timelineLayersPanel.updateMarkerAndRefresh(sliderBarx);
		timelineNamesPanel.refresh();
		refreshLayersContainersUI();
		refreshLayersUI();
	}
	
	private void refreshLayersContainersUI() {
		this.timelineNamesPanel.refresh();
	}
	
	private void refreshLayersUI() {
		ArrayList<Layer> layers = session.getLayers();
		for (Layer layer : layers) {
			layer.refreshUI();
		}
				
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
	
	public boolean isLayerBeingDragged() {
		return session.isLayerBeingDragged();
	}
	
	public Layer getCurrentDraggedLayer() {
		return session.getCurrentDraggedLayer();
	}
	
	public void setCurrentDraggedLayer(Layer layer) {
		session.setCurrentDraggedLayer(layer);
	}

	public void resetCurrentDraggedLayer() {
		session.resetCurrentDraggedLayer();
	}
	
	public Layer selectLayer(MouseEvent e) {
		return session.selectLayer(e);
	}
	
	//Should only be able to delete a layer if its not the only layer remaining
	public boolean layersCanBeDeleted(ArrayList<Integer> layerNums) {
		return session.layersCanBeDeleted(layerNums);
	}
	
	public void deleteLayers(ArrayList<Integer> layerNums) {
		session.deleteLayers(layerNums);
	}
	
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
	
	public Layer getCurrentLayer() {
		return session.getCurrentLayer();
	}
	
	public void setCurrentLayerColor(Color color) {
		session.getCurrentLayer().setColor(color);
	}
	
	public void setCurrentLayerName(String name) {
		session.getCurrentLayer().setName(name);
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
	
	public DrawFrame getCurrentLayerFrameAtCurrentTime() {
		return session.getCurrentLayerFrameAtCurrentTime();
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
	
	public void eraseCurrentLayerAtCurrentFrame() {
		int w  = canvas.getCanvasWidth();
		int h  = canvas.getCanvasHeight();
		if (w == 0) {
			w = KFCanvas.defaultPreferredWidth;
		} 
		if (h == 0) {
			h = KFCanvas.defaultPreferredHeight; 
		}
		session.eraseCurrentLayerAtCurrentFrame(w,h);
		refreshUI();
	}
	
	public void eraseAllLayersAtCurrentFrame() {
		int w  = canvas.getCanvasWidth();
		int h  = canvas.getCanvasHeight();
		if (w == 0) {
			w = KFCanvas.defaultPreferredWidth;
		} 
		if (h == 0) {
			h = KFCanvas.defaultPreferredHeight;
		}
		session.eraseAllLayersAtCurrentFrame(w,h);
		refreshUI();
	}
	
	private void prepareLayersForSerialization() {
		for(Layer layer : getLayers()) {
			layer.prepareForSerialization();
		}
	}
	
	public void updateLayersFromDeserialization() {
		for(Layer layer : getLayers()) {
			layer.updateFromDeserialization();
		}
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
		prepareLayersForSerialization();
		Utils.saveFile(frame);
	}
	public void saveAsProject() {
		prepareLayersForSerialization();
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
