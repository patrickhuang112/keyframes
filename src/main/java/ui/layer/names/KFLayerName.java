package ui.layer.names;

import javax.swing.JPanel;

import ui.UIComponent;

public interface KFLayerName extends UIComponent {

	public static int defaultWidth = 140;
	
	@Override
	public JPanel getSwingComponent();
	public void refresh();
	public void setName(String name);
}
