package ui.menubar;

import java.awt.Dimension;

import javax.swing.JMenuBar;

import ui.menubar.menu.Menu;

public class StandardKFMenuBar extends AbstractKFMenuBar {

	StandardKFMenuBar() {
		this.setPreferredSize(new Dimension(AbstractKFMenuBar.defaultWidth, AbstractKFMenuBar.defaultHeight));
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
