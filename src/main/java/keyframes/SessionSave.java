package keyframes;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import datatypes.Enums;
import datatypes.KeyFrames;
import datatypes.Layer;

public class SessionSave implements Serializable {

	public String savePath;

	// This contains layer info, and time info
	public ArrayList<Layer> drawLayers;
	
	
	//REPLACE BY SETTINGS
	public int longestTimeInSeconds;
	public int framesPerSecond;
	//Dependent on frames persecond and time in seconds, updated when we set the timeline slider
	public Integer spaceBetweenTicks;
	public int longestTimepoint;
	
	public SessionSave(Session session) {
		this.savePath = session.getSavePath();
		this.longestTimeInSeconds = session.getLongestTimeInSeconds();
		this.framesPerSecond = session.getFramesPerSecond();
		this.longestTimepoint = longestTimeInSeconds * framesPerSecond;
		this.drawLayers = session.getLayers();
	}
}
