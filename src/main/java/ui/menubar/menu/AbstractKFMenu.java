package ui.menubar.menu;

import javax.swing.JMenu;

import ui.menubar.menu.menuitem.KFMenuItem;

abstract class AbstractKFMenu extends JMenu implements KFMenu {

	AbstractKFMenu(String name) {
		super(name);
	}
	
	@Override
	public JMenu getSwingComponent() {
		return this;
	}

	@Override
	public void addMenuItem(KFMenuItem mi) {
		add(mi.getSwingComponent());
	}

}
