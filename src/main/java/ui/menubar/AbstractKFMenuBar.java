package ui.menubar;

import javax.swing.JMenuBar;

import ui.menubar.menu.KFMenu;

abstract class AbstractKFMenuBar extends JMenuBar implements KFMenuBar {
	public static final int defaultWidth = 300;
	public static final int defaultHeight = 30;
	
	public JMenuBar getSwingComponent() {
		return this;
	}
	
	public void addMenu(KFMenu menu) {
		add(menu.getSwingComponent());
	}
}
