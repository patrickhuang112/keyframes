package keyframes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class Timeline implements UIComponent, Serializable{
	
	private static final long serialVersionUID = -4549310200115960539L;
	private UIComponent parent;
	private JPanel mainTimelinePanel;
	
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
	}
	
	private void buildSlider() {
		//In seconds
		int endSec = getSession().getLongestTimeInSeconds();
		int endPoint= getSession().getLongestTimepoint();
		//In current tick (not necessarily seconds)
		int cur = getSession().getCurrentTimepoint();
		int fps = getSession().getFramesPerSecond();
		
		
		JSlider timelineSlider = new JSlider(JSlider.HORIZONTAL, 
				//STARTPOINT
				0, 
				//ENDPOINT
				endPoint,
				//INTIALPOINT
				cur);

		getSession().setTimelineSlider(timelineSlider);
		
		
		timelineSlider.setMajorTickSpacing(fps);
		timelineSlider.setMinorTickSpacing(1);
		timelineSlider.setPaintTicks(true);
		timelineSlider.setPaintLabels(true);
		timelineSlider.setSnapToTicks(true);
		
		
		Hashtable<Integer, JLabel> labelDict = new Hashtable<Integer, JLabel>();
		for(Integer i = 0; i < endSec; i++) {
			labelDict.put(i * fps, new JLabel(i.toString()));
		}
		//ADD THE ENDPOINT LABEL
		labelDict.put(endPoint, new JLabel(((Integer)endSec).toString()));
		timelineSlider.setLabelTable(labelDict);
		
		timelineSlider.addMouseListener(MouseAdapterFactory.clickToMouseAdapter);
		timelineSlider.addMouseListener(new MouseAdapter( ) {
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
		
		timelineSlider.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_C && e.isControlDown()) {
					getSession().copyFrames();
				} else if(e.getKeyCode() == KeyEvent.VK_V && e.isControlDown()) {
					getSession().pasteFrames();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mainTimelinePanel.add(timelineSlider, BorderLayout.CENTER);
	}
	
	
	private void updateFromMouse(MouseEvent e) {
		JSlider sourceSlider=(JSlider)e.getSource();
	    BasicSliderUI ui = (BasicSliderUI)sourceSlider.getUI();
	    int value = ui.valueForXPosition(e.getX());
	    getSession().setCurrentTimepoint(value);
	    getSession().refreshDrawPanel();
	}
	
	public void build() {
		mainTimelinePanel.setBackground(Color.gray);
		//CHANGELATER
		mainTimelinePanel.setPreferredSize(new Dimension(0,200));
		mainTimelinePanel.setLayout(new BorderLayout());
		buildSlider();
	}

	@Override
	public Session getSession() {
		return parent.getSession();
	}
}
