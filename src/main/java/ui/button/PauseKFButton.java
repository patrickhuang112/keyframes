package ui.button;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import datatypes.Enums;
import keyframes.Controller;
import keyframes.Session;
import ui.dialog.DialogFactory;

public class PauseKFButton extends AbstractKFButton {

	PauseKFButton() throws IOException {
		super("/pauseIcon.png", new MouseAdapter( ) {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					Controller.getController().pauseMovie();
				}
			}
		});
	}
}
