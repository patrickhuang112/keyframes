package keyframes;

import java.util.ArrayList;
import java.util.HashMap;

public class Layer {
	
	// Layer num is the priority of the list being drawn
	private Integer layerNum;
	// All the timestamped points for a layer
	private HashMap<Integer,ArrayList<ArrayList<DrawPoint>>> points;
	private String layerName;
	
	Layer(Integer layerNum, HashMap<Integer,ArrayList<ArrayList<DrawPoint>>> points) {
		this.layerNum = layerNum;
		this.points = points;
	}
	
	public Integer getLayerNum() {
		return layerNum;
	}
	
	public void setLayerNum(Integer num) {
		this.layerNum = num;
	}
	
	public HashMap<Integer,ArrayList<ArrayList<DrawPoint>>> getPoints() {
		return points;
	}
	
	public void setName(String name) {
		this.layerName = name;
	} 
	
	public String getName() {
		return layerName;
	}
}
