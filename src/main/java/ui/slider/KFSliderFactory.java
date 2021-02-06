package ui.slider;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import keyframes.MouseAdapterFactory;
import keyframes.Session;

public class KFSliderFactory {
	
	private static KFSlider createBasicSlider(int start, int end, int value) {
		return new HorizontalKFSlider(start, end, value);
	}
	
	public static KFSlider createStandardTenTickSlider(int start, int end, int value) {
		KFSlider newSlider = createBasicSlider(start, end, value);
		newSlider.getSwingComponent().setMajorTickSpacing(10);
		return newSlider;
		
	}
	
	public static KFSlider createStandardFiveTickSlider(int start, int end, int value) {
		KFSlider newSlider = createBasicSlider(start, end, value);
		newSlider.getSwingComponent().setMajorTickSpacing(5);
		return newSlider;
	}
	
	public static KFTimelineSlider createStandardTimelineSlider(int start, int end, int value, int endSec, int fps) {
		return new StandardKFTimelineSlider(start, end, value, endSec, fps);
	}
	
}
