package keyframes;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicSliderUI;

import datatypes.DrawPoint;
import datatypes.Interval;
import datatypes.KeyFrames;
import datatypes.Layer;
import datatypes.UIComponent;

public class Timeline extends JComponent implements UIComponent, Serializable{
	
	private static final long serialVersionUID = -4549310200115960539L;
	private UIComponent parent;
	private JPanel mainTimelinePanel;
	private TimelineSlider timelineSlider;
	private TimelineLayersPanel layersPane;
	
	@Override
	public void addChild(UIComponent child) {
		mainTimelinePanel.add(child.getMainComponent());
	}
	
	@Override
	public JComponent getMainComponent() {
		return mainTimelinePanel;
	}
	
	public Timeline(UIComponent parent) {
		this(parent, true);
	}
	
	public Timeline(UIComponent parent, boolean addToParent) {
		this.parent = parent;
		mainTimelinePanel = new JPanel();
		if(addToParent) { 
			this.parent.getMainComponent().add(mainTimelinePanel, BorderLayout.PAGE_END);
		}
		
		// Key bindings for anything within the mainTimelinePanel window
		mainTimelinePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control C"), "copied");
		mainTimelinePanel.getActionMap().put("copied", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getSession().copyFramesFromCurrentLayerAndCurrentTime();
			}
			
		});
		// Key bindings for anything within the mainTimelinePanel window
		mainTimelinePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control V"), "pasted");
		mainTimelinePanel.getActionMap().put("pasted", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getSession().pasteFramesToCurrentLayerAndCurrentTime();
			}
			
		});
	}
	
	private void buildSlider() {
		//In seconds
		int endSec = getSession().getLongestTimeInSeconds();
		int endPoint= getSession().getLongestTimepoint();
		//In current tick (not necessarily seconds)
		int cur = getSession().getCurrentTimepoint();
		int fps = getSession().getFramesPerSecond();
		
		
		timelineSlider = new TimelineSlider(JSlider.HORIZONTAL, 
				//STARTPOINT
				0, 
				//ENDPOINT
				endPoint,
				//INTIALPOINT
				cur, 
				//SESSION OBJ
				getSession());
		
		timelineSlider.setMajorTickSpacing(fps);
		timelineSlider.setMinorTickSpacing(1);
		timelineSlider.setPaintTicks(true);
		timelineSlider.setPaintLabels(true);
		timelineSlider.setSnapToTicks(true);
		
		getSession().setTimelineSlider(timelineSlider);
		
		Hashtable<Integer, JLabel> labelDict = new Hashtable<Integer, JLabel>();
		for(Integer i = 0; i < endSec; i++) {
			labelDict.put(i * fps, new JLabel(i.toString()));
		}
		
		//ADD THE ENDPOINT LABEL
		labelDict.put(endPoint, new JLabel(((Integer)endSec).toString()));
		timelineSlider.setLabelTable(labelDict);
		
		// MOUSE WITH SLIDER FUNCTIONALITY
		timelineSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				updateFromMouse(e);
			}
		});
		timelineSlider.addMouseMotionListener(new MouseAdapter() {
			@Override 
			public void mouseDragged(MouseEvent e) {
				updateFromMouse(e);
			}
		});
		
		
		/*
		timelineSlider.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}
			
			// COPY PASTE FUNCTIONALITY
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_C && e.isControlDown()) {
					System.out.println("Copied");
					getSession().copyFramesFromCurrentLayerAndCurrentTime();
				} else if(e.getKeyCode() == KeyEvent.VK_V && e.isControlDown()) {
					System.out.println("Pasted");
					getSession().pasteFramesToCurrentLayerAndCurrentTime();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
			}
		});
		*/
		
		
		mainTimelinePanel.add(timelineSlider, BorderLayout.NORTH);
	}
	
	private void buildLayers() {
		layersPane = new TimelineLayersPanel();
		
		ArrayList<Layer> layers = getSession().getLayers();
		for (Layer layer : layers) {
			buildIndividualLayerTimeline(layer);
		}
		mainTimelinePanel.add(layersPane);
	}
	
	private void buildIndividualLayerTimeline(Layer layer) {
		KeyFrames frames = layer.getFrames();
		// THe last tick on the timeline slider
		int longestTimepoint = getSession().getLongestTimepoint();
		
		
		ArrayList<Interval> layerIntervals = new ArrayList<>();
		Interval current = new Interval();
		for (int i = 0; i <= longestTimepoint; i++ ) {
			// Our interval hasn't started, and we now find something in our timeline
			if (frames.containsKey(i) && current.getStart() == null) {
				current.setStart(i);
			} 
			// Our interval has started, and we just came upon a time where there isn't an entry in frames, so
			// end the interval and create a new interval object
			else if (!frames.containsKey(i) && current.getStart() != null){
				current.setEnd(i-1);
				layerIntervals.add(current);
				current = new Interval();
			}
		}
		// If our interval has a start, then that means it goes from that start all the way to the end
		if (current.getStart() != null) {
			current.setEnd(longestTimepoint);
			layerIntervals.add(current);
		} 
		drawLayerIntervals(layerIntervals);
	}
	
	// Draw all of the boxes
	private void drawLayerIntervals(ArrayList<Interval> intervals) {
		// Need to first write code to get slider info stuff
	}
	
	
	public void build() {
		mainTimelinePanel.setBackground(Color.gray);
		//CHANGELATER
		mainTimelinePanel.setPreferredSize(new Dimension(0,200));
		mainTimelinePanel.setLayout(new BorderLayout());
		buildSlider();
		buildLayers();
		layersPane.addMouseListener(new MouseAdapter() {
			@Override 
			public void mouseClicked(MouseEvent e) {
				updateFromMouse(e);
			}
			@Override 
			public void mouseReleased(MouseEvent e) {
				updateFromMouse(e);
			}
		});
		layersPane.addMouseMotionListener(new MouseAdapter() {
			@Override 
			public void mouseDragged(MouseEvent e) {
				updateFromMouse(e);
			}
		});
	}

	
	public void updateFromMouse(MouseEvent e) {
		TimelineSliderUI ui = timelineSlider.getUI();
	    int value = ui.valueForXPosition(e.getX());
	    getSession().setCurrentTimepoint(value);
	    getSession().refreshDrawPanel();
	    
	    timelineSlider.setValue(value); 
	    timelineSlider.requestFocus();
	    
	    double sliderBarx = ui.getThumbRectMidX();
	    layersPane.setSliderBarx(sliderBarx);
		layersPane.repaint();
	}
	
	@Override
	public Session getSession() {
		return parent.getSession();
	}
}
