package ui.timeline.namespanel;

import javax.swing.JPanel;

import ui.UIComponent;

public interface TimelineNamesPanel extends UIComponent {

	@Override
	public JPanel getSwingComponent();
	
	public void buildLayersPanelLayers();
	public void refresh();
	
}
