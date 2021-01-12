package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JPanel;

import datatypes.UIComponent;
import keyframes.MagicValues;
import keyframes.Session;

public class Drawable implements UIComponent, Serializable {
	
	private static final long serialVersionUID = -7498204826751850301L;
	private UIComponent parent;
	private JPanel drawPanel;
	
	public Drawable(UIComponent parent) {
		this(parent, true);
	}
	
	public Drawable(UIComponent parent, boolean addToParent) {
		this.parent = parent;
		drawPanel = new DrawablePanel(this);
		if(addToParent) {
			parent.getMainComponent().add(drawPanel, BorderLayout.CENTER);
		}
		
		int defaultMinW = MagicValues.drawablePanelDefaultMinWidth;
		int defaultMinH = MagicValues.drawablePanelDefaultMinHeight;
		int defaultPrefW = MagicValues.drawablePanelDefaultPreferredWidth;
		int defaultPrefH = MagicValues.drawablePanelDefaultPreferredHeight;
		
		
		// Set default heights
		drawPanel.setMinimumSize(new Dimension(defaultMinW, defaultMinH));
		drawPanel.setPreferredSize(new Dimension(defaultPrefW, defaultPrefH));
	}
	
	public void buildUI() {
		drawPanel.setBackground(Color.white);
	}
	
	@Override
	public void addChild(UIComponent child) {
		drawPanel.add(child.getMainComponent());
	}
	
	@Override
	public JComponent getMainComponent() {
		return drawPanel;
	}

	@Override
	public Session getSession() {
		return parent.getSession();
	}
	
}
