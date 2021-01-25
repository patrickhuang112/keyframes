package commands.session.eraser;

import commands.session.SessionCommand;
import keyframes.Controller;
import keyframes.Session;
import keyframes.memento.Memento;

public class EraserSizeCommand extends SessionCommand {

	private int size;
	
	public EraserSizeCommand(int size) {
		this.size = size;
	}
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().setEraserSize(size);
	}
	
}
