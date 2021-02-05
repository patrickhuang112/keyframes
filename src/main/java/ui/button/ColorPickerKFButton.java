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

import datatypes.Enums;
import keyframes.Session;
import ui.dialog.DialogFactory;

public class ColorPickerKFButton extends AbstractKFButton {

	ColorPickerKFButton() throws IOException {
		super("/colorPickerIcon.png", new MouseAdapter( ) {
			@Override
			public void mouseClicked(MouseEvent e) {
				DialogFactory.createBrushAndFillColorDialog();
			}
		});
	}
}
