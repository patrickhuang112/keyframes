package ui.slider;

import javax.swing.JSlider;

import ui.UIComponent;

public interface Slider extends UIComponent {
	
	public void updateSliderStart(int newStart);
	
	public void updateSliderEnd(int newEnd);
	
	@Override
	public JSlider getSwingComponent();
	
}
