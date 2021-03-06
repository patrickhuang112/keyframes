package ui.dialog;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import commands.CommandFactory;
import keyframes.Controller;
import keyframes.Session;
import ui.slider.KFSliderFactory;

public class EraserSizeKFDialog implements KFDialog{
	
	private JDialog dialog;
	
	EraserSizeKFDialog(int start, int end, int value) {
		JOptionPane eraserSizeSelector = new JOptionPane();
		JSlider eraserSizeSlider = KFSliderFactory.createStandardTenTickSlider(
				start, end, value).getSwingComponent();
		
		eraserSizeSelector.setMessage(new Object[] { "Select an Eraser Size: ", eraserSizeSlider});
		eraserSizeSelector.setMessageType(JOptionPane.QUESTION_MESSAGE);
		eraserSizeSelector.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		//Centered on the mainWindow
		dialog = eraserSizeSelector.createDialog("Eraser Size");
		dialog.setVisible(true);
	    if(eraserSizeSelector.getValue() != null) {
	    	int res = (int)(eraserSizeSelector.getValue());
	    	if(res == JOptionPane.YES_OPTION) {
	    		int val = eraserSizeSlider.getValue();
	    		Controller.getController().addAndExecuteCommand(CommandFactory.createEraserSizeCommand(val));
	    	} else {
	    		System.out.println("Slider cancelled");
	    	}	
	    }
	}

	@Override
	public JDialog getSwingComponent() {
		return dialog;
	}

}
