package ui.button;

import java.io.IOException;

import javax.swing.JComponent;

import keyframes.Session;

public class ButtonFactory {

	public static Button createBrushButton() throws IOException {
		return new BrushButton();	
	}
	
	public static Button createEraseButton() throws IOException {
		return new EraseButton();	
	}
	
	public static Button createPlayButton() throws IOException {
		return new PlayButton();	
	}
	
	public static Button createPauseButton() throws IOException {
		return new PauseButton();	
	}
	
	public static Button createEraseAllButton() throws IOException {
		return new EraseAllButton();	
	}
	
	public static Button createColorPickerButton() throws IOException {
		return new ColorPickerButton();	
	}
	
	public static Button createFillButton() throws IOException {
		return new FillButton();	
	}
}
