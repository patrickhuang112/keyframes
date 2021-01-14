package ui.canvas;

import java.awt.Color;

import javax.swing.JPanel;

import ui.UIComponent;

//KF stands for key frames, because there is already a java.awt canvas type
public interface KFCanvas extends UIComponent {
	@Override
	public JPanel getSwingComponent();
	
	public int getCanvasWidth();
	public int getCanvasHeight();
	
	public Color getBackgroundColor();
	public void setBackgroundColor(Color color);
	
	public void refresh();
}
