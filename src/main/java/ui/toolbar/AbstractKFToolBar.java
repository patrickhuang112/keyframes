package ui.toolbar;

import java.awt.Dimension;

import javax.swing.JToolBar;

import ui.UIComponent;

public abstract class AbstractKFToolBar extends JToolBar implements KFToolBar {
	
	public static final int defaultWidth = 300;
	public static final int defaultHeight = 30;
	
	AbstractKFToolBar(int orientation) {
		super(orientation);
		int tbw = AbstractKFToolBar.defaultWidth;
		int tbh = AbstractKFToolBar.defaultHeight;
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
