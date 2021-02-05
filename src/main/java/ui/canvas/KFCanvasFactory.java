package ui.canvas;

import keyframes.Session;

public class KFCanvasFactory {
	public static KFCanvas createStandardKFCanvas() {
		int defaultPrefW = KFCanvas.defaultPreferredWidth;
		int defaultPrefH = KFCanvas.defaultPreferredHeight;
		return new StandardKFCanvas(defaultPrefW, defaultPrefH);
	}
}
