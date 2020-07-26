package keyframes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JSlider;

public class Session {
	
	
	public Session() {
		super();
	}
	
	
	
	public boolean isPlaying = false;
	private DrawablePanel drawPanel = null;
	private JSlider timelineSlider = null;
	
	
	
	private Color brushColor = Color.red;
	private Color eraserColor = Color.white;
	private int brushSize;
	private int eraserSize;
	private EnumFactory.PaintSetting paintSetting = EnumFactory.PaintSetting.DRAW;
	private HashMap<Integer, ArrayList<ArrayList<DrawPoint>>> drawFrames = 
			new HashMap<Integer, ArrayList<ArrayList<DrawPoint>>>();
	
	//REPLACE BY SETTINGS
	private int currentTimepoint = 0;
	
	
	private int longestTimeInSeconds = 10;
	private int framesPerSecond = 15;
	private int longestTimepoint = longestTimeInSeconds * framesPerSecond;
	private ArrayList<ArrayList<DrawPoint>> clipboardFrames = null;
	
	
	public Color getBrushColor() {
		return this.brushColor;
	}
	
	public void setBrushColor(Color color) {
		this.brushColor = color;
	}
	
	public Color getEraserColor() {
		return this.eraserColor;
	}
	
	public void setEraserColor(Color color) {
		this.eraserColor = color;
	}
	
	
	
	public void setTimelineSlider(JSlider ts) {
		this.timelineSlider = ts;
	}
	
	public void updateTimelineSliderPosition() {
		timelineSlider.setValue(currentTimepoint);
	}
	
	public void setDrawPanel(DrawablePanel dp) {
		this.drawPanel = dp;
	}
	
	
	public int getLongestTimepoint() {
		return this.longestTimepoint;
	}
	
	public void incrementTimepoint() {
		if(currentTimepoint == longestTimepoint) {
			currentTimepoint = 0;
		} else {
			currentTimepoint++;
		}
	}
	
	public void refreshDrawPanel(){
		drawPanel.repaint();
		drawPanel.revalidate();
		
	}
	
	private ArrayList<ArrayList<DrawPoint>> deepCopyFrames(ArrayList<ArrayList<DrawPoint>> originalFrame) {
		ArrayList<ArrayList<DrawPoint>> res = new ArrayList<>();
		for(ArrayList<DrawPoint> curInstruct : originalFrame) {
			ArrayList<DrawPoint> newInstruct = new ArrayList<>();
			for(DrawPoint curPoint : curInstruct) {
				DrawPoint newPoint = curPoint.createCopy();
				newInstruct.add(newPoint);
			}
			res.add(newInstruct);
		}
		return res;
	}
	
	public void copyFrames() {
		clipboardFrames = getCurrentFrame();
	}
	
	public void pasteFrames() {
		if(clipboardFrames != null) {
			setCurrentFrame(deepCopyFrames(clipboardFrames));
			refreshDrawPanel();
		}
	}
	
	
	//TIMELINE ENDPOINT
	public void setLongestTimeInSeconds(int time) {
		this.longestTimeInSeconds = time;
	}
	
	public int getLongestTimeInSeconds() {
		return this.longestTimeInSeconds;
	}
	
	
	//FPS
	public void setFramesPerSecond(int fps) {
		this.framesPerSecond = fps;
	}
	
	public int getFramesPerSecond() {
		return this.framesPerSecond;
	}
	
	
	//TIMELINE CURRENT TIME
	public void setCurrentTimepoint(int time) {
		this.currentTimepoint = time;
	}
	
	public int getCurrentTimepoint() {
		return this.currentTimepoint;
	}
	
	
	//DRAWABLE GET/SET FRAMEs
	public void setFrame(Integer timePoint, ArrayList<ArrayList<DrawPoint>> drawing) {
		this.drawFrames.put(timePoint, drawing);
	}
	

	public ArrayList<ArrayList<DrawPoint>> getFrame(Integer timePoint) {
		if(this.drawFrames.containsKey(timePoint)) {
			return this.drawFrames.get(timePoint);
		} else {
			setFrame(timePoint, new ArrayList<>());
			return this.drawFrames.get(timePoint);
		}
	}
	
	public void setCurrentFrame(ArrayList<ArrayList<DrawPoint>> drawing) {
		this.drawFrames.put(currentTimepoint, drawing);
	}
	
	
	public ArrayList<ArrayList<DrawPoint>> getCurrentFrame() {
		if(this.drawFrames.containsKey(currentTimepoint)) {
			return this.drawFrames.get(currentTimepoint);
		} else {
			setCurrentFrame(new ArrayList<>());
			return this.drawFrames.get(currentTimepoint);
		}
		
	}
	
	

	//BYE BYE ALL FRAMES
	public void eraseAllFrames() {
		this.drawFrames = new HashMap<Integer, ArrayList<ArrayList<DrawPoint>>>();
	}
	
	
	//DRAWABLE PAINT SETTING
	public void setPaintSetting(EnumFactory.PaintSetting paintSetting) {
		this.paintSetting = paintSetting;
	}
	
	public EnumFactory.PaintSetting getPaintSetting() {
		return this.paintSetting;
	}
	
	
	//DRAWABLE BRUSH/ERASER SETTINGS
	public int getBrushSize() {
		return brushSize;
	}
	
	public void setBrushSize(int brushSize) {
		this.brushSize = brushSize;
	}
	
	public int getEraserSize() {
		return eraserSize;
	}
	
	public void setEraserSize(int eraserSize) {
		this.eraserSize = eraserSize;
	}
	
}
