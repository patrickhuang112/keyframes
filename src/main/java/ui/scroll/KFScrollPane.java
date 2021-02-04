package ui.scroll;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import ui.UIComponent;

public interface KFScrollPane extends UIComponent {
	
	@Override
	public JScrollPane getSwingComponent();
	
	public void addScrollComponent(UIComponent comp);
}
