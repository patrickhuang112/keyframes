package ui.button;

import java.io.IOException;

import javax.swing.JComponent;

import keyframes.Session;

public class ButtonFactory {

	public static Button createBrushButton(JComponent parent, Session session) throws IOException {
		return new BrushButton(parent, session);	
	}
	
	public static Button createEraseButton(JComponent parent, Session session) throws IOException {
		return new EraseButton(parent, session);	
	}
	
	public static Button createPlayButton(Session session) throws IOException {
		return new PlayButton(session);	
	}
	
	public static Button createPauseButton(Session session) throws IOException {
		return new PauseButton(session);	
	}
	
	public static Button createEraseAllButton(Session session) throws IOException {
		return new EraseAllButton(session);	
	}
	
	public static Button createColorPickerButton(Session session) throws IOException {
		return new ColorPickerButton(session);	
	}
	
	public static Button createFillButton(Session session) throws IOException {
		return new FillButton(session);	
	}
}
