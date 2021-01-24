package ui.layer.rectangles;

import javax.swing.JPanel;

import keyframes.MagicValues;
import ui.UIComponent;

public interface KFLayerRectangles extends UIComponent {
	
	public static int DefaultWidth = MagicValues.layerUIDefaultWidth;
	public static int DefaultHeight = MagicValues.layerUIDefaultHeight;
	@Override
	public JPanel getSwingComponent();
	
	public void refresh();
}
