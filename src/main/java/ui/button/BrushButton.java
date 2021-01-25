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

public class BrushButton extends JButton implements Button {

	BrushButton() throws IOException {
		super();
		Image drawImage = ImageIO.read(this.getClass().getResource("/drawIcon.png"));
		setIcon(new ImageIcon(drawImage));
		
		setVisible(true);
		setPreferredSize(new Dimension(Button.mvw, Button.mvh));
		addMouseListener(new MouseAdapter( ) {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					Controller.getController().addAndExecuteCommand(CommandFactory.createBrushToolSelectedCommand());
				}
				else if(SwingUtilities.isRightMouseButton(e)) {
					DialogFactory.createBrushSizeDialog();
				}
			}
		});
	}
	
	@Override
	public JButton getSwingComponent() {
		return this;
	}

}
