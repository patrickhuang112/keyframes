package ui.button;

import javax.swing.JButton;

import keyframes.MagicValues;

public interface Button {
	
	public static final int mvw = MagicValues.mainViewIconsDefaultWidth;
	public static final int mvh = MagicValues.mainViewIconsDefaultHeight;

	public JButton getSwingComponent();
	
}
