package ui.slider;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import factories.MouseAdapterFactory;
import keyframes.Session;

public class SliderFactory {
	
	private static Slider createBasicSlider(int start, int end, int value) {
		return new StandardSlider(start, end, value);
	}
	
	public static Slider createStandardTenTickSlider(int start, int end, int value) {
		Slider newSlider = createBasicSlider(start, end, value);
		newSlider.getSwingComponent().setMajorTickSpacing(10);
		return newSlider;
		
	}
	
	public static Slider createStandardFiveTickSlider(int start, int end, int value) {
		Slider newSlider = createBasicSlider(start, end, value);
		newSlider.getSwingComponent().setMajorTickSpacing(5);
		return newSlider;
	}
	
	public static TimelineSlider createStandardTimelineSlider(int start, int end, int value, int endSec, int fps) {
		return new StandardTimelineSlider(start, end, value, endSec, fps);
	}
	
}
