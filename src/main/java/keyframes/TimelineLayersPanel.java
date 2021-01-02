package keyframes;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

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
import datatypes.UIComponent;

public class TimelineLayersPanel extends JScrollPane implements SessionObject, Serializable{
	
	// Set this manually cause I couldn't get other things to work
	private double sliderBarx = 5;
	private Timeline parent;
	private final int offset = 5;
	private JViewport viewport;
	private JPanel layersParentPane;
	
	private ArrayList<Color> defaultColors = new ArrayList<>();
	
	private Layer layerBeingDragged = null;
	private ArrayList<Layer> layersCopy = null;
	
	
	
	public TimelineLayersPanel(Timeline parent) {
		super();
		this.parent = parent;
		this.viewport = this.getViewport();
		this.layersParentPane = new JPanel();
		initializeDefaultColors();
		
		layersCopy = getSession().deepCopyLayers();
		
		layersParentPane.setLayout(new BoxLayout(layersParentPane, BoxLayout.Y_AXIS));
		layersParentPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		viewport.add(layersParentPane);
		setupLayersParentPaneListeners();
		updateTimelineLayersPanelLayerNumbers();
		
	}
	
	public void setupLayersParentPaneListeners() {
		layersParentPane.addMouseListener(new MouseAdapter() {
			@Override 
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					updateTimelineFromMouseClick(e);
				}
				else if(SwingUtilities.isRightMouseButton(e)) {
					displayRightClickMenu(e);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				parent.updateTimelineFromMouse(e);
				Layer layer = selectLayer(e);
				if (layer != null) {
					layerBeingDragged = layer;
					layerBeingDragged.setGhost(true);
					layersCopy = getSession().deepCopyLayers();
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (layerBeingDragged != null) {
					layerBeingDragged.setGhost(false);
					layerBeingDragged = null;
				}
			}
			
		});
		layersParentPane.addMouseMotionListener(new MouseAdapter() {
			@Override 
			public void mouseDragged(MouseEvent e) {
				
				if (layerBeingDragged != null) {
					ArrayList<Layer> layers = getSession().getLayers();
					for (int i = 0; i < layers.size(); i++) {
						Layer layer = layers.get(i);
						if(layer.inBounds(e.getX(), e.getY())) {
							int curLayNum = getSession().getCurrentLayerNum();
							
							//ASSERT statements are just for piece of mind so I know i'm doing the right
							//logic thing here
							assert(curLayNum == layerBeingDragged.getLayerNum());
							int boundLayNum = layer.getLayerNum();
							assert(boundLayNum == i);
							if (curLayNum != boundLayNum) {
								layers.remove(curLayNum);
								layers.add(boundLayNum, layerBeingDragged);
								getSession().setCurrentLayerNum(i);
							}
							break;
						}
					}
					
				}
				// This includes the timeline redraw refresh (so the timeline visual will be updated) 
				// so have this at the end after all the stuff above
				
				parent.updateTimelineFromMouse(e);
			}
		});
	}
	
	private Layer selectLayer(MouseEvent e) {
		ArrayList<Layer> layers = getSession().getLayers();
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			if(layer.inBounds(e.getX(), e.getY())) {
				int curLayNum = getSession().getCurrentLayerNum();
				layers.get(curLayNum).setSelected(false);
				layer.setSelected(true);
				getSession().setCurrentLayerNum(i);
				return layer;
			}
		}
		return null;
	}
	
	private void displayRightClickMenu(MouseEvent e) {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem newLayerMenuItem = new JMenuItem(new AbstractAction("New layer") {
			@Override
			public void actionPerformed(ActionEvent e) {
				getSession().addNewLayer();
			}
		});
		menu.add(newLayerMenuItem);
		
		Layer layer = selectLayer(e);
		if (layer != null) {
			// We do this first here, JUST so the selected black box shows around the layer
			parent.updateTimelineFromMouse(e);
			JMenuItem recolorLayerMenuItem = new JMenuItem(new AbstractAction("Change layer color") {
				@Override
				public void actionPerformed(ActionEvent ae) {
					Color newColor = JColorChooser.showDialog(null, "Choose a color", layer.getColor());
					layer.setColor(newColor);
					// We do this here to update the color of the layer straightaway
					// Kind of messy butwhatever
					parent.updateTimelineFromMouse(e);
				}
			});
			
			JMenuItem copyFramesFromCurrentLayerMenuItem = new JMenuItem(new AbstractAction("Copy frame from current time and layer") {
				@Override
				public void actionPerformed(ActionEvent ae) {
					getSession().copyFramesFromCurrentLayerAndCurrentTime();
					parent.updateTimelineFromMouse(e);
				}
			});
			
			JMenuItem pasteFramesOntoCurrentLayerMenuItem = new JMenuItem(new AbstractAction("Paste frame") {
				@Override
				public void actionPerformed(ActionEvent ae) {
					getSession().pasteFramesToCurrentLayerAndCurrentTime();
					parent.updateTimelineFromMouse(e);
				}
			});
			
			menu.add(recolorLayerMenuItem);
			menu.add(copyFramesFromCurrentLayerMenuItem);
			menu.add(pasteFramesOntoCurrentLayerMenuItem);
		}
		
		menu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	public void updateTimelineFromMouseClick(MouseEvent e) {
		selectLayer(e);
		parent.updateTimelineFromMouse(e);
		
	}
	
	// This is where we also update the layer nums of all the layers.
	public void updateTimelineLayersPanelLayerNumbers() {
		ArrayList<Layer> layers = parent.getSession().getLayers();
		layersParentPane.removeAll();
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			layer.setLayerNum(i);
			if (layer.getColor() == Color.black) {
				layer.setColor(defaultColors.get(i % defaultColors.size()));
			}
			layersParentPane.add(layer);
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
	public void setSliderBarx(double x) {
		sliderBarx = x;
	}
	
	public double getSliderBarx() {
		return sliderBarx;
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
