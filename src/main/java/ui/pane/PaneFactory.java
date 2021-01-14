package ui.pane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import keyframes.MagicValues;

public class PaneFactory {
	
	public static Pane createEmptyPane() {
		return new StandardPane();
	}
	
	public static Pane createMainViewTopContainer() {
		int tw = MagicValues.mainViewTopBarDefaultWidth;
		int th = MagicValues.mainViewTopBarDefaultHeight;
		
		Pane topBar = new StandardPane();
		topBar.getSwingComponent().setLayout(new BorderLayout());
		topBar.getSwingComponent().setPreferredSize(new Dimension(tw,th));
		topBar.getSwingComponent().setAlignmentX(Component.LEFT_ALIGNMENT);
		
		return topBar;
		
	}
	
	public static Pane createMainViewToolBarAndProgressBarContainer() {
		Pane container = new StandardPane();
		container.getSwingComponent().setLayout(new BoxLayout(container.getSwingComponent(), BoxLayout.X_AXIS));
		return container;
	}
	
}	
