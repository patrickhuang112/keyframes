package ui.dialog;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import keyframes.Controller;
import keyframes.Session;
import ui.slider.SliderFactory;

public class BrushSizeDialog implements Dialog {

	private JDialog dialog;
	
	BrushSizeDialog(int start, int end, int value) {
		JOptionPane brushSizeSelector = new JOptionPane();
		JSlider brushSizeSlider = SliderFactory.createStandardTenTickSlider(start, end, value).getSwingComponent();
		
		brushSizeSelector.setMessage(new Object[] { "Select a Brush Size: ", brushSizeSlider});
		brushSizeSelector.setMessageType(JOptionPane.QUESTION_MESSAGE);
		brushSizeSelector.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		//Centered on the mainWindow
		dialog = brushSizeSelector.createDialog("Brush Size");
	    dialog.setVisible(true);
	    
	    if(brushSizeSelector.getValue() != null) {
	    	int res = (int)(brushSizeSelector.getValue());
	    	if(res == JOptionPane.YES_OPTION) {
	    		int val = brushSizeSlider.getValue();
	    		if (val == 0) {
	    			val++;
	    		}
	    		Controller.getController().setBrushSize(val);
	    	} 
	    }
	}
	
	@Override
	public JDialog getSwingComponent() {
		return dialog;
	}

}
