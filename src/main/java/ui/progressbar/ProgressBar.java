package ui.progressbar;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import ui.UIComponent;

public interface ProgressBar extends UIComponent {
	
	public void setProgressBarEndpoint (int end) ;
	public void updateProgressBar(int progress) ;
	public void resetProgressBar() ;
	
	@Override
	public JPanel getSwingComponent();
	public JProgressBar getProgressBar() ;
	public String getLabel() ;
	
	
	public void toggleVisibility() ;
	public void setVisibility(boolean isVisible) ;
	public boolean getVisibility() ;
}
