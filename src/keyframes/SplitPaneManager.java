package keyframes;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

public class SplitPaneManager implements UIComponent {

	
	private JSplitPane splitPane;
	
	private JComponent topOrLeft;
	private JComponent bottomOrRight;
	
	private int orientation;
	
	
	
	public SplitPaneManager(UIComponent topOrLeft, UIComponent bottomOrRight) {
		this(topOrLeft, bottomOrRight, false);
	}
	
	public SplitPaneManager(UIComponent topOrLeft, UIComponent bottomOrRight, boolean vertSplit) {
		if(vertSplit) {
			this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			this.orientation = JSplitPane.VERTICAL_SPLIT;
		} else {
			this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			this.orientation = JSplitPane.HORIZONTAL_SPLIT;
		}
		
		
		this.topOrLeft = topOrLeft.getMainComponent();
		this.bottomOrRight = bottomOrRight.getMainComponent();
		
		this.topOrLeft.setMinimumSize(new Dimension(0, 200));
		this.topOrLeft.setMinimumSize(new Dimension(0, 200));
	}

	public void setTopOrLeft(UIComponent topOrLeft) {
		splitPane.setTopComponent(topOrLeft.getMainComponent());
	}
	
	public void setBottomOrRight(UIComponent bottomOrRight) {
		splitPane.setBottomComponent(bottomOrRight.getMainComponent());
	}
	
	@Override
	public void addToParent(UIComponent parent) {
		parent.getMainComponent().add(splitPane);
	}
	
	@Override
	public void addChild(UIComponent child) {
		splitPane.add(child.getMainComponent());
	}

	@Override
	public JComponent getMainComponent() {
		return splitPane;
	}
	
	
}
