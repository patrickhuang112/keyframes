package ui.scroll;

import java.awt.Dimension;

import javax.swing.JScrollPane;

public class StandardTimelineKFScrollPane extends StandardKFScrollPane {
	
	StandardTimelineKFScrollPane() {
		super();
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setPreferredSize(new Dimension(1200, 400));
	}
	
}
