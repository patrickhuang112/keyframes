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

import factories.EnumFactory;
import keyframes.Session;
import ui.dialog.DialogFactory;

public class PauseButton extends JButton implements Button {

	PauseButton(Session session) throws IOException {
		Image pauseImage = ImageIO.read(this.getClass().getResource("/pauseIcon.png"));
		setIcon(new ImageIcon(pauseImage));
		
		setVisible(true);
		setPreferredSize(new Dimension(Button.mvw, Button.mvh));
		addMouseListener(new MouseAdapter( ) {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					session.pauseMovie();
				}
			}
		});
	}
	
	@Override
	public JButton getSwingComponent() {
		return this;
	}
	
}
