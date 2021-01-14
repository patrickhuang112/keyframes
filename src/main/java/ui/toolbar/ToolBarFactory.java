package ui.toolbar;

import ui.button.ButtonFactory;

public class ToolBarFactory {
	
	public static ToolBar createEmptyToolBar() {
		return new StandardToolBar();
	}
	
	public static ToolBar createMainViewTopToolBar() {
		return ToolBarFactory.createEmptyToolBar();
		/*
		try {
			tb.addComponent(ButtonFactory.createBrushButton());
			tb.addComponent(ButtonFactory.createEraseButton());
			tb.addComponent(ButtonFactory.createEraseAllButton());
			tb.addComponent(ButtonFactory.createPlayButton());
			tb.addComponent(ButtonFactory.createPauseButton());
			tb.addComponent(ButtonFactory.createColorPickerButton());
			tb.addComponent(ButtonFactory.createFillButton());
		} catch(Exception e) {
			System.out.println("Button icon creation failed");
		}
		
		return tb;
		*/
	}
}
