package commands.session;

import commands.Command;
import keyframes.Controller;
import keyframes.Session;
import keyframes.memento.Memento;

public class SessionCommand implements Command {
	protected Memento<Session> memento;
	
	public void execute() {
		this.memento = new Memento<Session>(Controller.getController().createSessionDeepCopy());
	}
	
	public void unExecute() {
		Controller.getController().newSessionFromUndo(this.memento.getState());
	}
	
}
