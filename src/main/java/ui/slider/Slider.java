package ui.slider;

import javax.swing.JSlider;

public interface Slider {
	
	public void updateSliderStart(int newStart);
	
	public void updateSliderEnd(int newEnd);
	
	public JSlider getSwingComponent();
	
}
