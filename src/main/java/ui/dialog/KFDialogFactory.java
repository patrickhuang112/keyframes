package ui.dialog;

import javax.swing.JComponent;
import javax.swing.JFrame;

import keyframes.Controller;
import keyframes.Session;
import settings.Settings;


// FOR all dialogs created from this factory, they will instantly be visible after creation.
public class KFDialogFactory {
	
	public static KFDialog createCompositionLengthDialog () {
		return new CompositionLengthKFDialog(Session.lengthMin, Session.lengthMax,
											Controller.getController().getLongestTimeInSeconds());
	}
	
	public static KFDialog createFPSDialog () {
		return new KFFPSDialog(Session.fpsMin, Session.fpsMax, 
											Controller.getController().getFramesPerSecond());
	}
	
	public static KFDialog createEraserSizeDialog () {
		return new EraserSizeKFDialog(Session.eraserMin, Session.eraserMax, 
											Controller.getController().getEraserSize());
	}
	
	public static KFDialog createBrushSizeDialog () {
		return new BrushSizeKFDialog (Session.brushMin, Session.brushMax,
											Controller.getController().getBrushSize());
	}
	
	public static KFDialog createSettingsDialog (String windowTitle, Settings settings) {
		return new SettingsKFDialog(windowTitle, settings);
	};
	
	public static KFDialog createBackgroundColorDialog () {
		return new BackgroundColorKFDialog();
	}
	
	public static KFDialog createBrushAndFillColorDialog () {
		return new BrushAndFillColorKFDialog();
	}
	
	public static KFDialog createLayerColorDialog() {
		return new LayerColorKFDialog();
	}
	
	public static KFDialog createRenameCurrentLayerDialog() {
		return new RenameCurrentLayerKFDialog();
	}
}
