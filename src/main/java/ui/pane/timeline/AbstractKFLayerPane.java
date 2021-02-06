package ui.pane.timeline;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import commands.CommandFactory;
import datatypes.Layer;
import keyframes.Controller;
import ui.dialog.KFDialogFactory;

public abstract class AbstractKFLayerPane extends JPanel implements KFLayerPane {
	
	public static final int defaultPauseBeforeGhost = 100;
	
	protected AbstractKFLayerPane() {
		super();
		this.addMouseListener(new MouseAdapter() {
			@Override 
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					updateTimelineFromMouseClick(e);
				}
				else if(SwingUtilities.isRightMouseButton(e)) {
					displayRightClickMenu(e);
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
				Layer layer = Controller.getController().selectLayer(e);
				if (SwingUtilities.isLeftMouseButton(e) && layer != null) {
					Controller.getController().setCurrentDraggedLayer(layer);
					// This is so that only when the mouse is really being held will the layer
					// turn into a ghost visually
					// Mainly because without this, it keeps flickering whenever there is any simple
					// mouse click which is pretty annoying
					new Timer().schedule( 
				            new TimerTask() {
				                @Override
				                public void run() {
				                	if (Controller.getController().isLayerBeingDragged()) {
				                		Controller.getController().getCurrentDraggedLayer().setGhost(true);
				                		Controller.getController().refreshUI();
				                	}
				                }
				            }, defaultPauseBeforeGhost);
				}
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (Controller.getController().isLayerBeingDragged()) {
					Controller.getController().getCurrentDraggedLayer().setGhost(false);
					Controller.getController().resetCurrentDraggedLayer();
					Controller.getController().refreshUI();
				}
			}
			
		});
		this.addMouseMotionListener(new MouseAdapter() {
			@Override 
			public void mouseDragged(MouseEvent e) {
				
				if (Controller.getController().isLayerBeingDragged()) {
					ArrayList<Layer> layers = Controller.getController().getLayers();
					for (int i = 0; i < layers.size(); i++) {
						Layer layer = layers.get(i);
						if(layer.inBounds(e.getX(), e.getY())) {
							int curLayNum = Controller.getController().getCurrentLayerNum();
							
							//ASSERT statements are just for piece of mind so I know i'm doing the right
							//logic thing here
							assert(curLayNum == Controller.getController().getCurrentDraggedLayer().getLayerNum());
							int boundLayNum = layer.getLayerNum();
							assert(boundLayNum == i);
							if (curLayNum != boundLayNum) {
								layers.remove(curLayNum);
								layers.add(boundLayNum, Controller.getController().getCurrentDraggedLayer());
								Controller.getController().setCurrentLayerNum(i);
							}
							break;
						}
					}
					Controller.getController().refreshUI();
				}
			}
		});
	}
	
	public abstract void updateTimelineFromMouseClick(MouseEvent e);
	
	private void displayRightClickMenu(MouseEvent e) {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem newLayerMenuItem = new JMenuItem(new AbstractAction("New layer") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.getController().addAndExecuteCommand(CommandFactory.createAddLayerCommand());
			}
		});
		menu.add(newLayerMenuItem);
		
		Layer layer = Controller.getController().selectLayer(e);
		if (layer != null) {
			/* Change this later,
			Currently this is just so we can keep track of the layer being deleted
			In the future when you can delete multiple layers at once, we wnat to scale this list
			and have it be stored in the session as 'selected layers' */
			ArrayList<Integer> nums = new ArrayList<>();
			nums.add(layer.getLayerNum());
			
			// We do this first here, JUST so the selected black box shows around the layer
			JMenuItem recolorLayerMenuItem = new JMenuItem(new AbstractAction("Change layer color") {
				@Override
				public void actionPerformed(ActionEvent ae) {
					KFDialogFactory.createLayerColorDialog();
				}
			});
			
			JMenuItem copyFramesFromCurrentLayerMenuItem = new JMenuItem(new AbstractAction("Copy frame from current time and layer") {
				@Override
				public void actionPerformed(ActionEvent ae) {
					Controller.getController().copyFramesFromCurrentLayerAndCurrentTime();
				}
			});
			
			JMenuItem pasteFramesOntoCurrentLayerMenuItem = new JMenuItem(new AbstractAction("Paste frame") {
				@Override
				public void actionPerformed(ActionEvent ae) {
					Controller.getController().pasteFramesToCurrentLayerAndCurrentTime();
				}
			});
			
			JMenuItem renameLayerMenuItem = new JMenuItem(new AbstractAction("Rename layer") {
				@Override
				public void actionPerformed(ActionEvent ae) {
					KFDialogFactory.createRenameCurrentLayerDialog();
				}
			});
			
			JMenuItem deleteLayerMenuItem = new JMenuItem(new AbstractAction("Delete layer(s)") {
				@Override
				public void actionPerformed(ActionEvent ae) {
					Controller.getController().addAndExecuteCommand(CommandFactory.createDeleteLayersCommand(nums));
				}
			});
			
			menu.add(recolorLayerMenuItem);
			menu.add(copyFramesFromCurrentLayerMenuItem);
			menu.add(pasteFramesOntoCurrentLayerMenuItem);
			menu.add(renameLayerMenuItem);
			// Only add this menu item if layer(s) can be deleted
			if(Controller.getController().layersCanBeDeleted(nums)) {
				menu.add(deleteLayerMenuItem);
			}
			
		}
		
		menu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	public abstract void refresh();
	
}
