package ui.layer.rectangles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import datatypes.Layer;
import datatypes.LayerBoundingBox;
import keyframes.MagicValues;
import ui.layer.KFLayer;

public class StandardKFLayerRectangles extends JPanel implements KFLayerRectangles {

	private Layer layer;
	private int UIwidth = MagicValues.layerUIDefaultWidth;
	private int UIheight = MagicValues.layerUIDefaultHeight;
	private int bboxStartY = 0;
	
	private int selectLineThickness  = 2;
	private float []selectDashesArray = {10.0f};
	private float strokeThickness = 1.0f;
	private float miterLength = 10.0f;
	private float dashPhase = 0.0f;
	
	StandardKFLayerRectangles(Layer layer) {
		super();
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setMinimumSize(new Dimension(KFLayerRectangles.DefaultWidth, KFLayerRectangles.DefaultHeight));
		setPreferredSize(new Dimension(KFLayerRectangles.DefaultWidth, KFLayerRectangles.DefaultHeight));
		setMaximumSize(new Dimension(KFLayerRectangles.DefaultWidth, KFLayerRectangles.DefaultHeight));
		this.layer = layer;
	}
	
	@Override
	public JPanel getSwingComponent() {
		return this;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		ArrayList<LayerBoundingBox> boundingBoxes = layer.getLayerBoundingBoxes();
		
		if (layer.isGhost()) {
			g2d.setPaint(Color.LIGHT_GRAY);
			for (LayerBoundingBox bbox : boundingBoxes) {
				// These coordinates I think are relative to our layer coordinates, so 
				// y coordinates should basically always start at 0
				
				//LIGHT GREY INSIDE
				Rectangle box = bbox.getBox();
				g2d.fillRect(box.x, bboxStartY, box.width, box.height);
				
				//YELLOW DASHES?
				g2d.setPaint(Color.yellow);
				BasicStroke dashed = new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT, 
								BasicStroke.JOIN_MITER, miterLength, selectDashesArray, dashPhase);
				g2d.setStroke(dashed);
				g2d.drawRoundRect(box.x, bboxStartY, box.width, box.height, selectLineThickness, selectLineThickness);
				}
			}
		else {
			g2d.setPaint(layer.getColor());
			for (LayerBoundingBox bbox : boundingBoxes) {
				// These coordinates I think are relative to our layer coordinates, so 
				// y coordinates should basically always start at 0
				Rectangle box = bbox.getBox();
				g2d.setPaint(layer.getColor());
				g2d.fillRect(box.x, bboxStartY, box.width, box.height);
				if (layer.isSelected()) {
					g2d.setPaint(Color.black);
					Stroke s = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
					g2d.setStroke(s);
					g2d.drawRoundRect(box.x, bboxStartY, box.width, box.height, selectLineThickness, selectLineThickness);
				}
			}
		}
		
	}

	@Override
	public void refresh() {
		repaint();
		revalidate();
	}
	
}
