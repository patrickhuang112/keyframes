package ui.splitpane;

import javax.swing.JSplitPane;

import ui.UIComponent;

public class KFSplitPaneFactory {
	
	public static KFSplitPane createVerticalSplitPane() {
		return new VerticalKFSplitPane();
	}
	
	public static KFSplitPane createHorizontalSplitPane() {
		return new HorizontalKFSplitPane();
	}
	
}
