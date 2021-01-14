package ui.timeline;

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
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSliderUI;

import datatypes.DrawPoint;
import datatypes.Interval;
import datatypes.KeyFrames;
import datatypes.Layer;
import factories.EnumFactory;
import keyframes.Controller;
import keyframes.MagicValues;
import keyframes.Session;
import ui.UIComponent;
import ui.slider.SliderFactory;
import ui.slider.StandardTimelineSlider;
import ui.slider.StandardTimelineSliderUI;
import ui.slider.TimelineSlider;
import ui.timeline.layerspanel.StandardTimelineLayersPanel;
import ui.timeline.layerspanel.TimelineLayersPanel;

public class StandardTimeline extends JPanel implements Serializable, Timeline{
	
	private static final long serialVersionUID = -4549310200115960539L;
	private TimelineSlider timelineSlider;
	private JSlider timelineSliderComp;
	private StandardTimelineLayersPanel layersPane;
	private Session session;
	
	public StandardTimeline () {
		super();
		int dpw = MagicValues.timelineDefaultPreferredWidth;
		int dph = MagicValues.timelineDefaultPreferredHeight;
		int dmw = MagicValues.timelineDefaultMinWidth;
		int dmh = MagicValues.timelineDefaultMinHeight;
		// Default heights of the timeline on the bottom
		
		setBackground(Color.gray);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(dpw,dph));
		setMinimumSize(new Dimension(dmw, dmh));
	}
	
	private void buildSlider() {
		//In seconds
		int endSec = Controller.getController().getLongestTimeInSeconds();
		int endPoint= Controller.getController().getLongestTimepoint();
		//In current tick (not necessarily seconds)
		int cur = Controller.getController().getCurrentTimepoint();
		int fps = Controller.getController().getFramesPerSecond();
		
		
		timelineSliderComp = SliderFactory.createStandardTimelineSlider(0, endPoint, cur).getSwingComponent();
		
		timelineSliderComp.setMajorTickSpacing(fps);
		timelineSliderComp.setMinorTickSpacing(1);
		timelineSliderComp.setPaintTicks(true);
		timelineSliderComp.setPaintLabels(true);
		timelineSliderComp.setSnapToTicks(true);
		
		Controller.getController().setTimelineSlider(timelineSlider);
		
		Hashtable<Integer, JLabel> labelDict = new Hashtable<Integer, JLabel>();
		for(Integer i = 0; i < endSec; i++) {
			labelDict.put(i * fps, new JLabel(i.toString()));
		}
		
		//ADD THE ENDPOINT LABEL
		labelDict.put(endPoint, new JLabel(((Integer)endSec).toString()));
		timelineSliderComp.setLabelTable(labelDict);
		
		configureTimelineSliderListeners();
		
		mainTimelinePanel.add(timelineSliderComp, BorderLayout.NORTH);
	}
	
	private void buildLayers() {
		layersPane = new StandardTimelineLayersPanel(this);
		ArrayList<Layer> layers = Controller.getController().getLayers();
		for (Layer layer : layers) {
			//buildIndividualLayerTimeline(layer);
		}
		mainTimelinePanel.add(layersPane);
	}
	
	/*
	private void buildIndividualLayerTimeline(Layer layer) {
		KeyFrames frames = layer.getFrames();
		// THe last tick on the timeline slider
		int longestTimepoint = Controller.getController().getLongestTimepoint();
		
		
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
	}
	*/
	
	private void configureTimelineSliderListeners() {
		// MOUSE WITH SLIDER FUNCTIONALITY
		timelineSliderComp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					updateTimelineFromMouse(e);
				}
			}
		});
		timelineSliderComp.addMouseMotionListener(new MouseAdapter() {
			@Override 
			public void mouseDragged(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					updateTimelineFromMouse(e);
				}
			}
		});
	}
	
	public void buildUI() {

		
		buildSlider();
		buildLayers();
		
	}
	
	@Override
	public JPanel getSwingComponent() {
		return this;
	}

	@Override
	public void updateTimelineFromMouse(MouseEvent e) {
	    int value = timelineSlider.valueAtX(e.getX());
	    Controller.getController().updateTimelineFromValue(value);
	    timelineSliderComp.requestFocus();
	}

	
}
