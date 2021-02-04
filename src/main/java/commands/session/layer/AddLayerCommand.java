package commands.session.layer;

import commands.session.SessionCommand;
import keyframes.Controller;

public class AddLayerCommand extends SessionCommand {
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().addNewLayer();
	}
}
