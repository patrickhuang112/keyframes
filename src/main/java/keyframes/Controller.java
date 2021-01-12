package keyframes;

import datatypes.ProgressBar;
import ui.DrawablePanel;
import ui.MainView;
import ui.TimelineLayersPanel;
import ui.TimelineSlider;

public class Controller {

	private static Controller instance;
	
	private ProgressBar progressBar = null;
	private DrawablePanel drawPanel = null;
	private TimelineLayersPanel timelineLayersPanel = null;
	private TimelineSlider timelineSlider = null;
	private MainView mainView = null;
	
	
	private Controller () {
		mainView = MainView.getInstance();
	}
	
	private static Controller getController() {
		if (Controller.instance == null) {
			Controller.instance = new Controller();
		}
		return Controller.instance;
	}
	
}
