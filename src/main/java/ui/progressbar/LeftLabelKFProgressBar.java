package ui.progressbar;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import keyframes.Controller;
import keyframes.Session;

public class LeftLabelKFProgressBar extends AbstractKFProgressBar {
	
	private static final long serialVersionUID = -2566279214244797217L;
	
	private final int rightMarginSpacing = 20;
	private Component rightMargin;
	//ORIENTATIONS, uses BoxLayout containts
	//0 = HORIZONTAL
	//1 = VERTICAL
	
	LeftLabelKFProgressBar(String label, String finishedText, String labelText, int start, int end) {
		super(label, finishedText, labelText, start, end);
		
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		rightMargin = Box.createRigidArea(new Dimension(rightMarginSpacing,0));
		
		add(this.label);
		add(this.bar);
		add(this.rightMargin);
		updateVisibility();
	}
	
}
