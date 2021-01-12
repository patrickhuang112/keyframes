package ui.dialog;

import javax.swing.JComponent;
import javax.swing.JFrame;

import keyframes.Session;
import settings.Settings;


// FOR all dialogs created from this factory, they will instantly be visible after creation.
public class DialogFactory {
	
	public static Dialog createCompositionLengthDialog (JComponent parent, Session session) {
		return new CompositionLengthDialog(parent, session, session.lengthMin, session.lengthMax,
											session.getLongestTimeInSeconds());
	}
	
	public static Dialog createFPSDialog (JComponent parent, Session session) {
		return new FPSDialog(parent, session, session.fpsMin, session.fpsMax, 
											session.getFramesPerSecond());
	}
	
	public static Dialog createEraserSizeDialog (JComponent parent, Session session) {
		return new EraserSizeDialog(parent, session, session.eraserMin, session.eraserMax, 
											session.getEraserSize());
	}
	
	public static Dialog createBrushSizeDialog (JComponent parent, Session session) {
		return new BrushSizeDialog (parent, session, session.brushMin, session.brushMax,
											session.getBrushSize());
	}
	
	public static Dialog createSettingsDialog (JFrame frame, String windowTitle, Settings settings) {
		return new SettingsDialog(frame, windowTitle, settings);
	};
	
	public static Dialog createBackgroundColorDialog (Session session) {
		return new BackgroundColorDialog(session);
	}
	
	public static Dialog createBrushAndFillColorDialog (Session session) {
		return new BrushAndFillColorDialog(session);
	}
}
