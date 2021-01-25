package commands.session.eraser;

import commands.session.SessionCommand;
import datatypes.Enums;
import keyframes.Controller;
import keyframes.Session;
import keyframes.memento.Memento;

public class EraserToolSelectedCommand extends SessionCommand {
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().setPaintSetting(Enums.PaintSetting.ERASE);
	}
}
