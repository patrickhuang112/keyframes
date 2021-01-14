package ui.slider;

import java.awt.BasicStroke;
import java.awt.Color;
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
import javax.swing.plaf.basic.BasicSliderUI;

import keyframes.Session;



public class StandardTimelineSlider extends JSlider implements TimelineSlider{

	private static final int defaultOrientation = JSlider.HORIZONTAL;
	
	public StandardTimelineSlider(int min, int max, int value) {
		super(StandardTimelineSlider.defaultOrientation, min, max, value);
		StandardTimelineSliderUI newUI = new StandardTimelineSliderUI(this);
		newUI.installUI(this);
		this.setUI(newUI);
	}
	
	@Override
	public StandardTimelineSliderUI getUI() {
		return (StandardTimelineSliderUI)(super.getUI());
	}

	@Override
	public JSlider getSwingComponent() {
		return this;
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

	
	
}
