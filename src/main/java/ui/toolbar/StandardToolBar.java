package ui.toolbar;

import java.awt.Dimension;

import javax.swing.JToolBar;

import keyframes.MagicValues;
import ui.UIComponent;

public class StandardToolBar extends JToolBar implements ToolBar {

	StandardToolBar(){
		super(JToolBar.HORIZONTAL);
		int tbw = MagicValues.mainViewTopToolBarDefaultWidth;
		int tbh = MagicValues.mainViewTopToolBarDefaultHeight;
		setPreferredSize(new Dimension(tbw,tbh));
		setFloatable(false);
	}
	@Override
	public JToolBar getSwingComponent() {
		return this;
	}
	@Override
	public void addComponent(UIComponent comp) {
		this.add(comp.getSwingComponent());
	}
	
}
