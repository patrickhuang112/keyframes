package ui.toolbar;

import javax.swing.JToolBar;

import ui.UIComponent;

public interface KFToolBar extends UIComponent {
	
	public static final int defaultWidth = 300;
	public static final int defaultHeight = 30;
	
	@Override
	public JToolBar getSwingComponent ();
	
	public void addComponent(UIComponent comp);
}
