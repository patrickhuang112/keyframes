package datatypes;

import java.util.ArrayList;
import java.util.HashMap;

// This is just so I don't have to type out HashMap.... every time I want to reference this
public class KeyFrames extends HashMap<Integer, DrawFrame> {
	
	public KeyFrames() {
		super();
	}
	
	public KeyFrames deepCopy() {
		KeyFrames newFrames = new KeyFrames();
		for(Integer key : this.keySet()) {
			DrawFrame dp = this.get(key);
			DrawFrame newDps = dp.deepCopy();
			int newKey = key;
			newFrames.put(newKey, newDps);
		}
		return newFrames;
	}
}
