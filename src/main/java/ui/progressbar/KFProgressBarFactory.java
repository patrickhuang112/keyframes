package ui.progressbar;

import keyframes.Session;

public class KFProgressBarFactory {

	public static KFProgressBar createMainViewProgressBar(int start, int end) {
		return new LeftLabelKFProgressBar("Rendering Progress: ", "Rendering complete!  ",
										  "Rendering progress: ", start, end);
	}
	
}
