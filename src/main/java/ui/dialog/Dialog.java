package ui.dialog;

import javax.swing.JDialog;

import ui.UIComponent;

public interface Dialog extends UIComponent {
	
	@Override
	public JDialog getSwingComponent(); 
	
}
