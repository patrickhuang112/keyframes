package ui.layer.rectangles;

import javax.swing.JPanel;

import ui.UIComponent;

public interface KFLayerRectangles extends UIComponent {
	
	public static int defaultWidth = 1380;
	
	@Override
	public JPanel getSwingComponent();
	public void refresh();
}
