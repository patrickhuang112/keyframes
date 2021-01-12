package ui.dialog;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

import keyframes.Session;

public class BrushAndFillColorDialog implements Dialog {

	JDialog dialog;
	
	BrushAndFillColorDialog(Session session) {
		Color current = session.getBrushColor();
		Color newColor = JColorChooser.showDialog(null, "Choose a color", current);
		if (newColor != null) {
			session.setBrushColor(newColor);
		}
	}
	
	
	@Override
	public JDialog getSwingComponent() {
		// Nothing currently, replace with exception later
		return null;
	}

}
