package ui.toolbar;

import java.awt.Dimension;

import javax.swing.JToolBar;

import ui.UIComponent;

abstract class AbstractKFToolBar extends JToolBar implements KFToolBar {
	
	AbstractKFToolBar(int orientation) {
		super(orientation);
		int tbw = KFToolBar.defaultWidth;
		int tbh = KFToolBar.defaultHeight;
		setPreferredSize(new Dimension(tbw,tbh));
		setFloatable(false);
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
