package ui.layer;

import javax.swing.JPanel;

import datatypes.Layer;
import keyframes.MagicValues;
import ui.UIComponent;

public interface KFLayer extends UIComponent {
	
	public static int DefaultWidth = MagicValues.layerUIDefaultWidth;
	public static int DefaultHeight = MagicValues.layerUIDefaultHeight;
	
	@Override
	public JPanel getSwingComponent();
	
	public Layer getModel();
	public void refresh();
	public void setName(String name);
	
}
