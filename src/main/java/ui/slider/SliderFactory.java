package ui.slider;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import factories.MouseAdapterFactory;
import keyframes.Session;

public class SliderFactory {
	
	private static Slider createBasicSlider(int start, int end, int value) {
		return new BasicSlider(start, end, value);
	}
	
	public static Slider createBasicTenTickSlider(int start, int end, int value) {
		Slider newSlider = createBasicSlider(start, end, value);
		newSlider.getSwingComponent().setMajorTickSpacing(10);
		return newSlider;
		
	}
	
	public static Slider createBasicFiveTickSlider(int start, int end, int value) {
		Slider newSlider = createBasicSlider(start, end, value);
		newSlider.getSwingComponent().setMajorTickSpacing(5);
		return newSlider;
	}
	
}
