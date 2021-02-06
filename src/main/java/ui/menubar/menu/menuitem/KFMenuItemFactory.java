package ui.menubar.menu.menuitem;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import keyframes.Controller;
import keyframes.Utils;
import ui.dialog.KFDialogFactory;

public class KFMenuItemFactory {
	
	public static KFMenuItem createCustomMenuItem(AbstractAction aa) {
		return new StandardKFMenuItem(aa);
	}
	
	public static KFMenuItem createNewProjectMenuItem() {
		return new StandardKFMenuItem(new AbstractAction("New Project") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Controller.getController().newProject();
			}
		});
		
	}

	public static KFMenuItem createOpenProjectMenuItem() {
		return new StandardKFMenuItem(new AbstractAction("Open Project") {
			public void actionPerformed(ActionEvent e) {
				Controller.getController().openProject();
			}
		});
	}
	
	public static KFMenuItem createSaveMenuItem() {
		return new StandardKFMenuItem(new AbstractAction("Save") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().saveProject();
			}
		});
	}
	
	public static KFMenuItem createSaveAsMenuItem() {
		return new StandardKFMenuItem(new AbstractAction("Save As...") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().saveAsProject();
			}
		});
	}
	
	public static KFMenuItem createRenderMP4MenuItem() {
		return new StandardKFMenuItem(new AbstractAction("Render MP4") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Controller.getController().renderMP4();
			}
		});
	}
	
	
	public static KFMenuItem createRenderGIFMenuItem() {
		return new StandardKFMenuItem(new AbstractAction("Render GIF") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				Controller.getController().renderGIF();
			}
		});
	}

	
	public static KFMenuItem createSettingsMenuItem() {
		return new StandardKFMenuItem(new AbstractAction("Settings") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				KFDialogFactory.createSettingsDialog("Settings", Utils.getSettings());
			}
		});
	}
	
	
	public static KFMenuItem createEditFPSMenuItem() {
		return new StandardKFMenuItem(new AbstractAction("Edit FPS") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				KFDialogFactory.createFPSDialog();
			}
		});
	}
	
	public static KFMenuItem createEditCompositionLengthMenuItem() {
		return new StandardKFMenuItem(new AbstractAction("Edit Comp Length") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				KFDialogFactory.createCompositionLengthDialog();
			}
		});
	}
	
	public static KFMenuItem createEditBackgroundColorMenuItem() {
		return new StandardKFMenuItem(new AbstractAction("Edit Background Color") {
			@Override
			public void actionPerformed(ActionEvent ae) {
				KFDialogFactory.createBackgroundColorDialog();
			}
		});
	}
	
}
