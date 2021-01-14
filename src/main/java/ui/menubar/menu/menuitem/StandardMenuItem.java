package ui.menubar.menu.menuitem;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

public class StandardMenuItem extends JMenuItem implements MenuItem {

	StandardMenuItem(AbstractAction aa) {
		super(aa);
	}
	
	@Override
	public JMenuItem getSwingComponent() {
		return this;
	}

}
