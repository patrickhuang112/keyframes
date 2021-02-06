package ui.dialog;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

import commands.CommandFactory;
import keyframes.Controller;
import keyframes.Session;

public class BackgroundColorKFDialog implements KFDialog {

	BackgroundColorKFDialog() {
		Color current = Controller.getController().getDrawablePanelBackgroundColor();
		Color newColor = JColorChooser.showDialog(null, "Choose a new background color", current);
		if (newColor != null) {
			Controller.getController().addAndExecuteCommand(
					CommandFactory.createBackgroundColorSelectedCommand(newColor));
		}
	}
	
	
	@Override
	public JDialog getSwingComponent() {
		// Nothing currently, replace with exception later
		return null;
	}

}
