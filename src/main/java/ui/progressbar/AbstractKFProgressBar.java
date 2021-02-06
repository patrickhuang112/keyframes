package ui.progressbar;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import ui.toolbar.KFToolBar;

abstract class AbstractKFProgressBar extends JPanel implements KFProgressBar {
	
	protected JLabel label;
	protected JProgressBar bar;
	
	private boolean isVisible = false;
	private String labelText;
	private String finishedText;
	
	AbstractKFProgressBar(String label, String finishedText, String labelText, int start, int end) {
		this.bar = new JProgressBar(start, end);
		this.label = new JLabel(label);
		// -10 means 5 pixels margin above and below the progress bar
		bar.setPreferredSize(new Dimension(KFToolBar.defaultWidth, KFToolBar.defaultHeight - 10));
		bar.setMaximumSize(new Dimension(KFToolBar.defaultWidth, KFToolBar.defaultHeight - 10));
		bar.setValue(0);
		bar.setStringPainted(true);
	}
	
	public void setProgressBarEndpoint (int end) {
		bar.setMaximum(end);
	}
	
	public void updateProgressBar(int progress) {
		bar.setValue(progress);
		repaint();
		revalidate();
	}
	
	public void resetProgressBar() {
		bar.setValue(0);
		label.setText(labelText);
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
	
	protected void updateVisibility() {
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

	@Override
	public void setProgressLabelAndFinishedText(String label, String finished) {
		this.labelText = label;
		this.finishedText = finished;
	}

	@Override
	public void setIsIndeterminate(boolean isIndeterminate) {
		bar.setStringPainted(!isIndeterminate);
		bar.setIndeterminate(isIndeterminate);
	}

	@Override
	public boolean getIsIndeterminate() {
		return bar.isIndeterminate();
	}
	
	@Override
	public void displayCompletedIndeterminateBar() {
		if (bar.isIndeterminate()) {
			bar.setIndeterminate(false);
			bar.setStringPainted(false);
			bar.setValue(bar.getMaximum());
			label.setText(finishedText);
		}
	}

	@Override
	public void displayCompletedDeterminateBar() {
		if(!bar.isIndeterminate()) {
			bar.setValue(bar.getMaximum());
			label.setText(finishedText);
		}
	}

}
