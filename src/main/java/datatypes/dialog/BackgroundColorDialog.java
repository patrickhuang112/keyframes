package datatypes.dialog;

import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

import keyframes.Session;

public class BackgroundColorDialog implements Dialog {

	JDialog dialog;
	
	BackgroundColorDialog(Session session) {
		Color current = session.getDrawablePanelBackgroundColor();
		Color newColor = JColorChooser.showDialog(null, "Choose a new background color", current);
		if (newColor != null) {
			session.setDrawablePanelBackgroundColor(newColor);
			session.setEraserColor(newColor);
		}
	}
	
	
	@Override
	public JDialog getSwingComponent() {
		// Nothing currently, replace with exception later
		return null;
	}

}
