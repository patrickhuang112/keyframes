package keyframes;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

public class SplitPaneManager implements UIComponent {

	
	private JSplitPane splitPane;
	
	private UIComponent topOrLeft;
	private UIComponent bottomOrRight;
	
	private JComponent topOrLeftComp;
	private JComponent bottomOrRightComp;
	
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
		
		this.topOrLeftComp = topOrLeft.getMainComponent();
		this.bottomOrRightComp = bottomOrRight.getMainComponent();
		
		this.topOrLeftComp.setMinimumSize(new Dimension(0, 200));
		this.bottomOrRightComp.setMinimumSize(new Dimension(0, 200));
	}

	public UIComponent getTopOrLeft() {
		return topOrLeft;
	}
	
	public UIComponent getBottomOrRight() {
		return bottomOrRight;
	}
	
	public void setTopOrLeft(UIComponent topOrLeft) {
		this.topOrLeft = topOrLeft;
		splitPane.setTopComponent(topOrLeft.getMainComponent());
	}
	
	public void setBottomOrRight(UIComponent bottomOrRight) {
		this.bottomOrRight = bottomOrRight;
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
