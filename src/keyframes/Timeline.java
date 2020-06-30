package keyframes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class Timeline implements UIComponent{
	
	private UIComponent parent;
	private JPanel mainTimelinePanel;
	
	@Override
	public void addToParent(UIComponent parent) {
		parent.getMainComponent().add(mainTimelinePanel);
	}
	
	@Override
	public void addChild(UIComponent child) {
		mainTimelinePanel.add(child.getMainComponent());
	}
	
	@Override
	public JComponent getMainComponent() {
		return mainTimelinePanel;
	}
	
	public Timeline(UIComponent parent) {
		this(parent, true);
	}
	
	public Timeline(UIComponent parent, boolean addToParent) {
		this.parent = parent;
		mainTimelinePanel = new JPanel();
		if(addToParent) { 
			this.parent.getMainComponent().add(mainTimelinePanel, BorderLayout.PAGE_END);
		}
	}
	
	public void build() {
		mainTimelinePanel.setBackground(Color.green);
		mainTimelinePanel.setPreferredSize(new Dimension(0,200));
	}
}
