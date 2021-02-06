package ui.menubar;

import javax.swing.JMenuBar;

import ui.UIComponent;
import ui.menubar.menu.KFMenu;

public interface KFMenuBar extends UIComponent {

	@Override
	public JMenuBar getSwingComponent();
	
	public void addMenu(KFMenu menu);
}
