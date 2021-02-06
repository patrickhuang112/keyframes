package ui.dialog;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

import commands.CommandFactory;
import keyframes.Controller;
import keyframes.Session;

public class BrushAndFillColorKFDialog implements KFDialog {

	BrushAndFillColorKFDialog() {
		Color current = Controller.getController().getBrushColor();
		Color newColor = JColorChooser.showDialog(null, "Choose a color", current);
		if (newColor != null) {
			Controller.getController().addAndExecuteCommand(
					CommandFactory.createBrushAndFillColorSelectedCommand(newColor));
		}
	}
	
	
	@Override
	public JDialog getSwingComponent() {
		// Nothing currently, replace with exception later
		return null;
	}

}
