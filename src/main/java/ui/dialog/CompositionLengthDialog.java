package ui.dialog;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import commands.CommandFactory;
import keyframes.Controller;
import keyframes.Session;
import ui.slider.SliderFactory;

public class CompositionLengthDialog implements Dialog {

	private JDialog dialog;
	
	CompositionLengthDialog(int start, int end, int value) {
		JOptionPane timeSelector = new JOptionPane();
		JSlider timeSlider = SliderFactory.createStandardTenTickSlider(start, end, value).getSwingComponent();
		
		timeSelector.setMessage(new Object[] { "Adjust composition length ", timeSlider});
		timeSelector.setMessageType(JOptionPane.QUESTION_MESSAGE);
		timeSelector.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		//Centered on the mainWindow
		//dialog = timeSelector.createDialog(parent, "Composition length");
		dialog = timeSelector.createDialog("Composition length");
	    dialog.setVisible(true);
	    
	    if(timeSelector.getValue() != null) {
	    	int res = (int)(timeSelector.getValue());
	    	if(res == JOptionPane.YES_OPTION) {
	    		int val = timeSlider.getValue();
	    		if (val == 0) {
	    			val++;
	    		}
	    		Controller.getController().addAndExecuteCommand(CommandFactory.createCompLengthChangedCommand(val));
	    	} 
	    }
	}
	
	@Override
	public JDialog getSwingComponent() {
		return dialog;
	}

}
