package ui.menubar;

import java.awt.Dimension;

import javax.swing.JMenuBar;

import ui.menubar.menu.KFMenu;

public class StandardKFMenuBar extends AbstractKFMenuBar {

	StandardKFMenuBar() {
		this.setPreferredSize(new Dimension(AbstractKFMenuBar.defaultWidth, AbstractKFMenuBar.defaultHeight));
	}
}
