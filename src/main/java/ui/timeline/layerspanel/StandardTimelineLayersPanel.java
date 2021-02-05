package ui.timeline.layerspanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import datatypes.Layer;
import datatypes.SessionObject;
import keyframes.Controller;
import keyframes.Session;
import ui.UIComponent;
import ui.dialog.DialogFactory;
import ui.layer.KFLayer;
import ui.layer.rectangles.KFLayerRectangles;
import ui.layer.rectangles.KFLayerRectanglesFactory;
import ui.timeline.KFLayerPanel;
import ui.timeline.KFTimeline;

public class StandardTimelineLayersPanel extends KFLayerPanel implements TimelineLayersPanel, Serializable{
	
	
	public static final int defaultOffset = 5;
	public static final int defaultSliderBarX = 5;
	public static final int defaultLineYRadius = 500;
	public static final int defaultLineXRadius = 5;
	
	// Set this manually cause I couldn't get other things to work
	private double sliderBarx = defaultSliderBarX;
	private final int offset = defaultOffset;
	private ArrayList<Color> defaultColors = new ArrayList<>();
	
	private Layer layerBeingDragged = null;
	
	
	
	StandardTimelineLayersPanel() {
		super();
		initializeDefaultColors();
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
	}
	
	@Override
	public void updateTimelineFromMouseClick(MouseEvent e) {
		Controller.getController().selectLayer(e);
		Controller.getController().updateTimelineFromMouseClick(e);
	}
	
	// This is where we also update the layer nums of all the layers.
	public void updateTimelineLayersPanelLayerNumbers() {
		ArrayList<Layer> layers = Controller.getController().getLayers();
		this.removeAll();
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			layer.setLayerNum(i);
			if (layer.getColor() == Color.black) {
				layer.setColor(defaultColors.get(i % defaultColors.size()));
			}
			KFLayerRectangles layerRectUI = layer.getUIRectangles();
			this.add(layerRectUI.getSwingComponent());
		}
	}
	
	private void initializeDefaultColors() {
		defaultColors.add(Color.red);
		defaultColors.add(Color.cyan);
		defaultColors.add(Color.pink);
		defaultColors.add(Color.orange);
		defaultColors.add(Color.blue);
		defaultColors.add(Color.green);
		defaultColors.add(Color.gray);
		defaultColors.add(Color.magenta);
	}
	
	// These two functions literally just for the UI thumb line thing
	private void setSliderBarx(double x) {
		sliderBarx = x;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		
		// DO THIS LAST SO THE SLIDER LINE DRAWS OVER EVERYTHING ELSE
		// Draw the slider line in the timeline layers panel
		Graphics2D g2d = (Graphics2D) g;
		GeneralPath gp =  new GeneralPath();
		BasicStroke s = null;
		
		// Big values just to make sure it draws over the whole panel, look for a cleaner solution some other time
		gp.moveTo(sliderBarx, this.getY() - defaultLineYRadius);
		
		g2d.setPaint(Color.black);
		s = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2d.setStroke(s);
		
		// Big values just to make sure it draws over the whole panel, look for a cleaner solution some other time
		gp.lineTo(sliderBarx, this.getY() + defaultLineYRadius);
		
		g2d.draw(gp);
			
	}

	@Override
	public JPanel getSwingComponent() {
		return this;
	}

	@Override
	public void updateLayersPanelUI(double newMarkerX) {
	    //Updates the layers order on the layers panel
	    updateTimelineLayersPanelLayerNumbers();
	    
	    setSliderBarx(newMarkerX);
	    
	    repaint();
		revalidate();
	}

	@Override
	public void buildLayersPanelLayers() {
		updateTimelineLayersPanelLayerNumbers();
	}
	
}
