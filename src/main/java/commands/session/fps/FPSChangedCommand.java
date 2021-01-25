package commands.session.fps;

import commands.session.SessionCommand;
import keyframes.Controller;

public class FPSChangedCommand extends SessionCommand {
	
	private int val;
	
	public FPSChangedCommand(int val) {
		this.val = val;
	}
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().setFramesPerSecond(val);
	}
}
