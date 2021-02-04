package ui.dialog;

import javax.swing.JComponent;
import javax.swing.JFrame;

import keyframes.Controller;
import keyframes.Session;
import settings.Settings;


// FOR all dialogs created from this factory, they will instantly be visible after creation.
public class DialogFactory {
	
	public static Dialog createCompositionLengthDialog () {
		return new CompositionLengthDialog(Session.lengthMin, Session.lengthMax,
											Controller.getController().getLongestTimeInSeconds());
	}
	
	public static Dialog createFPSDialog () {
		return new FPSDialog(Session.fpsMin, Session.fpsMax, 
											Controller.getController().getFramesPerSecond());
	}
	
	public static Dialog createEraserSizeDialog () {
		return new EraserSizeDialog(Session.eraserMin, Session.eraserMax, 
											Controller.getController().getEraserSize());
	}
	
	public static Dialog createBrushSizeDialog () {
		return new BrushSizeDialog (Session.brushMin, Session.brushMax,
											Controller.getController().getBrushSize());
	}
	
	public static Dialog createSettingsDialog (String windowTitle, Settings settings) {
		return new SettingsDialog(windowTitle, settings);
	};
	
	public static Dialog createBackgroundColorDialog () {
		return new BackgroundColorDialog();
	}
	
	public static Dialog createBrushAndFillColorDialog () {
		return new BrushAndFillColorDialog();
	}
	
	public static Dialog createLayerColorDialog() {
		return new LayerColorDialog();
	}
	
	public static Dialog createRenameCurrentLayerDialog() {
		return new RenameCurrentLayerDialog();
	}
}
