package ui.timeline;

import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import keyframes.Controller;

abstract class AbstractKFTimeline extends JPanel implements KFTimeline {
	
	
	@Override
	public JPanel getSwingComponent() {
		return this;
	}

	@Override
	public void updateTimelineFromMouse(MouseEvent e) {
	    Controller.getController().updateTimelineFromMouseClick(e);
	}
}
