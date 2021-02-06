package ui.dialog;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

import commands.CommandFactory;
import keyframes.Controller;

public class LayerColorKFDialog implements KFDialog {

	LayerColorKFDialog () {
		Color current = Controller.getController().getCurrentLayer().getColor();
		Color newColor = JColorChooser.showDialog(null, "Choose a color", current);
		if (newColor != null) {
			Controller.getController().addAndExecuteCommand(
				CommandFactory.createLayerColorSelectedCommand(newColor));
		}
	}
	
	
	@Override
	public JDialog getSwingComponent() {
		return null;
	}
}
