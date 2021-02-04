package ui.splitpane;

import javax.swing.JSplitPane;

import ui.UIComponent;

public class KFSplitPane extends JSplitPane implements SplitPane{

	private UIComponent topOrLeft;
	private UIComponent bottomOrRight;
	
	KFSplitPane(int orientation) {
		super(orientation);
	}
	
	@Override
	public JSplitPane getSwingComponent() {
		return this;
	}
	
	@Override
	public void addTopLeftComponent(UIComponent c) {
		topOrLeft = c;
		this.setTopComponent(c.getSwingComponent());
	}

	@Override
	public void addBottomRightComponent(UIComponent c) {
		bottomOrRight = c;
		this.setBottomComponent(c.getSwingComponent());
	}
}
