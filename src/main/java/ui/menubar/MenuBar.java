package ui.menubar;

import javax.swing.JMenuBar;

import ui.UIComponent;
import ui.menubar.menu.Menu;

public interface MenuBar extends UIComponent {

	@Override
	public JMenuBar getSwingComponent();
	
	public void addMenu(Menu menu);
}
