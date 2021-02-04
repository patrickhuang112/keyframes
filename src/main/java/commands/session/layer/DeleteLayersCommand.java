package commands.session.layer;

import java.util.ArrayList;

import commands.session.SessionCommand;
import datatypes.Layer;
import keyframes.Controller;

public class DeleteLayersCommand extends SessionCommand {
	
	private ArrayList<Integer> layerNums;
	
	public DeleteLayersCommand(ArrayList<Integer> layerNums) {
		this.layerNums = layerNums;
	}
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().deleteLayers(layerNums);
	}
}
