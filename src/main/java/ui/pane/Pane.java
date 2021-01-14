package ui.pane;

import javax.swing.JPanel;

import ui.UIComponent;

public interface Pane extends UIComponent {
	@Override
	public JPanel getSwingComponent();
}
