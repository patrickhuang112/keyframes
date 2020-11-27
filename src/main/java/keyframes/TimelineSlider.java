package keyframes;

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



public class TimelineSlider extends JSlider {

	public TimelineSlider(int orientation, int min, int max, int value, Session session) {
		super(orientation, min, max, value);
		TimelineSliderUI newUI = new TimelineSliderUI(this);
		newUI.installUI(this);
		this.setUI(newUI);
	}
	
	@Override
	public TimelineSliderUI getUI() {
		return (TimelineSliderUI)(super.getUI());
	}
}
