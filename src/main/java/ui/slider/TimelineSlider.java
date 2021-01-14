package ui.slider;

import javax.swing.JSlider;

public interface TimelineSlider {

	public JSlider getSwingComponent();
	
	public int getSpacingBetweenTicks();
	public double getThumbMidX();
	public int valueAtX(int x);
	
	public void updateSlider(int newValue);
	
}
