package ui.dialog;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import commands.CommandFactory;
import keyframes.Controller;

public class RenameCurrentLayerKFDialog implements KFDialog {

	RenameCurrentLayerKFDialog () {
		String curName = Controller.getController().getCurrentLayer().getName();
		String answer = (String)JOptionPane.showInputDialog(null, "Rename Current Layer", curName);
		if (answer != null) {
			Controller.getController().addAndExecuteCommand(
				CommandFactory.createRenameCurrentLayerCommand(answer));
		}
	}
	
	@Override
	public JDialog getSwingComponent() {
		return null;
	}

}
