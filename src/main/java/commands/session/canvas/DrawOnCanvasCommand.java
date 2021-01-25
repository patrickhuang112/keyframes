package commands.session.canvas;

import java.util.ArrayList;

import commands.session.SessionCommand;
import datatypes.DrawPoint;
import keyframes.Controller;

public class DrawOnCanvasCommand extends SessionCommand {
	
	private ArrayList<DrawPoint> instructions;
	
	public DrawOnCanvasCommand(ArrayList<DrawPoint> instructions) {
		this.instructions = instructions;
	}
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().addToCurrentLayerFrameAtCurrentTime(instructions);
	}
}
