package ui.layer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;

import javax.swing.JPanel;

import datatypes.Layer;
import datatypes.LayerBoundingBox;
import keyframes.MagicValues;
import ui.layer.rectangles.KFLayerRectangles;
import ui.layer.rectangles.KFLayerRectanglesFactory;

public class StandardKFLayer extends JPanel implements KFLayer {

	private static final long serialVersionUID = 8006446073818767505L;
	private Layer layer;
	
	StandardKFLayer(Layer layer) {
		this.layer = layer;
	}
	
	@Override
	public JPanel getSwingComponent() {
		return this;
	}

	@Override
	public Layer getModel() {
		return layer;
	}

	@Override
	public void refresh() {
		repaint();
		revalidate();
		
	}
	
}
