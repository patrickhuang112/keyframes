package ui.menubar.menu.menuitem;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

abstract class AbstractKFMenuItem extends JMenuItem implements KFMenuItem {

	AbstractKFMenuItem(AbstractAction aa) {
		super(aa);
	}
	
	@Override
	public JMenuItem getSwingComponent() {
		return this;
	}

}
