package ui.pane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import keyframes.Controller;

public class KFPaneFactory {
	
	public static KFPane createEmptyPane() {
		return new StandardKFPane();
	}
	
	public static KFPane createMainViewTopContainer() {
		int w = 0;
		int h = 60;
		
		KFPane topBar = new StandardKFPane();
		topBar.getSwingComponent().setLayout(new BorderLayout());
		topBar.getSwingComponent().setPreferredSize(new Dimension(w,h));
		topBar.getSwingComponent().setAlignmentX(Component.LEFT_ALIGNMENT);
		
		return topBar;
		
	}
	
	public static KFPane createMainViewToolBarAndProgressBarContainer() {
		KFPane container = new StandardKFPane();
		container.getSwingComponent().setLayout(new BoxLayout(container.getSwingComponent(), BoxLayout.X_AXIS));
		return container;
	}
	
	public static KFPane createTimelineAdditionalInfoPane() {
		int tw = 150; 
		int th = 50; 
				
		KFPane sp = new StandardKFPane();
		sp.getSwingComponent().setPreferredSize(new Dimension(tw, th));
		sp.getSwingComponent().setMaximumSize(new Dimension(tw, th));
		sp.getSwingComponent().setMinimumSize(new Dimension(tw, th));
		return sp;
	}
	
	public static KFPane createNamesAndAdditionalInfoContainerPane() {
		int mw = 140;
		int mh = 0;
		
		int pw = 140;
		int ph = 0;
		
		KFPane sp = new StandardKFPane();
		sp.getSwingComponent().setLayout(new BoxLayout(sp.getSwingComponent(), BoxLayout.Y_AXIS));
		sp.getSwingComponent().setMinimumSize(new Dimension(mw, mh));
		sp.getSwingComponent().setPreferredSize(new Dimension(pw, ph));
		return sp;
	}
	
	public static KFPane createTimelineSliderAndRectanglesContainerPane() {

		int mw = 600;
		int mh = 0;
		
		int pw = 600;
		int ph = 0;
		
		KFPane sp = new StandardKFPane();
		sp.getSwingComponent().setLayout(new BoxLayout(sp.getSwingComponent(), BoxLayout.Y_AXIS));
		sp.getSwingComponent().setMinimumSize(new Dimension(mw, mh));
		sp.getSwingComponent().setPreferredSize(new Dimension(pw, ph));
		
		//BANDAID FIX
		sp.getSwingComponent().addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				Controller.getController().updateTimelineFromSplitPaneResize();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			
		});
		
		return sp;
	}
	
}	
