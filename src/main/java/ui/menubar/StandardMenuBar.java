package ui.menubar;

import java.awt.Dimension;

import javax.swing.JMenuBar;

import keyframes.MagicValues;
import ui.menubar.menu.Menu;

public class StandardMenuBar extends JMenuBar implements MenuBar {

	public static final int dw = MagicValues.mainViewTopMenuBarDefaultWidth;
	public static final int dh = MagicValues.mainViewTopMenuBarDefaultHeight;
	
	StandardMenuBar() {
		this.setPreferredSize(new Dimension(dw, dh));
	}
	@Override
	public JMenuBar getSwingComponent() {
		return this;
	}
	@Override
	public void addMenu(Menu menu) {
		add(menu.getSwingComponent());
	}
	
}
