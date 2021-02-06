package ui.dialog;

import javax.swing.JDialog;

import ui.UIComponent;

public interface KFDialog extends UIComponent {
	
	@Override
	public JDialog getSwingComponent(); 
	
}
