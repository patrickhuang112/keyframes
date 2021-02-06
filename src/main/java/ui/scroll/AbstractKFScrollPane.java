package ui.scroll;

import javax.swing.JScrollPane;
import javax.swing.JViewport;

import ui.UIComponent;

abstract class AbstractKFScrollPane extends JScrollPane implements KFScrollPane {
	
	private JViewport viewport;
	
	AbstractKFScrollPane () {
		this.viewport = getViewport();
	}
	
	@Override
	public JScrollPane getSwingComponent() {
		return this;
	}
	
	public void addScrollComponent(UIComponent comp) {
		this.viewport.add(comp.getSwingComponent());
	}
}
