package ui.button;

import java.io.IOException;

import javax.swing.JComponent;

import keyframes.Session;

public class KFButtonFactory {

	public static KFButton createBrushButton() throws IOException {
		return new BrushKFButton();	
	}
	
	public static KFButton createEraseButton() throws IOException {
		return new EraseKFButton();	
	}
	
	public static KFButton createPlayButton() throws IOException {
		return new PlayKFButton();	
	}
	
	public static KFButton createPauseButton() throws IOException {
		return new PauseKFButton();	
	}
	
	public static KFButton createEraseAllButton() throws IOException {
		return new EraseAllKFButton();	
	}
	
	public static KFButton createColorPickerButton() throws IOException {
		return new ColorPickerKFButton();	
	}
	
	public static KFButton createFillButton() throws IOException {
		return new FillKFButton();	
	}
}
