package ui.splitpane;

import javax.swing.JSplitPane;

import ui.UIComponent;

public class SplitPaneFactory {
	
	public static SplitPane createVerticalSplitPane() {
		return new KFSplitPane(JSplitPane.VERTICAL_SPLIT);
	}
	
	public static SplitPane createHorizontalSplitPane() {
		return new KFSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	}
	
}
