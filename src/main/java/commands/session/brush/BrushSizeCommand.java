package commands.session.brush;

import commands.Command;
import commands.session.SessionCommand;
import keyframes.Controller;
import keyframes.Session;
import keyframes.memento.Memento;

public class BrushSizeCommand extends SessionCommand {	
	
	private int size;
	
	public BrushSizeCommand(int size) {
		this.size = size;
	}
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().setBrushSize(size);
	}

}
