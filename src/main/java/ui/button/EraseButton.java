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
import ui.dialog.DialogFactory;

public class EraseButton extends JButton implements Button {

	EraseButton() throws IOException {
		Image eraserImage = ImageIO.read(this.getClass().getResource("/eraseIcon.png"));
		setIcon(new ImageIcon(eraserImage));
		
		setVisible(true);
		setPreferredSize(new Dimension(Button.mvw, Button.mvh));
		addMouseListener(new MouseAdapter( ) {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					Controller.getController().addAndExecuteCommand(CommandFactory.createEraserToolSelectedCommand());
				}
				else if(SwingUtilities.isRightMouseButton(e)) {
					DialogFactory.createEraserSizeDialog();
				}
			}
		});
	}
	
	@Override
	public JButton getSwingComponent() {
		return this;
	}
	
}
