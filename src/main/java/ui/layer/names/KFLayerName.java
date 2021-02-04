package ui.layer.names;

import javax.swing.JPanel;

import keyframes.MagicValues;
import ui.UIComponent;

public interface KFLayerName extends UIComponent {

	public static int DefaultWidth = MagicValues.layerUINameDefaultWidth;
	
	@Override
	public JPanel getSwingComponent();
	public void refresh();
	public void setName(String name);
}
