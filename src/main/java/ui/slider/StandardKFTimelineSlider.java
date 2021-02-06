package ui.slider;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSliderUI;

import keyframes.Controller;
import keyframes.Session;



public class StandardKFTimelineSlider extends AbstractKFSlider implements KFTimelineSlider{

	private static final int defaultOrientation = JSlider.HORIZONTAL;
	public static final int preferredWidth = 1400;
	public static final int preferredHeight = 50;
	
	
	public StandardKFTimelineSlider(int min, int max, int value, int endSec, int fps) {
		super(StandardKFTimelineSlider.defaultOrientation, min, max, value);
		StandardTimelineKFSliderUI newUI = new StandardTimelineKFSliderUI(this);
		newUI.installUI(this);
		this.setUI(newUI);
		
		setMajorTickSpacing(fps);
		setMinorTickSpacing(1);
		setPaintTicks(true);
		setPaintLabels(true);
		setSnapToTicks(true);
		
		int pw = preferredWidth;
		int ph = preferredHeight;
		
		this.setPreferredSize(new Dimension(pw, ph));
		
		Hashtable<Integer, JLabel> labelDict = new Hashtable<Integer, JLabel>();
		for(Integer i = 0; i < endSec; i++) {
			labelDict.put(i * fps, new JLabel(i.toString()));
		}
		
		//ADD THE ENDPOINT LABEL
		labelDict.put(max, new JLabel(((Integer)endSec).toString()));
		setLabelTable(labelDict);
		
		addMouseListeners();
		
	}
	
	private void addMouseListeners() {
		// MOUSE WITH SLIDER FUNCTIONALITY
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					Controller.getController().updateTimelineFromMouseClick(e);
				}
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@Override 
			public void mouseDragged(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					Controller.getController().updateTimelineFromMouseClick(e);
				}
			}
		});
	}
	
	
	@Override
	public StandardTimelineKFSliderUI getUI() {
		return (StandardTimelineKFSliderUI)(super.getUI());
	}

	@Override
	public int getSpacingBetweenTicks() {
		return getUI().getSpacingBetweenTicks();
	}

	@Override
	public void updateSlider(int newValue) {
		setValue(newValue);
	}

	@Override
	public double getThumbMidX() {
		return getUI().getThumbMidX();
	}

	@Override
	public int valueAtX(int x) {
		return getUI().valueForXPosition(x);
	}

	@Override
	public void updateSliderUIFromFPSOrLengthChange(int newFPS, int newLengthInSeconds) {
		updateSliderEnd(newLengthInSeconds * newFPS);
		setMajorTickSpacing(newFPS);
		
		Hashtable<Integer, JLabel> timelineLabelsDict = getUI().getTimelineLabelsDict();
		timelineLabelsDict.clear();
		//
		timelineLabelsDict = new Hashtable<Integer, JLabel>();
		
		for(Integer i = 0; i < newLengthInSeconds; i++) {
			timelineLabelsDict.put(i * newFPS, new JLabel(i.toString()));
		}
		//ADD THE ENDPOINT LABEL
		timelineLabelsDict.put(newLengthInSeconds * newFPS, 
				new JLabel(((Integer)newLengthInSeconds).toString()));
		setLabelTable(timelineLabelsDict);
		
	}

	
	
}
