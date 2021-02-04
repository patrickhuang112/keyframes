package commands.session.layer;

import java.awt.Color;

import commands.session.SessionCommand;
import keyframes.Controller;

public class LayerColorSelectedCommand extends SessionCommand {
	private Color newColor;
	
	public LayerColorSelectedCommand(Color color) {
		this.newColor = color;
	}
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().setCurrentLayerColor(newColor);
	}
}
