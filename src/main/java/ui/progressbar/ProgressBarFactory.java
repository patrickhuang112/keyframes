package ui.progressbar;

import keyframes.Session;

public class ProgressBarFactory {

	public static ProgressBar createRenderingProgressBar(int start, int end) {
		return new LeftLabelProgressBar("Rendering Progress: ", start, end);
	}
	
}
