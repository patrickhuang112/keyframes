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

public class LeftLabelProgressBar extends JPanel implements ProgressBar {
	
	private static final long serialVersionUID = -2566279214244797217L;
	
	private JLabel label;
	private JProgressBar bar;
	private Component rightMargin;
	private boolean isVisible = false;
	private String defaultText = "Rendering progress: ";
	
	private final int rightMarginSpacing = 20;
	
	//ORIENTATIONS, uses BoxLayout containts
	//0 = HORIZONTAL
	//1 = VERTICAL
	
	LeftLabelProgressBar(String label, int start, int end) {
		super();
		this.bar = new JProgressBar(start, end);
		this.label = new JLabel(label);
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		rightMargin = Box.createRigidArea(new Dimension(rightMarginSpacing,0));
		
		bar.setPreferredSize(new Dimension(300,20));
		bar.setMaximumSize(new Dimension(300,20));
		bar.setValue(0);
		bar.setStringPainted(true);
		
		add(this.label);
		add(this.bar);
		add(this.rightMargin);
		updateVisibility();
	}
	
	public void setProgressBarEndpoint (int end) {
		bar.setMaximum(end);
	}
	
	public void updateProgressBar(int progress) {
		bar.setValue(progress);
		// We have finished
		if (progress == bar.getMaximum()) {
			label.setText("Rendering complete!  ");
		}
		repaint();
		revalidate();
	}
	
	public void resetProgressBar() {
		bar.setValue(0);
		label.setText(defaultText);
	}
	
	public String getLabel() {
		return label.getText();
	}
	
	public JProgressBar getProgressBar() {
		return bar;
	}
	
	public void setProgressBar(JProgressBar bar) {
		this.bar = bar;
	}
	
	private void updateVisibility() {
		if (isVisible) {
			label.setVisible(true);
			bar.setVisible(true);
		} else {
			label.setVisible(false);
			bar.setVisible(false);
		}
	}
	
	public void toggleVisibility() {
		isVisible = !isVisible;
		updateVisibility();
	}
	
	public void setVisibility(boolean isVisible) {
		this.isVisible = isVisible;
		updateVisibility();
	}
	
	public boolean getVisibility () {
		return isVisible;
	}

	@Override
	public JPanel getSwingComponent() {
		return this;
	}
}