package ui.splitpane;

import java.awt.Dimension;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import datatypes.SessionObject;
import keyframes.Session;
import ui.UIComponent;

public class VerticalSplitPane extends JSplitPane implements Serializable, SplitPane {

	private static final long serialVersionUID = -4937128517824428147L;

	private JSplitPane splitPane;
	
	private UIComponent topOrLeft;
	private UIComponent bottomOrRight;
	
	VerticalSplitPane() {
		super(JSplitPane.VERTICAL_SPLIT);
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
