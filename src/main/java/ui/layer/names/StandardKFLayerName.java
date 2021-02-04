package ui.layer.names;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import datatypes.Layer;
import datatypes.LayerBoundingBox;
import ui.layer.KFLayer;
import ui.layer.rectangles.KFLayerRectangles;

public class StandardKFLayerName extends JPanel implements KFLayerName {

	private Layer layer;
	private JLabel label;
	
	private int roundness = 5;
	
	StandardKFLayerName(Layer layer) {
		super();
		setMinimumSize(new Dimension(KFLayerName.DefaultWidth, KFLayer.DefaultHeight));
		setPreferredSize(new Dimension(KFLayerName.DefaultWidth, KFLayer.DefaultHeight));
		setMaximumSize(new Dimension(KFLayerName.DefaultWidth, KFLayer.DefaultHeight));
		this.layer = layer;
		label = new JLabel(layer.getName());
		label.setFont(new Font("Arial", Font.PLAIN, 14));
		label.setForeground(Color.white);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		add(label);
	}
	
	@Override
	public JPanel getSwingComponent() {
		return this;
	}

	@Override
	public void refresh() {
		repaint();
		revalidate();
	}

	public void setName (String name) {
		this.label.setText(name);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		if (layer.isSelected()) {
			if (layer.isGhost()) {
				g2d.setPaint(Color.GRAY);
			} else {
				g2d.setPaint(layer.getColor());
			}
			Stroke s = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
			g2d.setStroke(s);
			g2d.drawRoundRect(10, 0, KFLayerName.DefaultWidth - 10, KFLayer.DefaultHeight, roundness, roundness);
		} 
	}
	
}
