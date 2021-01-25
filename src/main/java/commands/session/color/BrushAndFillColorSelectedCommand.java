package commands.session.color;

import java.awt.Color;

import commands.session.SessionCommand;
import keyframes.Controller;

public class BrushAndFillColorSelectedCommand extends SessionCommand {
	
	private Color newColor;
	
	public BrushAndFillColorSelectedCommand(Color newColor) {
		this.newColor = newColor;
	}
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().setBrushColor(newColor);
	}
}
