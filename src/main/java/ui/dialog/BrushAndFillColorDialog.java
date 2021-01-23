package ui.dialog;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

import keyframes.Controller;
import keyframes.Session;

public class BrushAndFillColorDialog implements Dialog {

	BrushAndFillColorDialog() {
		Color current = Controller.getController().getBrushColor();
		Color newColor = JColorChooser.showDialog(null, "Choose a color", current);
		if (newColor != null) {
			Controller.getController().setBrushColor(newColor);
		}
	}
	
	
	@Override
	public JDialog getSwingComponent() {
		// Nothing currently, replace with exception later
		return null;
	}

}