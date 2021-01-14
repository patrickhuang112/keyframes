package ui.splitpane;

import javax.swing.JSplitPane;

import ui.UIComponent;

public interface SplitPane extends UIComponent {
	
	@Override
	public JSplitPane getSwingComponent();
	
	public void addTopLeftComponent(UIComponent c);
	public void addBottomRightComponent(UIComponent c);
}
