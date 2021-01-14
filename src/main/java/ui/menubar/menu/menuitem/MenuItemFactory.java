package ui.menubar.menu.menuitem;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import keyframes.Controller;
import keyframes.Utils;
import ui.dialog.DialogFactory;

public class MenuItemFactory {
	
	public static MenuItem createCustomMenuItem(AbstractAction aa) {
		return new StandardMenuItem(aa);
	}
	
	public static MenuItem createNewProjectMenuItem() {
		return new StandardMenuItem(new AbstractAction("New Project") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Controller.getController().newProject();
			}
		});
		
	}

	public static MenuItem createOpenProjectMenuItem() {
		return new StandardMenuItem(new AbstractAction("Open Project") {
			public void actionPerformed(ActionEvent e) {
				Controller.getController().openProject();
			}
		});
	}
	
	public static MenuItem createSaveMenuItem() {
		return new StandardMenuItem(new AbstractAction("Save") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().saveProject();
			}
		});
	}
	
	public static MenuItem createSaveAsMenuItem() {
		return new StandardMenuItem(new AbstractAction("Save As...") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().saveAsProject();
			}
		});
	}
	
	public static MenuItem createRenderMP4MenuItem() {
		return new StandardMenuItem(new AbstractAction("Render MP4") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Controller.getController().renderMP4();
			}
		});
	}
	
	
	public static MenuItem createRenderGIFMenuItem() {
		return new StandardMenuItem(new AbstractAction("Render GIF") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Controller.getController().renderGIF();
			}
		});
	}

	
	public static MenuItem createSettingsMenuItem() {
		return new StandardMenuItem(new AbstractAction("Settings") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				DialogFactory.createSettingsDialog("Settings", Utils.getSettings());
			}
		});
	}
	
	
	public static MenuItem createEditFPSMenuItem() {
		return new StandardMenuItem(new AbstractAction("Edit FPS") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				DialogFactory.createFPSDialog();
			}
		});
	}
	
	public static MenuItem createEditCompositionLengthMenuItem() {
		return new StandardMenuItem(new AbstractAction("Edit Comp Length") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				DialogFactory.createCompositionLengthDialog();
			}
		});
	}
	
	public static MenuItem createEditBackgroundColorMenuItem() {
		return new StandardMenuItem(new AbstractAction("Edit Background Color") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				DialogFactory.createBackgroundColorDialog();
			}
		});
	}
	
}
