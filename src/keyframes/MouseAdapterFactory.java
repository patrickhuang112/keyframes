package keyframes;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class MouseAdapterFactory {
	public static MouseAdapter clickToMouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			JSlider sourceSlider=(JSlider)e.getSource();
		    BasicSliderUI ui = (BasicSliderUI)sourceSlider.getUI();
		    int value = ui.valueForXPosition(e.getX());
		    sourceSlider.setValue(value); 
		}
	};
}
