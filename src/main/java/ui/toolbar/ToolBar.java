package ui.toolbar;

import javax.swing.JToolBar;

import ui.UIComponent;

public interface ToolBar extends UIComponent {
	
	@Override
	public JToolBar getSwingComponent ();
	
	public void addComponent(UIComponent comp);
}
