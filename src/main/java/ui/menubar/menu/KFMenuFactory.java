package ui.menubar.menu;

import ui.menubar.menu.menuitem.KFMenuItemFactory;

public class KFMenuFactory {
	public static KFMenu createFileMenu() {
		return new StandardKFMenu("File");
		//m.addMenuItem(MenuItemFactory.createNewProjectMenuItem());
		//m.addMenuItem(MenuItemFactory.createOpenProjectMenuItem());
		//m.addMenuItem(MenuItemFactory.createSaveMenuItem());
		//m.addMenuItem(MenuItemFactory.createSaveAsMenuItem());
		//m.addMenuItem(MenuItemFactory.createRenderMP4MenuItem());
		//m.addMenuItem(MenuItemFactory.createRenderGIFMenuItem());
		//m.addMenuItem(MenuItemFactory.createSettingsMenuItem());
	}
	
	public static KFMenu createEditMenu() {
		return new StandardKFMenu("Edit");
		//m.addMenuItem(MenuItemFactory.createEditFPSMenuItem());
		//m.addMenuItem(MenuItemFactory.createEditCompositionLengthMenuItem());
		//m.addMenuItem(MenuItemFactory.createBackgroundColorMenuItem());
	}
}
