package ui.progressbar;

import keyframes.Session;

public class ProgressBarFactory {

	public static ProgressBar createRenderingProgressBar() {
		return new LeftLabelProgressBar("Rendering Progress: ");
	}
	
}
