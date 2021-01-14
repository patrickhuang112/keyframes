package ui.button;

import javax.swing.JButton;

import keyframes.MagicValues;
import ui.UIComponent;

public interface Button extends UIComponent {
	
	public static final int mvw = MagicValues.mainViewIconsDefaultWidth;
	public static final int mvh = MagicValues.mainViewIconsDefaultHeight;
	
	@Override
	public JButton getSwingComponent();
	
}
