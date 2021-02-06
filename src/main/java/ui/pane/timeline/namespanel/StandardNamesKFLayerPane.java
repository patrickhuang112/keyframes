package ui.pane.timeline.namespanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import datatypes.Layer;
import keyframes.Controller;
import ui.layer.names.KFLayerName;
import ui.layer.rectangles.KFLayerRectangles;

public class StandardNamesKFLayerPane extends ui.pane.timeline.AbstractKFLayerPane implements NamesKFLayerPane {

	StandardNamesKFLayerPane() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(Component.LEFT_ALIGNMENT);
	}
	@Override
	public JPanel getSwingComponent() {
		return this;
	}

	// This is where we also update the layer nums of all the layers.
	private void updatePanelLayers() {
		ArrayList<Layer> layers = Controller.getController().getLayers();
		this.removeAll();
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			layer.setLayerNum(i);
			KFLayerName layerNameUI = layer.getUINames();
			this.add(layerNameUI.getSwingComponent());
		}
	}
	public void buildLayersPanelLayers() {
		updatePanelLayers();
	}
	@Override
	public void updateTimelineFromMouseClick(MouseEvent e) {
		Controller.getController().selectLayer(e);
	}
	@Override
	public void refresh() {
		updatePanelLayers();
		repaint();
		revalidate();
	}
	
}
