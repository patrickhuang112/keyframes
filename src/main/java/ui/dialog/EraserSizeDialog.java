package ui.dialog;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import keyframes.Session;
import ui.slider.SliderFactory;

public class EraserSizeDialog implements Dialog{
	
	private JDialog dialog;
	
	EraserSizeDialog(JComponent parent, Session session, int start, int end, int value) {
		JOptionPane eraserSizeSelector = new JOptionPane();
		JSlider eraserSizeSlider = SliderFactory.createBasicTenTickSlider(
				start, end, value).getSwingComponent();
		
		eraserSizeSelector.setMessage(new Object[] { "Select an Eraser Size: ", eraserSizeSlider});
		eraserSizeSelector.setMessageType(JOptionPane.QUESTION_MESSAGE);
		eraserSizeSelector.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		//Centered on the mainWindow
		dialog = eraserSizeSelector.createDialog(parent, "Eraser Size");
		dialog.setVisible(true);
	    if(eraserSizeSelector.getValue() != null) {
	    	int res = (int)(eraserSizeSelector.getValue());
	    	if(res == JOptionPane.YES_OPTION) {
	    		int val = eraserSizeSlider.getValue();
	    		if (val == 0) {
	    			val++;
	    		}
	    		session.setEraserSize(val);
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
