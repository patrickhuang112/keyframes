package ui.pane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import keyframes.Controller;
import keyframes.MagicValues;

public class PaneFactory {
	
	public static Pane createEmptyPane() {
		return new StandardPane();
	}
	
	public static Pane createMainViewTopContainer() {
		int tw = MagicValues.mainViewTopBarDefaultWidth;
		int th = MagicValues.mainViewTopBarDefaultHeight;
		
		Pane topBar = new StandardPane();
		topBar.getSwingComponent().setLayout(new BorderLayout());
		topBar.getSwingComponent().setPreferredSize(new Dimension(tw,th));
		topBar.getSwingComponent().setAlignmentX(Component.LEFT_ALIGNMENT);
		
		return topBar;
		
	}
	
	public static Pane createMainViewToolBarAndProgressBarContainer() {
		Pane container = new StandardPane();
		container.getSwingComponent().setLayout(new BoxLayout(container.getSwingComponent(), BoxLayout.X_AXIS));
		return container;
	}
	
	public static Pane createTimelineAdditionalInfoPane() {
		int tw = MagicValues.timelineAdditionalInfoPaneDefaultWidth;
		int th = MagicValues.timelineAdditionalInfoPaneDefaultHeight;
		
		Pane sp = new StandardPane();
		sp.getSwingComponent().setPreferredSize(new Dimension(tw, th));
		sp.getSwingComponent().setMaximumSize(new Dimension(tw, th));
		sp.getSwingComponent().setMinimumSize(new Dimension(tw, th));
		return sp;
	}
	
	public static Pane createNamesAndAdditionalInfoContainerPane() {
		int mw = MagicValues.timelineNamesAndAdditionalInfoContainerPanelMinimumWidth;
		int mh = MagicValues.timelineNamesAndAdditionalInfoContainerPanelMinimumHeight;
		
		int pw = MagicValues.timelineNamesAndAdditionalInfoContainerPanelPreferredWidth;
		int ph = MagicValues.timelineNamesAndAdditionalInfoContainerPanelPreferredHeight;
		
		Pane sp = new StandardPane();
		sp.getSwingComponent().setLayout(new BoxLayout(sp.getSwingComponent(), BoxLayout.Y_AXIS));
		sp.getSwingComponent().setMinimumSize(new Dimension(mw, mh));
		sp.getSwingComponent().setPreferredSize(new Dimension(pw, ph));
		return sp;
	}
	
	public static Pane createTimelineSliderAndRectanglesContainerPane() {
		int mw = MagicValues.timelineSliderAndRectanglesContainerPanelMinimumWidth;
		int mh = MagicValues.timelineSliderAndRectanglesContainerPanelMinimumHeight;
		
		int pw = MagicValues.timelineSliderAndRectanglesContainerPanelPreferredWidth;
		int ph = MagicValues.timelineSliderAndRectanglesContainerPanelPreferredHeight;
		
		
		Pane sp = new StandardPane();
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
