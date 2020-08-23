package keyframes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class Session implements Serializable {
	
	
	private static final long serialVersionUID = 3817282456552806335L;

	public Session(Settings settings) {
		this();
		brushSize = settings.getBrushSize();
		eraserSize = settings.getEraserSize();
		longestTimeInSeconds = settings.getCompLength();
		framesPerSecond = settings.getFps();
	}
	
	public Session() {
		super();
		drawFrames.put(0, new ArrayList<>());
		brushSize = 5;
		eraserSize = 5;
		
		longestTimeInSeconds = 10;
		framesPerSecond = 15;
		longestTimepoint = longestTimeInSeconds * framesPerSecond;
	}
	
	private String savePath = null;
	public boolean isPlaying = false;
	private DrawablePanel drawPanel = null;
	private JSlider timelineSlider = null;
	private Hashtable<Integer, JLabel> timelineLabelDict = new Hashtable<Integer, JLabel>();
	
	private Color brushColor = Color.red;
	private Color eraserColor = Color.white;
	private int brushSize;
	private int eraserSize;
	private EnumFactory.PaintSetting paintSetting = EnumFactory.PaintSetting.DRAW;
	private HashMap<Integer, ArrayList<ArrayList<DrawPoint>>> drawFrames = 
			new HashMap<Integer, ArrayList<ArrayList<DrawPoint>>>();
	
	private HashMap<Integer, BufferedImage> drawImages = new HashMap<Integer, BufferedImage>();
	
	//REPLACE BY SETTINGS
	private int currentTimepoint = 0;
	
	private int longestTimeInSeconds;
	private int framesPerSecond;
	private int longestTimepoint = longestTimeInSeconds * framesPerSecond;
	private ArrayList<ArrayList<DrawPoint>> clipboardFrames = null;
	
	
	public Color getDrawablePanelBackgroundColor () {
		return drawPanel.getBackground();
	}
	
	public String getSavePath() {
		return savePath;
	}
	
	public void setSavePath(String path) {
		savePath = path;
	}
	
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
	
	
	public int getDrawablePaneWidth() {
		return drawPanel.getWidth();
	}
	
	public int getDrawablePaneHeight() {
		return drawPanel.getHeight();
	}
	
	public void setTimelineSlider(JSlider ts) {
		this.timelineSlider = ts;
	}
	
	public void updateTimelineSliderPosition() {
		timelineSlider.setValue(currentTimepoint);
	}
	
	public Hashtable<Integer, JLabel> getTimelineLabelDict() {
		return timelineLabelDict;
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
		longestTimepoint = framesPerSecond * longestTimeInSeconds;
		updateTimeline(framesPerSecond, framesPerSecond);
		updateFrames(framesPerSecond, framesPerSecond);
		
		refreshDrawPanel();
		
	}
	
	public int getLongestTimeInSeconds() {
		return this.longestTimeInSeconds;
	}
	
	
	//FPS
	private void updateTimeline(int oldFps, int newFps) {
		int newTimepoint = calculateNewTimelinePointerPositionFromOldFps(oldFps, newFps, currentTimepoint);
		timelineSlider.setMaximum(longestTimeInSeconds * newFps);
		timelineSlider.setMajorTickSpacing(newFps);
		timelineSlider.setMinorTickSpacing(1);
		timelineLabelDict = new Hashtable<Integer, JLabel>();
		
		for(Integer i = 0; i < longestTimeInSeconds; i++) {
			timelineLabelDict.put(i * newFps, new JLabel(i.toString()));
		}
		//ADD THE ENDPOINT LABEL
		timelineLabelDict.put(longestTimeInSeconds * newFps, 
				new JLabel(((Integer)longestTimeInSeconds).toString()));
		timelineSlider.setLabelTable(timelineLabelDict);
		
		timelineSlider.setValue(newTimepoint);
		setCurrentTimepoint(newTimepoint);
		timelineSlider.repaint();
		timelineSlider.revalidate();
	}
	
	public void setFramesPerSecond(int fps) {
		int oldFps = framesPerSecond;
		int newFps = fps;
		
		updateTimeline(oldFps, newFps);
		updateFrames(oldFps, newFps);
		
		
		framesPerSecond = fps;
		longestTimepoint = fps * longestTimeInSeconds;
		
		refreshDrawPanel();
	}
	
	public int getFramesPerSecond() {
		return this.framesPerSecond;
	}
	
	private int calculateNewTimelinePointerPositionFromOldFps(int oldFps, int newFps, int timePoint) {
		int sec = timePoint / oldFps;
		double rem = timePoint % oldFps;
		double frac = rem / ((double)oldFps);
		int newTimepoint = (sec * newFps) + (int)(frac * (double)(newFps));
		return newTimepoint;
	}
	
	private void updateFrames(int oldFps, int newFps) {
		HashMap<Integer, ArrayList<ArrayList<DrawPoint>>> newFrames = 
				new HashMap<Integer, ArrayList<ArrayList<DrawPoint>>>();
		for(Integer i : drawFrames.keySet()) {
			int newKey = calculateNewTimelinePointerPositionFromOldFps(oldFps, newFps, i);
			if(!newFrames.containsKey(newKey) && i <= longestTimepoint) {
				newFrames.put(newKey, drawFrames.get(i));
			} 
		}
		
		drawFrames = newFrames;
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
	
	public void setCurrentImage(BufferedImage img) {
		drawImages.put(currentTimepoint, img);
	}
	
	public HashMap<Integer, BufferedImage> getImages() {
		return drawImages;
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