package ui.slider;

import javax.swing.JSlider;

public interface KFTimelineSlider extends KFSlider {

	public int getSpacingBetweenTicks();
	public double getThumbMidX();
	public int valueAtX(int x);
	
	public void updateSlider(int newValue);
	public void updateSliderUIFromFPSOrLengthChange(int newFPS, int newLengthInSeconds);
}
