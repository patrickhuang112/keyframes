package ui.button;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import commands.CommandFactory;
import datatypes.Enums;
import keyframes.Controller;
import keyframes.Session;
import ui.dialog.KFDialogFactory;

public class EraseKFButton extends AbstractKFButton {

	EraseKFButton() throws IOException {
		super("/eraseIcon.png", new MouseAdapter( ) {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					Controller.getController().addAndExecuteCommand(CommandFactory.createEraserToolSelectedCommand());
				}
				else if(SwingUtilities.isRightMouseButton(e)) {
					KFDialogFactory.createEraserSizeDialog();
				}
			}
		});
	}
	
}
