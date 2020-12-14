package datatypes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

public class Layer extends JPanel{
	
	// Layer num is the priority of the list being drawn
	private Integer layerNum;
	// All the timestamped points for a layer
	private KeyFrames frames;
	private String layerName;
	
	private int UIwidth = 40;
	private int UIheight = 40;
	private Color color = Color.black;
	
	private boolean isSelected = false;
	
	public Layer() {
		super();
	}
		
	public Layer(Integer layerNum, KeyFrames frames) {
		this.layerNum = layerNum;
		this.frames = frames;
		this.setPreferredSize(new Dimension(UIwidth, UIheight));
	}


	private void drawRectangle(Graphics2D g2d, int x1, int y1, int x2, int y2) {
		int width = x2 - x1;
		int height = y2 - y1;
		g2d.setPaint(color);
		g2d.fillRect(x1, y1, width, height);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setPaint(color);
		g2d.fillRect(0, 0, UIwidth, UIheight);
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
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setSelected(boolean option) {
		isSelected = option;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
}
