package datatypes;

import java.util.ArrayList;
import java.util.HashMap;

public class Layer {
	
	// Layer num is the priority of the list being drawn
	private Integer layerNum;
	// All the timestamped points for a layer
	private KeyFrames frames;
	private String layerName;
	
	public Layer(Integer layerNum, KeyFrames frames) {
		this.layerNum = layerNum;
		this.frames = frames;
	}
	
	public Integer getLayerNum() {
		return layerNum;
	}
	
	public void setLayerNum(Integer num) {
		this.layerNum = num;
	}
	
	public KeyFrames getFrames() {
		return frames;
	}
	
	public void setFrames(KeyFrames frames) {
		this.frames = frames;
	}
	
	public void setName(String name) {
		this.layerName = name;
	} 
	
	public String getName() {
		return layerName;
	}
}
