package datatypes.dialog;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import datatypes.slider.SliderFactory;
import keyframes.Session;

public class FPSDialog implements Dialog {
	
	private JDialog dialog;
	
	FPSDialog(JComponent parent, Session session, int start, int end, int value) {
		JOptionPane fpsSelector = new JOptionPane();
		JSlider fpsSlider = SliderFactory.createBasicTenTickSlider(start, end, value).getSwingComponent();
		
		fpsSelector.setMessage(new Object[] { "Adjust Composition Frames Per Second ", fpsSlider});
		fpsSelector.setMessageType(JOptionPane.QUESTION_MESSAGE);
		fpsSelector.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		//Centered on the mainWindow
		dialog = fpsSelector.createDialog(parent, "Frames Per Second");
		dialog.setVisible(true);
	    if(fpsSelector.getValue() != null) {
	    	int res = (int)(fpsSelector.getValue());
	    	if(res == JOptionPane.YES_OPTION) {
	    		int val = fpsSlider.getValue();
	    		if (val == 0) {
	    			val++;
	    		}
	    		session.setFramesPerSecond(val);
	    	} 
	    }
	}

	@Override
	public JDialog getSwingComponent() {
		// TODO Auto-generated method stub
		return dialog;
	}

}
