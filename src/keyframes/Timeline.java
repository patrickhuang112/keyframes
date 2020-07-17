package keyframes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class Timeline implements UIComponent{
	
	private UIComponent parent;
	private JPanel mainTimelinePanel;
	
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
		mainTimelinePanel.setBackground(Color.gray);
		mainTimelinePanel.setPreferredSize(new Dimension(0,200));
		mainTimelinePanel.setLayout(new BorderLayout());
		JSlider brushSizeSlider = new JSlider(JSlider.HORIZONTAL, 0,30, 0);
		brushSizeSlider.setMajorTickSpacing(10);
		brushSizeSlider.setMinorTickSpacing(1);
		brushSizeSlider.setPaintTicks(true);
		brushSizeSlider.setPaintLabels(true);
		brushSizeSlider.setSnapToTicks(true);
		brushSizeSlider.addMouseListener(MouseAdapterFactory.clickToMouseAdapter);
		mainTimelinePanel.add(brushSizeSlider, BorderLayout.CENTER);
	}

	@Override
	public Session getSession() {
		return parent.getSession();
	}
}
