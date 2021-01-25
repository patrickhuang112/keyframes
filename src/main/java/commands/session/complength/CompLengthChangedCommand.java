package commands.session.complength;

import commands.session.SessionCommand;
import keyframes.Controller;

public class CompLengthChangedCommand extends SessionCommand {
	
	private int val;
	
	public CompLengthChangedCommand(int val) {
		this.val = val;
	}
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().setLongestTimeInSeconds(val);
	}
}
