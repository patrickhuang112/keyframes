package ui.scroll;

import java.awt.Dimension;

import javax.swing.JScrollPane;

public class StandardTimelineKFScrollPane extends AbstractKFScrollPane {
	
	public static final int viewportw = 1200;
	public static final int viewporth = 400;
	
	StandardTimelineKFScrollPane() {
		super();
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setPreferredSize(new Dimension(viewportw, viewporth));
	}
}
