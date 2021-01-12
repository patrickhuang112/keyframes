package ui.button;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import factories.EnumFactory;
import keyframes.Session;
import ui.dialog.DialogFactory;

public class ColorPickerButton extends JButton implements Button {

	ColorPickerButton(Session session) throws IOException {
		Image icon = ImageIO.read(this.getClass().getResource("/colorPickerIcon.png"));
		setIcon(new ImageIcon(icon));
		
		setVisible(true);
		setPreferredSize(new Dimension(Button.mvw, Button.mvh));
		addMouseListener(new MouseAdapter( ) {
			@Override
			public void mouseClicked(MouseEvent e) {
				DialogFactory.createBrushAndFillColorDialog(session);
			}
		});
	}
	
	@Override
	public JButton getSwingComponent() {
		return this;
	}
	
}
