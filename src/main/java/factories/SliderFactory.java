package factories;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import keyframes.Session;

public class SliderFactory {
	
	private static JSlider createBasicTenTickSlider(int defaultStart, int start, int end) {
		JSlider timeSlider = new JSlider(JSlider.HORIZONTAL, start, end, defaultStart);
		timeSlider.setMajorTickSpacing(10);
		timeSlider.setMinorTickSpacing(1);
		timeSlider.setPaintTicks(true);
		timeSlider.setPaintLabels(true);
		timeSlider.addMouseListener(MouseAdapterFactory.clickToMouseAdapter);
		
		return timeSlider;
	}
	
	public static JSlider createBasicFiveTickSlider(int defaultStart, int start, int end) {
		JSlider slide = createBasicTenTickSlider(defaultStart, start, end);
		slide.setMajorTickSpacing(5);
		return slide;
	}
	public static JSlider createBasicSettingsSlider(int defaultStart, int start, int end) {
		return createBasicTenTickSlider(defaultStart, start, end);
	}
	
	public static void createCompositionEndpointDialogSlider (JComponent parent, Session session) {
		JOptionPane timeSelector = new JOptionPane();
		JSlider timeSlider = createBasicTenTickSlider(session.getLongestTimeInSeconds(), session.lengthMin, session.lengthMax);
		
		timeSelector.setMessage(new Object[] { "Adjust composition length ", timeSlider});
		timeSelector.setMessageType(JOptionPane.QUESTION_MESSAGE);
		timeSelector.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		//Centered on the mainWindow
		JDialog dialog = timeSelector.createDialog(parent, "Composition length");
	    dialog.setVisible(true);
	    
	    
	    if(timeSelector.getValue() != null) {
	    	int res = (int)(timeSelector.getValue());
	    	if(res == JOptionPane.YES_OPTION) {
	    		session.setLongestTimeInSeconds(timeSlider.getValue());
	    	} else {
	    		System.out.println("Slider cancelled");
	    	}	
	    }
	}
	
	public static void createFPSDialogSlider (JComponent parent, Session session) {
		JOptionPane fpsSelector = new JOptionPane();
		JSlider fpsSlider = createBasicTenTickSlider(session.getFramesPerSecond(), session.fpsMin, session.fpsMax);
		
		fpsSelector.setMessage(new Object[] { "Adjust Composition Frames Per Second ", fpsSlider});
		fpsSelector.setMessageType(JOptionPane.QUESTION_MESSAGE);
		fpsSelector.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		//Centered on the mainWindow
		JDialog dialog = fpsSelector.createDialog(parent, "Frames Per Second");
	    dialog.setVisible(true);
	    
	    
	    if(fpsSelector.getValue() != null) {
	    	int res = (int)(fpsSelector.getValue());
	    	if(res == JOptionPane.YES_OPTION) {
	    		session.setFramesPerSecond(fpsSlider.getValue());
	    	} else {
	    		System.out.println("Slider cancelled");
	    	}	
	    }
	}
	
	public static void createEraserSizeDialogSlider (JComponent parent, Session session) {
		JOptionPane eraserSizeSelector = new JOptionPane();
		JSlider eraserSizeSlider = createBasicTenTickSlider(session.getEraserSize(), session.eraserMin, session.eraserMax);
		
		eraserSizeSelector.setMessage(new Object[] { "Select an Eraser Size: ", eraserSizeSlider});
		eraserSizeSelector.setMessageType(JOptionPane.QUESTION_MESSAGE);
		eraserSizeSelector.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		//Centered on the mainWindow
		JDialog dialog = eraserSizeSelector.createDialog(parent, "Eraser Size");
	    dialog.setVisible(true);
	    
	    
	    if(eraserSizeSelector.getValue() != null) {
	    	int res = (int)(eraserSizeSelector.getValue());
	    	if(res == JOptionPane.YES_OPTION) {
	    		session.setEraserSize(eraserSizeSlider.getValue());
	    	} else {
	    		System.out.println("Slider cancelled");
	    	}	
	    }
	}
	
	public static void createBrushSizeDialogSlider (JComponent parent, Session session) {
		JOptionPane brushSizeSelector = new JOptionPane();
		JSlider brushSizeSlider = createBasicTenTickSlider(session.getBrushSize(), session.brushMin, session.brushMax);
		
		brushSizeSelector.setMessage(new Object[] { "Select a Brush Size: ", brushSizeSlider});
		brushSizeSelector.setMessageType(JOptionPane.QUESTION_MESSAGE);
		brushSizeSelector.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		//Centered on the mainWindow
		JDialog dialog = brushSizeSelector.createDialog(parent, "Brush Size");
	    dialog.setVisible(true);
	    
	    
	    if(brushSizeSelector.getValue() != null) {
	    	int res = (int)(brushSizeSelector.getValue());
	    	if(res == JOptionPane.YES_OPTION) {
	    		session.setBrushSize(brushSizeSlider.getValue());
	    	} else {
	    		System.out.println("Slider cancelled");
	    	}	
	    }
	}
	
	
}
