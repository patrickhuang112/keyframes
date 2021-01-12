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

public class FillButton extends JButton implements Button {

	FillButton(Session session) throws IOException {
		super();
		Image icon = ImageIO.read(this.getClass().getResource("/fillIcon.png"));
		setIcon(new ImageIcon(icon));
		
		setVisible(true);
		setPreferredSize(new Dimension(Button.mvw, Button.mvh));
		addMouseListener(new MouseAdapter( ) {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					session.setPaintSetting(EnumFactory.PaintSetting.FILLSINGLE);
				}
			}
		});
	}
	
	@Override
	public JButton getSwingComponent() {
		return this;
	}

}
