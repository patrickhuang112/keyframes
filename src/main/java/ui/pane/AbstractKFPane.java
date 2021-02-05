package ui.pane;

import javax.swing.JPanel;

public abstract class AbstractKFPane extends JPanel implements KFPane {
	
	@Override
	public JPanel getSwingComponent() {
		return this;
	}

}
