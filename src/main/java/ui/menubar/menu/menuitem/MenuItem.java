package ui.menubar.menu.menuitem;

import javax.swing.JMenuItem;

import ui.UIComponent;

public interface MenuItem extends UIComponent {
	
	@Override 
	public JMenuItem getSwingComponent();

}
