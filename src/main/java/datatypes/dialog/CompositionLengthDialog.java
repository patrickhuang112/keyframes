package datatypes.dialog;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import datatypes.slider.SliderFactory;
import keyframes.Session;

public class CompositionLengthDialog implements Dialog {

	private JDialog dialog;
	
	CompositionLengthDialog(JComponent parent, Session session, int start, int end, int value) {
		JOptionPane timeSelector = new JOptionPane();
		JSlider timeSlider = SliderFactory.createBasicTenTickSlider(start, end, value).getSwingComponent();
		
		timeSelector.setMessage(new Object[] { "Adjust composition length ", timeSlider});
		timeSelector.setMessageType(JOptionPane.QUESTION_MESSAGE);
		timeSelector.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		//Centered on the mainWindow
		dialog = timeSelector.createDialog(parent, "Composition length");
	    dialog.setVisible(true);
	    
	    if(timeSelector.getValue() != null) {
	    	int res = (int)(timeSelector.getValue());
	    	if(res == JOptionPane.YES_OPTION) {
	    		int val = timeSlider.getValue();
	    		if (val == 0) {
	    			val++;
	    		}
	    		session.setLongestTimeInSeconds(val);
	    	} 
	    }
	}
	
	@Override
	public JDialog getSwingComponent() {
		return dialog;
	}

}
