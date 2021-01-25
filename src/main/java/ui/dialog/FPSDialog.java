package ui.dialog;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import commands.CommandFactory;
import keyframes.Controller;
import keyframes.Session;
import ui.slider.SliderFactory;

public class FPSDialog implements Dialog {
	
	private JDialog dialog;
	
	FPSDialog(int start, int end, int value) {
		JOptionPane fpsSelector = new JOptionPane();
		JSlider fpsSlider = SliderFactory.createStandardTenTickSlider(start, end, value).getSwingComponent();
		
		fpsSelector.setMessage(new Object[] { "Adjust Composition Frames Per Second ", fpsSlider});
		fpsSelector.setMessageType(JOptionPane.QUESTION_MESSAGE);
		fpsSelector.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		//Centered on the mainWindow
		dialog = fpsSelector.createDialog("Frames Per Second");
		dialog.setVisible(true);
	    if(fpsSelector.getValue() != null) {
	    	int res = (int)(fpsSelector.getValue());
	    	if(res == JOptionPane.YES_OPTION) {
	    		int val = fpsSlider.getValue();
	    		if (val == 0) {
	    			val++;
	    		}
	    		Controller.getController().addAndExecuteCommand(CommandFactory.createFPSChangedCommand(val));
	    	} 
	    }
	}

	@Override
	public JDialog getSwingComponent() {
		// TODO Auto-generated method stub
		return dialog;
	}

}
