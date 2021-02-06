package ui.slider;

import javax.swing.JSlider;

abstract class AbstractKFSlider extends JSlider implements KFSlider {

	AbstractKFSlider(int orientation, int start, int end, int value) {
		super(orientation, start, end, value);
	}
	
	public void updateSliderStart(int newStart) {
		setMinimum(newStart);
	}

	public void updateSliderEnd(int newEnd) {
		setMaximum(newEnd);
	}

	public JSlider getSwingComponent() {
		return this;
	}
}
