package ui.splitpane;

import javax.swing.JSplitPane;

import ui.UIComponent;

abstract class AbstractKFSplitPane extends JSplitPane implements KFSplitPane{

	private UIComponent topOrLeft;
	private UIComponent bottomOrRight;
	
	AbstractKFSplitPane(int orientation) {
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
