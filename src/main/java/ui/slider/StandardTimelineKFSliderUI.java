package ui.slider;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicSliderUI;

import ui.pane.timeline.layerspanel.StandardRectanglesKFLayerPane;

public class StandardTimelineKFSliderUI extends BasicSliderUI {
	
	private Hashtable<Integer, JLabel> timelineLabelsDict = new Hashtable<Integer, JLabel>();
	
	public StandardTimelineKFSliderUI(JSlider slider) {
		super(slider);
		slider.setEnabled(false);
	}
	
	// Gets the center of the thumb rect of the slider, which allows for the drawing of the line down below
	public double getThumbMidX() {
		return thumbRect.getCenterX();
	}
	
	// NEEDED FOR LAYERS UI
	public int getSpacingBetweenTicks() {
		return xPositionForValue(1) - valueForXPosition(0);
	}
	
	public Hashtable<Integer, JLabel> getTimelineLabelsDict() {
		return timelineLabelsDict;
	}
	
	@Override
	public void paintThumb(Graphics g) {
		super.paintThumb(g);
		Graphics2D g2d = (Graphics2D)g;

		int bottomy = (int)thumbRect.getMaxY();
        int midx = (int)thumbRect.getCenterX();
        
		
		GeneralPath gp =  new GeneralPath();
		BasicStroke s = null;
		
		gp.moveTo(midx, bottomy);
		g2d.setPaint(Color.black);
		s = new BasicStroke(StandardRectanglesKFLayerPane.defaultLineXRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2d.setStroke(s);
		
		// 50 is an arbitrary number just to make sure it draws on the screen properly
		gp.lineTo(midx, bottomy + StandardRectanglesKFLayerPane.defaultLineYRadius);
		
		g2d.draw(gp);
	}

}
