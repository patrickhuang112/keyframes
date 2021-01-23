package ui.timeline.layerspanel;

import javax.swing.JScrollPane;

import ui.UIComponent;

public interface TimelineLayersPanel extends UIComponent {
	
	@Override
	public JScrollPane getSwingComponent();

	public void updateLayersPanelUI(double newMarkerX);
	public void buildLayersPanelLayers();
}