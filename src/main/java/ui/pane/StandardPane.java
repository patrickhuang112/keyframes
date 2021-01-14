package ui.pane;

import javax.swing.JPanel;

public class StandardPane extends JPanel implements Pane {

	StandardPane() {
		super();
	}
	
	@Override
	public JPanel getSwingComponent() {
		return this;
	}

}
