package commands.session.brush;

import commands.session.SessionCommand;
import datatypes.Enums;
import keyframes.Controller;
import keyframes.Session;
import keyframes.memento.Memento;

public class BrushToolSelectedCommand extends SessionCommand {

	@Override
	public void execute() {
		super.execute();
		Controller.getController().setPaintSetting(Enums.PaintSetting.DRAW);
	}
	
}
