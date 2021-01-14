package ui.menubar.menu;

import javax.swing.JMenu;

import ui.menubar.menu.menuitem.MenuItem;

public class StandardMenu extends JMenu implements Menu {

	StandardMenu(String name) {
		super(name);
	}
	
	@Override
	public JMenu getSwingComponent() {
		return this;
	}

	@Override
	public void addMenuItem(MenuItem mi) {
		add(mi.getSwingComponent());
	}

}
