package ui.timeline.layerspanel;

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
import keyframes.MagicValues;
import keyframes.Session;
import ui.UIComponent;
import ui.layer.rectangles.KFLayerRectangles;
import ui.layer.rectangles.KFLayerRectanglesFactory;
import ui.timeline.Timeline;

public class StandardTimelineLayersPanel extends JScrollPane implements TimelineLayersPanel, Serializable{
	
	// Set this manually cause I couldn't get other things to work
	private double sliderBarx = MagicValues.timelineLayersPanelDefaultTimeIndicatorLineX;
	private final int offset = MagicValues.timelineLayersPanelDefaultTimeIndicatorLineOffset;
	private JViewport viewport;
	private JPanel layersParentPane;
	private Session session;
	private ArrayList<Color> defaultColors = new ArrayList<>();
	
	private Layer layerBeingDragged = null;
	private ArrayList<Layer> layersCopy = null;
	
	
	
	StandardTimelineLayersPanel() {
		super();
		this.viewport = this.getViewport();
		this.layersParentPane = new JPanel();
		initializeDefaultColors();
		
		layersParentPane.setLayout(new BoxLayout(layersParentPane, BoxLayout.Y_AXIS));
		layersParentPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		viewport.add(layersParentPane);
		setupLayersParentPaneListeners();
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
				
				Layer layer = Controller.getController().selectLayer(e);
				if (SwingUtilities.isLeftMouseButton(e) && layer != null) {
					layerBeingDragged = layer;
					layersCopy = Controller.getController().deepCopyLayers();
					// This is so that only when the mouse is really being held will the layer
					// turn into a ghost visually
					// Mainly because without this, it keeps flickering whenever there is any simple
					// mouse click which is pretty annoying
					new Timer().schedule( 
				            new TimerTask() {
				                @Override
				                public void run() {
				                	if (layerBeingDragged != null) {
				                		layerBeingDragged.setGhost(true);
				                		Controller.getController().refreshUI();
				                	}
				                }
				            }, MagicValues.timelineLayersPanelDefaultWaitTimeBeforeGhostActivatesAfterClick);
				}
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (layerBeingDragged != null) {
					layerBeingDragged.setGhost(false);
					layerBeingDragged = null;
					Controller.getController().refreshUI();
				}
			}
			
		});
		layersParentPane.addMouseMotionListener(new MouseAdapter() {
			@Override 
			public void mouseDragged(MouseEvent e) {
				
				if (layerBeingDragged != null) {
					ArrayList<Layer> layers = Controller.getController().getLayers();
					for (int i = 0; i < layers.size(); i++) {
						Layer layer = layers.get(i);
						if(layer.inBounds(e.getX(), e.getY())) {
							int curLayNum = Controller.getController().getCurrentLayerNum();
							
							//ASSERT statements are just for piece of mind so I know i'm doing the right
							//logic thing here
							assert(curLayNum == layerBeingDragged.getLayerNum());
							int boundLayNum = layer.getLayerNum();
							assert(boundLayNum == i);
							if (curLayNum != boundLayNum) {
								layers.remove(curLayNum);
								layers.add(boundLayNum, layerBeingDragged);
								Controller.getController().setCurrentLayerNum(i);
							}
							break;
						}
					}
					Controller.getController().refreshUI();
				}
			}
		});
	}
	
	private void displayRightClickMenu(MouseEvent e) {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem newLayerMenuItem = new JMenuItem(new AbstractAction("New layer") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().addNewLayer();
			}
		});
		menu.add(newLayerMenuItem);
		
		Layer layer = Controller.getController().selectLayer(e);
		if (layer != null) {
			// We do this first here, JUST so the selected black box shows around the layer
			JMenuItem recolorLayerMenuItem = new JMenuItem(new AbstractAction("Change layer color") {
				@Override
				public void actionPerformed(ActionEvent ae) {
					Color newColor = JColorChooser.showDialog(null, "Choose a color", layer.getColor());
					if (newColor != null) {
						layer.setColor(newColor);
					}
					// We do this here to update the color of the layer straightaway
					// Kind of messy butwhatever
				}
			});
			
			JMenuItem copyFramesFromCurrentLayerMenuItem = new JMenuItem(new AbstractAction("Copy frame from current time and layer") {
				@Override
				public void actionPerformed(ActionEvent ae) {
					Controller.getController().copyFramesFromCurrentLayerAndCurrentTime();
				}
			});
			
			JMenuItem pasteFramesOntoCurrentLayerMenuItem = new JMenuItem(new AbstractAction("Paste frame") {
				@Override
				public void actionPerformed(ActionEvent ae) {
					Controller.getController().pasteFramesToCurrentLayerAndCurrentTime();
				}
			});
			
			menu.add(recolorLayerMenuItem);
			menu.add(copyFramesFromCurrentLayerMenuItem);
			menu.add(pasteFramesOntoCurrentLayerMenuItem);
		}
		
		menu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	public void updateTimelineFromMouseClick(MouseEvent e) {
		Controller.getController().selectLayer(e);
		Controller.getController().updateTimelineFromMouseClick(e);
	}
	
	// This is where we also update the layer nums of all the layers.
	public void updateTimelineLayersPanelLayerNumbers() {
		ArrayList<Layer> layers = Controller.getController().getLayers();
		layersParentPane.removeAll();
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			layer.setLayerNum(i);
			if (layer.getColor() == Color.black) {
				layer.setColor(defaultColors.get(i % defaultColors.size()));
			}
			KFLayerRectangles layerUI = KFLayerRectanglesFactory.createStandardKFLayerRectangles(layer);
			layersParentPane.add(layerUI.getSwingComponent());
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
		gp.moveTo(sliderBarx, this.getY() - MagicValues.timelineLayersPanelDefaultTimeIndicatorLineYRadius);
		
		g2d.setPaint(Color.black);
		s = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2d.setStroke(s);
		
		// Big values just to make sure it draws over the whole panel, look for a cleaner solution some other time
		gp.lineTo(sliderBarx, this.getY() + MagicValues.timelineLayersPanelDefaultTimeIndicatorLineYRadius);
		
		g2d.draw(gp);
			
	}

	@Override
	public JScrollPane getSwingComponent() {
		return this;
	}

	@Override
	public void updateLayersPanelUI(double newMarkerX) {
		setSliderBarx(newMarkerX);
		    
	    //Updates the layers order on the layers panel
	    updateTimelineLayersPanelLayerNumbers();
	    
	    repaint();
		revalidate();
	}

	@Override
	public void buildLayersPanelLayers() {
		updateTimelineLayersPanelLayerNumbers();
	}
	
}
