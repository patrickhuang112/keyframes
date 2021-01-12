package datatypes.dialog;

import javax.swing.JComponent;
import javax.swing.JFrame;

import keyframes.Session;
import settings.Settings;


// FOR all dialogs created from this factory, they will instantly be visible after creation.
public class DialogFactory {
	
	public static CompositionLengthDialog createCompositionLengthDialog (JComponent parent, Session session) {
		return new CompositionLengthDialog(parent, session, session.lengthMin, session.lengthMax,
											session.getLongestTimeInSeconds());
	}
	
	public static FPSDialog createFPSDialog (JComponent parent, Session session) {
		return new FPSDialog(parent, session, session.fpsMin, session.fpsMax, 
											session.getFramesPerSecond());
	}
	
	public static EraserSizeDialog createEraserSizeDialog (JComponent parent, Session session) {
		return new EraserSizeDialog(parent, session, session.eraserMin, session.eraserMax, 
											session.getEraserSize());
	}
	
	public static BrushSizeDialog createBrushSizeDialog (JComponent parent, Session session) {
		return new BrushSizeDialog (parent, session, session.brushMin, session.brushMax,
											session.getBrushSize());
	}
	
	public static SettingsDialog createSettingsDialog (JFrame frame, String windowTitle, Settings settings) {
		return new SettingsDialog(frame, windowTitle, settings);
	};
	
	public static BackgroundColorDialog createBackgroundColorDialog (Session session) {
		return new BackgroundColorDialog(session);
	}
	
}
