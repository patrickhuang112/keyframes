package datatypes;

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

import keyframes.MouseAdapterFactory;
import keyframes.Session;



public class TimelineSlider extends JSlider {

	private Session session;
	private int sliderLineX = 0;
	
	public TimelineSlider(int orientation, int min, int max, int value, Session session) {
		super(orientation, min, max, value);
		this.session = session;
		addMouseListener(MouseAdapterFactory.clickToMouseAdapter);
		addMouseMotionListener(new MouseAdapter() {
			@Override 
			public void mouseDragged(MouseEvent e) {
				updateFromMouse(e);
			}
		});
		
		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_C && e.isControlDown()) {
					session.copyFramesFromCurrentLayerAndCurrentTime();
				} else if(e.getKeyCode() == KeyEvent.VK_V && e.isControlDown()) {
					session.pasteFramesToCurrentLayerAndCurrentTime();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
			}
		});
		
		TimelineSliderUI newUI = new TimelineSliderUI(this);
		newUI.installUI(this);
		this.setUI(newUI);
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		//TimelineSliderUI ui = (TimelineSliderUI) this.getUI();
	}
	
	public double updateFromMouse(MouseEvent e) {
	    TimelineSliderUI ui = (TimelineSliderUI) this.getUI();
	    int value = ui.valueForXPosition(e.getX());
	    session.setCurrentTimepoint(value);
	    session.refreshDrawPanel();
	    setValue(value); 
	    requestFocus();
	    return ui.getThumbRectMidX();
	}
}
