package commands;

import java.awt.Color;
import java.util.ArrayList;

import commands.session.brush.BrushSizeCommand;
import commands.session.brush.BrushToolSelectedCommand;
import commands.session.canvas.DrawOnCanvasCommand;
import commands.session.canvas.FillOnCanvasCommand;
import commands.session.color.BackgroundColorSelectedCommand;
import commands.session.color.BrushAndFillColorSelectedCommand;
import commands.session.complength.CompLengthChangedCommand;
import commands.session.eraser.EraserSizeCommand;
import commands.session.eraser.EraserToolSelectedCommand;
import commands.session.fill.FillToolSelectedCommand;
import commands.session.fps.FPSChangedCommand;
import datatypes.DrawFrame;
import datatypes.DrawPoint;

public class CommandFactory {
	
	public static Command createBrushSizeCommand(int size) {
		return new BrushSizeCommand(size);
	}
	
	public static Command createBrushToolSelectedCommand() {
		return new BrushToolSelectedCommand();
	}
	
	public static Command createDrawOnCanvasCommand(ArrayList<DrawPoint> instructions) {
		return new DrawOnCanvasCommand(instructions);
	}
	
	public static Command createFillOnCanvasCommand(DrawFrame df) {
		return new FillOnCanvasCommand(df);
	}
	
	public static Command createBackgroundColorSelectedCommand(Color newColor) {
		return new BackgroundColorSelectedCommand(newColor);
	}
	
	public static Command createBrushAndFillColorSelectedCommand(Color newColor) {
		return new BrushAndFillColorSelectedCommand(newColor);
	}
	
	public static Command createCompLengthChangedCommand(int val) {
		return new CompLengthChangedCommand(val);
	}
	
	public static Command createEraserSizeCommand(int size) {
		return new EraserSizeCommand(size);
	}
	
	public static Command createEraserToolSelectedCommand() {
		return new EraserToolSelectedCommand();
	}
	
	public static Command createFillToolSelectedCommand() {
		return new FillToolSelectedCommand();
	}
	
	public static Command createFPSChangedCommand(int val) {
		return new FPSChangedCommand(val);
	}
	
}
