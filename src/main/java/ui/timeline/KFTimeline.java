package ui.timeline;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import ui.UIComponent;
import ui.slider.TimelineSlider;
import ui.timeline.layerspanel.TimelineLayersPanel;

public interface KFTimeline extends UIComponent {
	
	@Override
	public JPanel getSwingComponent();
	public void updateTimelineFromMouse(MouseEvent e);
}

