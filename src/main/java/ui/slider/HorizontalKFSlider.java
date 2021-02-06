package ui.slider;

import javax.swing.JSlider;

import keyframes.MouseAdapterFactory;

public class HorizontalKFSlider extends AbstractKFSlider{
	
	private static final long serialVersionUID = 3472121957836700369L;

	HorizontalKFSlider(int start, int end, int value) {
		super(JSlider.HORIZONTAL, start, end, value);
		setMinorTickSpacing(1);
		setPaintTicks(true);
		setPaintLabels(true);
		addMouseListener(MouseAdapterFactory.sliderClickToMouseAdapter);
	}
}
