package keyframes;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;

import datatypes.Layer;
import datatypes.SessionObject;
import datatypes.UIComponent;

public class TimelineLayersPanel extends JScrollPane implements SessionObject, Serializable{
	
	// Set this manually cause I couldn't get other things to work
	private double sliderBarx = 5;
	private Timeline parent;
	private final int offset = 5;
	private JViewport viewport;
	private JPanel layersParentPane;
	
	private ArrayList<Color> defaultColors = new ArrayList<>();
	
	public TimelineLayersPanel(Timeline parent) {
		super();
		this.parent = parent;
		this.viewport = this.getViewport();
		this.layersParentPane = new JPanel();
		initializeDefaultColors();
		
		layersParentPane.setLayout(new BoxLayout(layersParentPane, BoxLayout.Y_AXIS));
		layersParentPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		viewport.add(layersParentPane);
		
		updateTimelineLayersPanel();
		
	}
	
	public void updateTimelineLayersPanel() {
		ArrayList<Layer> layers = parent.getSession().getLayers();
		layersParentPane.removeAll();
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			if (layer.getColor() == Color.black) {
				layer.setColor(defaultColors.get(i % defaultColors.size()));
			}
			layer.setAlignmentX(Component.LEFT_ALIGNMENT);
			layer.setMaximumSize(new Dimension(40,40));
			layersParentPane.add(layer);
			
		}
		
	}
	
	private void initializeDefaultColors() {
		defaultColors.add(Color.red);
		defaultColors.add(Color.cyan);
		defaultColors.add(Color.pink);
		defaultColors.add(Color.orange);
		defaultColors.add(Color.blue);
		defaultColors.add(Color.orange);
		defaultColors.add(Color.green);
		defaultColors.add(Color.gray);
		defaultColors.add(Color.magenta);
	}
	
	// These two functions literally just for the UI thumb line thing
	public void setSliderBarx(double x) {
		sliderBarx = x;
	}
	
	public double getSliderBarx() {
		return sliderBarx;
	}
	
	private void drawRectangle(Graphics2D g2d, int x1, int y1, int x2, int y2) {
		int width = x2 - x1;
		int height = y2 - y1;
		g2d.setPaint(Color.black);
		g2d.fillRect(x1, y1, width, height);
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
		gp.moveTo(sliderBarx, this.getY()-1000);
		
		g2d.setPaint(Color.black);
		s = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2d.setStroke(s);
		
		// Big values just to make sure it draws over the whole panel, look for a cleaner solution some other time
		gp.lineTo(sliderBarx, this.getY()+1000);
		
		g2d.draw(gp);
			
	}

	public Session getSession() {
		return parent.getSession();
	}
	
}
