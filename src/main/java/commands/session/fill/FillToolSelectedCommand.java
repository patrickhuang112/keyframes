package commands.session.fill;

import commands.session.SessionCommand;
import datatypes.Enums;
import keyframes.Controller;

public class FillToolSelectedCommand extends SessionCommand {
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().setPaintSetting(Enums.PaintSetting.FILL);
	}
}
