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

public class PlayButton extends JButton implements Button {

	PlayButton() throws IOException {
		Image playImage = ImageIO.read(this.getClass().getResource("/playIcon.png"));
		setIcon(new ImageIcon(playImage));
		
		setVisible(true);
		setPreferredSize(new Dimension(Button.mvw, Button.mvh));
		addMouseListener(new MouseAdapter( ) {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					Controller.getController().playMovie();
				}
			}
		});
	}
	
	@Override
	public JButton getSwingComponent() {
		return this;
	}
	
}
