package ui.menubar;

import ui.menubar.menu.MenuFactory;

public class MenuBarFactory {
	
	public static MenuBar createMainViewTopMenuBar() {
		return new StandardMenuBar();
		//m.addMenu(MenuFactory.createFileMenu());
		//m.addMenu(MenuFactory.createEditMenu());
	}
	
}
