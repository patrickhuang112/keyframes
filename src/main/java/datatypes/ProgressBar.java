package datatypes;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBar extends JPanel {
	
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
	
	public ProgressBar(JProgressBar bar, String label) {
		this(bar, label, BoxLayout.X_AXIS);
	}
	
	public ProgressBar(JProgressBar bar, String label, int orientation) {
		super();
		this.bar = bar;
		this.label = new JLabel(label);
		this.setLayout(new BoxLayout(this, orientation));
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
	
	public void setLabel(String label) {
		this.label.setText(label);
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
}
