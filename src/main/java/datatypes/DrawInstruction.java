package datatypes;

import java.util.ArrayList;

// A Draw instruction is the instructions to draw on the frame.
public class DrawInstruction extends ArrayList<ArrayList<DrawPoint>>{
	public DrawInstruction() {
		super();
	}
	
	public DrawInstruction deepCopy() {
		DrawInstruction newDps = new DrawInstruction();
		for (int i = 0; i < this.size(); i++) {
			ArrayList<DrawPoint>dpss = this.get(i);
			ArrayList<DrawPoint> newDpss = new ArrayList<DrawPoint>();
			for (DrawPoint dp : dpss) {
				DrawPoint newDp = dp.deepCopy();
				newDpss.add(newDp);
			}
			newDps.add(newDpss);
		}
		return newDps;
	}
}
