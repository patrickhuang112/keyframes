package ui.button;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import datatypes.Enums;
import keyframes.Controller;
import keyframes.Session;
import ui.dialog.DialogFactory;

public class EraseAllButton extends JButton implements Button {

	private static int menuPopupOffset = 5;
	
	EraseAllButton() throws IOException {
		Image eraserImage = ImageIO.read(this.getClass().getResource("/eraseAllIcon.png"));
		setIcon(new ImageIcon(eraserImage));
		
		setVisible(true);
		setPreferredSize(new Dimension(Button.mvw, Button.mvh));
		addMouseListener(new MouseAdapter( ) {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)) {
					Controller.getController().eraseCurrentLayerAtCurrentFrame();
					
				}
				else if(SwingUtilities.isRightMouseButton(e)) {
					JPopupMenu menu = new JPopupMenu();
					JMenuItem eraseAllLayersAtCurrentTimeMenuItem 
						= new JMenuItem(new AbstractAction("Erase all layer frames at current time") {
						@Override
						public void actionPerformed(ActionEvent e) {
							Controller.getController().eraseAllLayersAtCurrentFrame();
						}
					});
					menu.add(eraseAllLayersAtCurrentTimeMenuItem);
					
					//Very arbitrary right now just to make it look good
					int offset = EraseAllButton.menuPopupOffset;
					menu.show(e.getComponent(), e.getX() + offset, e.getY() + offset);
				}
			}
		});
	}
	
	@Override
	public JButton getSwingComponent() {
		return this;
	}
	
}
