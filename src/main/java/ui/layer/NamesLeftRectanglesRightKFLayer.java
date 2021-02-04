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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import datatypes.Layer;
import datatypes.LayerBoundingBox;
import keyframes.MagicValues;
import ui.layer.names.KFLayerName;
import ui.layer.names.KFLayerNameFactory;
import ui.layer.rectangles.KFLayerRectangles;
import ui.layer.rectangles.KFLayerRectanglesFactory;

public class NamesLeftRectanglesRightKFLayer extends JPanel implements KFLayer {

	private static final long serialVersionUID = 8006446073818767505L;
	private Layer layer;
	
	private KFLayerName name;
	private KFLayerRectangles rectangles;
	
	NamesLeftRectanglesRightKFLayer(Layer layer) {
		this.layer = layer;
		this.name = KFLayerNameFactory.createStandardKFLayerNames(layer);
		this.rectangles = KFLayerRectanglesFactory.createStandardKFLayerRectangles(layer);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setMinimumSize(new Dimension(KFLayer.DefaultWidth, KFLayer.DefaultHeight));
		setPreferredSize(new Dimension(KFLayer.DefaultWidth, KFLayer.DefaultHeight));
		setMaximumSize(new Dimension(KFLayer.DefaultWidth, KFLayer.DefaultHeight));
		
		// How it should be arranged, swing name left, glue middle, rectangles right;
		add(this.name.getSwingComponent());
		add(this.rectangles.getSwingComponent());
	}
	
	@Override
	public JPanel getSwingComponent() {
		return this;
	}

	@Override
	public Layer getModel() {
		return layer;
	}
	
	public KFLayerRectangles getRectanglesUI (){
		return this.rectangles;
	}

	public KFLayerName getNameUI() {
		return this.name;
	}
	
	@Override
	public void refresh() {
		this.rectangles.refresh();
		this.name.refresh();
		repaint();
		revalidate();
	}
	
	public void setName(String name) {
		this.name.setName(name);
	}
	
}
