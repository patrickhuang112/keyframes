package keyframes;

import java.awt.Dimension;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import datatypes.SessionObject;
import datatypes.UIComponent;

public class SplitPaneManager implements UIComponent, Serializable {

	private static final long serialVersionUID = -4937128517824428147L;

	private SessionObject parent;
	
	private JSplitPane splitPane;
	
	private UIComponent topOrLeft;
	private UIComponent bottomOrRight;
	
	private JComponent topOrLeftComp;
	private JComponent bottomOrRightComp;
	
	private int orientation;
	
	public void setParent(SessionObject parent) {
		this.parent = parent;
	}
	
	public SessionObject getParent() {
		return parent;
	}
	
	public SplitPaneManager(SessionObject parent, boolean vertSplit) {
		this.parent = parent;
		if(vertSplit) {
			this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			this.orientation = JSplitPane.VERTICAL_SPLIT;
		} else {
			this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			this.orientation = JSplitPane.HORIZONTAL_SPLIT;
		}
	}
	
	public SplitPaneManager(SessionObject parent, UIComponent topOrLeft, UIComponent bottomOrRight) {
		this(parent, topOrLeft, bottomOrRight, false);
	}
	
	public SplitPaneManager(SessionObject parent, UIComponent topOrLeft, UIComponent bottomOrRight, boolean vertSplit) {
		this.parent = parent;
		if(vertSplit) {
			this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			this.orientation = JSplitPane.VERTICAL_SPLIT;
		} else {
			this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			this.orientation = JSplitPane.HORIZONTAL_SPLIT;
		}
		
		setTopOrLeft(topOrLeft);
		setBottomOrRight(bottomOrRight);
	}

	public UIComponent getTopOrLeft() {
		return topOrLeft;
	}
	
	public UIComponent getBottomOrRight() {
		return bottomOrRight;
	}
	
	public void setTopOrLeft(UIComponent topOrLeft) {
		this.topOrLeft = topOrLeft;
		this.topOrLeftComp = topOrLeft.getMainComponent();
		splitPane.setTopComponent(this.topOrLeftComp);
	}
	
	public void setBottomOrRight(UIComponent bottomOrRight) {
		this.bottomOrRight = bottomOrRight;
		this.bottomOrRightComp = bottomOrRight.getMainComponent();
		splitPane.setBottomComponent(this.bottomOrRightComp);
	}
	
	@Override
	public void addChild(UIComponent child) {
		splitPane.add(child.getMainComponent());
	}

	@Override
	public JComponent getMainComponent() {
		return splitPane;
	}

	@Override
	public Session getSession() {
		return parent.getSession();
	}

}
