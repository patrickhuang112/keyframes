package ui.dialog;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

import keyframes.Controller;
import keyframes.Session;

public class BackgroundColorDialog implements Dialog {

	BackgroundColorDialog() {
		Color current = Controller.getController().getDrawablePanelBackgroundColor();
		Color newColor = JColorChooser.showDialog(null, "Choose a new background color", current);
		if (newColor != null) {
			Controller.getController().setDrawablePanelBackgroundColor(newColor);
			Controller.getController().setEraserColor(newColor);
		}
	}
	
	
	@Override
	public JDialog getSwingComponent() {
		// Nothing currently, replace with exception later
		return null;
	}

}
