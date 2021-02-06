package ui.timeline;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import ui.UIComponent;
import ui.pane.timeline.layerspanel.RectanglesKFLayerPane;
import ui.slider.KFTimelineSlider;

public interface KFTimeline extends UIComponent {
	
	@Override
	public JPanel getSwingComponent();
	public void updateTimelineFromMouse(MouseEvent e);
}

