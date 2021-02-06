package ui.menubar.menu;

import javax.swing.JMenu;

import ui.UIComponent;
import ui.menubar.menu.menuitem.KFMenuItem;

public interface KFMenu extends UIComponent {
	@Override
	public JMenu getSwingComponent();
	
	public void addMenuItem(KFMenuItem mi);
}
