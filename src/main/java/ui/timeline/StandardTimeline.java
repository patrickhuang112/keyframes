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
	
	@Override
	public JPanel getSwingComponent() {
		return this;
	}

	@Override
	public void updateTimelineFromMouse(MouseEvent e) {
	    Controller.getController().updateTimelineFromMouseClick(e);
	}
}
