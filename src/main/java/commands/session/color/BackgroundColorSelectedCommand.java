package commands.session.color;

import java.awt.Color;

import commands.session.SessionCommand;
import keyframes.Controller;

public class BackgroundColorSelectedCommand extends SessionCommand {
	
	private Color newColor;
	
	public BackgroundColorSelectedCommand(Color color) {
		this.newColor = color;
	}
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().setDrawablePanelBackgroundColor(newColor);
		Controller.getController().setEraserColor(newColor);
	}
}
