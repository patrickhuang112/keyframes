package ui.timeline;

import keyframes.Session;

public class KFTimelineFactory {
	
	public static KFTimeline createStandardTimeline() {
		return new StandardKFTimeline();
	}
	
}
