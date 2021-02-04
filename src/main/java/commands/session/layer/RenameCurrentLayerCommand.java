package commands.session.layer;

import java.awt.Color;

import commands.Command;
import commands.session.SessionCommand;
import keyframes.Controller;

public class RenameCurrentLayerCommand extends SessionCommand {

	private String name;
	
	public RenameCurrentLayerCommand(String name) {
		this.name = name;
	}
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().setCurrentLayerName(name);
	}

	
}
