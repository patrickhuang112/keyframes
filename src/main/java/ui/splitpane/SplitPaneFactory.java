package ui.splitpane;

import ui.UIComponent;

public class SplitPaneFactory {
	
	public static SplitPane createHorizontalSplitPane() {
		return new VerticalSplitPane();
	}
	
}
