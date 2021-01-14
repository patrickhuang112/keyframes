package ui.menubar.menu;

import javax.swing.JMenu;

import ui.UIComponent;
import ui.menubar.menu.menuitem.MenuItem;

public interface Menu extends UIComponent {
	@Override
	public JMenu getSwingComponent();
	
	public void addMenuItem(MenuItem mi);
}
