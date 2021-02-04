package ui.timeline.layerspanel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ui.UIComponent;

public interface TimelineLayersPanel extends UIComponent {
	
	@Override
	public JPanel getSwingComponent();

	public void updateLayersPanelUI(double newMarkerX);
	public void buildLayersPanelLayers();
}
