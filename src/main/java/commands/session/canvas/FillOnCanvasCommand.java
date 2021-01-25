package commands.session.canvas;

import java.util.ArrayList;

import commands.session.SessionCommand;
import datatypes.DrawFrame;
import datatypes.DrawPoint;
import keyframes.Controller;

public class FillOnCanvasCommand extends SessionCommand {

	private DrawFrame newlyDrawnFrame;
	
	public FillOnCanvasCommand(DrawFrame newlyDrawnFrame) {
		this.newlyDrawnFrame = newlyDrawnFrame;
	}
	
	@Override
	public void execute() {
		super.execute();
		Controller.getController().setCurrentLayerFrameAtCurrentTime(newlyDrawnFrame);
	}
	

}
