package keyframes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class Drawable implements UIComponent, Serializable {
	
	private static final long serialVersionUID = -7498204826751850301L;
	private UIComponent parent;
	private JPanel drawPanel;
	private float brushSize;
	
	public Drawable(UIComponent parent) {
		this(parent, true);
	}
	
	public Drawable(UIComponent parent, boolean addToParent) {
		this.parent = parent;
		drawPanel = new DrawablePanel(this);
		if(addToParent) {
			parent.getMainComponent().add(drawPanel, BorderLayout.CENTER);
		}
	}
	
	public void build() {
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
