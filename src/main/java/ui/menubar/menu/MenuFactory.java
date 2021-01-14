package ui.menubar.menu;

import ui.menubar.menu.menuitem.MenuItemFactory;

public class MenuFactory {
	public static Menu createFileMenu() {
		return new StandardMenu("File");
		//m.addMenuItem(MenuItemFactory.createNewProjectMenuItem());
		//m.addMenuItem(MenuItemFactory.createOpenProjectMenuItem());
		//m.addMenuItem(MenuItemFactory.createSaveMenuItem());
		//m.addMenuItem(MenuItemFactory.createSaveAsMenuItem());
		//m.addMenuItem(MenuItemFactory.createRenderMP4MenuItem());
		//m.addMenuItem(MenuItemFactory.createRenderGIFMenuItem());
		//m.addMenuItem(MenuItemFactory.createSettingsMenuItem());
	}
	
	public static Menu createEditMenu() {
		return new StandardMenu("Edit");
		//m.addMenuItem(MenuItemFactory.createEditFPSMenuItem());
		//m.addMenuItem(MenuItemFactory.createEditCompositionLengthMenuItem());
		//m.addMenuItem(MenuItemFactory.createBackgroundColorMenuItem());
	}
}
