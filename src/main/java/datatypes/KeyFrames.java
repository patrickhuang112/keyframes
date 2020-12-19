package datatypes;

import java.util.ArrayList;
import java.util.HashMap;

// This is just so I don't have to type out HashMap.... every time I want to reference this
public class KeyFrames extends HashMap<Integer, ArrayList<ArrayList<DrawPoint>>> {
	
	public KeyFrames() {
		super();
	}
	
	public KeyFrames deepCopy() {
		KeyFrames newFrames = new KeyFrames();
		for(Integer key : this.keySet()) {
			ArrayList<ArrayList<DrawPoint>> newDps = new ArrayList<ArrayList<DrawPoint>>();
			for (ArrayList<DrawPoint> dpss : this.get(key)) {
				ArrayList<DrawPoint> newDpss = new ArrayList<DrawPoint>();
				for (DrawPoint dp : dpss) {
					DrawPoint newDp = dp.createCopy();
					newDpss.add(newDp);
				}
				newDps.add(newDpss);
			}
			int newKey = key;
			newFrames.put(newKey, newDps);
		}
		return newFrames;
	}
}
