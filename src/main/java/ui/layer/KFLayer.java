package ui.layer;

import javax.swing.JPanel;

import datatypes.Layer;
import ui.UIComponent;

public interface KFLayer extends UIComponent {
	
	//Layer UI
	
	public static int defaultWidth = 1534;
	public static int defaultHeight = 40;
	
	@Override
	public JPanel getSwingComponent();
	
	public Layer getModel();
	public void refresh();
	public void setName(String name);
	
}
