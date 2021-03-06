package datatypes;

import java.awt.Rectangle;
import java.io.Serializable;

public class LayerBoundingBox implements Serializable {

	private static final long serialVersionUID = 7866263167732213747L;
	
	private Rectangle box;
	private int numFrames;
	
	public LayerBoundingBox (int frames) {
		this.numFrames = frames;
	}
	
	public LayerBoundingBox (int frames, Rectangle box) {
		this.box = box;
		this.numFrames = frames;
	}
	
	public LayerBoundingBox deepCopy() {
		LayerBoundingBox newBox = new LayerBoundingBox(this.numFrames);
		Rectangle newRect = new Rectangle(box.x, box.y, box.width, box.height);
		newBox.setBox(newRect);
		return newBox;
	}
	
	public Rectangle getBox() {
		return box;
	}
	
	public void setBox(Rectangle box) {
		this.box = box;
	}
	
	public int getNumFrames() {
		return numFrames;
	}
	
	public void setNumFrames(int frames) {
		this.numFrames = frames;
	}
}
