package ui.pane.timeline.layerspanel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ui.UIComponent;
import ui.pane.KFPane;
import ui.pane.timeline.KFLayerPane;

public interface RectanglesKFLayerPane extends KFLayerPane {
	
	public void updateMarkerAndRefresh(double newMarkerX);
}
