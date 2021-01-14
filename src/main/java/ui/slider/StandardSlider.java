package ui.slider;

import javax.swing.JSlider;

import factories.MouseAdapterFactory;

public class StandardSlider extends JSlider implements Slider{
	
	private static final long serialVersionUID = 3472121957836700369L;

	StandardSlider(int start, int end, int value) {
		super(JSlider.HORIZONTAL, start, end, value);
		setMajorTickSpacing(10);
		setMinorTickSpacing(1);
		setPaintTicks(true);
		setPaintLabels(true);
		addMouseListener(MouseAdapterFactory.clickToMouseAdapter);
	}
	
	@Override
	public void updateSliderStart(int newStart) {
		setMinimum(newStart);
	}

	@Override
	public void updateSliderEnd(int newEnd) {
		setMaximum(newEnd);
	}

	@Override
	public JSlider getSwingComponent() {
		return this;
	}
}
