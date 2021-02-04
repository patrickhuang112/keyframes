package ui.scroll;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import ui.UIComponent;

public abstract class StandardKFScrollPane extends JScrollPane implements KFScrollPane {
	
	private JViewport viewport;
	
	StandardKFScrollPane() {
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
