package ui.toolbar;

import javax.swing.JToolBar;

import ui.UIComponent;

public class StandardToolBar extends JToolBar implements ToolBar {

	StandardToolBar(){
		super();
	}
	@Override
	public JToolBar getSwingComponent() {
		return this;
	}
	@Override
	public void addComponent(UIComponent comp) {
		this.add(comp.getSwingComponent());
	}
	
}
