package ui.progressbar;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import ui.UIComponent;

public interface KFProgressBar extends UIComponent {
	
	public void setProgressBarEndpoint (int end) ;
	public void updateProgressBar(int progress) ;
	public void resetProgressBar() ;
	
	@Override
	public JPanel getSwingComponent();
	public JProgressBar getProgressBar() ;
	public String getLabel() ;
	public void setProgressLabelAndFinishedText(String label, String finished);
	
	public void setIsIndeterminate(boolean isIndeterminate);
	public boolean getIsIndeterminate();
	public void toggleVisibility() ;
	public void setVisibility(boolean isVisible) ;
	public boolean getVisibility() ;
	
	// Only for determinate bars
	public void displayCompletedDeterminateBar();
	
	/* Only for indeterminate bars */
	public void displayCompletedIndeterminateBar();
}
